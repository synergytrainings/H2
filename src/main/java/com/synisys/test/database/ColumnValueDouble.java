package com.synisys.test.database;

public class ColumnValueDouble extends AbstractColumnValue<Double> {

	public ColumnValueDouble(Double value) {
		super(value);
	}

	@Override
	public void accept(ColumnValueVisitor columnValueVisitor) {
		columnValueVisitor.visit(this);		
	}

	
}
