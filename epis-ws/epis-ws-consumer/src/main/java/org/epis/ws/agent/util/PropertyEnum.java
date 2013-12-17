package org.epis.ws.agent.util;

/**
 * <pre>
 * Agent측 프로퍼티인 agent.properties와 job.properties에
 * 기재된 프로퍼티 키에 대한 문자열을 리터럴 상수로 나열한 Enum
 * </pre> 
 * @author developer
 *
 */
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
