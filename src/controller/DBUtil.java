package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	static final String DRIVER = "com.mysql.jdbc.Driver";
	static final String URL = "jdbc:mysql://localhost/studentdb";

	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(URL, "root", "123456");
		return con;
	}
}
