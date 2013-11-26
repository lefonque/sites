package org.epis.manage.desktop.model;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import org.epis.manage.desktop.ui.CheckBoxHeader;

public class AgentTableColumnModel extends DefaultTableColumnModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6558794608412523769L;
	
	public void init(){
		TableColumn column = new TableColumn(0);
		column.setHeaderRenderer(new CheckBoxHeader(0));
		column.setWidth(10);
		this.addColumn(column);
		for(AgentTableColumnEnum columnEnum : AgentTableColumnEnum.values()){
			column = new TableColumn(columnEnum.getIndex());
			column.setHeaderValue(columnEnum.getHeaderText());
			this.addColumn(column);
		}
	}
	
	public AgentTableColumnModel(){
		init();
	}
	
}
