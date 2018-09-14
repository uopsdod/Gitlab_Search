package com.amazonaws.lambda.demo.search.http_entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class LoginHttpPost extends HttpPost{
	static private final String loginUrl = "https://gitlab.com/users/sign_in";
	
	public LoginHttpPost(String username, String password, String authenticity_token) throws UnsupportedEncodingException {
		super(LoginHttpPost.loginUrl);
        
		/** set form data **/
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("authenticity_token", authenticity_token));
        params.add(new BasicNameValuePair("user[login]", username));
        params.add(new BasicNameValuePair("user[password]", password));
        params.add(new BasicNameValuePair("user[remember_me]", "0"));
        params.add(new BasicNameValuePair("utf8", "✓"));
        this.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        
        /** set headers **/
        this.setHeader("origin", "https://gitlab.com");
        this.setHeader("referer", " https://gitlab.com/users/sign_in");
        this.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
        this.setHeader("host", "gitlab.com");
        this.setHeader("Content-Type", "application/x-www-form-urlencoded");
        
//        this.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        this.setHeader("Accept-Encoding", "gzip, deflate, br");
//        this.setHeader("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7");
//        this.setHeader("Cache-Control", "max-age=0");
//        this.setHeader("Connection", "keep-alive");
//        this.setHeader("Content-Length", "222");
//        this.setHeader("Cookie", "optimizelyEndUserId=oeu1536925262552r0.28961190637722; _biz_uid=1577acdae66c402ffa35e8975fb27dd1; _biz_flagsA=%7B%22Version%22%3A1%2C%22XDomain%22%3A%221%22%7D; _mkto_trk=id:194-VVC-221&token:_mch-gitlab.com-1536925264908-86878; _ga=GA1.2.1841359800.1536925264; _gid=GA1.2.70546797.1536925264; _biz_nA=3; _biz_pendingA=%5B%5D; _sp_ses.6b85=*; sidebar_collapsed=false; _gitlab_session=dcd2e9ed96f27ecf6c63cfb81a666732; _sp_id.6b85=d9684827-6cf3-4f66-b2af-5f9d8a8676fd.1536925284.1.1536931557.1536925284.9c7e8a25-6651-4d60-aa16-ac94000e9643");
//        this.setHeader("Upgrade-Insecure-Requests", "1");

	}
	
//	// Lazy initialization holder class idiom for static fields
//	static private class SingletonHolder {
//		static public HttpPost loginHttpPost = new LoginHttpPost();
//	}
//	static public HttpPost getInstance() { return SingletonHolder.loginHttpPost; }
	
}
//	: 
//	: 
//	: 
//	: 
//	: 
//	: 
//	: 
//	: 
//	: 
