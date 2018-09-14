package com.amazonaws.lambda.demo.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchUtil {
	private static BaseLogger logger;
	
	static {
		setLogger(new ConsoleLogger());
	}

	public static BaseLogger getLogger() {
		return logger;
	}

	public static void setLogger(BaseLogger logger) {
		SearchUtil.logger = logger;
	}
	
}
