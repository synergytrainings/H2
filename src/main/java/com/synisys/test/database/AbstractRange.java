package com.synisys.test.database;

public abstract class AbstractRange<T> implements DataRange<T> {
	private String columnName;

	public AbstractRange(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}
}
