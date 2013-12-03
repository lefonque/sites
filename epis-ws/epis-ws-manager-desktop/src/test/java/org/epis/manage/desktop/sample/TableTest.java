package org.epis.manage.desktop.sample;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    private JTable table;
    private JScrollPane tableScrollPane;      
    String[] columnNames;

    ListSelectionModel columnSelectionModel = new DefaultListSelectionModel();


    public static void main(String[] args) {
         new TableTest();
    }
    public TableTest()
    {
         TestTableModel tableModel = new TestTableModel();
         table = new JTable(tableModel) {
              public JTableHeader createDefaultTableHeader() {
                   return new JTableHeader(columnModel) {
                        protected void processMouseEvent(MouseEvent e) {
                             switch (e.getID()) {
                             case MouseEvent.MOUSE_CLICKED:
                             case MouseEvent.MOUSE_PRESSED:
                             case MouseEvent.MOUSE_RELEASED:
                                  JTable tableView = getTable();
                                  TableColumnModel columnModel = tableView.getColumnModel();
                                  int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                                  int column = tableView.convertColumnIndexToModel(viewColumn);
                                  
                                  Rectangle bounds = tableView.getCellRect(-1, column, false);
                                  if (e.getX() < bounds.x + 16) {
                                       if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                                    	   logger.info("2 Table is Same : {}",tableView==table);
                                    	   logger.info("2 TableModel is Same : {}",table.getColumnModel()==columnModel);
                                            TableColumn tableColumn = table.getColumnModel().getColumn(column);
                                            boolean selected = !columnSelectionModel.isSelectedIndex(column);
                                            int width;
                                            if (selected) {
                                                 width = 15;
                                                 columnSelectionModel.addSelectionInterval(column, column);
                                            } else {
                                                 width = 100;
                                                 columnSelectionModel.removeSelectionInterval(column, column);
                                            }
                                            tableColumn.setMinWidth(width);
                                            tableColumn.setPreferredWidth(width); 

                                       }
                                       return;
                                  }
                             }
                             super.processMouseEvent(e);
                        }
                   };
              }
         };

//         TableRowSorter sorter = new TableRowSorter<TestTableModel>(tableModel);        
//         table.setPreferredScrollableViewportSize(new Dimension(750, 250));
//         table.setFillsViewportHeight(true);
//         table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//         table.setAutoCreateRowSorter(true);
//         table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//         table.setRowSorter(sorter);
         TableColumn column = null;
         for (int i = 0; i < columnNames.length; i++)
         {
              column = table.getColumnModel().getColumn(i);
              column.setMinWidth(100);
         }
         //        MyItemListener myItemListener = new MyItemListener(); 
         Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns(); 
         while (enumeration.hasMoreElements()) {
              TableColumn aColumn = (TableColumn)enumeration.nextElement(); 
              aColumn.setHeaderRenderer(new CheckBoxHeader()); 
         }
         tableScrollPane = new JScrollPane(table);
         JPanel panel = new JPanel(new GridLayout(1, 0));
         panel.add(tableScrollPane);
         JFrame  frame = new javax.swing.JFrame("Test");        
         frame.setContentPane(panel);              
         frame.pack();
         frame.setSize(new Dimension(200,200));
         frame.setPreferredSize(new Dimension(200,200));
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
    }     

    public class CheckBoxHeader extends JCheckBox implements TableCellRenderer /*,
   MouseListener */
    {


         //          protected int column;
         //          protected boolean mousePressed = false;          
         //          private boolean mouseListenerAdded=false;
         public CheckBoxHeader() {
              //               rendererComponent.addItemListener(itemListener);
              //               setSelected(true);
         }
         public Component getTableCellRendererComponent(JTable table,
                   Object value, boolean isSelected, boolean hasFocus, int row,
                   int column) {
              //               if (table != null) {
              //                    JTableHeader header = table.getTableHeader();
              //                    if (header != null) {
              //                         if(!mouseListenerAdded)
              //                         {
              //                              header.addMouseListener(rendererComponent);
              //                              mouseListenerAdded=true;
              //                         }
              //                    }
              //               }
              //               setColumn(column);
              setText((value == null) ? "" : value.toString());
              setOpaque(true);
              setSelected(columnSelectionModel.isSelectedIndex(column));
              setBorder(UIManager.getBorder("TableHeader.cellBorder"));
              return this;
         }
    }
    class TestTableModel extends AbstractTableModel
    {
         private Object[][] data;
         public TestTableModel()
         {
              columnNames = new String[1];
              columnNames[0] ="Col 1";
              data = new Object[5][1];
              data[0][0] = 0;
              data[1][0] = 1;
              data[2][0] = 2;
              data[3][0] = 3;
              data[4][0] = 4;
         }
         public int getColumnCount()
         {
              return columnNames.length;
         }
         public int getRowCount()
         {
              return data.length;
         }
         public String getColumnName(int col)
         {
              return columnNames[col];
         }
         public Object getValueAt(int rowIndex, int columnIndex)
         {
              return data[rowIndex][columnIndex];
         }
         public void setValueAt(Object o, int rowIndex, int columnIndex)
         {
              data[rowIndex][columnIndex] = o;
         }
    }
}