package ejb;

import entities.Device;
import entities.IoTUser;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class DeviceDao {
	
    // Injected database connection:
	@PersistenceContext(unitName="InternetOfThings")
    private EntityManager em;
	
	
//	@Inject
//	@JMSConnectionFactory("jms/dat250/ConnectionFactory")
//	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
//	private JMSContext context;
	
//	@Resource(lookup = "jms/dat250/Topic")
//	private Topic topic;
	
    // Stores a new device:
    public void persist(Device device) throws NamingException, JMSException {
        em.persist(device);
    }

    // Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<Device> getAllDevices() {
        Query query = em.createQuery("SELECT d FROM Device d");
        List<Device> devices = new ArrayList<Device>();
        devices = query.getResultList();
        return devices;
    }
}