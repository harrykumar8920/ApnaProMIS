package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.ComplaintReport;

import jakarta.transaction.Transactional;

public interface ComplaintReportRepository extends JpaRepository<ComplaintReport, Long> {
	
	
	ComplaintReport findByAssignedTaskPuhAndTypeOfReport(AssignedTaskPuh assignedtaskpuh,int typeOfReport);
	
	ComplaintReport findByAssignedTaskPuh(AssignedTaskPuhAfterCOurt assignedtaskpuh);

	@Transactional
	@Query( value = "SELECT n FROM ComplaintReport n where n.assignedTaskPuh=:assignedTaskPuh")
	
	
	ComplaintReport findaaaaa(@Param("assignedTaskPuh") AssignedTaskPuh assignedTaskPuh);
	
	
	@Query("SELECT COUNT(*) FROM ComplaintReport p where p.approveStatus = 1")
			Integer findByApproveStatus();
		
	
	
	
	
	}


