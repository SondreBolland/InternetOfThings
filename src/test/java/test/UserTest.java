package test;


import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.IoTUser;


/**
 * @Author Alejandro Rodriguez
 * Dat250
 * 
 * Test class for displaying the users in the database
 * 
 */

public class UserTest {

    private static final String PERSISTENCE_UNIT_NAME = "InternetOfThings";
    private static EntityManagerFactory factory;
	
    
    
	public static void main(String[] args) {
		 factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	        EntityManager em = factory.createEntityManager();
			Logger logger = Logger.getLogger("UserTest");
	        // read the existing entries and write to console
	        Query q = em.createQuery("select u from User u");
	        @SuppressWarnings("unchecked")
			List<IoTUser> users = q.getResultList();
	        for (IoTUser s : users) {
	            logger.info("User "+s.getId()+": " + s.getUsername());
	        }
	}

}
