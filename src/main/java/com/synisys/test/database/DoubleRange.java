package com.synisys.test.database;

import java.util.Iterator;

import com.synisys.test.database.metadata.TableColumn;

public class DoubleRange extends AbstractRange<Double> {

	private Double[] values;
	private int index = 0;

	public DoubleRange(TableColumn tableColumn, Double... values) {
		super(tableColumn);
		this.values = values;
	}

	@Override
	public Iterator<ColumnValue<Double>> iterator() {

		return new Iterator<ColumnValue<Double>>() {
			boolean isFinished = false;

			@Override
			public boolean hasNext() {
				return isFinished;
			}

			@Override
			public ColumnValueDouble next() {
				isFinished = true;
				Double value = values[index];
				index = (index + 1) % values.length;
				return new ColumnValueDouble(value);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();

			}
		};
	}

}
