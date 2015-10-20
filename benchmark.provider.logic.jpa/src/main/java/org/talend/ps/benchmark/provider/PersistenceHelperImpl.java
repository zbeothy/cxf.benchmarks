package org.talend.ps.benchmark.provider;

import org.talend.ps.benchmark.model.Person;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

public class PersistenceHelperImpl implements PersistenceHelper {

	static final String QUERY_ALL_PERSONS = "ALL_PERSONS";

	EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void persist(String id) {
		try {
			Person person = new Person(id, "Name", "TwitterName");
			entityManager.persist(person);
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Persistency error: " + e.getMessage(), e);
		}
	}

	public void persistAndDelete() {
		try {
			String id = UUID.randomUUID().toString();
			persist(id);
			Person person = entityManager.find(Person.class, id);
			entityManager.remove(person);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Persistency error: " + e.getMessage(), e);
		}
	}

	public void select() {
		Query preparedQuery = entityManager.createNamedQuery(QUERY_ALL_PERSONS);
		@SuppressWarnings("unchecked")
		List<Person> results = preparedQuery.getResultList();
		results.size();
	}
}
