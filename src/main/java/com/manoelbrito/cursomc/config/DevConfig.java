package com.manoelbrito.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.manoelbrito.cursomc.services.DBService;
import com.manoelbrito.cursomc.services.EmailService;
import com.manoelbrito.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean InstatiateDataBase() throws ParseException {
		
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.InstatiateTestDataBase();
		return true;
	}
	
	@Bean
	public EmailService emailService2() {
		return new SmtpEmailService();
	}

}
