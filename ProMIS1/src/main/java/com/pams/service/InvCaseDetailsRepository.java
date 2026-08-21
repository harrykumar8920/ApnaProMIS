package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.InvCaseDetails;

public interface InvCaseDetailsRepository extends JpaRepository<InvCaseDetails, Long>{

	InvCaseDetails findAllByInvcaseDetailsId(Long invcaseDetailsId);

	InvCaseDetails findAllById(Long caseId);

}
