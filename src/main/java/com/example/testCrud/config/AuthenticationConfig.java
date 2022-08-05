package com.example.testCrud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.testCrud.interceptor.AuthencationInterceptor;



@Configuration
public class AuthenticationConfig implements WebMvcConfigurer{
	
	@Autowired
	private AuthencationInterceptor authencationInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(authencationInterceptor)
		.addPathPatterns("/posts/**");
		
		
	}
}
