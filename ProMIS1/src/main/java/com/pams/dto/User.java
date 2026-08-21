package com.pams.dto;

public class User {
	
public User(String username, String token) {
		super();
		this.username = username;
		this.token = token;
	}

public User() {
	super();
}

private String	username;
private String	token;
/**
 * @return the token
 */
public String getToken() {
	return token;
}
/**
 * @param token the token to set
 */
public void setToken(String token) {
	this.token = token;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}

}