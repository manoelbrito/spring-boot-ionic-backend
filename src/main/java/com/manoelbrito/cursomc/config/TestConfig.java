package com.manoelbrito.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.manoelbrito.cursomc.services.DBService;
import com.manoelbrito.cursomc.services.EmailService;
import com.manoelbrito.cursomc.services.SmtpEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean InstatiateDataBase() throws ParseException {
		dbService.InstatiateTestDataBase();
		return true;
	}
	
//	@Bean
//	public EmailService emailService() {
//		return new MockEmailService();
//	}
	
	@Bean
	public EmailService emailService2() {
		return new SmtpEmailService();
	}

}
