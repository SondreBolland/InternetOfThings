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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
	
	
//	@Inject
//	@JMSConnectionFactory("jms/dat250/ConnectionFactory")
//	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
//	private JMSContext context;
	
//	@Resource(lookup = "jms/dat250/Topic")
//	private Topic topic;
	
    // Stores a new user:
    public void persist(IoTUser user) throws NamingException, JMSException {
        em.persist(user);
        //Send the topic to the JMS Topic
		//context.createProducer().setProperty("usernameUser", user.getUsername()).send(topic, user);

    }

    // Retrieves all the users:
	@SuppressWarnings("unchecked")
	public List<IoTUser> getAllUsers() {
        Query query = em.createQuery("SELECT u FROM User u");
        List<IoTUser> users = new ArrayList<IoTUser>();
        users = query.getResultList();
        return users;
    }
}