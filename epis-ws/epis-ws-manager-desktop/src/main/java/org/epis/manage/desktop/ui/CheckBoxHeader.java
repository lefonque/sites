package org.epis.manage.desktop.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBoxHeader implements TableCellRenderer, MouseListener {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final JCheckBox checkbox;
	private int columnIndex;
	public CheckBoxHeader(int columnIndex){
		this.columnIndex = columnIndex;
		checkbox = new JCheckBox();
		checkbox.addMouseListener(this);
		logger.debug("Listener Added!!");
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5425996642109018334L;

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		
		JTableHeader header = table.getTableHeader();
		
		if((header!=null) && !ArrayUtils.contains(header.getMouseListeners(), this)){
			
			checkbox.setForeground(header.getForeground());
			checkbox.setBackground(header.getBackground());
		}
		checkbox.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		return checkbox;
	}
	
	protected void handlerClickEvent(MouseEvent e){
		JTableHeader header = JTableHeader.class.cast(e.getSource());
		JTable table = header.getTable();
		
		int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
		int modelIndex = table.convertColumnIndexToModel(columnIndex);
		logger.debug("columnIndex : {}\tmodelIndex : {}",new Object[]{columnIndex,modelIndex});
		logger.debug("selection : {}",checkbox.isSelected());
		if(columnIndex==this.columnIndex){
			checkbox.doClick();
			logger.debug("after doClick() selection : {}",checkbox.isSelected());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		handlerClickEvent(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}