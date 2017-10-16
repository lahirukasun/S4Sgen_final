package itech.s4sgen.dto;

public class PatientDto {

	private long id;
	private String admitId;
	private String name;
	private String partnerMobile;
	private String admittedIn;
	private int no;
	private String admitDate;
	private String disease;
	private double fee;
	private String doctorName;
	private int age;
	private boolean checked;
	
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
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAdmitDate() {
		return admitDate;
	}
	public void setAdmitDate(String admitDate) {
		this.admitDate = admitDate;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
