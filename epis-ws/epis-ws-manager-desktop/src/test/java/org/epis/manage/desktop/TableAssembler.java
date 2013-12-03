package org.epis.manage.desktop;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableAssembler {

	public TableColumnModel assembleAgentTableColumns(String[] headerText){
		TableColumnModel result = new DefaultTableColumnModel();
		TableColumn column = null;
		for(int idx = 0; idx < headerText.length; idx++){
			column = new TableColumn(idx);
			column.setHeaderValue(headerText[idx]);
			result.addColumn(column);
		}
		return result;
	}
	
}
