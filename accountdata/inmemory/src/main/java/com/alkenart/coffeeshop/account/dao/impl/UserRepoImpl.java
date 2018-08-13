/**
 * 
 */
package com.alkenart.coffeeshop.account.dao.impl;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkenart.coffeeshop.account.dao.UserRepo;
import com.alkenart.coffeeshop.account.model.User;
import com.alkenart.coffeeshop.config.ApplicationConstants;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

@Service
public class UserRepoImpl implements UserRepo {

	@Autowired
	HazelcastInstance hzInstance;

	@Override
	public User findByEmailAndPassword(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByEmail(String email) {
		IMap<String, User> userStore = hzInstance.getMap(ApplicationConstants.USER_DATA_STORE);
		@SuppressWarnings("unchecked")
		Predicate<String, User> userPredicate = Predicates.equal("email", email);
		Collection<User> users = userStore.values(userPredicate);
		if (!users.isEmpty()) {
			return users.iterator().next();
		}
		return null;
	}

	@Override
	public Set<User> findByRole(String role) {
		IMap<String, User> userStore = hzInstance.getMap(ApplicationConstants.USER_DATA_STORE);
		@SuppressWarnings("unchecked")
		Predicate<String, User> userPredicate = Predicates.equal("role", role);
		Set<User> users = (Set<User>) userStore.values(userPredicate);
		return users;
	}

	public void save(User user) {
		IMap<String, User> userStore = hzInstance.getMap(ApplicationConstants.USER_DATA_STORE);
		String key = user.getEmail() + user.getRole() + user.getCreateDate();
		userStore.put(key, user);
	}
}
