package com.alkenart.coffeeshop.security;

import io.jsonwebtoken.JwtException;

public class JwtInvalidException extends JwtException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1340925382884625124L;

	public JwtInvalidException(String message) {
		super(message);
	}

}
