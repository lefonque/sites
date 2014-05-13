package com.mdm.cleanse.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mdm.cleanse.base.FunctionUnit;
import com.mdm.cleanse.base.SMCCleanseLibrary;
import com.mdm.cleanse.base.UnifiedCleanseFunction;
import com.siperian.mrm.cleanse.api.CleanseException;


public class CleanseLibraryTest {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	public static void main(String[] args) {
		try {
			new CleanseLibraryTest().test();
		} catch (CleanseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() throws CleanseException{
		
		SMCCleanseLibrary library = new SMCCleanseLibrary();
		library.initialize(new Properties());
		Map<String,Object> context = new HashMap<String,Object>();
		Map<String,Object> input = new HashMap<String,Object>();
		Map<String,Object> output = new HashMap<String,Object>();
		
		UnifiedCleanseFunction func = (UnifiedCleanseFunction)library.getCleanseFunction("QSA_CN_SSNCHK");
		FunctionUnit unit = func.getFunctionUnit();
		input.put(unit.getInputNames()[0], "751232");
		input.put(unit.getInputNames()[1], "1120612");
		
		func.cleanse(context, input, output);
		logger.debug("result : " + output.get(unit.getOutputNames()[3]));
	}
}
