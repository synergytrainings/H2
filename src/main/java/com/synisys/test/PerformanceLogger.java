package com.synisys.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerformanceLogger {

	private List<PerformanceMeasure> items = Collections.synchronizedList(new ArrayList<PerformanceMeasure>());

	private PerformanceMeasure databaseCreation;

	public void add(String threadId, long duration) {
		items.add(new PerformanceMeasure(threadId, duration));
	}

	public void addDatabaseCreation(long duration) {
		databaseCreation = new PerformanceMeasure("Database creation time", duration);
	}

	private void printMeasure(PerformanceMeasure performanceMeasure, PrintStream writer) {
		writer.print(String.format("%s (seconds):\t%2$,.1f\n", performanceMeasure.getName(),
				performanceMeasure.getDuration() / 1000.0));// seconds
	}

	public void printAggregatedStatistics(PrintStream writer) {
		if (items.size() == 0) {
			return;
		}
		long minTime = Long.MAX_VALUE;
		long maxTime = 0;
		long avgTime = 0;
		for (PerformanceMeasure performanceMeasure : items) {
			if (performanceMeasure.getDuration() > maxTime) {
				maxTime = performanceMeasure.getDuration();
			}
			if (performanceMeasure.getDuration() < minTime) {
				minTime = performanceMeasure.getDuration();
			}
			avgTime += performanceMeasure.getDuration();
		}
		avgTime /= items.size();
		
		printMeasure(databaseCreation, writer);
		
		writer.print(String.format("Min time (seconds):\t%1$,.1f\n", minTime / 1000.0));// seconds
		writer.print(String.format("Max time (seconds):\t%1$,.2f\n", maxTime / 1000.0));// seconds
		writer.print(String.format("Average time (seconds):\t%1$,.2f\n", avgTime / 1000.0));// seconds
	}

	public void printAll(PrintStream writer) {
		for (PerformanceMeasure performanceMeasure : items) {
			writer.print(String.format("time:\t%1$,.2f\n", performanceMeasure.getDuration() / 1000.0));// seconds
		}
	}

}
