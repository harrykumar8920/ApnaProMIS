package com.pams.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {



	Optional<AppUser> findByUserId(Long UserId);

	List<AppUser> findAllByEnabledAndIsApproved(int i, boolean b);

	//void findAllByEnabled(int i);

	//void findByEnable(int i);

}
