package com.synisys.test.database.metadata;

public class ForeignKeyColumn extends TableColumn {

	private TableColumn referencedKeyColumn;

	public ForeignKeyColumn(Table table, String name, ColumnType columnType, TableColumn referencedKeyColumn) {
		super(table, name, columnType);
		this.referencedKeyColumn = referencedKeyColumn;
	}
	
	public TableColumn getReferencedKeyColumn() {
		return referencedKeyColumn;
	}

}
