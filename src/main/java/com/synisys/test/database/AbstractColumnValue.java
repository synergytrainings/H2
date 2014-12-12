package com.synisys.test.database;

public abstract class AbstractColumnValue<T> implements ColumnValue<T>{

	private T value;
	public AbstractColumnValue(T value) {
		this.value = value;
	}
	@Override
	public T getColumnValue() {
		return value;
	}

	

}
