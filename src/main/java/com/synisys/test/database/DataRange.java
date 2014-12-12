package com.synisys.test.database;

public interface DataRange<T> extends Iterable<ColumnValue<T>>{
	String getColumnName();
}
