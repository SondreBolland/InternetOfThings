package ejb;

import entities.Device;
import entities.IoTUser;
import entities.Register;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named(value = "deviceController")
@Stateless
public class DeviceController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String picture;
	private String URL;
	private Boolean published;
	private Boolean online;

	private String searchKey;

	// Injected DAO EJB:
	@EJB
	private DeviceDao deviceDao;
	private RegisterDao registerDao;
	private UserDao userDao;

	private Device device;

	public DeviceController() {
		deviceDao = new DeviceDao();
		registerDao = new RegisterDao();
		userDao = new UserDao();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<Device> getDevices() {
		List<Device> deviceList = new ArrayList<Device>();
		if (searchKey == null) {
			deviceList.addAll(this.deviceDao.getAllDevices());
			return deviceList;
		}
		if (searchKey.equals("")) {
			deviceList.addAll(this.deviceDao.getAllDevices());
			return deviceList;
		}
		return searchDevice();
	}
	
	public List<Device> getPublishedDevices() {
		List<Device> deviceList = new ArrayList<Device>();
		if (searchKey == null) {
			deviceList.addAll(this.deviceDao.getPublishedDevices());
			return deviceList;
		}
		if (searchKey.equals("")) {
			deviceList.addAll(this.deviceDao.getPublishedDevices());
			return deviceList;
		}
		return searchDevice();
	}

	public List<Device> searchDevice() {
		List<Device> deviceList = new ArrayList<Device>();
		deviceList.addAll(this.deviceDao.searchDevice(this.searchKey));
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

	public String addDevice(String username) {
		IoTUser user = userDao.getUser(username);
		device = new Device();
		device.setName(this.name);
		device.setOnline(true);
		device.setPicture(this.picture);
		device.setPublished(this.published);
		device.setURL(this.URL);
		user.getOwnDevices().add(device);
		return Constants.MY_DEVICES;
	}
	
	public String deleteDevice(Integer deviceId, String username) {
		Device device = deviceDao.getDevice(deviceId);
		IoTUser user = userDao.getUser(username);
		try {
			deviceDao.remove(device);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		user.getOwnDevices().remove(device);
		return Constants.MY_DEVICES;
	}
	
	public String invertPublish() {
		int id = device.getId();
		Device device = deviceDao.getDevice(id);
		device.setPublished(!device.isPublished());
		try {
			deviceDao.merge(device);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return Constants.MY_DEVICES;
	}

	public String registerUser(Integer deviceId, String username) {
		System.out.println("kake1");
		Register registration = new Register();
		Device device = deviceDao.getDevice(deviceId.intValue());
		IoTUser user = userDao.getUser(username);
		registration.setUser(user);
		registration.setApproved(false);
		registration.setDevice(device);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		registration.setTime(formatter.format(date));
		user.getSubscribedDevices().add(registration);
		return Constants.SUBSCRIBED;
	}
	
	public String unsubscribe(Integer deviceId, Integer registrationId, String username) {
		Register register = registerDao.getRegistrationForDevice(deviceId, registrationId);
		IoTUser user = userDao.getUser(username);
		System.out.println(user.getSubscribedDevices().remove(register));
		System.out.println("device: " + deviceId + ", register: " + registrationId);
		try {
			registerDao.remove(register);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return Constants.SUBSCRIBED;
	}

	public String saveDevice(String name, String url) throws NamingException, JMSException {
		device.setName(name);
		device.setURL(url);
		// osv
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
