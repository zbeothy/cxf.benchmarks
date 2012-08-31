package org.talend.ps.benchmark.common.events;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TickCounter {

	private AtomicLong counter = new AtomicLong(0);

	private List<TicksEntry> history = new ArrayList<TicksEntry>();

	private TicksEntry lastEntry;
	
	private Thread myThread;

	private Boolean started = Boolean.FALSE;

	private OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

	private long samplePeriod;
	
	public TickCounter(long aSamplePeriod) {
		this.samplePeriod = aSamplePeriod;
		myThread = new TickerThread(aSamplePeriod);
	}

	public void reset() {
		synchronized (this) {
			history.clear();
			counter.set(0);
		}
	}
	
	public void start() {
		synchronized (this) {
			if (started) {
				throw new IllegalStateException("TickCounter already started");
			}
			started = Boolean.TRUE;
			history = new ArrayList<TicksEntry>();
			myThread = new TickerThread(samplePeriod);
			myThread.start();
		}
	}

	public void stop() {
		synchronized (this) {
			myThread.interrupt();
		}
		try {
			myThread.join();
		} catch (InterruptedException e) {
			System.out.println("Join interrupted");
		}
		started = Boolean.FALSE;
		reset();
	}

	public void tick() {
		counter.incrementAndGet();
	}

	public List<TicksEntry> getHistory() {
		synchronized (history) {
			return new ArrayList<TicksEntry>(history);
		}
	}
	
	public class TicksEntry {
		private double loadAverage;
		private long timestamp;
		private Long count;

		public TicksEntry(Long count, long timestamp, double loadAverage) {
			this.count = count;
			this.timestamp = timestamp;
			this.loadAverage = loadAverage;
		}

		public double getLoadAverage() {
			return loadAverage;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public Long getCount() {
			return this.count;
		}
	}

	private class TickerThread extends Thread {

		private long samplePeriod;

		public TickerThread(long samplePeriod) {
			this.samplePeriod = samplePeriod;
		}

		public void run() {
			try {
				while (true) {
					Thread.sleep(samplePeriod);
					toggle();
				}
			} catch (InterruptedException e) {
				toggle();
			}
		}

		private void toggle() {
			long oldCount = counter.getAndSet(0);
			if (oldCount > 0) {
				lastEntry = new TicksEntry(new Long(oldCount), System.currentTimeMillis(), operatingSystemMXBean.getSystemLoadAverage());
				synchronized (history) {
					TickCounter.this.history.add(lastEntry);
				}
			}
		}
	}

	public TicksEntry getLastEntry() {
		return lastEntry;
	}

}
