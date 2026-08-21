package com.pams.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.DetailsType;
import com.pams.entity.PairaviDetails;
import com.pams.entity.ProCourtCaseDetails;

public interface PairaviDetailsRepository extends JpaRepository<PairaviDetails, Long>{

	List<PairaviDetails> findAllByProcourtdtl(ProCourtCaseDetails procourtdt);
	
	List<PairaviDetails> findAllByProcourtdtlAndApproveStatus(ProCourtCaseDetails procourtdt,int approveStatus);
	List<PairaviDetails> findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdt,AssignedTaskPuhAfterCOurt assignedTask);
	List<PairaviDetails> findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(ProCourtCaseDetails procourtdt,AssignedTaskPuhAfterCOurt assignedTask,int approveStatus,int approveStatus1);
	

	PairaviDetails findAllByProcourtdtlAndDetailsType(ProCourtCaseDetails courtdtl, DetailsType petType);
	PairaviDetails findAllByProcourtdtlAndToDateGreaterThan(ProCourtCaseDetails courtdtl, Date date1);
	PairaviDetails findAllByProcourtdtlAndIsActive(ProCourtCaseDetails courtdtl, Boolean isActive);
	PairaviDetails findAllByProcourtdtlAndIsActiveAndApproveStatus(ProCourtCaseDetails courtdtl, Boolean isActive,int approveStatus);
	
	PairaviDetails findAllByIsActive(Boolean isActive);
	
	@Query("SELECT COUNT(*) FROM PairaviDetails p where p.approveStatus = 1")
			Integer findByApproveStatus();
	

	
	

}
