package com.synisys.test.database;

import java.util.Iterator;

public class IntRange extends AbstractRange<Integer> {

	private int start;
	private int end;

	public IntRange(String columnName, int start, int end) {
		super(columnName);
		this.start = start;
		this.end = end;
		ColumnValueInteger x=null;
		ColumnValue<Integer> y = x;
	}

	@Override
	public Iterator<ColumnValue<Integer>> iterator() {
		return new Iterator<ColumnValue<Integer>>() {
			private int i = start;
			@Override
			public boolean hasNext() {
				return i<=end;
			}

			@Override
			public ColumnValueInteger next() {
				return new ColumnValueInteger(i++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
				
			}
		};
		
		
	}

}
