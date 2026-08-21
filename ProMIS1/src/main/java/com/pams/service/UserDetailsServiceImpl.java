package com.pams.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.pams.dao.AppRoleDAO;
import com.pams.dao.AppUserDAO;
import com.pams.entity.AppUser;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AppRoleDAO appRoleDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser appUser = this.appUserDAO.findUserAccount(userName);

		if (appUser == null) {

			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}
		if (appUser.getEncrytedPassword() == null || "".equals(appUser.getEncrytedPassword().trim())
				|| "null".equalsIgnoreCase(appUser.getEncrytedPassword())) {
			throw new UsernameNotFoundException("Password not found");
		}

		// [ROLE_USER, ROLE_ADMIN,..]
		List<String> roleNames = this.appRoleDAO.getRoleNames(appUser.getUserId());

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), //
				appUser.getEncrytedPassword(), grantList);

		return userDetails;
	}

	public HttpSession getCurrentSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession();
	}

	public String getLoginUserName() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		if (name != null) {
			return name;

		} else {
			throw new InternalAuthenticationServiceException("UserId or Password is wrong. ");
		}

	}

	public AppUser getUserDetails() throws Exception {
		AppUser appUser = this.appUserDAO.findUserAccount(getLoginUserName());
		return appUser;
	}

	public com.pams.entity.UserDetails getUserDetailssss() throws Exception {
		com.pams.entity.UserDetails usrDetails = this.appUserDAO.findUserDetails(getUserDetails());
		return usrDetails;
	}

	public String getFullName(com.pams.entity.UserDetails userDet) {
		StringBuffer fullName = new StringBuffer();
		fullName.append(userDet.getFirstName().toString());
		if (userDet.getMiddleName() != null && !"".equalsIgnoreCase(userDet.getMiddleName().trim())) {
			fullName.append(" " + userDet.getMiddleName().trim());
		}
		if (userDet.getLastName() != null && !"".equalsIgnoreCase(userDet.getLastName().trim())) {
			fullName.append(" " + userDet.getLastName().trim());
		}
		return fullName.toString();
	}

	public String getPrimaryMobile(com.pams.entity.UserDetails userDet) {
		return userDet.getPrimaryMobile();
	}
}