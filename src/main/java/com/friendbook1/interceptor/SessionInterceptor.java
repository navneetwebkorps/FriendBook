package com.friendbook1.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    String requestURI = request.getRequestURI();
	    HttpSession session = request.getSession(false);

	    // Exclude the login page and any other publicly accessible URLs
	    if (session == null && !requestURI.endsWith("/Login") && !requestURI.endsWith("/RegisterPage") 
	            && !requestURI.contains("/userLogin") // If you have static resources like CSS or JS
	            && !requestURI.contains("/public/")) {  // Any other public pages can be added here
	        System.out.println("Redirecting to login page");
	        String contextPath = request.getContextPath();
	        response.sendRedirect(contextPath + "/Login");
	        return false;
	    }

	    // Allow the request to proceed if the session is not null or accessing a public URL
	    return true;
	}
}