package org.epis.manage.desktop;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class CheckboxColumnHeader extends JCheckBox implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5608348165238097282L;

	private ListSelectionModel columnSelectionModel;
	
	public CheckboxColumnHeader(ListSelectionModel columnSelectionModel){
		this.columnSelectionModel = columnSelectionModel;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setText("");
        setOpaque(true);
		setSelected(columnSelectionModel.isSelectedIndex(column));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		return this;
	}

}
