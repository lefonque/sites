package com.mdm.cleanse.unit;

import java.util.Map;

import org.apache.log4j.Logger;

import com.mdm.cleanse.base.FunctionUnit;
import com.siperian.mrm.cleanse.api.ParameterTypes;

public class SampleFunctionUnit implements FunctionUnit {
	
	private static final Logger logger = Logger.getLogger(SampleFunctionUnit.class);

	@Override
	public String[] getInputNames() {
		
		return new String[]{"inString1","inString2"};
	}

	@Override
	public String[] getInputTypes() {
		
		return new String[]{ParameterTypes.STRING,ParameterTypes.STRING};
	}

	@Override
	public String[] getInputDescriptions() {
		
		return new String[]{"first param","second param"};
	}

	@Override
	public String[] getOutputNames() {
		
		return new String[]{"outString1"};
	}

	@Override
	public String[] getOutputTypes() {
		
		return new String[]{ParameterTypes.STRING};
	}

	@Override
	public String[] getOutputDescriptions() {
		
		return new String[]{"Concatenated String"};
	}

	@Override
	public String getFuncName() {
		
		return "Concatenate Function";
	}

	@Override
	public String getFuncDesc() {
		
		return "This is Function that concatenate two string and for sample";
	}

	@Override
	public void cleanse(Map<String, Object> input, Map<String, Object> output) {
		StringBuilder builder = new StringBuilder();
		builder.append(input.get(getInputNames()[0])).append(input.get(getInputNames()[1]));
		output.put(getOutputNames()[0], builder.toString());
		logger.debug("=== Concatenate Processing Executed ===");
	}

}
