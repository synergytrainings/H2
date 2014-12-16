package com.synisys.test.database;

public class ColumnValueString extends AbstractColumnValue<String> {

	public ColumnValueString(String value) {
		super(value);
	}

	@Override
	public void accept(ColumnValueVisitor columnValueVisitor) {
		columnValueVisitor.visit(this);
		
	}

}
