package ejb;

import entities.Device;
import entities.Register;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class RegisterDao {
	
    // Injected database connection: does not inject??
	@PersistenceContext(unitName="InternetOfThings")
    private EntityManager em;
	private EntityManagerFactory emf;

	public RegisterDao(){
        emf = Persistence.createEntityManagerFactory("InternetOfThings");
    }

    // Stores a new registration:
    public void persist(Register register) throws NamingException, JMSException {
	    em = emf.createEntityManager();
        em.persist(register);
        em.close();
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
        Query query = em.createQuery("SELECT d FROM Register d WHERE d.device.id = " + deviceId +
                " AND d.id = " + registrationId);
        List<Register> registrations = query.getResultList();
        if(registrations.size()>0){
            em.close();
            return registrations.get(0);
        }
        em.close();
        return null;
    }

}