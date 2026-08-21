package com.pams.controllers;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class MyResourceHttpRequestHandler extends ResourceHttpRequestHandler {
    final static String ATTR_FILE = MyResourceHttpRequestHandler.class.getName() + ".file";

    @Override
    protected org.springframework.core.io.Resource getResource(HttpServletRequest request) throws IOException {
        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
        
       
    }
}