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

	private static volatile int currentRunningTaskCount = 0;
	private static volatile int maxRunningTaskCount = 0;
	

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
			incrementCount();
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
//			Thread.currentThread().sleep(10000);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		finally{
			decrementCount();
		}

	}
	
	private static synchronized void incrementCount(){
		currentRunningTaskCount++;
		if(currentRunningTaskCount>maxRunningTaskCount){
			maxRunningTaskCount = currentRunningTaskCount;
		}
	}
	
	private static synchronized void decrementCount(){
		currentRunningTaskCount--;
	}
	
	public static int getMaxConcurentTaskCount(){
		return maxRunningTaskCount;
	}

}
