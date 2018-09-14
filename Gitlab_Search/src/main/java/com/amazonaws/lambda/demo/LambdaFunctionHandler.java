package com.amazonaws.lambda.demo;

import java.util.logging.Level;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.amazonaws.lambda.demo.search.SearchService;
import com.amazonaws.lambda.demo.util.MyLambdaLogger;
import com.amazonaws.lambda.demo.util.SearchUtil;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * TODO it's time to work on tempalte stuff
 * @author sam
 *
 */
public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    public LambdaFunctionHandler() {}

    @Override
    public String handleRequest(Object input, Context context) {
    	SearchUtil.setLogger(new MyLambdaLogger(context.getLogger()));
    	SearchUtil.getLogger().log(Level.INFO, "Lambda function starts");
    	SearchUtil.getLogger().log(Level.INFO, "input DailyRewardInput: " + ReflectionToStringBuilder.toString(input));
    	SearchUtil.getLogger().log(Level.INFO, "input Context: " + ReflectionToStringBuilder.toString(context));
    	
    	execute("autologin");
		
		SearchUtil.getLogger().log(Level.INFO, "Lambda function ends");
		
        // Return will include the log stream name so you can look 
        // up the log later.
        return String.format("Execution finished. Log stream = %s", context.getLogStreamName());
    }
    
	public void execute(String keyword) {
		SearchService searchService = new SearchService();
		searchService.search(keyword);
	}
    
}