package com.alkenart.coffeeshop.account.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkenart.coffeeshop.account.AccountService;
import com.alkenart.coffeeshop.account.Role;
import com.alkenart.coffeeshop.account.UserInfo;
import com.alkenart.coffeeshop.account.dao.UserRepo;
import com.alkenart.coffeeshop.account.model.User;
import com.alkenart.coffeeshop.util.TimeUtil;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	UserRepo userRepo;

	private static String ACC_INVALID_DATA = "Invalid Data to create account\"}";
	private static String SUCCESS = "SUCCESS\"}";
	private static String ACC_EXISTS = "Account Already Exists\"}";
	private static String CUR_USER_DEL_FAILED = "Logged in user can not be deleted\"}";
	private static String ADMIN_DEL_FAILED = "Admin user can not be deleted\"}";
	private static String ADMIN_MOD_FAILED = "Other Admin can not be modified\"}";
	private static String RESULT = "{\"result\":\"";

	@Override
	public String addUser(UserInfo userInfo) {
		String result = RESULT;
		if (null == userInfo) {
			result += ACC_INVALID_DATA;
			return result;
		}

		userInfo.setUserId(userInfo.getUserId().toLowerCase());
		User user = userRepo.findByEmail(userInfo.getUserId());
		if (user != null) {
			result += ACC_EXISTS;
			return result;
		}

		user = new User(userInfo.getUserId(), userInfo.getFirstName(), userInfo.getLastName(), userInfo.getPassword(),
				userInfo.getRole().toString());
		Date date = new Date();
		user.setCreateDate(date);
		user.setLastUpdateDate(date);
		userRepo.save(user);
		result += SUCCESS;
		return result;
	}

	@Override
	public String deleteUser(String userId, String currentUser) {
		String result = RESULT;

		if (currentUser.equals(userId)) {
			result += CUR_USER_DEL_FAILED;
			return result;
		}
		User user = userRepo.findByEmail(userId);
		if (user.getRole().equals(Role.ADMIN)) {
			result += ADMIN_DEL_FAILED;
			return result;
		}

		userRepo.delete(user);
		result += SUCCESS;
		return result;
	}

	@Override
	public String modifyUser(UserInfo userInfo, String currentUser) {
		String result = RESULT;

		boolean checkAdmin = userInfo.getRole().equals(Role.ADMIN);
		boolean checkLoggedUser = currentUser.equals(userInfo.getUserId());

		if (checkAdmin && !checkLoggedUser) {
			result += ADMIN_MOD_FAILED;
			return result;
		}

		User user = userRepo.findByEmail(userInfo.getUserId());
		user.setPassword(userInfo.getPassword());
		user.setFirstName(userInfo.getFirstName());
		user.setLastName(userInfo.getLastName());
		user.setLastUpdateDate(TimeUtil.toDate(userInfo.getLastLoginDate()));
		userRepo.save(user);
		result += SUCCESS;
		return result;
	}

	@Override
	public String save(UserInfo userInfo) {
		String result = RESULT;
		User user = userRepo.findByEmail(userInfo.getUserId());
		user = new User(userInfo.getUserId(), userInfo.getFirstName(), userInfo.getLastName(), userInfo.getPassword(),
				userInfo.getRole().toString());
		user.setCreateDate(TimeUtil.toDate(userInfo.getCreateDate()));
		user.setLastUpdateDate(TimeUtil.toDate(userInfo.getLastLoginDate()));
		userRepo.save(user);
		result += SUCCESS;
		return result;
	}

	@Override
	public Set<UserInfo> getAllUsers() {
		Set<UserInfo> retVal = new HashSet<UserInfo>();
		Iterable<User> users = userRepo.findAll();
		if (null == users) {
			return retVal;
		}
		for (User user : users) {
			UserInfo userInfo = new UserInfo(user.getEmail(), user.getFirstName(), user.getLastName(),
					user.getPassword(), Role.valueOf(user.getRole()), user.getCreateDate(), user.getLastUpdateDate());
			retVal.add(userInfo);
		}
		return retVal;
	}

	@Override
	public UserInfo getUser(String userName) {
		User user = userRepo.findByEmail(userName);
		UserInfo userInfo = new UserInfo(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(),
				Role.valueOf(user.getRole()), user.getCreateDate(), user.getLastUpdateDate());
		return userInfo;
	}

	@Override
	public Set<String> getAllRoles() {
		Set<String> retVal = new HashSet<String>();
		Role[] roles = Role.values();
		for (Role role : roles) {
			retVal.add(role.toString());
		}
		return retVal;
	}

	@Override
	public void init() {
//		userRepo.deleteAll();
		UserInfo userInfo = new UserInfo("staffuser@alkenart.com", "Staff", "User", "password", Role.STAFF, new Date(),
				new Date());
		UserInfo userInfo1 = new UserInfo("lokesh@alkenart.com", "Lokesh", "Nayak", "lokesh", Role.ADMIN, new Date(),
				new Date());
		addUser(userInfo);
		addUser(userInfo1);
	}
}
