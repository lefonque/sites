package com.mdm.idduxt.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class LoggerUtil {
	
	private static final String PARAM_REGEX = "\\{\\}";

	public static void debugLog(Logger logger, String message, Object... args){
		if(logger.isDebugEnabled()){
			if(args!=null){
				if(args.length!=paramCount(message)){
					logger.error("### invalid logging parameters ###");
					return;
				}
				logger.debug(String.format(message.replaceAll(PARAM_REGEX, "%s"), args));
			}
			else{
				logger.debug(message);
			}
		}
	}
	
	private static int paramCount(String str){
		final Pattern pattern = Pattern.compile(PARAM_REGEX);
		final Matcher matcher = pattern.matcher(str);
		int count = 0;
		while(matcher.find()){
			count++;
		}
		return count;
	}
}
