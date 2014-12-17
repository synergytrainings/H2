package com.synisys.test.database;

import java.util.Iterator;

import com.synisys.test.database.metadata.TableColumn;

public class StringRange extends AbstractRange<String> {

	private int index;
	private int start;
	private int end;
	private String prefix;

	public StringRange(TableColumn tableColumn, String prefix, int start, int end) {
		super(tableColumn);
		this.start = start;
		this.end = end;
		this.index = start;
		this.prefix = prefix;
	}

	@Override
	public Iterator<ColumnValue<String>> iterator() {
		return new Iterator<ColumnValue<String>>() {
			boolean isFinished = false;

			@Override
			public boolean hasNext() {
				return !isFinished;
			}

			@Override
			public ColumnValueString next() {
				isFinished = true;
				
				ColumnValueString columnValueString =  new ColumnValueString(prefix + index);
				index++;
				if(index == end){
					index = start;
				}
				return columnValueString;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();

			}
		};
	}
}
