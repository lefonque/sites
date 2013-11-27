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
		TableColumn column = null;
		for(AgentTableColumnEnum columnEnum : AgentTableColumnEnum.values()){
			
			column = new TableColumn(columnEnum.getIndex());
			switch(columnEnum){
			case CHECKBOX:
				column.setHeaderRenderer(new CheckBoxHeader(columnEnum.getIndex()));
				column.setPreferredWidth(10);
				break;
				
			default:
				column.setHeaderValue(columnEnum.getHeaderText());
				break;
			}
			this.addColumn(column);
		}
	}
	
	public AgentTableColumnModel(){
		init();
	}
	
}
