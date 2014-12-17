package com.synisys.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.synisys.test.database.DBParameters;
import com.synisys.test.database.DatabaseCreator;
import com.synisys.test.database.H2DatabaseCreator;

public class Main {

	@SuppressWarnings("unused")
	private static DBParameters mssql = new DBParameters(
			"jdbc:jtds:sqlserver://sis2s027:1433;DatabaseName=cu_timor_test;selectmethod=Cursor",
			"com/synisys/test/queries/mssql", "sa", "sa");

	@SuppressWarnings("unused")
	private static DBParameters postgre = new DBParameters("jdbc:postgresql://localhost:5434/cu_timor",
			"com/synisys/test/queries/postgre", "postgres", "root");

	@SuppressWarnings("unused")
	private static DBParameters h2Embedded = new DBParameters(
			"jdbc:h2:mem:db1;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;LOCK_MODE=0;LOG=0", "com/synisys/test/queries/h2",
			"sa", "sa");

	@SuppressWarnings("unused")
	private static DBParameters h2ServerMode = new DBParameters(
			"jdbc:h2:tcp://localhost/mem:db1;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;", "com/synisys/test/queries/h2", "sa",
			"sa");

	private static final int THREAD_COUNT = 100;
	private static final int ROWS_COUNT = 100_000;//100_000;

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		DBParameters dbParameters = h2ServerMode;//
		PerformanceLogger performanceLogger = new PerformanceLogger();
		DatabaseCreator databaseCreator = new H2DatabaseCreator(dbParameters.getQueryPath(), performanceLogger); 
		main.init();
		//main.test1(performanceLogger, dbParameters, databaseCreator);
		main.test2(performanceLogger, dbParameters, databaseCreator);
	}
	private void test2(PerformanceLogger performanceLogger, DBParameters dbParameters, DatabaseCreator databaseCreator) throws SQLException{
		SampleDb1 sampleDB1 = new SampleDb1();
		try (Connection connection = DriverManager.getConnection(dbParameters.getConnectionString(),
				dbParameters.getUser(), dbParameters.getPassword())) {
			sampleDB1.build(databaseCreator, connection);
		}
		
	
	}
	
	private  void init() throws ClassNotFoundException{
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		Class.forName("org.postgresql.Driver");
	}
	
	public void test1(PerformanceLogger performanceLogger, DBParameters dbParameters, DatabaseCreator databaseCreator) throws Exception {

		try (Connection connection = DriverManager.getConnection(dbParameters.getConnectionString(),
				dbParameters.getUser(), dbParameters.getPassword())) {
			 databaseCreator.createDatabase(connection, ROWS_COUNT);
		}
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.execute(new SelectTask(String.valueOf(i), dbParameters.getConnectionString(), dbParameters
					.getUser(), dbParameters.getPassword(), performanceLogger, dbParameters.getQueryPath()));
		}
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		performanceLogger.printAll(System.out);
		System.out.println("");
		performanceLogger.printAggregatedStatistics(System.out);

		System.out.println("Max running threads:\t" + SelectTask.getMaxConcurentTaskCount());
	}

}