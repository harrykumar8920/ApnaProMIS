package com.pams.validation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.pams.entity.ChargeInstaceMain;
import com.pams.service.ChanrgeInstanceRepository;

import jakarta.validation.Valid;


public class ChargeInstanceValidation {
	@Autowired
	private ChanrgeInstanceRepository chargeInstanceRepo;
	
	public void chargeValidation(@Valid ChargeInstaceMain chargeInstace, Errors errors) throws IOException {
		
		
		if(chargeInstace.getAccuseName1().isEmpty()) {
		errors.rejectValue("accuseName", "msg.instanseName");
		}
		/*
		 * if(chargeInstace.getAct().getId()==0) { errors.rejectValue("act",
		 * "msg.instanseName"); }
		 */
		if(chargeInstace.getCharge()==null) {
			errors.rejectValue("charge", "msg.instanseName");
			}
		
			
			/*
			 * if(chargeInstace.getReleventSection().length()>=250) {
			 * errors.rejectValue("releventSection", "msg.instanseName"); }
			 */
			 
		
	}

}
