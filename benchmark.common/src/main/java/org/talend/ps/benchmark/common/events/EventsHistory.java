package org.talend.ps.benchmark.common.events;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.talend.ps.benchmark.common.events.TickCounter.TicksEntry;


public class EventsHistory implements History {
	private ConcurrentHashMap<Long, List<PerformanceInfo>> historyMap = new ConcurrentHashMap<Long, List<PerformanceInfo>>();
	private TickCounter counter = new TickCounter(1000);
	
	private boolean saveLoad;
	private boolean saveHistory;
	private String historyFileName;
	
	public void setSaveLoad(String saveLoadString) {
		this.saveLoad = Boolean.parseBoolean(saveLoadString);
	}

	public void setSaveHistory(String saveHistoryString) {
		this.saveHistory = Boolean.parseBoolean(saveHistoryString);
	}

	public void setHistoryFileName(String historyFileName) {
		this.historyFileName = historyFileName;
	}

	@Override
	public void start() {
		counter.start();
	}

	@Override
	public void stop() {
		counter.stop();
	}

	@Override
	public void reset() {
		historyMap.clear();
		counter.reset();
	}
	
	@Override
	public void release() {
		reset();
		counter.stop();
	}

	@Override
	public void addEvent(String id) {
		long time = System.currentTimeMillis();
		if(saveHistory) {
			Long threadID = Thread.currentThread().getId();
			List<PerformanceInfo> list = historyMap.get(threadID);
			if (list == null) {
				list = new ArrayList<PerformanceInfo>();
				historyMap.put(threadID, list);
			}
			list.add(new PerformanceInfo(id, time));
		}
		if (saveLoad) {
			counter.tick();
		}
	}
	
	
	@Override
	public String save() {
		FileOutputStream fo = null;
	    SimpleDateFormat sdf = new SimpleDateFormat(BenchmarkConstants.DATE_FORMAT);
	    String time = sdf.format(new Date());
	    
		String historyFile = String.format(historyFileName, time);
		try {
		    // 1. Save history
			if (saveHistory && !historyMap.isEmpty()) {
				fo = new FileOutputStream(historyFile);
				for (List<PerformanceInfo> list : historyMap.values()) {
					for (PerformanceInfo info : list) {
						StringBuffer out = new StringBuffer();
						out.append(info.getId()).append(";").append(info.getTime()).append("\n");
						fo.write(out.toString().getBytes());
					}
				}
			}

			// 2. Save load
			if (saveLoad) {
				String loadFile = String.format(historyFileName, "load-" + time);
				saveLoad(loadFile);
			}
		} catch(IOException e) {
			System.out.println("Cannot save history: " + e.getMessage());
			throw new IllegalStateException("Cannot save history: " + e.getMessage(), e);
		} finally {
			if (fo != null) {
				try {
					fo.close();
				} catch (IOException e) {
					System.out.println("Cannot close history file");
				}
			}
		}
		return historyFile;
	}
	
	@Override
	public void reportLoad(PrintWriter out, int displaySize) {
		List<TicksEntry> history = counter.getHistory();
		String DATE_FORMAT = "HH:mm:ss";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		for (int i = 0; (i < displaySize) && (i < history.size()); i++) {
			TicksEntry entry = history.get(history.size() - i - 1);
		    String time = sdf.format(new Date(entry.getTimestamp()));
			out.println("<p>" + time + " Count: " + entry.getCount() + " at load "
					+ entry.getLoadAverage() + "</p>");
		}
	}
	
	private void saveLoad(String fileName) throws IOException {
		FileOutputStream fos = null;
		try {
			List<TicksEntry> history = counter.getHistory();
			if (history.isEmpty()) {
				return;
			}
			String DATE_FORMAT = "HH:mm:ss";
			String CSV_HEADER = "Time; Count; CPU\n";
			String SEPARATOR = ";";
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    fos = new FileOutputStream(fileName);
		    fos.write(CSV_HEADER.getBytes());
		    for (TicksEntry entry : history) {
			    String time = sdf.format(new Date(entry.getTimestamp()));
			    StringBuffer sb = new StringBuffer();
		    	sb.append(time).append(SEPARATOR).append(entry.getCount())
						.append(SEPARATOR).append(entry.getLoadAverage())
						.append("\n");
		    	fos.write(sb.toString().getBytes());
		    }
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
	
}
