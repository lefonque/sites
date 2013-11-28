package org.epis.ws.consumer.util;

public enum PropertyEnum {

	JOB_SQL_PRE(".sqlPre")
	,JOB_SQL_MAIN(".sqlMain")
	,JOB_SQL_POST(".sqlPost")
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
