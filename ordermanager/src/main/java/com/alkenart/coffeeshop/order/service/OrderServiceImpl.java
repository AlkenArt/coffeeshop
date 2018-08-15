package com.alkenart.coffeeshop.order.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkenart.coffeeshop.account.dao.OrderRepo;
import com.alkenart.coffeeshop.account.model.Order;
import com.alkenart.coffeeshop.config.ApplicationConstants;
import com.alkenart.coffeeshop.order.OrderCategory;
import com.alkenart.coffeeshop.order.OrderFailedException;
import com.alkenart.coffeeshop.order.OrderInfo;
import com.alkenart.coffeeshop.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepo orderRepo;

	private static String ORDER_INVALID_DATA = "Invalid Data to create Order\"}";
	private static String SUCCESS = "SUCCESS\"}";
	private static String ORDER_EXISTS = "Account Already Exists\"}";
	private static String RESULT = "{\"result\":\"";

	@Override
	public String createOrder(OrderInfo orderDetails) throws OrderFailedException {
		String result = RESULT;
		if (null == orderDetails) {
			result += ORDER_INVALID_DATA;
			return result;
		}
		Order order = orderRepo.findByOrderId(orderDetails.getOrderId());
		if (order != null) {
			result += ORDER_EXISTS;
			return result;
		}
		order = new Order(orderDetails.getOrderId(), orderDetails.getQuantity(), orderDetails.getPrice(),
				orderDetails.getOrder().toString(), orderDetails.getOrderDate(), orderDetails.getStatus(),
				orderDetails.getCustomerName());

		String orderId = order.getOrder().substring(0, 3).toUpperCase() + "_" + order.getCustomerName().toUpperCase()
				+ "_" + order.getQuantity();
		order.setOrderId(orderId);
		order.setStatus(ApplicationConstants.QUEUED);
		Date date = new Date();
		order.setOrderDate(date);
		try {
			orderRepo.save(order);
		} catch (Exception e) {
			throw new OrderFailedException(e.getMessage());
		}
		result += SUCCESS;
		return result;
	}

	@Override
	public String deleteOrder(String orderID) {
		String result = RESULT;
		Order order = orderRepo.findByOrderId(orderID);
		orderRepo.delete(order);
		result += SUCCESS;
		return result;
	}

	@Override
	public String updateOrder(OrderInfo orderDetails) throws OrderFailedException {
		String result = RESULT;
		Order order = orderRepo.findByOrderId(orderDetails.getOrderId());
		order.setCustomerName(orderDetails.getCustomerName());
		order.setOrder(orderDetails.getOrder().toString());
		order.setOrderDate(new Date());
		order.setPrice(orderDetails.getPrice());
		order.setQuantity(orderDetails.getQuantity());
		try {
			orderRepo.save(order);
		} catch (Exception e) {
			throw new OrderFailedException(e.getMessage());
		}
		result += SUCCESS;
		return result;
	}

	@Override
	public OrderInfo getOrderDetails(String orderId) {
		Order order = orderRepo.findByOrderId(orderId);
		OrderInfo orderInfo = new OrderInfo(order.getOrderId(), order.getQuantity(), order.getPrice(),
				OrderCategory.valueOf(order.getOrder()), order.getOrderDate(), order.getStatus(),
				order.getCustomerName());
		return orderInfo;
	}

	@Override
	public String getOrderStatus(String orderId) {
		Order order = orderRepo.findByOrderId(orderId);
		return order.getStatus();
	}

	@Override
	public void init() {
//		orderRepo.deleteAll();
	}

	@Override
	public Set<OrderInfo> getAllOrders() {
		List<Order> orders = orderRepo.findAll();
		Set<OrderInfo> orderDetails = orders.parallelStream()
				.map(order -> new OrderInfo(order.getOrderId(), order.getQuantity(), order.getPrice(),
						OrderCategory.valueOf(order.getOrder()), order.getOrderDate(), order.getStatus(),
						order.getCustomerName()))
				.collect(Collectors.toSet());
		return orderDetails;
	}

	@Override
	public Set<String> getOrderCategories() {
		Set<String> retVal = new HashSet<String>();
		OrderCategory[] categories = OrderCategory.values();
		for (OrderCategory category : categories) {
			retVal.add(category.toString());
		}
		return retVal;
	}

}
