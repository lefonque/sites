package org.epis.manage.desktop.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBoxHeader implements TableCellRenderer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final JCheckBox checkbox;
	private int checkboxColumnIndex;

	public CheckBoxHeader(JTableHeader header, int columnIndex) {
		this.checkboxColumnIndex = columnIndex;
		checkbox = new JCheckBox();
		
		logger.debug("Listener Added to Header!!");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JTableHeader header = table.getTableHeader();

		if ((header != null)
				&& !ArrayUtils.contains(header.getMouseListeners(), this)) {

			checkbox.setForeground(header.getForeground());
			checkbox.setBackground(header.getBackground());
			
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