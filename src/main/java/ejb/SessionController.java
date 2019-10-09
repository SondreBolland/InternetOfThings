package ejb;

import entities.IoTUser;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named(value = "sessionController")
@SessionScoped
public class SessionController implements Serializable {
	private static final long serialVersionUID = 1L;

	UserController userController = new UserController();

	private String password;
	private String username;
	private String email;

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

	public String validateUsernamePassword() {
		HttpSession session = SessionUtils.getSession();
		session.setAttribute(Constants.USERNAME, this.username);
		IoTUser user = userController.getUser(this.username);
		if(user == null) {
			return Constants.LOGIN_ERROR;
		}
		if(!user.getPassword().equals(this.password)) {//clear text password are the best, we can sell them if we go bankrupt
			return Constants.LOGIN_ERROR;
		}
		return Constants.MY_DEVICES;
	}

	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return Constants.LOGIN;
	}
	
	public String redirect() throws IOException {
		HttpSession session = SessionUtils.getSession();
		if (session.getAttribute(Constants.USERNAME)==null) {
			SessionUtils.getResponse().sendRedirect(Constants.LOGIN + ".xhtml");
		}
		return Constants.INDEX;
	}
}
