package com.synisys.test.database.metadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Table {

	private String tableName;
	private List<TableColumn> columns = new ArrayList<>();
	private List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();
	private List<PrimaryKeyColumn> primaryKeyColumns = new ArrayList<>();
	private Set<String> columnNames = new HashSet<>();
	private Table(String name) {
		this.tableName = name;
	}

	public static Table createTable(String name) {
		return new Table(name);
	}

	public TableColumn createPrimaryKeyColumn(String name, ColumnType columnType) {
		PrimaryKeyColumn tableColumn = new PrimaryKeyColumn(this, name, columnType);
		addColumn(tableColumn);
		primaryKeyColumns.add(tableColumn);
		return tableColumn;
	}

	public TableColumn createColumn(String name, ColumnType columnType) {
		TableColumn tableColumn = new TableColumn(this, name, columnType);
		addColumn(tableColumn);
		return tableColumn;
	}

	public TableColumn createForeignKeyColumn(String name, TableColumn referencedKey) {
		ForeignKeyColumn tableColumn = new ForeignKeyColumn(this, name, referencedKey.getColumnType(), referencedKey);
		addColumn(tableColumn);
		foreignKeyColumns.add(tableColumn);
		return tableColumn;
	}

	public Object getName() {
		return this.tableName;
	}

	@Override
	public int hashCode() {
		return tableName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Table) {
			return tableName.equals(((Table) obj).getName());
		}
		else {
			return false;
		}

	}

	public List<ForeignKeyColumn> getForeignKeyColumns() {
		return this.foreignKeyColumns;
	}

	public List<PrimaryKeyColumn> getPrimaryKeyColumns() {
		return this.primaryKeyColumns;
	}

	public List<TableColumn> getColumns() {
		return this.columns;
	}

	private void addColumn(TableColumn tableColumn){
		if(columnNames.contains(tableColumn.getColumnName())){
			throw new IllegalArgumentException(String.format("Column with name %s already exists in table %s", tableColumn.getColumnName(), this.tableName));
		}
		columnNames.add(tableColumn.getColumnName());
		columns.add(tableColumn);
	}
}
