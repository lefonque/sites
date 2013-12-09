package org.epis.ws.agent.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.epis.ws.common.utils.ConstEnum;
import org.epis.ws.common.utils.RuntimeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractScheduleRegister {


	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected RuntimeExecutor runtimeExecutor;

	@Autowired
	@Qualifier("jobProp")
	protected Properties jobProp;
	
	@Autowired
	@Qualifier("agentProp")
	protected Properties agentProp;
	
	public abstract Map<ConstEnum,List<String>> registerSchedule(String[] jobNames) throws Exception;
	
}


