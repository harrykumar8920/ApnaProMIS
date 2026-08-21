package com.pams.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AddCompany;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;

public interface CaseCompanyRepository extends JpaRepository<CaseCompany, Long> {

	List<CaseCompany> findByProcourtdtl(ProCourtCaseDetails courtdtl);
	List<CaseCompany> findByProcourtdtlAndApproveStatus(ProCourtCaseDetails courtdtl,int approveStatus);

	CaseCompany findAllByCompanyAndProcourtdtl(AddCompany addCompany, ProCourtCaseDetails courtdtl);
	CaseCompany findAllByCompanyAndAssignedTask(AddCompany addCompany, AssignedTaskPuhAfterCOurt assignedTask);

	List<CaseCompany> findByAssignedTask(AssignedTaskPuh assignedTask);
	List<CaseCompany> findByAssignedTaskAndApproveStatusBetween(AssignedTaskPuhAfterCOurt assignedTask,int approveStatus,int approveStatus1);
	
	
			@Query("SELECT COUNT(*) FROM CaseCompany p where p.approveStatus = 1")
			Integer findByApproveStatus();
			
			@Query( value = "select * from prosecution.prosecution_case_company_details where assigned_task_id=:assignedTask and company_id >0",  nativeQuery = true)

			public List<CaseCompany> findAllIbyAssignTask(@Param("assignedTask") Long assignedTask);
			
			
	
			
}
