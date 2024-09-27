package com.friendbook1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.friendbook1.interceptor.SessionInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor());
                
    }
}
