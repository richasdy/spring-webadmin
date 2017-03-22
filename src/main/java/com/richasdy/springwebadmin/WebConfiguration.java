package com.richasdy.springwebadmin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

@Configuration
public class WebConfiguration {

	// pagination engine configuration
	// you can place it in main configuration
	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}
	
}
