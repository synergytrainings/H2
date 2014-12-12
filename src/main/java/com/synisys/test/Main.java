package com.synisys.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	@SuppressWarnings("unused")
	private static DBParameters mssql = new DBParameters(
			"jdbc:jtds:sqlserver://sis2s027:1433;DatabaseName=cu_timor_test;selectmethod=Cursor",
			"com/synisys/test/queries/mssql", "sa", "sa");

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

	private static final int THREAD_COUNT = 10;
	private static final int ROWS_COUNT = 10_000;//100_000;

	//private static final String DATABASE_USER = "sa";

	//private static final String DATABASE_PASSWORD = "sa";

	public static void main(String[] args) throws Exception {

		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		Class.forName("org.postgresql.Driver");

		DBParameters dbParameters = postgre;//
		PerformanceLogger performanceLogger = new PerformanceLogger();

		try (Connection connection = DriverManager.getConnection(dbParameters.getConnectionString(),
				dbParameters.getUser(), dbParameters.getPassword())) {
			new DatabaseCreator(dbParameters.getQueryPath(), performanceLogger).createDatabase(connection, ROWS_COUNT);
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

		System.out.println("Max running threads:" + SelectTask.getMaxConcurentTaskCount());
	}

}