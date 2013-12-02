package org.epis.ws.common.entity;

import java.util.List;

public class BizVO {

	private List<MapWrapper> dataList;
	
	private String agentId;
	
	private String jobId;

	public List<MapWrapper> getDataList() {
		return dataList;
	}

	public String getAgentId() {
		return agentId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setDataList(List<MapWrapper> dataList) {
		this.dataList = dataList;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
}
