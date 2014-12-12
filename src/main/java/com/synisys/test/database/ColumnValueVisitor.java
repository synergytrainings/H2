package com.synisys.test.database;

public interface ColumnValueVisitor {
	
	public void visit(ColumnValueInteger columnValue);
	public void visit(ColumnValueDouble columnValue);
	public void visit(ColumnValueString columnValue);
}
