package com.synisys.test;

public class PerformanceMeasure {
	private final String threadId;
	private final long duration;

	public PerformanceMeasure(String threadId, long duration) {
		this.threadId = threadId;
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public String getThreadId() {
		return threadId;
	}
}
