package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.Complaintdetl;
import com.pams.entity.ProCourtCaseDetails;

public interface ComplaintdetlRepository extends JpaRepository<Complaintdetl, Long> {

	Complaintdetl findByProcourtdtl(ProCourtCaseDetails proCourtCase);

	Complaintdetl findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails courtDtl, AssignedTaskPuhAfterCOurt assignedTaskPuh);
	
	 List<Complaintdetl> findAllByAssignedTask(AssignedTaskPuhAfterCOurt assignedTaskPuh);
	
	@Query("SELECT COUNT(*) FROM Complaintdetl p where p.approve_status = 1")
			Integer findByApproveStatus();

}
