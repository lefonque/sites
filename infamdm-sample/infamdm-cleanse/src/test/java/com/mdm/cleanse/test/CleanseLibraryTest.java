package com.mdm.cleanse.test;

import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mdm.cleanse.base.SMCCleanseLibrary;
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
	
	public void test() throws CleanseException{
		
		SMCCleanseLibrary library = new SMCCleanseLibrary();;
		library.initialize(new Properties());
		Map context = new HashMap();
		Map input = new HashMap();
		Map output = new HashMap();
		
		input.put("inString1", "123");
		input.put("inString2", "asd");
		library.getCleanseFunction("Concatenate Function").cleanse(context, input, output);
		logger.debug("result : " + output.get("outString1"));
	}
}
