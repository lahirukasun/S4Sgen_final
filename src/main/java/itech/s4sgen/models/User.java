
package itech.s4sgen.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="login")	
	private String login;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")	
	private String lastName;
	
	@Column(name="password")	
	private String password;
	
	@Column(name="email")	
	private String email;
	
	@Column(name="image_url")	
	private String imageUrl;
	
	@Column(name="create_date")	
	private Date createDate;
	
	@ManyToOne
	@JoinColumn(name="management_system", referencedColumnName = "id")	
	private ManagementSystem managementSystem;
	
	@ManyToOne
	@JoinColumn(name="sub_management_system", referencedColumnName = "id")	
	private SubManagementSystem subManagementSystem;
	
	@Column(name="features")
	private String features;
	
	@Column(name="active")	
	private boolean active;

	@ManyToMany//(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public ManagementSystem getManagementSystem() {
		return managementSystem;
	}

	public void setManagementSystem(ManagementSystem managementSystem) {
		this.managementSystem = managementSystem;
	}

	public SubManagementSystem getSubManagementSystem() {
		return subManagementSystem;
	}

	public void setSubManagementSystem(SubManagementSystem subManagementSystem) {
		this.subManagementSystem = subManagementSystem;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}  

