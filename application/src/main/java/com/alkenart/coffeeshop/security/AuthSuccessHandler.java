package com.alkenart.coffeeshop.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alkenart.coffeeshop.account.AccountService;
import com.alkenart.coffeeshop.account.UserInfo;
import com.alkenart.coffeeshop.config.service.ConfigServiceImpl;
import com.alkenart.coffeeshop.util.TimeUtil;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	ConfigServiceImpl configService;
	
	@Autowired
	AccountService accountService;

	JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
//		String token = jwtTokenUtil.createToken(authentication, configService.getExpiration());
//		Cookie myCookie = new Cookie(configService.getAuthHeader(), "Bearer " + token);
//		response.addCookie(myCookie);
//		response.addHeader(configService.getAuthHeader(), "Bearer " + token);
		
		String username = authentication.getName();
		UserInfo user = accountService.getUser(username);
		user.setLastLoginDate(TimeUtil.toString(new Date()));
		accountService.save(user);
		String successUrl = request.getRequestURL().substring(0, request.getRequestURL().length() - 5);
		response.sendRedirect(successUrl);
	}

}
