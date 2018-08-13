package com.alkenart.coffeeshop.config.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkenart.coffeeshop.config.Config;
import com.alkenart.coffeeshop.config.ConfigConstants;
import com.alkenart.coffeeshop.config.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService, ConfigConstants {
	
	@Autowired
	Config config;
	
	public ConfigServiceImpl(){
	}

	@Override
	public String getProperty(String paramName) {
		Map<String, String> context = config.getAllProperties();
		return context.get(paramName);
	}
}
