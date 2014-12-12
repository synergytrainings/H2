package com.synisys.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

import com.synisys.test.database.ColumnValue;
import com.synisys.test.database.ColumnValueDouble;
import com.synisys.test.database.ColumnValueInteger;
import com.synisys.test.database.ColumnValueString;
import com.synisys.test.database.ColumnValueVisitor;
import com.synisys.test.database.DataRange;
import com.synisys.test.database.DoubleRange;
import com.synisys.test.database.IntRange;
import com.synisys.test.database.StringRange;

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

		initFunctionalTableData("TableName", new IntRange("ProjectId", 10, 20), new IntRange("DonorId", 0, 3), new DoubleRange("CommitedAmount", 100.0, 200.0,
				300.0), new StringRange("Comment", "Prefix", 1, 5));

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

	private void initFunctionalTableData(String tableName, DataRange<?>... columnRanges) throws SQLException {
		String query = "";//generate query
		PreparedStatement preparedStatement = null;//create statement
		initFunctionalTableData(tableName, 0, new Stack<ColumnValue<?>>(), preparedStatement, columnRanges);
	}

	private void initFunctionalTableData(String tableName, int columnIndex, Stack<ColumnValue<?>> columnValues, PreparedStatement preparedStatement,
			DataRange<?>... columnRanges) throws SQLException {
		if (columnIndex == columnRanges.length) {
			insertRow(tableName, preparedStatement, columnValues);
		}
		else {
			for (ColumnValue<?> columnValue : columnRanges[columnIndex]) {
				columnValues.push(columnValue);
				initFunctionalTableData(tableName, columnIndex + 1, columnValues, preparedStatement, columnRanges);
				columnValues.pop();
			}
		}
	}

	private void insertRow(String tableName, final PreparedStatement preparedStatement, Stack<ColumnValue<?>> columnValues) throws SQLException {
		
		//values
		final int []i=new int[]{0}; 
		
		ColumnValueVisitor columnValueVisitor = new ColumnValueVisitor() {
			
			@Override
			public void visit(ColumnValueString columnValue) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void visit(ColumnValueDouble columnValue) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void visit(ColumnValueInteger columnValue) {
				try {
					preparedStatement.setInt(i[0], columnValue.getColumnValue());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				
			}
		};
		
		for(ColumnValue<?> columnValue : columnValues){
			columnValue.accept(columnValueVisitor);
			
			i[0]++;
		}
	}

}
