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

/**
 * <pre>
 * <p>Schedule을 등록하는 역할을 하는 SchedulerRegister의 추상클래스</p>
 * 
 * 사전에 준비된 .bat 또는 .sh 파일을 OS쪽 Scheduler(schtasks.exe 또는 crontab)에 등록하여
 * 스케쥴링 처리를 수행하도록 구성될 경우에, OS쪽 Scheduler에 등록하는 역할을 수행하는 클래스
 * 
 * 사전에 준비된 .bat 또는 .sh 파일은 파라메터를 받아서 처리되게끔 되어 있으므로, 등록시에는 이에 적합한
 * 파라메터값을 넣어주어야 한다.
 * </pre>
 * @author developer
 *
 */
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
	
	/**
	 * 스케쥴을 등록한다.
	 * @param jobNames
	 * @return
	 * @throws Exception
	 */
	public abstract Map<ConstEnum,List<String>> registerSchedule(String[] jobNames) throws Exception;
	
}


