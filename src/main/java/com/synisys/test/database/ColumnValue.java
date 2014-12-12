package com.synisys.test.database;

public interface ColumnValue<T> {

	public T getColumnValue();
	
	public void accept(ColumnValueVisitor columnValueVisitor);
}
