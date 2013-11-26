package org.epis.ws.consumer.util;

public enum PropertyEnum {

	CONFIG("agent.config."),
	
	SCHEDULE("agent.schedule."),
	SCHEDULE_CMD("agent.schedule.cmd."),
	SCHEDULE_NAME("agent.schedule.name."),
	
	SYS_ROOT_DIR("consumer.root.dir"),
	SYS_JOB_NAME("job.name")
	;
	
	private String key;
	PropertyEnum(String key){
		this.key = key;
	}
	public String getKey(){
		return key;
	}
}
