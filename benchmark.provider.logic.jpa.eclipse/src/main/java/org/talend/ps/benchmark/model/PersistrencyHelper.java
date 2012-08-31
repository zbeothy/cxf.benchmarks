package org.talend.ps.benchmark.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PersistrencyHelper {
	
	private static final String QUERY_ALL_PERSONS = "ALL_PERSONS";
	private EntityManagerFactory factory;
	
	public PersistrencyHelper() {
		factory = Persistence.createEntityManagerFactory("TEST-LOCAL");
	}
	
	public void persist(String id) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Person person = new Person(id, "Name", "TwitterName"); 
		em.persist(person);
		em.getTransaction().commit();
	}

	public void persistAndDelete() {
		EntityManager em = factory.createEntityManager();
		String id = UUID.randomUUID().toString();
		persist(id); 
		em.getTransaction().begin();
		Person person = em.find(Person.class, id);
		em.remove(person);
		em.getTransaction().commit();
	}

	public void select() {
		EntityManager em = factory.createEntityManager();
		Query preparedQuery = em.createNamedQuery(QUERY_ALL_PERSONS);
		@SuppressWarnings("unchecked")
		List<Person> results = preparedQuery.getResultList();
		results.size();
	}
}
