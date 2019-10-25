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
@Table(name = "Tag")
@NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag t")
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;

	// Create elements ids automatically, incremented 1 by 1
	@TableGenerator(name = "yourTableGeneratorTag", allocationSize = 1, initialValue = 1)

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "yourTableGeneratorTag")
	private int id;
	
	private String name;
	
	private String description;
	
	public static final String FIND_ALL = "Tag.findAll";

	public Tag() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
