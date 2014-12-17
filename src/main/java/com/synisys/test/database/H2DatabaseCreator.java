package com.synisys.test.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.synisys.test.PerformanceLogger;
import com.synisys.test.database.metadata.ColumnType;
import com.synisys.test.database.metadata.ForeignKeyColumn;
import com.synisys.test.database.metadata.KeyType;
import com.synisys.test.database.metadata.Table;
import com.synisys.test.database.metadata.TableColumn;

public class H2DatabaseCreator extends DatabaseCreator {
	public H2DatabaseCreator(String pathForQueries, PerformanceLogger performanceLogger) {
		super(pathForQueries, performanceLogger);
	}

	public void build(Connection connection, Table table) throws SQLException {
		StringBuilder query = new StringBuilder();
		query.append("CREATE TABLE ");
		query.append(table.getName());
		query.append("(");
		//		boolean isFirst = true;
		query.append(Joiner.on(",").join(Iterables.transform(table.getColumns(), new Function<TableColumn, String>() {
			public String apply(TableColumn column) {
				return column.getColumnName() + " " + getDatabaseType(column.getColumnType()) + " NULL";
			}
		})));
		//		for(TableColumn column: table.getColumns()){
		//			if(isFirst){
		//				isFirst = false;
		//			}
		//			else {
		//				query.append(",");
		//			}
		//			query.append(column.getColumnName());
		//			query.append(" ");
		//			query.append(getDatabaseType(column.getType()));
		//		}
		if (table.getPrimaryKeyColumns().size() > 0) {
			query.append(",\nPRIMARY KEY (");
			query.append(Joiner.on(",").join(
					Iterables.transform(table.getPrimaryKeyColumns(), new Function<TableColumn, String>() {
						public String apply(TableColumn column) {
							return column.getColumnName();
						}
					})));
			query.append(")");

		}
		if (table.getForeignKeyColumns().size() > 0) {
			query.append(",\n");
			query.append(Joiner.on(",").join(
					Iterables.transform(table.getForeignKeyColumns(), new Function<ForeignKeyColumn, String>() {
						public String apply(ForeignKeyColumn column) {
							TableColumn referencedColumn = column.getReferencedKeyColumn();
							return "FOREIGN KEY (" + column.getColumnName() + ") REFERENCES public."
									+ referencedColumn.getTable().getName() + "(" + referencedColumn.getColumnName()
									+ ")";
						}
					})));

		}
		query.append(")");
		try(Statement statement = connection.createStatement()){
			statement.executeUpdate(query.toString());
		}
		
	}

	protected String getDatabaseType(ColumnType columnType) {
		switch (columnType) {
			case DECIMAL6_36:
				return "decimal(36, 6)";
			case INT:
				return "int";
			case STRING100:
				return "nvarchar(100)";
			case STRING1000:
				return "nvarchar(1000)";
			case STRING4000:
				return "nvarchar(4000)";
			case STRING8000:
				return "nvarchar(8000)";
			default:
				throw new IllegalStateException();
		}
	}

}
