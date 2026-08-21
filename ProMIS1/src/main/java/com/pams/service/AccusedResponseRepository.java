package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AccusedResponse;
import com.pams.entity.AddAccused;
import com.pams.entity.AddCase;

import jakarta.transaction.Transactional;

public interface AccusedResponseRepository extends JpaRepository<AccusedResponse, Long> {
	
	List<AccusedResponse> findByAddCaseAndAccusedDetails(AddCase addcase,AddAccused accusedDetails,Sort sort);
	
	List<AccusedResponse> findByAddCase(AddCase addcase);
	@Transactional
	@Query("select max(id) from AccusedResponse")
	public Long findMaxid();
}
