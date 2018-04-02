package com.cjb.baiduyun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	// JDBC 驱动名及数据库 URL
	static final private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final private String DB_URL = "jdbc:mysql://localhost:3306/baiduyun";
	// 数据库的用户名与密码，需要根据自己的设置
	static final private String USER = "root";
	static final private String PASS = "123";
	private Connection conn = null;
	private Statement stmt = null;

	public void initConn() {
		try {
			// 注册 JDBC 驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 打开链接
			System.out.println("连接数据库...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// 执行查询
			System.out.println("实例化Statement成功...");
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public void closeConn() throws SQLException {
		if (stmt != null)
			stmt.close();
		if (conn != null)
			conn.close();
	}

	public boolean exeSql(String sql) throws SQLException {
		return stmt.executeUpdate(sql)>0;
	}
}
