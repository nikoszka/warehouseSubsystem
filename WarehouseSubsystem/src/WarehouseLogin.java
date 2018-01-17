import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;

public class WarehouseLogin extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private DatabaseController db = new DatabaseController();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WarehouseLogin frame = new WarehouseLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WarehouseLogin() {

		setTitle("WarehouseLogin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(68, 200, 117, 29);
		contentPane.add(btnLogin);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(258, 200, 117, 29);
		contentPane.add(btnCancel);

		passwordField = new JPasswordField();
		passwordField.setBounds(68, 130, 307, 26);
		contentPane.add(passwordField);

		JFormattedTextField usernameField = new JFormattedTextField();
		usernameField.setBounds(68, 78, 307, 26);
		contentPane.add(usernameField);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(68, 63, 103, 16);
		contentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(68, 116, 61, 16);
		contentPane.add(lblPassword);

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String userpass = passwordField.getText();
				String username = usernameField.getText();

				// Username, Password verification

				db.verifyUser(userpass, username);

				if (db.isUserStatus()) {
					LoggedInUI logedUI;
					try {
						logedUI = new LoggedInUI();
						logedUI.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else {
					LoggedInUI logedUI;
					JOptionPane.showMessageDialog(null, "You are not supposed to be here", "Unauthorized user",
							JOptionPane.WARNING_MESSAGE);
					try {
						logedUI = new LoggedInUI();
						logedUI.setVisible(false);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});

	}
}
