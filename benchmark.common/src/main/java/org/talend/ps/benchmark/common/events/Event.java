package org.talend.ps.benchmark.common.events;

import java.util.UUID;


public class Event {
	
	private final String id;
	private final String content;
	
	public Event(String id) {
		this.id = id;
		this.content = null;
	}

	public Event(UUID id, String content) {
		this.id = id.toString();
		this.content = content;
	}

	public Event(String id, String content) {
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
