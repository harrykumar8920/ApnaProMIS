package com.pams.captcha;

import java.io.Serializable;



import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;



public class CaptchaDetails implements Serializable {

	private static final long serialVersionUID = 8372386434886698719L;

	private final String answer;
	private final String captcha;

	public CaptchaDetails(HttpServletRequest request) {
		this.answer = request.getParameter("captcha");
		this.captcha =  (String) WebUtils.getSessionAttribute(request, "captcha");
	}

	public String getAnswer() {
		return answer;
	}

	public String getCaptcha() {
		return captcha;
	}

}