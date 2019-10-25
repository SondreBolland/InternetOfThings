package ejb;

import entities.Device;
import entities.Feedback;
import entities.IoTUser;
import entities.Register;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;

import java.io.IOException;
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
	private IoTUser user;
	private String feedback;

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

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
	
	public List<Device> getPublishedDevices(String username) {
		IoTUser user = userDao.getUser(username);
		List<Device> deviceList = new ArrayList<Device>();
		if (searchKey == null) {
			deviceList.addAll(this.deviceDao.getPublishedDevices(user.getId()));
			return deviceList;
		}
		if (searchKey.equals("")) {
			deviceList.addAll(this.deviceDao.getPublishedDevices(user.getId()));
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
		System.out.println(user.getOwnDevices().remove(device));
		try {
			deviceDao.remove(device);
			deviceDao.deleteDevice(deviceId);
			userDao.merge(user);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return Constants.MY_DEVICES;
	}
	
	public String invertPublish(Integer deviceId) {
		Device device = deviceDao.getDevice(deviceId);
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
		Register registration = new Register();
		Device device = deviceDao.getDevice(deviceId.intValue());
		IoTUser user = userDao.getUser(username);
		registration.setUser(user);
		registration.setApproved(false);
		registration.setDevice(device);
		registration.setTopic("dweet");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		registration.setTime(formatter.format(date));
		user.getSubscribedDevices().add(registration);
		return Constants.SUBSCRIBED;
	}
	
	public String unsubscribe(Integer deviceId, Integer registrationId, String username) {
		Register register = registerDao.getRegistrationForDevice(deviceId, registrationId);
		register = registerDao.getRegister(register);
		IoTUser user = userDao.getUser(username);
		this.device = deviceDao.getDevice(deviceId);
		this.user = user;
		try {
			registerDao.remove(register);
			registerDao.deleteRegister(registrationId.intValue());
			userDao.merge(user);
			deviceDao.merge(device);
		} catch (NamingException e) {
			e.printStackTrace();
			return Constants.SUBSCRIBED;
		} catch (JMSException e) {
			e.printStackTrace();
			return Constants.SUBSCRIBED;
		}
		return Constants.FEEDBACK;
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
	
	public void goExternal(Integer deviceId) {
		Device device = deviceDao.getDevice(deviceId);
		String URL = device.getURL();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		try {
			externalContext.redirect(URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String goExternalSubscriber(Integer deviceId, Boolean approved) {
		if (!approved) {
			return Constants.SUBSCRIBED;
		}
		Device device = deviceDao.getDevice(deviceId);
		String URL = device.getURL();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		try {
			externalContext.redirect(URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String addFeedback(String username) {
		Feedback f = new Feedback();
		f.setFeedback(feedback);
		IoTUser user = userDao.getUser(username);
		f.setUser(user);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		f.setTime(formatter.format(date));
		device.getFeedback().add(f);
		try {
			deviceDao.merge(device);
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.SUBSCRIBED;
		}
		System.out.println(feedback);
		return Constants.MY_DEVICES;
	}
	
	public String readFeedback(int deviceId) {
		device = deviceDao.getDevice(deviceId);
		return Constants.READ_FEEDBACK;	
	}
	
	public List<Feedback> getDeviceFeedback() {
		return device.getFeedback();
	}
	
	public List<Register> getRequests(String username) {
		IoTUser user = userDao.getUser(username);
		return registerDao.getRequests(user.getId());
	}
	
	public String invertApproved(Register register) {
		register.setApproved(!register.isApproved());
		try {
			registerDao.merge(register);
			if (register.isApproved())
				registerDao.sendMessage(register);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return Constants.REQUESTS;
	}

}
