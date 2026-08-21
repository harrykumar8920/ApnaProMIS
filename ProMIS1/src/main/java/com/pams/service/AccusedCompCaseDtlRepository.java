package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;


import com.pams.entity.AccusedCompCaseDtl;

public interface AccusedCompCaseDtlRepository extends JpaRepository<AccusedCompCaseDtl, Long> {

	AccusedCompCaseDtl findAllByProCourtId(Long id);
	
	
	
	
}
