package com.alkenart.coffeeshop.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alkenart.coffeeshop.account.AccountService;
import com.alkenart.coffeeshop.order.OrderFailedException;
import com.alkenart.coffeeshop.order.OrderInfo;
import com.alkenart.coffeeshop.order.OrderService;

@RequestMapping("/api/order/")
@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	AccountService accountService;

	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public String placeOrder(@RequestBody OrderInfo orderInfo) throws OrderFailedException {
		String response = orderService.createOrder(orderInfo);
		return response;
	}

	@RequestMapping(value = "/orderUpdate", method = RequestMethod.PUT)
	public String orderUpdate(@RequestBody OrderInfo orderInfo) throws OrderFailedException {
		String response = orderService.updateOrder(orderInfo);
		return response;
	}

	@RequestMapping(value = "/getAllOrders", method = RequestMethod.GET)
	public Set<OrderInfo> getAllOrders() {
		Set<OrderInfo> response = orderService.getAllOrders();
		return response;
	}

	@RequestMapping(value = "/getAllCategories", method = RequestMethod.GET)
	public Set<String> getAllCategories() {
		Set<String> response = orderService.getOrderCategories();
		return response;
	}

	@RequestMapping(value = "/getStatus", method = RequestMethod.GET)
	public String getAllCategories(@RequestParam(value = "orderId") String orderId) {
		String response = orderService.getOrderStatus(orderId);
		return response;
	}

	@RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
	public String cancelOrder(@RequestParam(value = "orderId") String orderId) {
		String response = orderService.deleteOrder(orderId);
		return response;
	}
}
