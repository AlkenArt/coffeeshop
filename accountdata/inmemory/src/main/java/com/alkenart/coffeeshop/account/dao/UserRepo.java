package com.alkenart.coffeeshop.account.dao;

import java.util.Set;

import com.alkenart.coffeeshop.account.model.User;

public interface UserRepo {

    public User findByEmailAndPassword(String email,String password);

	public User findByEmail(String email);

	public Set<User> findByRole(String admin);
	
	public void save(User user);
}
