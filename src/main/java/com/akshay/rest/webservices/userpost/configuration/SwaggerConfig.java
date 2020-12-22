package com.akshay.rest.webservices.userpost.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static final Contact DEFAULT_CONTACT = new Contact("Akshay Reddy", "", "koppulaakshayreddy@gmail.com");
	
	public static final ApiInfo USER_POST_API_INFO
      = new ApiInfo(
      "User and User Posts Api Documentation",
      "Information about user and user posts",
      "1.0",
      "urn:tos",
      DEFAULT_CONTACT,
      "Apache 2.0",
      "http://www.apache.org/licenses/LICENSE-2.0",
      new ArrayList<>());

	public static final Set<String> DEFAULT_PRODUCERS_CONSUMERS = new HashSet<>(Arrays.asList("application/json","application/xml"));

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(USER_POST_API_INFO)
				.produces(DEFAULT_PRODUCERS_CONSUMERS)
				.consumes(DEFAULT_PRODUCERS_CONSUMERS);
	}
}
