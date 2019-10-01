package ejb;

import entities.Device;
import entities.IoTUser;
import entities.Register;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Named(value = "deviceController")
@Stateless
public class DeviceController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Injected DAO EJB:
	@EJB
	private DeviceDao deviceDao;
	private RegisterDao registerDao;
	private UserDao userDao;

	public DeviceController(){
		deviceDao = new DeviceDao();
		registerDao = new RegisterDao();
		userDao = new UserDao();
	}

	public List<Device> getDevices() {
		List<Device> deviceList = new ArrayList<Device>();
		deviceList.addAll(this.deviceDao.getAllDevices());
		return deviceList;
	}

	public List<Register> getRegistrations(int deviceId) {
		List<Register> registrations = new ArrayList<Register>();
		registrations.addAll(this.registerDao.getRegistrationsForDevice(deviceId));
		return registrations;
	}

	public List<Integer> getRegistrationIds(int deviceId) {
		List<Integer> registrations = new ArrayList<Integer>();
		registrations.addAll(this.registerDao.getRegistrationIdsForDevice(deviceId));
		return registrations;
	}

	public Register getRegistration(int deviceId, int registrationId) {
		return this.registerDao.getRegistrationForDevice(deviceId, registrationId);
	}

	public Register registerUser(int deviceId, IoTUser user) throws JMSException, NamingException {
		Register registration = new Register();
		Device device = deviceDao.getDevice(deviceId);
		registration.setUser(user);
		registration.setApproved(false);
		registration.setDevice(device);
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		registration.setTime(formatter.format(date));

		//userDao.persist(user);
		registerDao.persist(registration);
		return registration;
	}

	public String saveDevice(String name, String url) throws NamingException, JMSException {
		Device device = new Device();
		device.setName(name);
		device.setURL(url);
		//osv
		this.deviceDao.persist(device);
		return Constants.INDEX;
	}

	public Device getDevice(int id) {
		return deviceDao.getDevice(id);
	}
	public List<Register> getAllRegistrations() {
		return registerDao.getAllRegistrations();
	}

}
