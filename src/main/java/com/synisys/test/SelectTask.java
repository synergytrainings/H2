package com.synisys.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

public class SelectTask implements Runnable {
	private String id;
	private String connectionString;
	private String databaseUser;
	private String databasePassword;
	private PerformanceLogger performanceLogger;

	// public static final String H2_CONNECTION_STRING =
	// "jdbc:h2:tcp://sis2w095/mem:db1;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=3;";
	//
	// public static final String MSSQL_CONNECTION_STRING =
	// "jdbc:jtds:sqlserver://sis2s027:1433;DatabaseName=cu_timor;selectmethod=Cursor";
	// public static final String QUERY1 =
	// "SELECT top 1  min(View_IIs.CreatedUser) A2_II_CreatedUser ,  min(View_IIs.NameCode) A2_II_Name_IIs , isnull(View_IIs.II_IIs, '-1') II_IIs , 0 IREPORTBRANCHID, sum(1) as TempColumn FROM (SELECT View_IIs.*  , ApplicationStatusID AS II_ApplicationStatuses, BeneficiaryTypeID AS II_BeneficiaryTypes, DisabilityLevelID AS II_DisabilityLevels, DisabilityTypeID AS II_DisabilityTypes, IIID AS II_IIs, SocialAnimatorID AS II_SocialAnimators, StateProgramID AS II_StatePrograms  FROM View_IIs) View_IIs GROUP BY  View_IIs.II_IIs;";
	// public static final String QUERY2 = "SELECT * from queryAnalyzer";
	//
	// public static final String DURATIONS_QUERY =
	// "insert into queryAnalyzer (threadId, duration) values (?, ?)";

	public SelectTask(String id, String connectionString, String databaseUser,
			String databasePassword, PerformanceLogger performanceLogger) {
		this.id = id;
		this.connectionString = connectionString;
		this.databaseUser = databaseUser;
		this.databasePassword = databasePassword;
		this.performanceLogger = performanceLogger;
	}

	@Override
	public void run() {
		try (Connection connection = DriverManager.getConnection(
				connectionString, databaseUser, databasePassword)) {

			String query = "";
			try (InputStream inputStream = DatabaseCreator.class
					.getClassLoader().getResourceAsStream(
							"com/synisys/test/query1.sql")) {
				query = IOUtils.toString(inputStream, "Cp1252");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// String query = QUERY1;
			long startTime = System.currentTimeMillis();
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultset = statement.executeQuery(query)) {
					int columnCount = resultset.getMetaData().getColumnCount();
					while (resultset.next()) {
						for (int i = 1; i <= columnCount; ++i) {
							resultset.getObject(i);
						}
					}

				}

			}
			long duration = System.currentTimeMillis() - startTime;

			/*
			 * try(PreparedStatement preparedStatement =
			 * connection.prepareStatement(DURATIONS_QUERY)){
			 * preparedStatement.setLong(1, Thread.currentThread().getId());
			 * preparedStatement.setLong(2, duration);
			 * preparedStatement.executeUpdate();
			 * 
			 * }
			 */
			// System.out.println(duration);
			performanceLogger.add(id, duration);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
