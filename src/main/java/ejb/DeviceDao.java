
package ejb;

import entities.Device;
import entities.IoTUser;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DeviceDao implements java.io.Serializable {

	// Injected database connection:
	@PersistenceContext(unitName = "InternetOfThings")
	private EntityManager em;
	private EntityManagerFactory emf;

	public DeviceDao() {
		emf = Persistence.createEntityManagerFactory("InternetOfThings");
	}

	// Stores a new device:
	public void persist(Device device) throws NamingException, JMSException {
		em = emf.createEntityManager();
		em.persist(device);
		em.close();
	}

	public void merge(Device device) throws NamingException, JMSException {
		em = emf.createEntityManager();
		em.merge(device);
		em.close();
	}
	
	public void remove(Device device) throws NamingException, JMSException {
		em = emf.createEntityManager();
		if (!em.contains(device)) {
		    device = em.merge(device);
		}
		em.remove(device);
		em.close();
	}

	// Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Device> getAllDevices() {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Device d");
		List<Device> devices = query.getResultList();
		em.close();
		return devices;
	}

	// Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Device> getPublishedDevices(int userId) {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Device d where d.published = 'true'");
		List<Device> devices = query.getResultList();
		em.close();
		return devices;
	}

	// Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Device> searchDevice(String searchKey) {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Device d where d.name LIKE '" + searchKey + "%' and d.published = 'true'");
		List<Device> devices = query.getResultList();
		em.close();
		return devices;
	}

	public Device getDevice(int id) {
		em = emf.createEntityManager();
		Query query = em.createQuery("SELECT d FROM Device d where d.id = " + id);
		List<Device> devices = query.getResultList();
		if (devices.size() > 0) {
			em.close();
			return devices.get(0);
		}
		em.close();
		return null;
	}

	 public void deleteDevice(int deviceId) {
			em = emf.createEntityManager();
			Query query = em.createNativeQuery("DELETE FROM IOTUSER_DEVICE WHERE owndevices_id = " + deviceId);
			
			System.out.println(query.executeUpdate());
		}

}