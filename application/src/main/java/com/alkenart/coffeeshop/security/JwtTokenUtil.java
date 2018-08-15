package com.alkenart.coffeeshop.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alkenart.coffeeshop.config.service.ConfigServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {

	@Autowired
	ConfigServiceImpl configService;

	private static final String AUTHORITIES_KEY = "auth";

	private static final String SECRETKEY = "7f096d5c01e469d7ee628d572446d195";

	public String resolveToken(HttpServletRequest request) {
		if (!request.getServletPath().contains("/api/")) {
			return "";
		}
		String bearerToken = request.getHeader(configService.getAuthHeader());
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public String getAuthority(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		Set<String> groups = new HashSet<String>();
		for (GrantedAuthority role : authorities) {
			groups.add(role.toString());
		}
		String retVal = groups.iterator().next().replace("ROLE_", "");
		return retVal;
	}

	public String getUser(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public String createToken(Authentication authentication, String expiration) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + Long.parseLong(expiration));

		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS512, SECRETKEY).setExpiration(validity).compact();
	}

	public boolean validateToken(String authToken) throws JwtInvalidException {
		try {
			Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			throw new JwtInvalidException("Invalid Token signature.");
		} catch (MalformedJwtException e) {
			throw new JwtInvalidException("Invalid token.");
		} catch (ExpiredJwtException e) {
			throw new JwtInvalidException("Token expired.");
		} catch (UnsupportedJwtException e) {
			throw new JwtInvalidException("Unsupported token.");
		} catch (IllegalArgumentException e) {
			throw new JwtInvalidException("Token compact of handler are invalid.");
		}
	}
}