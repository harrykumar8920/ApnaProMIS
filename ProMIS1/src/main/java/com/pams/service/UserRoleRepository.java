package com.pams.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AppUser;
import com.pams.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{



	Optional<UserRole> findByAppUser(Long userId);
	
	UserRole findByAppUser(AppUser appUser);

}
