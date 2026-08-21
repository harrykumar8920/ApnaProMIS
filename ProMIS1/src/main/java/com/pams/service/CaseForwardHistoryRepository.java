package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.CaseForwardHistory;
import com.pams.entity.ProCourtCaseDetails;

@Repository
public interface CaseForwardHistoryRepository extends JpaRepository<CaseForwardHistory, Long> {
	
	List<CaseForwardHistory> findAllByProCourtCaseDetails(ProCourtCaseDetails proCourtCaseDetails);

}
