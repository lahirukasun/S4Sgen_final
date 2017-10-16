
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
@Table(name="restaurant_orders")
public class Order implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="customer_name")
	private String customerName;
	
	@Column(name="dish")
	private String orderedDish;
	
	@Column(name="customer_type")
	private String customerType;
	
	@Column(name="order_type")
	private String orderType;
	
	@Column(name="delivery_time")
	private String deliveryTime;
	
	@Column(name="order_bill")
	private double orderBill;
	
	@ManyToOne
	@JoinColumn(name="system_owner", referencedColumnName="id")
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}  

