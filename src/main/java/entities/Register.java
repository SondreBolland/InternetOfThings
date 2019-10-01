package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "Register")
@NamedQuery(name = "Register.findAll", query = "SELECT r FROM Register r")
public class Register implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Create elements ids automatically, incremented 1 by 1
	@TableGenerator(name = "yourTableGenerator", allocationSize = 1, initialValue = 1)

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "yourTableGenerator")
	private int id;
	private String time;
	private boolean approved;
	private IoTUser user;
	private Device device;

	public Register() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public IoTUser getUser() {
		return user;
	}

	public void setUser(IoTUser user) {
		this.user = user;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	

}
