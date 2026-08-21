package com.pams.service;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.ProCourtCaseDetails;

public interface CaseProcessingDatesRepository extends JpaRepository<CaseProcessingDates, Long> {
	
	
	 List<CaseProcessingDates> findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdtl,AssignedTaskPuhAfterCOurt assignedTask);
	 
	 
	 List<CaseProcessingDates> findAllByProcourtdtl(ProCourtCaseDetails procourtdtl);
	 
	 
	 List<CaseProcessingDates> findAllByAssignedTask(AssignedTaskPuhAfterCOurt assignedTask); 
	
	 CaseProcessingDates findByProcourtdtl(ProCourtCaseDetails procourtdtl);
	 
		
		@Query("SELECT COUNT(*) FROM CaseProcessingDates p where p.approveStatus = 1")
				Integer findByApproveStatus();

		List<CaseProcessingDates> findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
				ProCourtCaseDetails procasedetails,  AssignedTaskPuhAfterCOurt assignedTaskPuh, int approveStatus,
				int approveStatus1);
}
