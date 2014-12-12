package com.synisys.test.database;

public class IntRangeTest {

	public void test1(){
		int count = 0;
		for(ColumnValue<Integer> iterator: new IntRange("", 10, 20)){
			count++;
		}
		
		assert(count==11);
	}
	
	public void test1(){
		int count = 0;
		assert new IntRange("", 10, 20).iterator().next()==10;
		
	}
	
	public void test1(){
		int i;
		for(ColumnValue<Integer> iterator: new IntRange("", 10, 20)){
			i=iterator.getColumnValue();
		}
		assert i=20;
		
	}
}
