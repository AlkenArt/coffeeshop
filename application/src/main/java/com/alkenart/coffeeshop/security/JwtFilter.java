/*package com.alkenart.coffeeshop.security;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.alkenart.coffeeshop.config.service.ConfigServiceImpl;

@Component
public class JwtFilter extends GenericFilterBean {

	private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

	@Autowired
	ConfigServiceImpl configService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			String jwt = jwtTokenUtil.resolveToken(httpServletRequest);
			if (null == jwt)
				throw new AuthenticationException();
			if (StringUtils.hasText(jwt) && jwtTokenUtil.validateToken(jwt)) {
				Authentication authentication = jwtTokenUtil.getAuthentication(jwt);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (JwtInvalidException jie) {
			log.info(jie.getMessage());
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, jie.getMessage());
		} catch (AuthenticationException ae) {
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Authentication failed: Authentication Token Not specified");
		}
	}
}*/