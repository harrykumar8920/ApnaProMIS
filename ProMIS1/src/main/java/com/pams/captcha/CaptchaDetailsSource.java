package com.pams.captcha;



import org.springframework.security.authentication.AuthenticationDetailsSource;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
@Service
public class CaptchaDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CaptchaDetails> {

	@Override
	public CaptchaDetails buildDetails(HttpServletRequest context) {
		return new CaptchaDetails(context);
	}

}
