package org.talend.ps.benchmark.model;


public interface PersistenceHelper {
	
	void persist(String id);
	void persistAndDelete();
	void select();
}
