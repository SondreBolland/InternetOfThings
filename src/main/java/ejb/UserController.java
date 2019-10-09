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

	public String createUser(){
		if(user.getUsername() == null || !user.getUsername().equals(""))
			return "Enter username";
		if(user.getUsername().length()<6)
			return "Username must be at least 6 characters";
		if(!isStringOnlyAlphabet(user.getUsername()))
			return "Username can only contain letters and numbers";
		if(getUser(user.getUsername()) != null)
			return "Username is already taken";
		if(!(user.getEmail().contains("@")||user.getEmail().contains("@")))
			return "Please enter a valid email address";
		if(user.getPassword().length()<8)
			return "Password must be at least 8 characters";
		IoTUser user = new IoTUser();
		user.setUsername(user.getUsername());
		user.setPassword(user.getPassword());
		user.setEmail(user.getEmail());
		try {
			saveUser();
		} catch (NamingException e) {
			return "I like your input, but the database doesn't.. Try again! " + e.getMessage();
		} catch (JMSException e) {
			return "I like your input, but the database doesn't.. Try again! "+ e.getMessage();
		}
		return "Well done, you entered valid information, as a reward your account have been created";
	}

	private static boolean isStringOnlyAlphabet(String str)
	{
		return ((!str.equals(""))
				&& (str != null)
				&& ((str.matches("^[a-zA-Z]*$") || str.matches("[^0-9]"))));
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

	public IoTUser getUser(String username) {
		IoTUser user = userDao.getUser(username);
		return user;
	}

}
