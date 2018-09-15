package com.amazonaws.lambda.demo.search.http_entity;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

public class GetProjectHttpGet extends HttpGet{
	static private final String url = "https://gitlab.com";
	
	public GetProjectHttpGet(String projectUri) throws UnsupportedEncodingException {
		super(GetProjectHttpGet.url + "/" + projectUri);
        
		/** set form data **/
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        this.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        
        /** set headers **/
//        this.setHeader("origin", "http://www.1point3acres.com");
//        this.setHeader("Upgrade-Insecure-Requests", "1");
//        this.setHeader("User-Agen", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
//        this.setHeader("host", "gitlab.com");
//        
//        this.setHeader("If-None-Match", "W/\"eabb81f1e475cfae9b894e7c4ee17fc8\"");
	}
}

//	Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//	Accept-Encoding: gzip, deflate, br
//	Accept-Language: zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7
//	Cache-Control: max-age=0
//	Connection: keep-alive
//	Cookie: optimizelyEndUserId=oeu1536925262552r0.28961190637722; _biz_uid=1577acdae66c402ffa35e8975fb27dd1; _biz_flagsA=%7B%22Version%22%3A1%2C%22XDomain%22%3A%221%22%7D; _mkto_trk=id:194-VVC-221&token:_mch-gitlab.com-1536925264908-86878; _ga=GA1.2.1841359800.1536925264; _gid=GA1.2.70546797.1536925264; _biz_nA=3; _biz_pendingA=%5B%5D; _sp_ses.6b85=*; sidebar_collapsed=false; _gitlab_session=b7f9021f003fd57fce671207c1ecde7d; _sp_id.6b85=d9684827-6cf3-4f66-b2af-5f9d8a8676fd.1536925284.1.1536928064.1536925284.9c7e8a25-6651-4d60-aa16-ac94000e9643
