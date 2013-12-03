package org.epis.manage.desktop;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class AgentTableHeader extends JTableHeader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6420157642527706354L;
	
	private ListSelectionModel columnSelectionModel;
	public AgentTableHeader(TableColumnModel columnModel, ListSelectionModel columnSelectionModel){
		super(columnModel);
		this.columnSelectionModel = columnSelectionModel;
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e) {
		switch(e.getID()){
		case MouseEvent.MOUSE_CLICKED:
		case MouseEvent.MOUSE_PRESSED:
		case MouseEvent.MOUSE_RELEASED:
			JTable tbl = getTable();
			TableColumnModel colModel = tbl.getColumnModel();
			int modelIndex = tbl.convertColumnIndexToModel(colModel.getColumnIndexAtX(e.getX()));
			Rectangle rect = tbl.getCellRect(-1, modelIndex, false);
			if(e.getX() < rect.x + 16){
				if(e.getID()==MouseEvent.MOUSE_CLICKED){
					boolean selected = !columnSelectionModel.isSelectedIndex(modelIndex);
					if(selected){
						columnSelectionModel.addSelectionInterval(modelIndex, modelIndex);
					}
					else{
						columnSelectionModel.removeSelectionInterval(modelIndex, modelIndex);
					}
				}
				break;
			}
			super.processMouseEvent(e);
			break;
		default:
			super.processMouseEvent(e);
			break;
		}
	}
	
	
}
