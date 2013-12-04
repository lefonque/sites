package org.epis.ws.common.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlAccessorType(XmlAccessType.FIELD)
public class BizVO {

	private List<RecordMap> dataList;

	private String agentId;
	@XmlCDATA
	private String jobId;

	public List<RecordMap> getDataList() {
		return dataList;
	}

	public String getAgentId() {
		return agentId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setDataList(List<RecordMap> dataList) {
		this.dataList = dataList;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
}
