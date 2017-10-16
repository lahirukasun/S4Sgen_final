package itech.s4sgen.dto;

public class OrderDto {

	long id;
	String customerName;
	String orderedDish;
	String customerType;
	String orderType;
	String deliveryTime;
	double orderBill;
	String review;
	int orderId;
	double amountReceived;
	double amountDeducted;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getOrderedDish() {
		return orderedDish;
	}
	public void setOrderedDish(String orderedDish) {
		this.orderedDish = orderedDish;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public double getOrderBill() {
		return orderBill;
	}
	public void setOrderBill(double orderBill) {
		this.orderBill = orderBill;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public double getAmountDeducted() {
		return amountDeducted;
	}
	public void setAmountDeducted(double amountDeducted) {
		this.amountDeducted = amountDeducted;
	}
}
