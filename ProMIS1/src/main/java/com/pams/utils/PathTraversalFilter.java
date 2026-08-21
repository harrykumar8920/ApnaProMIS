package com.pams.utils;


import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

@Component
@Order(1)
public class PathTraversalFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(PathTraversalFilter.class);
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIP = request.getRemoteAddr();
        
        // Check for path traversal patterns
        boolean isSuspicious = false;
        
        if (uri.contains("..") || uri.contains(".;")) {
            isSuspicious = true;
        }
        
        // Check for URL encoded traversal
        if (uri.contains("%2e") || uri.contains("%2f") || 
            uri.contains("%252e") || uri.contains("%252f")) {
            isSuspicious = true;
        }
        
        if (queryString != null) {
            if (queryString.contains("..") || queryString.contains(".;") ||
                queryString.contains("%2e") || queryString.contains("%2f")) {
                isSuspicious = true;
            }
        }
        
        if (isSuspicious) {
            logger.warn("Path traversal attempt blocked - IP: {}, URI: {}, Query: {}", 
                clientIP, uri, queryString);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                "Access Denied: Invalid path characters");
            return;
        }
        
        chain.doFilter(req, res);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("PathTraversalFilter initialized");
    }
    
    @Override
    public void destroy() {
        logger.info("PathTraversalFilter destroyed");
    }
}