
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
@Table(name="reg_customers_order")
public class RegCustomerOrder implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="customer_id")
	private int customerId;
	
	@Column(name="dish")
	private String orderedDish;
	
	@Column(name="order_type")
	private String orderType;
	
	@Column(name="delivery_time")
	private String deliveryTime;
	
	@Column(name="order_bill")
	private double orderBill;
	
	@ManyToOne
	@JoinColumn(name="customer", referencedColumnName="id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name="user", referencedColumnName="id")
	private User user;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getOrderedDish() {
		return orderedDish;
	}

	public void setOrderedDish(String orderedDish) {
		this.orderedDish = orderedDish;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}  

