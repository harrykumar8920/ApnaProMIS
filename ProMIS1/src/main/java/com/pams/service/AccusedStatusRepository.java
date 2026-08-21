package com.pams.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AccusedStatus;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Status;
import com.pams.entity.ProCourtCaseDetails;

import java.util.List;
import java.lang.Long;

public interface AccusedStatusRepository extends JpaRepository<AccusedStatus, Long> {
	
	List<AccusedStatus> findByAssignedTaskAndApproveStatus(AssignedTaskPuh assignedtask,int approveStatus,Sort sort);
	List<AccusedStatus> findByAssignedTaskAndApproveStatus(AssignedTaskPuh assignedtask,int approveStatus);
	List<AccusedStatus> findByAssignedTaskAndApproveStatusAndAddAccusedAndStatus(AssignedTaskPuh assignedtask,int approveStatus,AddAccused addAccused,boolean status);
	List<AccusedStatus> findByAssignedTaskAndApproveStatusAndAddAccusedAndStatus(AssignedTaskPuhAfterCOurt assignedtask,int approveStatus,AddAccused addAccused,boolean status);
	List<AccusedStatus> findByHearingDetails(Long hearingdetails);
	
	List<AccusedStatus> findByAssignedTaskAndApproveStatusAndStatus(AssignedTaskPuhAfterCOurt assignedtask,int approveStatus,boolean status,Sort sort);
	
	List<AccusedStatus> findByProcourtdtlAndApproveStatusAndStatus(ProCourtCaseDetails procourtdtl,int approveStatus,boolean status,Sort sort);
	
	
	
	List<AccusedStatus> findByAssignedTaskAndApproveStatusAndStatus(AssignedTaskPuh assignedtask,int approveStatus,boolean status);
	
	List<AccusedStatus> findByHearingDetailsAndStatus(Long hearingdetails,boolean status);
	
	
	List<AccusedStatus> findByAssignedTaskAndApproveStatusAndAddAccusedAndCaseStatus(AssignedTaskPuh assignedtask,int approveStatus,AddAccused addAccused,Status caseStatus);

}
