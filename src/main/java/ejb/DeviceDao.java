package ejb;

import entities.Device;
import entities.IoTUser;

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
public class DeviceDao {
	
    // Injected database connection:
	@PersistenceContext(unitName="InternetOfThings")
    private EntityManager em;
	
	public DeviceDao(){
        em = Persistence.createEntityManagerFactory("InternetOfThings").createEntityManager();
    }

    // Stores a new device:
    public void persist(Device device) throws NamingException, JMSException {
        em.persist(device);
    }

    // Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Device> getAllDevices() {
        Query query = em.createQuery("SELECT d FROM Device d");
        List<Device> devices = query.getResultList();
        return devices;
    }

    public Device getDevice(int id) {
        Query query = em.createQuery("SELECT d FROM Device d where d.id = "+id);
        List<Device> devices = query.getResultList();
        if(devices.size()>0)
            return devices.get(0);
        return null;
    }
}