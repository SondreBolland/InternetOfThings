package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.resource.spi.SecurityException;

import entities.IoTUser;


@Named(value = "userController")
@RequestScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Injected DAO EJB:
	@EJB
	private UserDao userDao;

	private IoTUser user;

	public UserController(){
		this.userDao = new UserDao();
	}

	public List<IoTUser> getUsers() {
		List<IoTUser> users = this.userDao.getAllUsers();
		return users;
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
