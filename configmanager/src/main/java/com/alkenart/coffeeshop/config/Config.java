package com.alkenart.coffeeshop.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class Config implements ConfigConstants{
	
	@Value("${ale.timeZone:America/Newyork}")
	private String timeZone;
	
	@Value("${ale.account.usernameSeparator://}")
	private String usernameSeparator;
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public String getUsernameSeparator(){
		return usernameSeparator;
	}
	
	public Map<String,String> getAllProperties(){
		Map<String,String> context = new HashMap<String,String>();
		context.put(TIMEZONE, getTimeZone());
		context.put(USERNAMESEPARATOR, getUsernameSeparator());
		return context;
	}

}
