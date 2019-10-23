package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSSessionMode;
import javax.jms.Topic;
import javax.naming.NamingException;
import javax.persistence.*;

import entities.IoTUser;

/**
 * 
 * @author Alejandro Rodriguez
 * Dat250 course
 *
 *Data Access Object connecting the Database with the business logic
 */

@Stateless
public class UserDao {
    // Injected database connection:
	@PersistenceContext(unitName="InternetOfThings")
    private EntityManager em;
	private EntityManagerFactory emf;

	public UserDao(){
		emf = Persistence.createEntityManagerFactory("InternetOfThings");
    }
	
    // Stores a new user:
    public void persist(IoTUser user) throws NamingException, JMSException {
	    em = emf.createEntityManager();
        em.persist(user);
        em.close();
    }
    
    public void merge(IoTUser user) throws NamingException, JMSException {
    	em = emf.createEntityManager();
    	em.merge(user);
    	em.close();
    }

    // Retrieves all the users:
	@SuppressWarnings("unchecked")
	public List<IoTUser> getAllUsers() {
	    em = emf.createEntityManager();
        Query query = em.createQuery("SELECT u FROM IoTUser u");
        List<IoTUser> users =  query.getResultList();
        em.close();
        return users;
    }

    // Retrieves the user with the given username:
	@SuppressWarnings("unchecked")
	public IoTUser getUser(String username) {
	    em = emf.createEntityManager();
        Query query = em.createQuery("SELECT u FROM IoTUser u WHERE u.username = '" + username + "'");
        List<IoTUser> users =  query.getResultList();
        IoTUser user = null;
        if(users.size()>0)
            user = users.get(0);
        em.close();
        return user;
    }
}