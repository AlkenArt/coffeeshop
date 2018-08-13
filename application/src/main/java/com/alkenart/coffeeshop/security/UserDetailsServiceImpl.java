package com.alkenart.coffeeshop.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.alkenart.coffeeshop.account.dao.UserRepo;
import com.alkenart.coffeeshop.account.model.User;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		User user = repo.findByEmail(emailId);
		org.springframework.security.core.userdetails.User userdetails = new org.springframework.security.core.userdetails.User(
				user.getEmail().toLowerCase(), user.getPassword(), true, true, true, true, getAuthorities(user));
		return userdetails;
	}

	public List<SimpleGrantedAuthority> getAuthorities(User user) {
		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
		String roleStr = user.getRole().toString();
		String[] roles = roleStr.split(",");

		for (String role : roles)
			authList.add(new SimpleGrantedAuthority(role));
		return authList;
	}
}
