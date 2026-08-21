package com.pams.config;


import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilterBrowser extends OncePerRequestFilter {

	
		
		@Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {
	        String userAgent = request.getHeader("User-Agent");
	        

	        if (request.getRemoteUser() != null) {
	            
	            String boundUserAgent = (String) request.getSession().getAttribute("USER_AGENT");

	            if (boundUserAgent != null && !boundUserAgent.equals(userAgent)) {
	                // If user agent does not match, invalidate the session
	                request.getSession().invalidate();
	            } else {
	                // Bind the user agent to the session
	                request.getSession().setAttribute("USER_AGENT", userAgent);
	            }
	        }

	        filterChain.doFilter(request, response);
	    }
	}