package org.talend.ps.benchmark.common.events;

public class PerformanceInfo {
	private final String id;
	private final long time;
	
	public PerformanceInfo(String id, long time) {
		this.id = id;
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public long getTime() {
		return time;
	}
}
