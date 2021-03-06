package com.skorobahatko.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class App {
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/api/**"))
				.apis(RequestHandlerSelectors.basePackage("com.skorobahatko.university"))
				.build()
				.apiInfo(getApiInfo())
				.ignoredParameterTypes(LocalDate.class, LocalTime.class);
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"University REST API Documentation",
				"REST API Documentation for University application",
				"1.0",
				"Free to use",
				new Contact("Stanislav Skorobahatko", "http://skorobahatko.com", "skorobahatko@mail.com"),
				"API License",
				"http://skorobahatko.com",
				Collections.emptyList());
	}

}
