package com.synisys.test.database;

public class DBParameters {
	private String connectionString;
	private String queryPath;
	private String user;
	private String password;

	public DBParameters(String connectionString, String queryPath, String user, String password) {
		super();
		this.connectionString = connectionString;
		this.queryPath = queryPath;
		this.user = user;
		this.password = password;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public String getQueryPath() {
		return queryPath;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
