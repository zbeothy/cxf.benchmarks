package org.talend.ps.benchmark.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class PersistenceHelperImpl implements PersistenceHelper {

	private static final String QUERY_ALL_PERSONS = "ALL_PERSONS";
	private EntityManager em;
//	private EntityManagerFactory emFactory;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public void setEmFactory(EntityManagerFactory emFactory) {
//		this.emFactory = emFactory;
		em = emFactory.createEntityManager();
	}

	public void persist(String id) {
		try {
//			em.getTransaction().begin();
			System.out.println("ID: " + id);
			Person person = new Person(id, "Name", "TwitterName");
			System.out.println("Before persist");
			em.persist(person);
			System.out.println("After persist");
			em.flush();
			System.out.println("After flush");
//			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Persistency error: "
					+ e.getMessage(), e);
		}
	}

	public void persistAndDelete() {
		try {
			String id = UUID.randomUUID().toString();
			persist(id);
			Person person = em.find(Person.class, id);
			em.remove(person);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Persistency error: "
					+ e.getMessage(), e);
		}
	}

	public void select() {
		Query preparedQuery = em.createNamedQuery(QUERY_ALL_PERSONS);
		@SuppressWarnings("unchecked")
		List<Person> results = preparedQuery.getResultList();
		results.size();
	}
}
