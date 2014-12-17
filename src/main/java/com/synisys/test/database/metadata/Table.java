package com.synisys.test.database.metadata;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String name;
	private List<TableColumn> columns = new ArrayList<>();
	private List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();
	private List<PrimaryKeyColumn> primaryKeyColumns = new ArrayList<>();

	private Table(String name) {
		this.name = name;
	}

	public static Table createTable(String name) {
		return new Table(name);
	}

	public TableColumn createPrimaryKeyColumn(String string, ColumnType i) {
		PrimaryKeyColumn tableColumn = new PrimaryKeyColumn(this, name, ColumnType.INT);
		columns.add(tableColumn);
		primaryKeyColumns.add(tableColumn);
		return tableColumn;
	}

	public TableColumn createColumn(String name, ColumnType columnType) {
		TableColumn tableColumn = new TableColumn(this, name, columnType);
		columns.add(tableColumn);
		return tableColumn;
	}

	public TableColumn createForeignKeyColumn(String string, TableColumn referencedKey) {
		ForeignKeyColumn tableColumn = new ForeignKeyColumn(this, name, ColumnType.INT, referencedKey);
		columns.add(tableColumn);
		foreignKeyColumns.add(tableColumn);
		return tableColumn;
	}

	public Object getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Table) {
			return name.equals(((Table) obj).getName());
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

}
