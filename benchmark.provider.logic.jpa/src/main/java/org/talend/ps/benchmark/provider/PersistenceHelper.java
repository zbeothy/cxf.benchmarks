package org.talend.ps.benchmark.provider;


public interface PersistenceHelper {
	
	void persist(String id);
	void persistAndDelete();
	void select();

}
