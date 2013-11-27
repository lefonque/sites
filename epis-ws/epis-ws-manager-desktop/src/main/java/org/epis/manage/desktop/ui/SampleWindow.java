package org.epis.manage.desktop.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import net.miginfocom.swing.MigLayout;

import org.epis.manage.desktop.model.AgentTableColumnEnum;
import org.epis.manage.desktop.model.AgentTableModel;
import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.provider.entity.JQGridVO;
import org.epis.ws.provider.service.core.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SampleWindow {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private JFrame frame;
	private JTable tblAgent;
	private JTable tblJobs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/context-common.xml"
				,"classpath:/META-INF/spring/context-db.xml"
				,"classpath:/META-INF/spring/context-tx.xml");
		
		ConfigurationService service = ctx.getBean(ConfigurationService.class);
		JQGridVO searchParam = new JQGridVO();
		searchParam.setPage(1);
		searchParam.setSidx("agent_id");
		searchParam.setSord("asc");
		searchParam.setRows(10);
		final List<AgentVO> rows = service.getAgentList(searchParam);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SampleWindow window = new SampleWindow();
					window.frame.setVisible(true);
					

					window.tblModelAgent.setList(rows);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	final AgentTableModel tblModelAgent;
	/**
	 * Create the application.
	 */
	public SampleWindow() {

		tblModelAgent = new AgentTableModel();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(245, 255, 250));
		frame.setBounds(100, 100, 700, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][][][][][grow][][]"));
		
		JLabel lblClientList = new JLabel("Client List");
		lblClientList.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblClientList, "cell 0 0,alignx left,aligny center");
		
		JScrollPane scrollPaneClients = new JScrollPane();
		frame.getContentPane().add(scrollPaneClients, "cell 0 1,grow");
		
		
		tblAgent = new JTable(tblModelAgent);
		
		final int checkboxColumnIndex = AgentTableColumnEnum.CHECKBOX.getIndex();
		final CheckBoxHeader checkboxHeader
			= new CheckBoxHeader(tblAgent.getTableHeader(),checkboxColumnIndex);
		JTableHeader header = tblAgent.getTableHeader();
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader header = JTableHeader.class.cast(e.getSource());
				JTable table = header.getTable();

				int columnModelIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
				int modelIndex = table.convertColumnIndexToModel(columnModelIndex);
				logger.debug("columnModelIndex : {}\tmodelIndex : {}", new Object[] {
						columnModelIndex, modelIndex });
				
				if (modelIndex == checkboxColumnIndex) {
					checkboxHeader.selectAllCheckbox(table.getModel());
				}
			}
		});
		tblAgent.getColumnModel()
			.getColumn(checkboxColumnIndex).setHeaderRenderer(checkboxHeader);
		
		
		scrollPaneClients.setViewportView(tblAgent);
		
		
		//Pagination for Agent List
		JButton btnPrevAgentList = new JButton("prev");
		frame.getContentPane().add(btnPrevAgentList, "flowx,cell 0 2,alignx center,aligny center");

		JComboBox cmbPage = new JComboBox();
		frame.getContentPane().add(cmbPage, "cell 0 2");
		
		JButton btnNextAgentList = new JButton("next");
		frame.getContentPane().add(btnNextAgentList, "cell 0 2");
//		tblClients.setModel(new DefaultTableModel(
//			new Object[][] {
//			},
//			new String[] {
//				"Org Code", "Client ID", "Officer", "Contact"
//			}
//		));
		
		//Buttons for Managing Agent Data
		JButton btnAddClient = new JButton("Add");
		frame.getContentPane().add(btnAddClient, "flowx,cell 0 3,alignx right");

		JButton btnModifyClient = new JButton("Modify");
		frame.getContentPane().add(btnModifyClient, "cell 0 3,alignx right");
		
		JButton btnRemoveClient = new JButton("Remove");
		frame.getContentPane().add(btnRemoveClient, "cell 0 3,alignx right");
		
		JSeparator dividePart = new JSeparator();
		dividePart.setToolTipText("For Testing");
		dividePart.setForeground(new Color(128, 0, 0));
		frame.getContentPane().add(dividePart, "cell 0 4,grow");
		
		
		
		//Job List
		JLabel lblJobList = new JLabel("Job List");
		lblJobList.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblJobList, "cell 0 5,alignx left,aligny center");
		
		JScrollPane scrollPaneJobs = new JScrollPane();
		frame.getContentPane().add(scrollPaneJobs, "cell 0 6,grow");
		
		tblJobs = new JTable();
		tblJobs.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Job ID", "Job Name", "DB Type", "JDBC URL" 
			}
		));
		scrollPaneJobs.setViewportView(tblJobs);
		
		JCheckBox chckbxTestcheck = new JCheckBox("testCheck");
		frame.getContentPane().add(chckbxTestcheck, "flowx,cell 0 7,alignx center");
		
		//pagination for Job List
		JButton btnPrevJobList = new JButton("prev");
		frame.getContentPane().add(btnPrevJobList, "cell 0 7,alignx center,aligny center");

		JComboBox cmbPageJobList = new JComboBox();
		frame.getContentPane().add(cmbPageJobList, "cell 0 7");
		
		JButton btnNextJobList = new JButton("next");
		frame.getContentPane().add(btnNextJobList, "cell 0 7");
		
		//Buttons for Managing Job Data
		JButton btnAddJob = new JButton("Add");
		frame.getContentPane().add(btnAddJob, "flowx,cell 0 8,alignx right");
		
		JButton btnModifyJob = new JButton("Modify");
		frame.getContentPane().add(btnModifyJob, "cell 0 8,alignx right");
		
		JButton btnRemoveJob = new JButton("Remove");
		frame.getContentPane().add(btnRemoveJob, "cell 0 8,alignx right");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu1");
		menuBar.add(mnMenu);
		
		JMenuItem mntmSub = new JMenuItem("Sub1");
		mnMenu.add(mntmSub);
		
		JMenuItem mntmSub_1 = new JMenuItem("Sub2");
		mnMenu.add(mntmSub_1);
		
		mnMenu.addSeparator();
		
		JMenuItem mntmSub_2 = new JMenuItem("Sub3");
		mnMenu.add(mntmSub_2);
		
		JMenuItem mntmSub_3 = new JMenuItem("Sub4");
		mnMenu.add(mntmSub_3);
		
	}
	
}
