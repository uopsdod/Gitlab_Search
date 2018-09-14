package com.amazonaws.lambda.demo.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class MyLambdaLogger implements BaseLogger{
	private LambdaLogger logger;
	
	public MyLambdaLogger(LambdaLogger logger) {
		super();
		this.logger = logger;
	}



	@Override
	public void log(Level level, String msg) {
		this.logger.log(level.getName() + " - " + msg);
	}



	@Override
	public void log(Level level, Throwable error) {
		/** get the whole stack trace of error **/
	    StringWriter trace = new StringWriter();
	    error.printStackTrace(new PrintWriter(trace));
	    String eMsg = trace.toString();
	    
	    this.log(level, eMsg);
	}

}
