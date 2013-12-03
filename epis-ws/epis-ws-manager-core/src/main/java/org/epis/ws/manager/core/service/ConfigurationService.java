package org.epis.ws.manager.core.service;

import java.util.List;
import java.util.Map;

import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.manager.core.dao.ConfigurationDAO;
import org.epis.ws.manager.entity.JQGridVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationService {


	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationDAO dao;
	
	public Map<String, String> getLoginUserInfo(String loginUsername){
		Map<String, String> result = dao.selectLoginPassword(loginUsername);
		return result;
	}
	
	public String getWebServicePass(String userID) throws Exception {
		String result = dao.selectWebserviceUserPassword(userID);
		return result;
	}
	
	public ConfigurationVO getConfigurationInfo(String agentId) throws Exception{
		
		AgentVO agentInfo = dao.selectAgentInfo(agentId);
		
		List<JobVO> jobList = dao.selectJobList(agentId);
		ConfigurationVO result = new ConfigurationVO();
		
		result.setAgentInfo(agentInfo);
		result.setJobList(jobList);
		
		return result;
	}
	
	/**
	 * client id 에 해당하는 Configuration 정보를 가져온다.
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	public AgentVO getAgentInfo(String clientID) throws Exception{
		
		AgentVO result = dao.selectAgentInfo(clientID);
		logger.debug("Get {}'s client info",clientID);
		
		return result;
	}
	
	/**
	 * 전체 Configuration 갯수를 가져온다.
	 * @return
	 */
	public int getAgentCount(){
		int result = dao.selectAgentCount();
		return result;
	}
	
	/**
	 * 페이징정보에 맞게 Configuration 목록을 가져온다.
	 * @param searchParam
	 * @return
	 */
	public List<AgentVO> getAgentList(JQGridVO searchParam){
		
		List<AgentVO> result = dao.selectAgentList(searchParam);
		return result;
	}
	
	/**
	 * 화면에서 추가한 Configuration 정보를 반영한다.
	 * @param config
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int addAgent(AgentVO config){
		int result = dao.insertAgent(config);
		return result;
	}
	
	/**
	 * 화면에서 수정된 Configuration 정보를 반영한다.
	 * @param config
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int modifyAgent(AgentVO config){
		int result = dao.updateAgent(config);
		return result;
	}
	
	/**
	 * client id 목록에 대항하는 Configuration 정보를 삭제한다.
	 * @param clientIds
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int removeAgent(String[] clientIds){
		int result = 0;
		for(String clientId : clientIds){
			result += dao.deleteAgent(clientId);
			result += dao.deleteJobByClientId(clientId);
		}
		return result;
	}

	/**
	 * client id에 해당하는 Schedule목록 갯수를 가져온다.
	 * @param clientId
	 * @return
	 */
	public int getJobCount(String clientId){
		int result = dao.selectJobCount(clientId);
		return result;
	}
	
	/**
	 * client id에 해당하는 Schedule목록을 가져온다.
	 * @param clientId
	 * @return
	 */
	public List<JobVO> getJobList(JQGridVO searchParam, String clientId){
		List<JobVO> result = dao.selectJobList(searchParam, clientId);
		return result;
	}
	
	/**
	 * Schedule ID에 해당하는 Schedule 정보를 가져온다.
	 * Agent에서 webservice로 보내온 데이터를 처리할 SQL을 취득할 때 사용함
	 * @param agentId
	 * @param jobId
	 * @return
	 */
	public JobVO getJobInfo(String agentId, String jobId){
		JobVO result = dao.selectJobInfo(agentId, jobId);
		return result;
	}
	
	/**
	 * 화면에서 추가한 Schedule정보를 반영한다.
	 * @param job
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int addJob(JobVO job){
		int result = dao.insertJob(job);
		return result;
	}
	
	/**
	 * 화면에서 수정된 Schedule정보를 반영한다.
	 * @param job
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int modifyJob(JobVO job){
		int result = dao.updateSchedule(job);
		return result;
	}
	
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int removeJobById(String[] jobIds){
		int result = 0;
		for(String jobId : jobIds){
			result += dao.deleteJob(jobId);
		}
		return result;
	}
	
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int removeJobByClientId(String clientId){
		int result = dao.deleteJobByClientId(clientId);
		return result;
	}
	
	
	public Map<String,Object> getJdbcInfo(JobVO job){
		Map<String,Object> result = dao.selectJdbcInfo(job);
		return result;
	}
}
