package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.CaseStatus;
import com.pams.entity.ProCourtCaseDetails;

public interface CaseStatusRepository  extends JpaRepository<CaseStatus, Long> {

	List<CaseStatus> findAllByProcourtdtl(ProCourtCaseDetails procourtdt);

}
