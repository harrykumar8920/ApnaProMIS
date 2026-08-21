package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.ResponseOfRespondent;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.transaction.Transactional;

public interface ResponseOfRespondentRepository extends JpaRepository<ResponseOfRespondent, Long> {
	@Transactional
	@Query("select max(id) from ResponseOfRespondent")
	public Long findMaxid();
	List<ResponseOfRespondent> findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdtl,AssignedTaskPuhAfterCOurt assignedTaskId, Sort sort);
	
	List<ResponseOfRespondent> findAllByAssignedTaskAndApprovalStatusBetween(AssignedTaskPuhAfterCOurt assignedTaskId, int approvalStatus,int approvalStatus1);

}
