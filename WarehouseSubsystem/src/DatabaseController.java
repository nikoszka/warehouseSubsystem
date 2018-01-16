import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.Executor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;

public class DatabaseController {

	public boolean userStatus = false;
	private static String wheels;

	public Connection connect() {
		// String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:sqlite:warehouse.db";
		Connection connection = null;

		try {
			// Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url);
			System.out.println("Connected to database");

		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return connection;
	}

	// insert from java to sqlite
	public void insertGoods(String itemName, int amount, int itemID, String unit, int priority) {
		String sql = "INSERT INTO items(itemName, amount, itemID, unit, priority) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, itemName);
			pstmt.setInt(2, amount);
			pstmt.setInt(3, itemID);
			pstmt.setString(4, unit);
			pstmt.setInt(5, priority);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	// was used for adding users
	public void insertUsers(String username, String password) {
		String sql = "INSERT INTO users(username, password) VALUES(?,?)";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	//updating database on AGV call
	public void updateSupplies(int amount, String itemName) {
		String sql = "UPDATE items SET amount = amount - ? WHERE itemName = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, amount);
			pstmt.setString(2, itemName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	
	public void updateItems(int amount, String itemName) {
		String sql = "UPDATE items SET amount = ? WHERE itemName = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, amount);
			pstmt.setString(2, itemName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void verifyUser(String uname, String pw) {
		String sql = "SELECT username, password " + "FROM users WHERE username = ? AND password = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, uname);
			pstmt.setString(2, pw);
			int count = 0;
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				count = count + 1;
				if (count == 1) {
					System.out.println("Authorized user");
					userStatus = true;
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void orderSupplies() {
		WarehouseCommunicationManager wcm = new WarehouseCommunicationManager();
		int baseAmount = 500;
		int orderAmount = (baseAmount / 100) * 20;
		String sql = "SELECT itemName, amount " + "FROM items WHERE amount < ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setDouble(1, orderAmount);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String itemName = rs.getString("itemName");
				int amount1 = rs.getInt("amount");
				if (amount1 < orderAmount) {
					int x = baseAmount - amount1;
					updateItems(x, itemName);
					System.out.println("Ordered items automatically " + itemName + " " + x);
					wcm.orderMaterial(itemName, x);
					wcm.arrangeNeeded();
				} else {
					int x = baseAmount - amount1;
					updateItems(x, itemName);
					System.out.println(itemName + " " + x);
					wcm.orderMaterial(itemName, x);
					wcm.arrangeNeeded();

				}
			}
			System.out.println("Order sent to monitoring system");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public boolean isUserStatus() {
		return userStatus;
	}

	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}

}
