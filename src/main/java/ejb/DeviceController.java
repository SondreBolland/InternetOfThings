package ejb;

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
	private UserDao userDao;

	private IoTUser user;

	public List<IoTUser> getUsers() {
		List<IoTUser> reverseTweetList = new ArrayList<IoTUser>();
		reverseTweetList.addAll(this.userDao.getAllUsers());
		Collections.reverse(reverseTweetList);
		return reverseTweetList;

	}

	public String saveUser() throws NamingException, JMSException {
		SessionUtils.getUserName();
		this.user.setUsername(SessionUtils.getUserName());
		this.userDao.persist(this.user);
		return Constants.INDEX;
	}

	public IoTUser getUser() {
		if (this.user == null) {
			user = new IoTUser();
		}
		return user;

	}

}
