package com.amazonaws.lambda.demo.search;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.amazonaws.lambda.demo.search.http_entity.GetAllProjectsHttpGet;
import com.amazonaws.lambda.demo.search.http_entity.GetProjectHttpGet;
import com.amazonaws.lambda.demo.search.http_entity.GetSearchResultHttpGet;
import com.amazonaws.lambda.demo.search.http_entity.GetSignInPageHttpGet;
import com.amazonaws.lambda.demo.search.http_entity.LoginHttpPost;
import com.amazonaws.lambda.demo.util.SearchUtil;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public class SearchService {
	private static final String NO_RESULT_FOUND = "no result found";
	private static final String SERVER_ERROR = "server errors";
	private static final String CLIENT_ERROR = "client errors";
	
	private org.apache.http.client.HttpClient client;
	
	public SearchService() {
		this.client = HttpClients.custom()
				        .setDefaultRequestConfig(RequestConfig.custom()
				        						.setCookieSpec(CookieSpecs.STANDARD).build()) // we need to set this to prevent invalid cookie error 
				        .build();
	}
	
	public String search(String keyword) {
		SearchUtil.getLogger().log(Level.INFO, "search starts");
		List<String> searchObjs = new ArrayList<>();
		try {
			/** get authenticity_token from SignIn Page **/
			HttpResponse getSignInPageHttpPostResp = this.client.execute(new GetSignInPageHttpGet());
			String signInPageRespContent = EntityUtils.toString(getSignInPageHttpPostResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, "get sign-in page");
			
			String authenticity_token = "";
			Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"authenticity_token\" value=\"(.*?)\" />");
			Matcher matcher = pattern.matcher(signInPageRespContent);

			if (!matcher.find()) {
				SearchUtil.getLogger().log(Level.INFO, "cannot find authentiction_token");
				return SearchService.SERVER_ERROR + ": cannot find authentiction_token";
			}else {
				authenticity_token = matcher.group(1);
				SearchUtil.getLogger().log(Level.INFO, "get authenticity_token from sign-in page: " + authenticity_token);
			}
			
			/** Sing in **/
			HttpResponse loginHttpPostResp = this.client.execute(new LoginHttpPost("uopsdod@gmail.com", "eg359157", authenticity_token));
			String loginRespContent = EntityUtils.toString(loginHttpPostResp.getEntity());
			if (loginRespContent.contains("Invalid Login or password")) {
				SearchUtil.getLogger().log(Level.INFO, "Invalid Login or password");
				return SearchService.CLIENT_ERROR + ": Invalid Login or password";
			}else {
				SearchUtil.getLogger().log(Level.INFO, "login successfully");
			}
			
			/** Get Project Uri from Project List **/
			HttpResponse getAllProjectsResp = this.client.execute(new GetAllProjectsHttpGet());
			String getAllProjectsRespContent = EntityUtils.toString(getAllProjectsResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, "get all projects");
			
			Pattern pattern_getAllProjects = Pattern.compile("<a class=\"project\" href=\"(.*?)\">");
			Matcher matcher_getAllProjects = pattern_getAllProjects.matcher(getAllProjectsRespContent);
			
			while (matcher_getAllProjects.find()) {
				String projct_uri = matcher_getAllProjects.group(1);
				SearchUtil.getLogger().log(Level.INFO, "get projct_uri: " + projct_uri);
				
				/** get the project_id of each project **/
				HttpResponse getProjectResp = this.client.execute(new GetProjectHttpGet(projct_uri));
				String getProjectRespContent = EntityUtils.toString(getProjectResp.getEntity());
				
				Pattern pattern_projectId = Pattern.compile("<input type=\"hidden\" name=\"project_id\" id=\"project_id\" value=\"(.*?)\" />");
				Matcher matcher_projectId = pattern_projectId.matcher(getProjectRespContent);

				if (matcher_projectId.find()) {
					String projectId = matcher_projectId.group(1);
					SearchUtil.getLogger().log(Level.INFO, "get projectId: " + projectId);

					/** search the keyword for each project **/ 
					HttpResponse getSearchResultProjectsResp = this.client.execute(new GetSearchResultHttpGet(projectId, keyword));
					String getSearchResultRespContent = EntityUtils.toString(getSearchResultProjectsResp.getEntity());
					
					/** if found sth., restore the page in the final result list **/
					if (!getSearchResultRespContent.contains("We couldn't find any results matching")) {
						SearchUtil.getLogger().log(Level.INFO, "find result for projectId: " + projectId);
						URI searchURI = new URIBuilder("https://gitlab.com/search")
								.setParameter("utf8", "✓") // %E2%9C%93
								.setParameter("project_id", projectId)
								.setParameter("search", keyword)
								.build();
						searchObjs.add(searchURI.toString());
					}else {
						SearchUtil.getLogger().log(Level.INFO, "no result for projectId: " + projectId);
					}
				} // end of if (matcher_projectId.find())
			} // end of while (matcher_project.find())
			
			
		} catch (IOException | URISyntaxException e) {
			SearchUtil.getLogger().log(Level.INFO, e);
		}
		
		/** create a simple html with a list of projects that have some results from the keyword **/
		String render = html(
			    head(
			        title("HTML from API Gateway/Lambda")
			        // ,link().withRel("stylesheet").withHref("/css/main.css")
			    ),
			    body(
			            p("Projects that contain the keyword - " + keyword + " are as following: "),
			            ul(
			              each(searchObjs, searchObj ->
			                li(
			                  a(searchObj.toString()).withHref(searchObj.toString()).withTarget("_blank")
			                )
			              )
			            )
			          )
	   ).render();
		SearchUtil.getLogger().log(Level.INFO, "search ends");
		SearchUtil.getLogger().log(Level.INFO, render);
		return render;
	}
	
}
