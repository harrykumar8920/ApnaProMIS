package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.CouncilDetails;
import com.pams.entity.DetailsType;

import com.pams.entity.ProCourtCaseDetails;

public interface CouncilDetailsRepository extends JpaRepository<CouncilDetails, Long> {
	
	
	List<CouncilDetails> findAllByProcourtdtl(ProCourtCaseDetails procourtdt);

	CouncilDetails findAllByProcourtdtlAndDetailsType(ProCourtCaseDetails courtdtl, DetailsType petType);

}
