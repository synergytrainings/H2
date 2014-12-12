package com.synisys.test.database;

public class ColumnValueInteger extends AbstractColumnValue<Integer> {

	public ColumnValueInteger(Integer value) {
		super(value);
	}

	@Override
	public void accept(ColumnValueVisitor columnValueVisitor) {
		columnValueVisitor.visit(this);
		
	}

}
