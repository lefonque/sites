package org.epis.ws.common.entity;

import java.util.List;

public class ConfigurationVO {

	private AgentVO agentInfo;
	
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
