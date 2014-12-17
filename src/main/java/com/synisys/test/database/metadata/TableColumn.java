package com.synisys.test.database.metadata;

public class TableColumn {

	
	private Table table;
	private String name;
	private ColumnType columnType;
	public TableColumn(Table table, String name, ColumnType columnType) {
		this.table = table;
		this.name = name;
		this.columnType = columnType;
	}
	public String getColumnName() {
		return this.name;
	}
	public Table getTable() {
		return table;
	}
	
	public ColumnType getColumnType() {
		return columnType;
	}


}
