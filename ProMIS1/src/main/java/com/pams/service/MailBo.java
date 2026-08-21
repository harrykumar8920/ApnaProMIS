package com.pams.service;

import org.springframework.stereotype.Service;

import com.pams.dto.MailInfo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface MailBo
{
    void sendMail(MailInfo info, int messageType, HttpServletRequest req) throws Exception;

	/*void sendMessage(String obj, int smsVarifyOtp,String mobileno) throws Exception;
	
	String getAutoEmailTemplate(MailInfo info, int messageType);*/
}
