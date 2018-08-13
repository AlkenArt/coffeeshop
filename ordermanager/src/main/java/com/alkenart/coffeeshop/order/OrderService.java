package com.alkenart.coffeeshop.order;

import java.util.Set;

public interface OrderService {

	String createOrder(OrderInfo orderDetails) throws OrderFailedException;
	
	String deleteOrder(String irderId);
	
	String updateOrder(OrderInfo orderDetails) throws OrderFailedException;

	String getOrderStatus(String orderId);

	OrderInfo getOrderDetails(String orderrId);

	void init();

	Set<OrderInfo> getAllOrders();
	
	Set<String> getOrderCategories();

}