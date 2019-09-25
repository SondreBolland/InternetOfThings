package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @Author Alejandro Rodriguez
 * Dat250
 * 
 * The persistent class for the TWEETS extension from TWEET.
 * 
 */

@XmlRootElement
@XmlSeeAlso(IoTUser.class)
public class Users extends ArrayList<IoTUser> {

	private static final long serialVersionUID = 1L;
	
	public Users() {
		super();
	}

	public Users(Collection<? extends IoTUser> c) {
		super(c);
	}

	@XmlElement(name = "IoTUser")
	public List<IoTUser> getUsers() {
		return this;
	}

	public void setUsers(List<IoTUser> tweets) {
		this.addAll(tweets);
	}
}
