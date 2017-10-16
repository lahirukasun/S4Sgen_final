
package itech.s4sgen.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="sub_management_systems")
public class SubManagementSystem implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="name")	
	private String name;
	
	@Column(name="logo")
	private String logo;
	
	@ManyToOne
	@JoinColumn(name="parent_System", referencedColumnName="id")	
	private ManagementSystem parentSystem;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public ManagementSystem getParentSystem() {
		return parentSystem;
	}
	public void setDescription(ManagementSystem parentSystem) {
		this.parentSystem = parentSystem;
	}


}  

