package com.mdm.cleanse.base;

import java.util.Map;

public interface FunctionUnit {

	public String[] getInputNames();
	public String[] getInputTypes();
	public String[] getInputDescriptions();
	
	public String[] getOutputNames();
	public String[] getOutputTypes();
	public String[] getOutputDescriptions();
	
	public String getFuncName();
	public String getFuncDesc();
	
	public void cleanse(Map<String,String> input, Map<String,String> output);
}
