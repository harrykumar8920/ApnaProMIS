package com.pams.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.dto.ProsecutionAccusedDetailsDTO;
import com.pams.entity.AccusedMaster;
import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.PairaviDetails;
import com.pams.entity.Status;
import com.pams.entity.ProCourtCaseDetails;

public interface AddAccusedRepository extends JpaRepository<AddAccused, Long> {

	//AddAccused findAllByAccusedMaster(AccusedMaster accusedMasterdtl);
	List<AddAccused> findAllByAssignedTask(AssignedTaskPuhAfterCOurt assignedTask);
	List<AddAccused> findAllByAssignedTask(AssignedTaskPuhAfterCOurt assignedTask,Sort sort);

	//AddAccused findAllByAccusedMasterAndProcourtdtl(AccusedMaster accusedMasterdtl, proCourtCaseDetails courtdtl);

	//AddAccused findAllByAccusedMasterAndProcourtdtlAndCompany(AccusedMaster accusedMasterdtl,proCourtCaseDetails courtdtl, CaseCompany company);
			

	//List<AddAccused> findAllByProcourtdtl(proCourtCaseDetails procourtdtl);
	
	List<AddAccused> findAllByProcourtdtl(ProCourtCaseDetails procourtdt);
	List<AddAccused> findAllByProcourtdtlAndAccusedType(ProCourtCaseDetails procourtdt,String accusedType);
	
	List<AddAccused> findAllByProcourtdtlAndApproveStatus(ProCourtCaseDetails procourtdt,int approveStatus);
	
	
	List<AddAccused> findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdt,AssignedTaskPuh assignedTask);
	List<AddAccused> findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdt,AssignedTaskPuhAfterCOurt assignedTask,Sort sort);
	List<AddAccused> findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(ProCourtCaseDetails procourtdt,AssignedTaskPuhAfterCOurt assignedTask,int approveStatus,int approveStatus1);

	List<AddAccused> findAllByAssignedTaskAndAccusedTypeNot(AssignedTaskPuhAfterCOurt assignedTask,String accusedType);
	
	List<AddAccused> findAllByProcourtdtlAndAccusedTypeNot(ProCourtCaseDetails assignedTask,String accusedType);
	
	//AddAccused findAllByAccusedMasterAndprocourtdtl(AccusedMaster accusedMasterdtl, proCourtCaseDetails courtdtl);
	
	//List<AddAccused> findAllByCreatedDateBetweenAndAccusedstatus(Date fromDate,Date toDate,Status accusedstatus);
	List<AddAccused> findAllByCreatedDateBetween(Date fromDate,Date toDate);
	
	List<AddAccused> findAllByProcourtdtlAndCompany(ProCourtCaseDetails courtdtl, CaseCompany company);
	
	@Query("SELECT COUNT(*) FROM AddAccused p where p.approveStatus = 1")
			Integer findByApproveStatus();
	
	
	//AddAccused findAllByAccusedMasterAndProcourtdtlAndActAndSection(AccusedMaster accusedMasterdtl, proCourtCaseDetails courtdtl,AddAct act,AddActSec section);
	
	@Query(value = """
	        SELECT ad.accused_id AS accusedId, 
	               ad.accused_name AS accusedName, 
	               ad.pan_number AS panNumber
	        FROM prosecution.prosecution_accused_details AS ad
	        INNER JOIN prosecution.prosecution_court_case_details AS co
	        ON ad.procourtdtl_court_case_id = co.court_case_id
	        INNER JOIN prosecution.prosecution_sanction_order_details AS so
	        ON co.add_case_pro_sanction_order_id = so.pro_sanction_order_id
	        WHERE so.investigation_order_no = :investigationOrderNo
	        """, nativeQuery = true)
	    List<ProsecutionAccusedDetailsDTO> findAccusedDetailsByInvestigationOrderNo(@Param("investigationOrderNo") String investigationOrderNo);
	

}
