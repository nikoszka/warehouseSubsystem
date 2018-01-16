import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.DefaultMenuLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class LoggedInUI extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private ResultSet rs = null;
	private Statement stmt = null;
	private DatabaseController db = new DatabaseController();

	/**
	 * Launch the application.
	 */

	public void run() {
		try {
			LoggedInUI frame = new LoggedInUI();
			frame.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */
	public LoggedInUI() throws SQLException {

		setTitle("Warehouse");
		initialize();
		

	}

	public void initialize() throws SQLException {

		loadData();
		db.orderSupplies();
		

		DefaultTableModel tableModel = new DefaultTableModel();
		JTable table = new JTable(tableModel);
		table.setModel(DbUtils.resultSetToTableModel(rs));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 650, 500);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		table.setBounds(20, 23, 613, 377);
		JScrollPane panel = new JScrollPane(table);
		getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JButton btnFirstOrder = new JButton("Force Order");
		btnFirstOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				db.orderSupplies();
			}
		});

		btnFirstOrder.setBounds(135, 52, 121, 25);
		panel_1.add(btnFirstOrder);

		JButton btnPrior = new JButton("Priotize");
		btnPrior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					loadPriority();
					table.setModel(DbUtils.resultSetToTableModel(rs));

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnPrior.setBounds(396, 52, 97, 25);
		panel_1.add(btnPrior);

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					db.connect().close();
					System.out.println("Connection Closed");
					System.exit(0);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLogout.setBounds(523, 188, 97, 25);
		panel_1.add(btnLogout);
	}

	private void loadData() throws SQLException {
		
		stmt = db.connect().createStatement();
		rs = stmt.executeQuery("SELECT * FROM items");
		
	}

	private void loadPriority() throws SQLException {

		stmt = db.connect().createStatement();
		rs = stmt.executeQuery("SELECT * FROM items ORDER BY priority ASC");
		

	}
}
