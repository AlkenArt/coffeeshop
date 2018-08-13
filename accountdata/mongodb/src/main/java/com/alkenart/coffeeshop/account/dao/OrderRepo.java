package com.alkenart.coffeeshop.account.dao;

import java.util.Date;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.alkenart.coffeeshop.account.model.Order;

public interface OrderRepo extends MongoRepository<Order, String> {

	public Order findByOrderId(String orderId); 
	
	public Set<Order> findByStatus(String status);
	
	public Set<Order> findByOrderDate(Date date);
}
