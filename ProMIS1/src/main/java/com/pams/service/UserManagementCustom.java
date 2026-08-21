package com.pams.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pams.entity.AppUser;
import com.pams.entity.UnitDetails;
import com.pams.entity.UserDetails;

@Repository
public interface UserManagementCustom {
	
	List<UserDetails> findByUnit(UnitDetails unitDetails);
	
	
	public List<UserDetails> findByRole(int roleUser,UnitDetails unitDetails);

	UserDetails findUserDetailsMobile(String mobile);
}
