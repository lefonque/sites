package org.epis.ws.common.entity;

import java.util.List;

/**
 * <pre>
 * <p>Agent설정 및 그에 해당하는 Job설정정보를 담는 Value Object</p>
 * 
 * WebService를 이용하여 설정정보를 전송하기 위해 작성된 VO이나,
 * Agent측 properties 파일 설정을 수동으로 함에 따라, 현재는 사용안함.
 * </pre>
 * @author developer
 *
 */
public class ConfigurationVO {

	/**
	 * Agent 설정정보
	 */
	private AgentVO agentInfo;
	
	/**
	 * Agent에 소속된 Job의 목록정보
	 */
	private List<JobVO> jobList;

	public AgentVO getAgentInfo() {
		return agentInfo;
	}

	public List<JobVO> getJobList() {
		return jobList;
	}

	public void setAgentInfo(AgentVO agentInfo) {
		this.agentInfo = agentInfo;
	}

	public void setJobList(List<JobVO> jobList) {
		this.jobList = jobList;
	}
}
