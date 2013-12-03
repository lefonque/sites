package org.epis.ws.manage.desktop.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.AgentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentTableModel extends AbstractTableModel {

	private final Logger logger = LoggerFactory.getLogger(AgentTableModel.class);

//	private final String[] columnName;
	
	private final List<AgentVO> dataList;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8466209088692459873L;
	
	public AgentTableModel(){
		this(null);
	}
	
	public AgentTableModel(final List<AgentVO> dataList){
//		this.columnName = columnName;
		if(dataList==null){
			this.dataList = new ArrayList<AgentVO>();
		}
		else{
			this.dataList = dataList;
		}
	}

	@Override
	public int getRowCount() {
		int result = dataList.size();
		return result;
	}

	@Override
	public int getColumnCount() {
		return AgentTableColumnEnum.values().length;
	}
	
	public void addAgentInfo(AgentVO agentInfo){
		int rowIndex = dataList.size();
		dataList.add(agentInfo);
		fireTableRowsInserted(rowIndex, rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AgentVO row = dataList.get(rowIndex);
		AgentTableColumnEnum columnEnum = AgentTableColumnEnum.valueOf(columnIndex);
		String result = null;
		
		if(columnEnum!=null){
			String exceptionMessage = "EXCEPTION OCCURRED BY BeanUtils";
			if(StringUtils.isNotEmpty(columnEnum.getFieldName())){
				try {
					result = BeanUtils.getSimpleProperty(row, columnEnum.getFieldName());
				} catch (IllegalAccessException e) {
					logger.error(exceptionMessage, e);
				} catch (InvocationTargetException e) {
					logger.error(exceptionMessage, e);
				} catch (NoSuchMethodException e) {
					logger.error(exceptionMessage, e);
				}
			}
		}
		return result;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		AgentVO row = dataList.get(rowIndex);
		AgentTableColumnEnum columnEnum = AgentTableColumnEnum.valueOf(columnIndex);
		
		if(columnEnum!=null){
			if(StringUtils.isNotEmpty(columnEnum.getFieldName())){
				String exceptionMessage = "EXCEPTION OCCURRED BY BeanUtils";
				try {
					BeanUtils.setProperty(row, columnEnum.getFieldName(), aValue);
				} catch (IllegalAccessException e) {
					logger.error(exceptionMessage, e);
				} catch (InvocationTargetException e) {
					logger.error(exceptionMessage, e);
				}
			}
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	public void setList(List<AgentVO> list){
		dataList.clear();
		dataList.addAll(list);
		fireTableDataChanged();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> result = null;
		switch(columnIndex){
		case 0:
			result = Boolean.class;
			break;
			
		default:
			result = super.getColumnClass(columnIndex);
			break;
		}
		return result;
	}
	
	@Override
	public String getColumnName(int column) {
		AgentTableColumnEnum columnEnum = AgentTableColumnEnum.valueOf(column);
		return columnEnum.getHeaderText();
	}
}
