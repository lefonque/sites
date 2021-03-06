package org.epis.ws.common.entity;


/**
 * <pre>
 * <p>웹서비스를 통해 송수신될 업무 데이터를 담는 Value Object</p>
 * 데이터의 구성은 다음과 같다.
 * 	-비지니스 데이터 리스트
 * 	-Agent ID
 * 	-Job ID
 * </pre>
 * @author developer
 *
 */
public class BizVO {

	/**
	 * <pre>
	 * <p>비지니스 데이터 리스트</p>
	 * 
	 * 웹서비스에서는 Map계열 Collection을 직접적으로 사용할 수 없기 때문에
	 * Map을 wrapping하는 MapWrapper클래스를 사용하여 List에 담음.
	 * </pre>
	 */
	private String jsonData;

	/**
	 * Agent ID
	 */
	private String agentId;
	
	/**
	 * Job ID
	 */
	private String jobId;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
}
