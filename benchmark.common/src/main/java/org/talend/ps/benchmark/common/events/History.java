package org.talend.ps.benchmark.common.events;

import java.io.PrintWriter;

public interface History {
	void start();

	void stop();

	void reset();

	void release();

	void addEvent(String id);

	String save();

	void reportLoad(PrintWriter out, int displaySize);
}
