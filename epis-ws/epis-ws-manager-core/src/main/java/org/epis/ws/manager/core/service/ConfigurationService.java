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

/**
 * <pre>
 * <p>Agent 및 Job 데이터의 처리를 담당하는 Service</p>
 * 
 * ConfigurationDAO 를 이용하여 Agent 및 Job데이터를 처리하는 Service
 * </pre>
 * @author developer
 *
 */
@Service
public class ConfigurationService {


	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationDAO dao;
	
	/**
	 * 웹서비스 계정ID에 대한 패스워드값을 취득한다.
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public String getWebServicePass(String userID) throws Exception {
		String result = dao.selectWebserviceUserPassword(userID);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>지정된 Agent ID에 해당하는 Configuration정보를 취득하는 메서드</p>
	 * 
	 * WebService를 이용하여 Agent의 Configuration(Agent정보 및 소속된 Job정보)
	 * 동기화를 위해 작성된 메서드
	 * 
	 * 현재 사용되지 않음.
	 * </pre>
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public ConfigurationVO getConfigurationInfo(String agentId) throws Exception{
		
		AgentVO agentInfo = dao.selectAgentInfo(agentId);
		
		List<JobVO> jobList = dao.selectJobList(agentId);
		ConfigurationVO result = new ConfigurationVO();
		
		result.setAgentInfo(agentInfo);
		result.setJobList(jobList);
		
		return result;
	}
	
	/**
	 * agent id 에 해당하는 Agent 정보를 가져온다.
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public AgentVO getAgentInfo(String agentId) throws Exception{
		
		AgentVO result = dao.selectAgentInfo(agentId);
		logger.debug("Get {}'s client info",agentId);
		
		return result;
	}
	
	/**
	 * 전체 Agent 갯수를 가져온다.
	 * @return
	 */
	public int getAgentCount(){
		int result = dao.selectAgentCount();
		return result;
	}
	
	/**
	 * 페이징정보에 맞게 Agent 목록을 가져온다.
	 * @param paging
	 * @return
	 */
	public List<AgentVO> getAgentList(JQGridVO paging){
		
		List<AgentVO> result = dao.selectAgentList(paging);
		return result;
	}
	
	/**
	 * 화면에서 추가한 Agent 정보를 반영한다.
	 * @param agent
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int addAgent(AgentVO agent){
		int result = dao.insertAgent(agent);
		return result;
	}
	
	/**
	 * 화면에서 수정된 Agent 정보를 반영한다.
	 * @param agent
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int modifyAgent(AgentVO agent){
		int result = dao.updateAgent(agent);
		return result;
	}
	
	/**
	 * client id 목록에 대항하는 Agent 정보를 삭제한다.
	 * @param agentIDs
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int removeAgent(String[] agentIDs){
		int result = 0;
		for(String agentID : agentIDs){
			result += dao.deleteAgent(agentID);
			result += dao.deleteJobByClientId(agentID);
		}
		return result;
	}

	/**
	 * agent id에 해당하는 Job목록 갯수를 가져온다.
	 * @param agentId
	 * @return
	 */
	public int getJobCount(String agentId){
		int result = dao.selectJobCount(agentId);
		return result;
	}
	
	/**
	 * agent id에 해당하는 Job목록을 가져온다.
	 * @param agentId
	 * @return
	 */
	public List<JobVO> getJobList(JQGridVO searchParam, String agentId){
		List<JobVO> result = dao.selectJobList(searchParam, agentId);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>Job ID에 해당하는 Schedule 정보를 가져온다.</p>
	 * 
	 * Agent에서 webservice로 보내온 데이터를 처리할 SQL을 취득할 때 사용함
	 * </pre>
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
	
	/**
	 * 지정된 Job Id목록에 해당하는 Job정보를 삭제한다.
	 * @param jobIds
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int removeJobById(String[] jobIds){
		int result = 0;
		for(String jobId : jobIds){
			result += dao.deleteJob(jobId);
		}
		return result;
	}
	
	/**
	 * 지정된 Agent ID에 소속된 Job정보를 삭제한다.
	 * @param agentId
	 * @return
	 */
	@Transactional(value="defaultTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public int removeJobByClientId(String agentId){
		int result = dao.deleteJobByClientId(agentId);
		return result;
	}
	
	
	/**
	 * Job ID와 관계된 JDBC 정보를 취득한다.
	 * @param job
	 * @return
	 */
	public Map<String,Object> getJdbcInfo(JobVO job){
		Map<String,Object> result = dao.selectJdbcInfo(job);
		return result;
	}
}
