package ejb;

import entities.Device;
import entities.IoTUser;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Named(value = "deviceController")
@RequestScoped
public class DeviceController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Injected DAO EJB:
	@EJB
	private DeviceDao deviceDao;

	private Device device;

	public List<Device> getDevices() {
		List<Device> deviceList = new ArrayList<Device>();
		deviceList.addAll(this.deviceDao.getAllDevices());
		return deviceList;

	}

	public String saveDevice(String name, String url) throws NamingException, JMSException {
		this.device.setName(name);
		this.device.setURL(url);
		//osv
		this.deviceDao.persist(this.device);
		return Constants.INDEX;
	}

	public Device getDevice() {
		if (this.device == null) {
			device = new Device();
		}
		return device;

	}

}
