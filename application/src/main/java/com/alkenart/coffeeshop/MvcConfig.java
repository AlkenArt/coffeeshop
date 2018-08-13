package com.alkenart.coffeeshop;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration()
public class MvcConfig extends WebMvcConfigurerAdapter {   
    
    @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.addViewController("/home").setViewName("forward:/index.html");
		registry.addViewController("/login").setViewName("forward:/view/login.html");
		registry.addViewController("/register").setViewName("forward:/view/register.html");
		registry.addViewController("/error").setViewName("forward:/view/error.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}
    
}
