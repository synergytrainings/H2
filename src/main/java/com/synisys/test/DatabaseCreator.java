package com.synisys.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

public class DatabaseCreator {
	private static final String DATA_STRING = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	private String pathForQueries;
	private PerformanceLogger performanceLogger;

	public DatabaseCreator(String pathForQueries, PerformanceLogger performanceLogger) {
		this.pathForQueries = pathForQueries;
		this.performanceLogger = performanceLogger;
	}

	public void createDatabase(Connection connection, int rowsCount) throws IOException, SQLException {
		long startTime = System.currentTimeMillis();
		createTable(connection);
		initData(connection, rowsCount);
		performanceLogger.addDatabaseCreation(System.currentTimeMillis() - startTime);
	}

	private void createTable(Connection connection) throws IOException, SQLException {
		String dropQuery = getQuery(pathForQueries + "/droptable.sql");

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(dropQuery);
		}

		String createQuery = getQuery(pathForQueries + "/createtable.sql");

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createQuery);
		}

	}

	private void initData(Connection connection, int rowsCount) throws IOException, SQLException {

		String insertQuery = getQuery(pathForQueries + "/insertTable.sql");

		try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			for (int i = 0; i < rowsCount; i++) {
				initRow(preparedStatement, i);
				preparedStatement.execute();
			}

		}
	}

	private void initRow(PreparedStatement preparedStatement, int i) throws SQLException {
		preparedStatement.setInt(1, i);
		preparedStatement.setInt(2, i);
		preparedStatement.setInt(3, i);
		preparedStatement.setInt(4, i);
		preparedStatement.setInt(5, i);
		preparedStatement.setInt(6, i);
		final int STATUS_NAME_LENGTH = 150;
		final int CREATED_USER_LENGTH = 101;
		final int NAME_CODE_LENGTH = 15;
		String index = String.valueOf(i);
		preparedStatement.setString(7, DATA_STRING.substring(0, STATUS_NAME_LENGTH - index.length()) + index);// 150
		preparedStatement.setString(8, DATA_STRING.substring(0, CREATED_USER_LENGTH - index.length()) + index);// 101
		preparedStatement.setString(9, DATA_STRING.substring(0, NAME_CODE_LENGTH - index.length()) + index);// 15
	}

	private String getQuery(String resoursePath) throws IOException {
		try (InputStream inputStream = DatabaseCreator.class.getClassLoader().getResourceAsStream(resoursePath)) {
			return IOUtils.toString(inputStream, "Cp1252");
		}
	}
}
