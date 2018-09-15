package com.amazonaws.lambda.demo.search.http_entity;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class GetSearchResultHttpGet extends HttpGet{
	static private final String url = "https://gitlab.com/search";
	
	public GetSearchResultHttpGet(String projectId, String keyword) throws UnsupportedEncodingException, URISyntaxException {
		super(new URIBuilder(GetSearchResultHttpGet.url)
				.setParameter("utf8", "✓") // %E2%9C%93
				.setParameter("project_id", projectId)
				.setParameter("search", keyword)
				.build()
				);
		
//		super("https://gitlab.com/search?utf8=%E2%9C%93&snippets=&scope=&search=AutoLogin&project_id=8380941");

		/** set form data **/
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        this.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        
        /** set headers **/
//        this.setHeader("origin", "http://www.1point3acres.com");
//        this.setHeader("referer", "http://www.1point3acres.com/bbs/");
//        this.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
//        this.setHeader("host", "www.1point3acres.com");

	}
	
//	// Lazy initialization holder class idiom for static fields
//	static private class SingletonHolder {
//		static public HttpPost loginHttpPost = new LoginHttpPost();
//	}
//	static public HttpPost getInstance() { return SingletonHolder.loginHttpPost; }
	
}
