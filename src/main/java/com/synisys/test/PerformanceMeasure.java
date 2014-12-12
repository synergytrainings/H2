package com.synisys.test;

public class PerformanceMeasure {
	private final String name;
	private final long duration;

	public PerformanceMeasure(String name, long duration) {
		this.name = name;
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}
}
