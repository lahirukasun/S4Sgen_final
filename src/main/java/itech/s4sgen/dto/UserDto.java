package itech.s4sgen.dto;

import java.util.Date;
import java.util.List;

import itech.s4sgen.models.ManagementSystem;
import itech.s4sgen.models.SubManagementSystem;
import itech.s4sgen.models.SystemFeatures;


public class UserDto {

	private long id;	
	private String login;
    private String firstName;	
	private String lastName;	
	private String password;	
	private String email;	
	private String imageUrl;	
	private Date createDate;
	private ManagementSystem manaementSystem;
	private SubManagementSystem subManagementSystem;
	private List<SystemFeatures> features;
	private boolean active;
	
	
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
	public ManagementSystem getManaementSystem() {
		return manaementSystem;
	}
	public void setManaementSystem(ManagementSystem manaementSystem) {
		this.manaementSystem = manaementSystem;
	}
	public SubManagementSystem getSubManagementSystem() {
		return subManagementSystem;
	}
	public void setSubManagementSystem(SubManagementSystem subManagementSystem) {
		this.subManagementSystem = subManagementSystem;
	}
	public List<SystemFeatures> getFeatures() {
		return features;
	}
	public void setFeatures(List<SystemFeatures> features) {
		this.features = features;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
