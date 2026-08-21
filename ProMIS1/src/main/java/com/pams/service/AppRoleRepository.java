package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AppRole;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long>{

	
} 
