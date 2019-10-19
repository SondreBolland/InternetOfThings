package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "IoTUser")
@NamedQuery(name="IoTUser.findAll", query="SELECT u FROM IoTUser u")
public class IoTUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// Create elements ids automatically, incremented 1 by 1
	@TableGenerator(name = "yourTableGenerator", allocationSize = 1, initialValue = 1)

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "yourTableGenerator")
	private int id;

	private String email;

	private String username;

	private String password;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Device> ownDevices;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Register> subscribedDevices;
	
	public static final String FIND_ALL = "IoTUser.findAll";

	public IoTUser() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Device> getOwnDevices() {
		return ownDevices;
	}

	public void setOwnDevices(List<Device> ownDevices) {
		this.ownDevices = ownDevices;
	}

	public List<Register> getSubscribedDevices() {
		return subscribedDevices;
	}

	public void setSubscribedDevices(List<Register> subscribedDevices) {
		this.subscribedDevices = subscribedDevices;
	}
	
}
