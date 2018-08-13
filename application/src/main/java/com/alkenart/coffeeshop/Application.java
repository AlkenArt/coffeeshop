package com.alkenart.coffeeshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;

import com.alkenart.coffeeshop.account.AccountService;

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	AccountService accountService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		accountService.init();
	}

}
