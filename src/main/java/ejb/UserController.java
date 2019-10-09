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
import javax.servlet.http.HttpSession;

import entities.IoTUser;

@Named(value = "userController")
@RequestScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 1L;

	// Injected DAO EJB:
	@EJB
	private UserDao userDao;
	private IoTUser user;
	
	private String password;
	private String username;
	private String email;

	public UserController() {
		this.userDao = new UserDao();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<IoTUser> getUsers() {
		List<IoTUser> users = this.userDao.getAllUsers();
		return users;
	}

	public String createUser() {
		HttpSession session = SessionUtils.getSession();
		session.setAttribute(Constants.USERNAME, this.username);
		this.user = new IoTUser();
		this.user.setUsername(username);
		this.user.setEmail(email);
		System.out.println(username + " " + email + " " + password);
		this.user.setPassword(password);
		if (user.getUsername() == null) {
			System.out.println("im here");
			return Constants.SIGNUP_ERROR;
		}
		if (user.getUsername().length() < 6) {
			System.out.println("username length");
			return Constants.SIGNUP_ERROR;
		}
		if (!isStringOnlyAlphabet(user.getUsername())) {
			System.out.println("Alphabet");
			return Constants.SIGNUP_ERROR;
		}
		if (!(user.getEmail().contains("@"))) {
			System.out.println("Contains @");
			return Constants.SIGNUP_ERROR;
		}
		if (user.getPassword().length() < 8) {
			System.out.println("Password length");
			return Constants.SIGNUP_ERROR;
		}
		System.out.println("Passed tests");
		try {
			saveUser();
		} catch (NamingException e) {
			return Constants.SIGNUP_ERROR;
		} catch (JMSException e) {
			return Constants.SIGNUP_ERROR;
		} catch (Exception e) {
			return Constants.SIGNUP_ERROR;
		}
		
		return Constants.MY_DEVICES;
	}

	private static boolean isStringOnlyAlphabet(String str) {
		return ((!str.equals("")) && (str != null) && ((str.matches("^[a-zA-Z]*$") || str.matches("[^0-9]"))));
	}

	public void saveUser() throws NamingException, JMSException, Exception {
		SessionUtils.getUserName();
		this.user.setUsername(SessionUtils.getUserName());
		this.userDao.persist(this.user);
		System.out.println("Saved user");
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
