
package itech.s4sgen.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="patient")
public class Patient implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="admit_id")
	private String admitId;
	
	@Column(name="name")
	private String Name;
	
	@Column(name="partner_mobile")
	private String partnerMobile;
	
	@Column(name="admitted_in")
	private String admittedIn;
	
	@Column(name="room_ward_no")
	private int no;
	
	@Column(name="admit_date")
	private Date admitDate;
	
	@Column(name="disease")
	private String disease;
	
	@ManyToOne
	@JoinColumn(name="system_owner", referencedColumnName="id")
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAdmitId() {
		return admitId;
	}

	public void setAdmitId(String admitId) {
		this.admitId = admitId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPartnerMobile() {
		return partnerMobile;
	}

	public void setPartnerMobile(String partnerMobile) {
		this.partnerMobile = partnerMobile;
	}

	public String getAdmittedIn() {
		return admittedIn;
	}

	public void setAdmittedIn(String admittedIn) {
		this.admittedIn = admittedIn;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public Date getAdmitDate() {
		return admitDate;
	}

	public void setAdmitDate(Date admitDate) {
		this.admitDate = admitDate;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}  

