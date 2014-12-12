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
			"com/synisys/test/queries/mssql");
	@SuppressWarnings("unused")
	private static DBParameters h2Embedded = new DBParameters(
			"jdbc:h2:mem:db1;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;LOCK_MODE=0;LOG=0", "com/synisys/test/queries/h2");
	@SuppressWarnings("unused")
	private static DBParameters h2ServerMode = new DBParameters(
			"jdbc:h2:tcp://localhost/mem:db1;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;", "com/synisys/test/queries/h2");

	private static final int THREAD_COUNT = 100;
	private static final int ROWS_COUNT = 100_000;//100_000;
	private static final String DATABASE_USER = "sa";

	private static final String DATABASE_PASSWORD = "sa";

	public static void main(String[] args) throws Exception {

		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		DBParameters dbParameters = h2Embedded;//
		PerformanceLogger performanceLogger = new PerformanceLogger();

		try (Connection connection = DriverManager.getConnection(dbParameters.getConnectionString(), DATABASE_USER,
				DATABASE_PASSWORD)) {
			new DatabaseCreator(dbParameters.getQueryPath(), performanceLogger).createDatabase(connection, ROWS_COUNT);
		}
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.execute(new SelectTask(String.valueOf(i), dbParameters.getConnectionString(),
					DATABASE_USER, DATABASE_PASSWORD, performanceLogger));
		}
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		performanceLogger.printAll(System.out);
		System.out.println("");
		performanceLogger.printAggregatedStatistics(System.out);
		
		System.out.println("Max running threads:" + SelectTask.getMaxConcurentTaskCount());
	}

}