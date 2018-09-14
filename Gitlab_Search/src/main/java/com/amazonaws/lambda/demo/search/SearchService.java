package com.amazonaws.lambda.demo.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.amazonaws.lambda.demo.search.http_entity.GetAllProjectsHttpPost;
import com.amazonaws.lambda.demo.search.http_entity.GetSignInPageHttpPost;
import com.amazonaws.lambda.demo.search.http_entity.LoginHttpPost;
import com.amazonaws.lambda.demo.search.http_entity.SearchHttpPost;
import com.amazonaws.lambda.demo.util.SearchUtil;

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
		try {
			/** get authenticity_token from SignIn Page **/
			HttpResponse getSignInPageHttpPostResp = this.client.execute(new GetSignInPageHttpPost());
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
			HttpResponse getAllProjectsResp = this.client.execute(new GetAllProjectsHttpPost());
			String getAllProjectsRespContent = EntityUtils.toString(getAllProjectsResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, getAllProjectsRespContent);
			
			HttpResponse searchResp = this.client.execute(new SearchHttpPost(keyword));
			String searchRespContent = EntityUtils.toString(searchResp.getEntity());
			SearchUtil.getLogger().log(Level.INFO, "here");
			
		} catch (IOException e) {
			SearchUtil.getLogger().log(Level.INFO, e);
		}
		
		SearchUtil.getLogger().log(Level.INFO, "search ends");
		return "ok";
	}
	
}
