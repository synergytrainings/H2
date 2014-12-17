package com.synisys.test.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

import com.synisys.test.PerformanceLogger;
import com.synisys.test.database.metadata.ColumnType;
import com.synisys.test.database.metadata.Table;
import com.synisys.test.database.metadata.TableColumn;

public abstract class DatabaseCreator {
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
		String query = prepareInsertIntoQuery(table, columnRanges);
		PreparedStatement preparedStatement = connection.prepareStatement(query);

		initFunctionalTableData(0, new Stack<ColumnValue<?>>(), preparedStatement, columnRanges);
	}

	private String prepareInsertIntoQuery(Table table, DataRange<?>... columnRanges) {
		StringBuilder query = new StringBuilder();
		query.append("insert into ");
		query.append(table.getName());
		query.append(" (");
		boolean isFirst = true;
		
		for (DataRange<?> dataRange : columnRanges) {

			if (isFirst) {
				isFirst = false;
			}
			else {
				query.append(", ");
			}
			query.append(dataRange.getColumnName());
		}
		query.append(")\nvalues (");
		
		isFirst = true;
		
		for (int i=0; i<columnRanges.length; i++) {

			if (i>0) {
				query.append(", ");
			}
			query.append("?");
		}
		query.append(")");
		
		return query.toString();
	}

	private void initFunctionalTableData(int currentColumnIndex, Stack<ColumnValue<?>> columnValues,
			PreparedStatement preparedStatement, DataRange<?>... columnRanges) throws SQLException {
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

	private void insertRow(final PreparedStatement preparedStatement, Stack<ColumnValue<?>> columnValues)
			throws SQLException {

		//values
		final int[] i = new int[] { 0 };

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

		for (ColumnValue<?> columnValue : columnValues) {
			columnValue.accept(columnValueVisitor);

			i[0]++;
		}

	}

	public void build(Connection connection, Table... tables) throws SQLException {
		Set<Table> pendingTables = new LinkedHashSet<>(tables.length);//queue and set simultaneously
//		Set<Table> createdTables = new HashSet<>();
		pendingTables.addAll(Arrays.asList(tables));
		
		int maxIterationCount = pendingTables.size();
		while(pendingTables.size()>0 && maxIterationCount>=0){
			Table currentTable = pendingTables.iterator().next();
			pendingTables.remove(currentTable);
			//check if table have dependencies to tables which are not created yet
			for(TableColumn foreignKeyColumn: currentTable.getForeignKeyColumns()){
				if(pendingTables.contains(foreignKeyColumn.getTable())){// can't create this stable now, will try later
					pendingTables.add(currentTable);
					currentTable = null;
					break;
				}
			}
			
			if(currentTable!=null){
				build(connection, currentTable);
				maxIterationCount = pendingTables.size();
//				createdTables.add(currentTable);
			}
			else {
				maxIterationCount--;
			}
			
		}
		if(pendingTables.size()>0){
			throw new IllegalStateException("Some tables can't be built due to their foreign key dependency(dependency cycles)");
		}
		
	}
	
	public abstract void build(Connection connection, Table table) throws SQLException;

}
