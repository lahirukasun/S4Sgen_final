
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
@Table(name="systems_features")
public class SystemFeatures implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="feature")	
	private String feature;
	
	@ManyToOne
	@JoinColumn(name="parent_sub_system", referencedColumnName="id")
	private SubManagementSystem parentSystem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public SubManagementSystem getParentSystem() {
		return parentSystem;
	}

	public void setParentSystem(SubManagementSystem parentSystem) {
		this.parentSystem = parentSystem;
	}
	
	
}  

