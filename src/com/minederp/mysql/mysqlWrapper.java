package com.minederp.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

public class mysqlWrapper {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public void connectDatabase() throws Exception {
		try { // This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Setup the connection with the DB

			Properties pro=new Properties();
			pro.put("user", "dested");
			pro.put("password", "minederp");
			connect = DriverManager.getConnection("jdbc:mysql://minederp.com/dested",pro);
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
		} catch (Exception e) {
			throw e;
		} 
	}
 
	public ResultSet selectQuery(String columns, String table, String extra) throws SQLException {
		// Result set get the result of the SQL query
		resultSet = statement.executeQuery("select " + columns + " from " + table + " " + extra+";");
		// writeResultSet(resultSet);
		return resultSet;
	}

	public void insertQuery(String table,String qru) throws SQLException {

		statement.execute("insert into " + table + " values ( "+qru+"  );");
		 
	}
	public void updateQuery(String query) throws SQLException {

		// PreparedStatements can use variables and are more efficient
		preparedStatement = connect.prepareStatement(query);
	
		preparedStatement.executeUpdate();
	}

	public void deleteQuery(String table, String conditional) throws SQLException {
		// Remove again the insert comment
		preparedStatement = connect.prepareStatement("delete from " + table + " where ?; ");
		preparedStatement.setString(1, conditional);
		preparedStatement.executeUpdate();
 

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summery = resultSet.getString("summery");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summery: " + summery);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

}
