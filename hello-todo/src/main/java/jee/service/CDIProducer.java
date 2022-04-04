package jee.service;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//by default has dependency scope

//creates a producer with entityManager context

public class CDIProducer {
	@Produces
	@PersistenceContext
	EntityManager entityManager;

}
