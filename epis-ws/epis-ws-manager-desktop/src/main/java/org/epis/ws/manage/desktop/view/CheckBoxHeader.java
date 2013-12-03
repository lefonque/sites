package org.epis.ws.manage.desktop.view;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBoxHeader extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6590280952207602394L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final JCheckBox checkbox;
	private int checkboxColumnIndex;

	public CheckBoxHeader(JTableHeader header, int columnIndex) {
		this.checkboxColumnIndex = columnIndex;
		checkbox = new JCheckBox();
		checkbox.setHorizontalAlignment(CENTER);
		logger.debug("Listener Added to Header!!");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JTableHeader header = table.getTableHeader();

		if ((header != null)
				&& !ArrayUtils.contains(header.getMouseListeners(), this)) {

			//checkbox.setForeground(header.getForeground());
			//checkbox.setBackground(header.getBackground());
			
		}
		checkbox.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		return checkbox;
	}
	
	public void selectAllCheckbox(TableModel model){
		
		checkbox.doClick();
		logger.debug("after doClick() selection : {}",
				checkbox.isSelected());
		
		for(int idx = 0; idx < model.getRowCount(); idx++){
			model.setValueAt(checkbox.isSelected(), idx, checkboxColumnIndex);
		}
	}
}