package com.pams.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceLayerAPI {
	
	@Autowired
	private RestTemplate resttemplate;

	
	
	public String  cunsumeAPI( String url) {
		
		
		
		
		return resttemplate.getForObject(url, String.class);
	}
	
		
	
}
