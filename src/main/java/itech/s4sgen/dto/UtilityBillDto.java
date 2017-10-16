package itech.s4sgen.dto;

public class UtilityBillDto {

	private long id;
	private String billId;
	private String billPayer;
	private String utility;
	private double amount;
	private String paidDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillPayer() {
		return billPayer;
	}
	public void setBillPayer(String billPayer) {
		this.billPayer = billPayer;
	}
	public String getUtility() {
		return utility;
	}
	public void setUtility(String utility) {
		this.utility = utility;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

}
