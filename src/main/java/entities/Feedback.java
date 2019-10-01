package entities;

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
@Table(name = "Feedback")
@NamedQuery(name = "Feedback.findAll", query = "SELECT f FROM Feedback f")
public class Feedback {

	// Create elements ids automatically, incremented 1 by 1
	@TableGenerator(name = "yourTableGenerator", allocationSize = 1, initialValue = 1)

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "yourTableGenerator")
	private int id;
	
	private IoTUser user;
	
	private String feedback;
	
	private String time;
	
	public static final String FIND_ALL = "Feedback.findAll";

	public Feedback() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IoTUser getUser() {
		return user;
	}

	public void setUser(IoTUser user) {
		this.user = user;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
