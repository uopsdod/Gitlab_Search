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
	
	private org.apache.http.client.HttpClient client;
	
	public SearchService() {
//		this.client = HttpClientBuilder.create().build();
//		this.client = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();
//		this.client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build(); // not working ... 
		this.client = HttpClients.custom()
		        .setDefaultRequestConfig(RequestConfig.custom()
		        .setCookieSpec(CookieSpecs.STANDARD).build())
		        .build();
		
		
		
//		SchemeRegistry schemeRegistry = new SchemeRegistry();
//		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//		SingleClientConnManager mgr = new SingleClientConnManager(schemeRegistry);
//		client = new DefaultHttpClient(mgr);
		
//		CloseableHttpClient httpclient = null;
//		HttpHost target = new HttpHost('www.mysite.com', 443, "https");

//		SSLContext sslcontext = SSLContexts.createSystemDefault();
//		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
//		        sslcontext, new String[] { "TLSv1", "SSLv3" }, null,
//		        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//
//		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//		        .register("http", PlainConnectionSocketFactory.INSTANCE)
//		        .register("https", sslConnectionSocketFactory)
//		        .build();
//		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//
//		httpclient = HttpClients.custom()
//					.setSSLSocketFactory(sslConnectionSocketFactory)
//					.setConnectionManager(cm)
//					.build();
//		
//		this.client = httpclient;
	}
	
	public String search(String keyword) {
		SearchUtil.getLogger().log(Level.INFO, "search starts");
		List<String> searchObjs = new ArrayList<>();
		StringBuilder result = new StringBuilder();
		result.append("<html><head><title>HTML from API Gateway/Lambda</title></head>");
		result.append("<body>");
		result.append("<ul>");
		try {
			/** get authenticity_token from SignIn Page **/
			HttpResponse getSignInPageHttpPostResp = this.client.execute(new GetSignInPageHttpGet());
			String signInPageRespContent = EntityUtils.toString(getSignInPageHttpPostResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, "here");
			
			String authenticity_token = "";
			Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"authenticity_token\" value=\"(.*?)\" />");
			Matcher matcher = pattern.matcher(signInPageRespContent);

			if (matcher.find()) {
				authenticity_token = matcher.group(1);
				SearchUtil.getLogger().log(Level.INFO, "authenticity_token: " + authenticity_token);
			}
			
			/** Sing in **/
			HttpResponse loginHttpPostResp = this.client.execute(new LoginHttpPost("uopsdod@gmail.com", "eg359157", authenticity_token));
			String loginRespContent = EntityUtils.toString(loginHttpPostResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, "here");
			
			/** Get Project List **/
			HttpResponse getAllProjectsResp = this.client.execute(new GetAllProjectsHttpGet());
			String getAllProjectsRespContent = EntityUtils.toString(getAllProjectsResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, "here");
			
			// TODO: iterate the project list
//			<a class="project" href="/uopsdod/test_search">
			Pattern pattern_project = Pattern.compile("<a class=\"project\" href=\"(.*?)\">");
			Matcher matcher_project = pattern_project.matcher(getAllProjectsRespContent);

			
			while (matcher_project.find()) {
				String projct_uri = matcher_project.group(1);
				SearchUtil.getLogger().log(Level.INFO, "projct_uri: " + projct_uri);
				// TODO: get the project_id of each project
				HttpResponse getProjectResp = this.client.execute(new GetProjectHttpGet(projct_uri));
				String getProjectRespContent = EntityUtils.toString(getProjectResp.getEntity());
				SearchUtil.getLogger().log(Level.INFO, "here");
				
				// <input type="hidden" name="project_id" id="project_id" value="8382931" />
				String projectId = "";
				Pattern pattern_projectId = Pattern.compile("<input type=\"hidden\" name=\"project_id\" id=\"project_id\" value=\"(.*?)\" />");
				Matcher matcher_projectId = pattern_projectId.matcher(getProjectRespContent);

				if (matcher_projectId.find()) {
					projectId = matcher_projectId.group(1);
					SearchUtil.getLogger().log(Level.INFO, "projectId: " + projectId);
				}
				
				// TODO: use the keyword to search for each project 
//				String keyword = "AutoLogin";
				HttpResponse getSearchResultProjectsResp = this.client.execute(new GetSearchResultHttpGet(projectId, keyword));
				String getSearchResultRespContent = EntityUtils.toString(getSearchResultProjectsResp.getEntity());
				SearchUtil.getLogger().log(Level.INFO, "here");				
				
				// TODO: if found sth., restore the page in the final result list 
				if (!getSearchResultRespContent.contains("We couldn't find any results matching")) {
					URI searchURI = new URIBuilder("https://gitlab.com/search")
										.setParameter("utf8", "✓") // %E2%9C%93
										.setParameter("project_id", projectId)
										.setParameter("search", keyword)
										.build();
					searchObjs.add(searchURI.toString());
				}
				
			} // end of while 
			
			/** create a simple html with a list of projects that have some results from the keyword **/
			for (String searchObj : searchObjs) {
				result.append("<li><a href=\"" + searchObj + "\">" + searchObj + "</a></li>");
			}
			result.append("</ul>");
			result.append("</body></html>");
		} catch (IOException | URISyntaxException e) {
			SearchUtil.getLogger().log(Level.INFO, e);
		}
		
		
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
