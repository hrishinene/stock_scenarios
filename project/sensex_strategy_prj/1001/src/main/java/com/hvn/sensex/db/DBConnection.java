package com.hvn.sensex.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hvn.sensex.utils.Constants;

public class DBConnection implements AutoCloseable {
	Connection con;

	public DBConnection() {
		try {
			con = DriverManager.getConnection(Constants.DBUrl, Constants.Username, Constants.Password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void close() throws Exception {
		// TODO Auto-generated method stub
		con.close();

	}

	public ResultSet executeQuery(String query) {
		PreparedStatement preparedStatement;
		try {
//			System.out.println(query);
			preparedStatement = con.prepareStatement(query);
			return preparedStatement.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
