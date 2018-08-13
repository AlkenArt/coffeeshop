package com.alkenart.coffeeshop.account.dao;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.alkenart.coffeeshop.account.model.User;

public interface UserRepo extends MongoRepository<User, String> {

    public User findByEmailAndPassword(String email,String password);

	public User findByEmail(String email);

	public Set<User> findByRole(String admin);  
}
