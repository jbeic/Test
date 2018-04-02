package com.cjb.baiduyun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	// JDBC �����������ݿ� URL
	static final private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final private String DB_URL = "jdbc:mysql://localhost:3306/baiduyun";
	// ���ݿ���û��������룬��Ҫ�����Լ�������
	static final private String USER = "root";
	static final private String PASS = "123";
	private Connection conn = null;
	private Statement stmt = null;

	public void initConn() {
		try {
			// ע�� JDBC ����
			Class.forName("com.mysql.jdbc.Driver");
			// ������
			System.out.println("�������ݿ�...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// ִ�в�ѯ
			System.out.println("ʵ����Statement�ɹ�...");
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
