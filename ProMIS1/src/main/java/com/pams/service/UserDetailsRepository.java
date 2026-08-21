package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AddDesignation;
import com.pams.entity.UserDetails;
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>{
	
	public UserDetails findByDesignation(AddDesignation designation);

	public UserDetails findAllByEmail(String username);

	
	

}
