package com.mdm.cleanse.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mdm.cleanse.base.FunctionUnit;
import com.mdm.cleanse.base.LoggerHelper;
import com.mdm.cleanse.base.SMCCleanseLibrary;
import com.mdm.cleanse.base.UnifiedCleanseFunction;
import com.siperian.mrm.cleanse.api.CleanseException;


public class CleanseLibraryTest {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	
	@Test
	public void testRRN() throws CleanseException{
		
		String funcName = "QSA_CN_SSNCHK";
		Map<String,Object> output = execTest(funcName, "750231", "2178423");
		LoggerHelper.debugLog(logger, "=== Result of {} ===", funcName);
		for(String key : output.keySet()){
			LoggerHelper.debugLog(logger, "{} : [{}]", key, output.get(key));
		}
		LoggerHelper.debugLog(logger, "=================");
	}
	
	@Test
	public void testURL() throws CleanseException {

		String funcName = "QSA_CN_URLCHK";
		Map<String,Object> output = execTest(funcName, "http://www.yahoo.com");
		LoggerHelper.debugLog(logger, "=== Result of {} ===", funcName);
		for(String key : output.keySet()){
			LoggerHelper.debugLog(logger, "{} : [{}]", key, output.get(key));
		}
		LoggerHelper.debugLog(logger, "=================");
	}
	
	private Map<String,Object> execTest(String funcName,Object... values) throws CleanseException{
		SMCCleanseLibrary library = new SMCCleanseLibrary();
		library.initialize(new Properties());
		Map<String,Object> context = new HashMap<String,Object>();
		Map<String,Object> input = new HashMap<String,Object>();
		Map<String,Object> output = new HashMap<String,Object>();
		
		UnifiedCleanseFunction func = (UnifiedCleanseFunction)library.getCleanseFunction(funcName);
		FunctionUnit unit = func.getFunctionUnit();
		int idx = 0;
		for(Object val : values){
			input.put(unit.getInputNames()[idx++], val);
		}
		
		func.cleanse(context, input, output);
		return output;
	}
}
