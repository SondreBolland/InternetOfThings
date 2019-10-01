package ejb;

import entities.Device;
import entities.Register;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class RegisterDao {
	
    // Injected database connection:
	@PersistenceContext(unitName="InternetOfThings")
    private EntityManager em;

	public RegisterDao(){
        em = Persistence.createEntityManagerFactory("InternetOfThings").createEntityManager();
    }

    // Stores a new registration:
    public void persist(Register register) throws NamingException, JMSException {
        em.persist(register);
    }

    // Updates a new registration:
    public void merge(Register register) throws NamingException, JMSException {
        em.merge(register);
    }

    // Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Register> getAllRegistrations() {
        Query query = em.createQuery("SELECT d FROM Register d");
        List<Register> registrations = query.getResultList();
        return registrations;
    }

    public List<Register> getRegistrationsForDevice(int deviceId) {
        Query query = em.createQuery("SELECT d FROM Register d WHERE d.user.id = " + deviceId);
        List<Register> registrations = new ArrayList<Register>();
        registrations = query.getResultList();
        return registrations;
    }

    public List<Integer> getRegistrationIdsForDevice(int deviceId) {
        Query query = em.createQuery("SELECT d.id FROM Register d WHERE d.device.id = " + deviceId);
        List<Integer> registrations = query.getResultList();
        return registrations;
    }

    public Register getRegistrationForDevice(int deviceId, int registrationId) {
        Query query = em.createQuery("SELECT d FROM Register d WHERE d.device.id = " + deviceId +
                " AND d.id = " + registrationId);
        List<Register> registrations = query.getResultList();
        if(registrations.size()>0)
            return registrations.get(0);
        return null;
    }

}