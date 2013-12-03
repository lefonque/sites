package org.epis.manage.desktop;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections.CollectionUtils;
import org.epis.ws.common.entity.AgentVO;

public class AgentTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4254400211964459017L;
	
	private String[] columnHeaders;
	
	private List<AgentVO> agentList;
	
	public AgentTableModel(String[] columnHeaders){
		super(columnHeaders,0);
		this.columnHeaders = columnHeaders;
	}
	
	public AgentTableModel(List<AgentVO> dataList, String[] columnHeaders){
		super(new Vector<AgentVO>(dataList),convertToVector(columnHeaders));
		this.columnHeaders = columnHeaders;
	}
	
	public void setColumnHeaders(String[] columnHeaders){
		this.columnHeaders = columnHeaders;
	}
	@Override
	public int getRowCount() {
		int result = CollectionUtils.isEmpty(agentList) ? 0 : agentList.size();
		return result;
	}

	@Override
	public int getColumnCount() {
		return columnHeaders.length;
	}
	
	@Override
	public String getColumnName(int column) {
		
		return columnHeaders[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AgentVO agent = agentList.get(rowIndex);
		Object result = null;
		switch(columnIndex){
		case 0:
			result = "";
			break;
			
		case 1:
			result = agent.getAgentId();
			break;
			
		case 2:
			result = agent.getOperatingSystem();
			break;
			
		case 3:
			result = agent.getWebsvcUser();
			break;
			
		case 4:
			result = agent.getSmsUseYn();
			break;
			
		case 5:
			result = agent.getOfficerName();
			break;
			
		case 6:
			result = agent.getCreatedDate();
			break;
			
		case 7:
			result = agent.getModifiedDate();
			break;
		}
		return result;
	}

	public void setAgentList(List<AgentVO> agentList){
		this.agentList = agentList;
	}
	
	public void addAgent(AgentVO agent){
		this.agentList.add(agent);
	}
}
