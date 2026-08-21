package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.DetailsType;
import com.pams.entity.ProCourtCaseDetails;

public interface DetailsTypeRespository extends JpaRepository<DetailsType, Long> {

	DetailsType findAllById(long l);
	

}
