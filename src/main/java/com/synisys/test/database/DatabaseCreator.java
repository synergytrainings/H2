package com.synisys.test.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

import com.synisys.test.PerformanceLogger;
import com.synisys.test.database.metadata.ColumnType;
import com.synisys.test.database.metadata.Table;
import com.synisys.test.database.metadata.TableColumn;

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

//		initTableData("View_IIs", new IntRange("IIID", 10, 20), new IntRange("IICount", 0, 3), new IntRange("PersonID", 5, 30),
//				new IntRange("BeneficiaryTypeID", 5, 30), new IntRange("DisabilityTypeID", 5, 30), new IntRange("DisabilityLevelID", 0, 3), 
//				new StringRange("Statusname_POR", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 10, 50),
//				new StringRange("CreatedUser", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 20, 60),
//				new StringRange("NameCode", "aaaaaaaaa", 20, 60));
//
//		
		
		
		
		
		
		
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

	public void initTableData(Table table,Connection connection, DataRange<?>... columnRanges) throws SQLException {
		StringBuilder query = new StringBuilder();//generate query
		query.append("insert into ");
		query.append(table.getName());
		
		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());

		initFunctionalTableData(0, new Stack<ColumnValue<?>>(), preparedStatement, columnRanges);
	}
	
	private void initQuery() {
		
	}

	private void initFunctionalTableData(int currentColumnIndex, Stack<ColumnValue<?>> columnValues, PreparedStatement preparedStatement,
			DataRange<?>... columnRanges) throws SQLException {
		if (currentColumnIndex == columnRanges.length) {
			insertRow(preparedStatement, columnValues);
		}
		else {
			for (ColumnValue<?> columnValue : columnRanges[currentColumnIndex]) {
				columnValues.push(columnValue);
				initFunctionalTableData(currentColumnIndex + 1, columnValues, preparedStatement, columnRanges);
				columnValues.pop();
			}
		}
	}

	private void insertRow(final PreparedStatement preparedStatement, Stack<ColumnValue<?>> columnValues) throws SQLException {
		
		//values
		final int []i=new int[]{0}; 
		
		ColumnValueVisitor columnValueVisitor = new ColumnValueVisitor() {
			
			@Override
			public void visit(ColumnValueString columnValue) {
				try {
					preparedStatement.setString(i[0], columnValue.getColumnValue());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				
			}
			
			@Override
			public void visit(ColumnValueDouble columnValue) {
				try {
					preparedStatement.setDouble(i[0], columnValue.getColumnValue());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				
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

	public void build(Table ...tables) {
		// TODO Auto-generated method stub
		
	}

}
