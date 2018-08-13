package com.alkenart.coffeeshop.order;

import java.util.Date;

public class OrderInfo {

	private String orderId;
	private int quantity;
	private int price;
	private OrderCategory order;
	private Date orderDate;
	private String status;
	private String customerName;

	public OrderInfo() {
	}
	
	public OrderInfo(String orderId, int quantity, int price, OrderCategory order, Date orderDate, String status,
			String customerName) {
		super();
		this.orderId = orderId;
		this.quantity = quantity;
		this.price = price;
		this.order = order;
		this.orderDate = orderDate;
		this.status = status;
		this.customerName = customerName;
	}



	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public OrderCategory getOrder() {
		return order;
	}

	public void setOrder(OrderCategory order) {
		this.order = order;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
