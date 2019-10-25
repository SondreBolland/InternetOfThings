package ejb;

import entities.Device;
import entities.Register;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSSessionMode;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RegisterDao {

	@Inject
	@JMSConnectionFactory("jms/InternetOfThings/ConnectionFactory")
	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
	private JMSContext context;

	@Resource(lookup = "jms/InternetOfThings/Topic")
	private Topic topic;

	// Injected database connection: does not inject??
	@PersistenceContext(unitName = "InternetOfThings")
	private EntityManager em;
	private EntityManagerFactory emf;

	public RegisterDao() {
		emf = Persistence.createEntityManagerFactory("InternetOfThings");
	}

	// Stores a new registration:
	public void persist(Register register) throws NamingException, JMSException {
		em = emf.createEntityManager();
		em.persist(register);
		em.close();

	}

	public void sendMessage(Register register) throws NamingException {
		// get the initial context
		InitialContext ctx = new InitialContext();

		// lookup the topic object
		Topic topic = (Topic) ctx.lookup("jms/InternetOfThings/Topic");

		// lookup the topic connection factory
		TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx
				.lookup("jms/InternetOfThings/ConnectionFactory");
		context = connFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE);
		context.createProducer().setProperty("topicDevice", register.getTopic()).send(topic, register);
	}

	// Updates a new registration:
	public void merge(Register register) throws NamingException, JMSException {
		em = emf.createEntityManager();
		em.merge(register);
		em.close();
	}

	public void remove(Register register) throws NamingException, JMSException {
		em = emf.createEntityManager();
		if (!em.contains(register)) {
			register = em.merge(register);
		}
		em.remove(register);
		em.close();
	}

	public Register getRegister(Register register) {
		em = emf.createEntityManager();
		if (!em.contains(register)) {
			register = em.merge(register);
		}
		em.close();
		return register;
	}

	// Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Register> getAllRegistrations() {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Register d");
		List<Register> registrations = query.getResultList();
		em.close();
		return registrations;
	}

	public List<Register> getRegistrationsForDevice(int deviceId) {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Register d WHERE d.user.id = " + deviceId);
		List<Register> registrations = new ArrayList<Register>();
		registrations = query.getResultList();
		em.close();
		return registrations;
	}

	public List<Integer> getRegistrationIdsForDevice(int deviceId) {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d.id FROM Register d WHERE d.device.id = " + deviceId);
		List<Integer> registrations = query.getResultList();
		em.close();
		return registrations;
	}

	public Register getRegistrationForDevice(int deviceId, int registrationId) {
		em = emf.createEntityManager();
		Query query = em.createQuery(
				"SELECT d FROM Register d WHERE d.device.id = " + deviceId + " AND d.id = " + registrationId);
		List<Register> registrations = query.getResultList();
		if (registrations.size() > 0) {
			em.close();
			return registrations.get(0);
		}
		em.close();
		return null;
	}

	public void deleteRegister(int registerId) {
		em = emf.createEntityManager();
		Query query = em.createNativeQuery("DELETE FROM IOTUSER_REGISTER WHERE subscribeddevices_id = " + registerId);

		System.out.println(query.executeUpdate());
	}

	@SuppressWarnings("unchecked")
	public List<Register> getRequests(int userId) {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Register d WHERE d.user.id = " + userId);
		List<Register> registrations = query.getResultList();
		em.close();
		return registrations;
	}

}