package com.mdm.cleanse.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class LoggerHelper {

	private static final String PARAM_REGEX = "\\{\\}";

	/**
	 * <pre>
	 * debug 로깅에 한하여 기본적인 사용법이 slf4j-api와 동일하게 구성된 로깅 메서드
	 * 
	 * 사용예) LoggerHelper.debugLog(logger, "### Cleanse Function [{}] Start ###",funcName);
	 * </pre>
	 * @param logger
	 * @param message
	 * @param args
	 */
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
