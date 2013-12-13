package org.epis.ws.agent.scheduler.utils;

public enum PropertyEnum {

	JOB_SUFFIX_EXEC_TIME(".execTime")
	,AGENT_OS("consumer.operatingSystem")
	;
	
	private String key;
	PropertyEnum(String key){
		this.key = key;
	}
	public String getKey(){
		return key;
	}
}
