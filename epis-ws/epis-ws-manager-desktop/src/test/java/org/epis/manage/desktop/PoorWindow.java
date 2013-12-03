package org.epis.manage.desktop;

import java.awt.EventQueue;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import org.epis.ws.manager.core.service.ConfigurationService;
import org.epis.ws.manager.entity.JQGridVO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PoorWindow {

	private JFrame frame;
	private JTable tblAgent;
	private AgentTableModel tableModel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PoorWindow window = new PoorWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ConfigurationService configService;
	private ListSelectionModel columnSelectionModel;
	/**
	 * Create the application.
	 */
	public PoorWindow() {
		ApplicationContext ctx
			= new ClassPathXmlApplicationContext("classpath*:/META-INF/spring/context-*.xml");
		configService = ctx.getBean(ConfigurationService.class);
		columnSelectionModel = new DefaultListSelectionModel();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		String[] headerText = {"","Agent ID","OS","웹서비스유저명","SMS여부","담당자명","등록일자","수정일자"};
		tableModel = new AgentTableModel(headerText);
		
		JQGridVO paging = new JQGridVO();
		paging.setPage(1);
		paging.setSidx("agent_id");
		paging.setSord("asc");
		paging.setRows(10);
		tableModel.setAgentList(configService.getAgentList(paging));
		
//		tblAgent = new JTable(tableModel);
		tblAgent = new JTable(tableModel){
			/**
			 * 
			 */
			private static final long serialVersionUID = -3518682878192126384L;

			@Override
			public JTableHeader createDefaultTableHeader() {
				return new AgentTableHeader(columnModel, columnSelectionModel);
			}
		};
		TableColumn col = tblAgent.getColumnModel().getColumn(0);
		col.setHeaderRenderer(new CheckboxColumnHeader(columnSelectionModel));
		
		JScrollPane scrollpAgent = new JScrollPane(tblAgent);
		frame.getContentPane().add(scrollpAgent, "cell 0 0,grow");
		
	}

}
