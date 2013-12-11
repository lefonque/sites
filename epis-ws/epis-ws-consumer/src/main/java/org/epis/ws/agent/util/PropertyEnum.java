package org.epis.ws.agent.util;

public enum PropertyEnum {

	JOB_SUFFIX_BATCH_SIZE(".batchSelectCount")
	,JOB_SUFFIX_JOB_NAME(".jobName")
	,JOB_SUFFIX_EXEC_TIME(".execTime")
	
	,AGENT_ID("consumer.agentId")
	,SYS_ROOT_DIR("consumer.root.dir")
	,SYS_JOB_NAME("job.id")
	;
	
	private String key;
	PropertyEnum(String key){
		this.key = key;
	}
	public String getKey(){
		return key;
	}
}
