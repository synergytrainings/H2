package com.synisys.test;

public class DBParameters {
	private String connectionString;
	private String queryPath;

	public DBParameters(String connectionString, String queryPath) {
		this.connectionString = connectionString;
		this.queryPath = queryPath;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public String getQueryPath() {
		return queryPath;
	}

}
