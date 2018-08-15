package com.alkenart.coffeeshop.account;

import java.util.Set;

public interface AccountService {

	Set<UserInfo> getAllUsers();

	String addUser(UserInfo user);

	String deleteUser(String userId, String currentUser);

	String modifyUser(UserInfo userInfo, String currentUser);

	UserInfo getUser(String userName);
	
	Set<String> getAllRoles();

	void init();

	String save(UserInfo userInfo);
}
