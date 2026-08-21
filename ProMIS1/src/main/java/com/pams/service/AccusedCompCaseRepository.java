package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AccusedCompCaseDtl;


public interface AccusedCompCaseRepository extends JpaRepository< AccusedCompCaseDtl, Integer>{
	
	public List<AccusedCompCaseDtl> findByProCourtId(Long proCourtId );
	
	

}
