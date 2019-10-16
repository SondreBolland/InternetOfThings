package ejb;

import entities.Device;
import entities.IoTUser;

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
import java.util.ArrayList;
import java.util.List;


@Stateless
public class DeviceDao implements java.io.Serializable{
	
    // Injected database connection:
	@PersistenceContext(unitName="InternetOfThings")
    private EntityManager em;
	private EntityManagerFactory emf;
	
	
	//Messaging connection factory
	@Inject
	@JMSConnectionFactory("jms/InternetOfThings/ConnectionFactory")
	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
	private JMSContext context;
	
	@Resource(lookup = "jms/InternetOfThings/Topic")
	private Topic topic;
	
	
	public DeviceDao(){
        emf = Persistence.createEntityManagerFactory("InternetOfThings");
    }

    // Stores a new device:
    public void persist(Device device) throws NamingException, JMSException {
	    em = emf.createEntityManager();
        em.persist(device);
        em.close();
      //Send the topic to the JMS Topic
      //context.createProducer().setProperty("topicUser", tweet.getTopic()).send(topic, tweet);
        context.createProducer().setProperty("topicDevice", device.getTopic()).send(topic, device);
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

    public Device getDevice(int id) {
	    em = emf.createEntityManager();
        Query query = em.createQuery("SELECT d FROM Device d where d.id = "+id);
        List<Device> devices = query.getResultList();
        if(devices.size()>0){
            em.close();
            return devices.get(0);
        }
        em.close();
        return null;
    }
}