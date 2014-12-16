package com.synisys.test.database;

import com.synisys.test.database.metadata.TableColumn;

public abstract class AbstractRange<T> implements DataRange<T> {
	private TableColumn tableColumn;

	public AbstractRange(TableColumn columnName) {
		this.tableColumn = columnName;
	}

	@Override
	public String getColumnName() {
		return tableColumn.getColumnName();
	}
}
