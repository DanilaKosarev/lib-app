package com.example.library;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public String[] whiteListUrls(){
		return new String[]{
				"/api/auth/**",
				"/v2/api-docs",
				"/v3/api-docs",
				"/v3/api-docs/**",
				"/swagger-resources",
				"/swagger-resources/**",
				"/swagger-ui/**",
				"/webjars/**",
				"/swagger-ui.html"
		};
	}
}
