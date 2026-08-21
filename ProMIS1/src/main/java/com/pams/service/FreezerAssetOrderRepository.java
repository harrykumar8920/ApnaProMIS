package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.FreezerAssetOrder;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.transaction.Transactional;

public interface FreezerAssetOrderRepository extends JpaRepository<FreezerAssetOrder, Long> {

		@Transactional
		@Query("select max(id) from FreezerAssetOrder")
		public Long findMaxid();
		List<FreezerAssetOrder> findAllByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdtl,AssignedTaskPuhAfterCOurt assignedTaskId, Sort sort);
		List<FreezerAssetOrder> findAllByAssignedTask(AssignedTaskPuh assignedTask);
		
		List<FreezerAssetOrder> findAllByAssignedTaskAndApprovalStatusBetween(AssignedTaskPuhAfterCOurt assignedTaskId, int approvalStatus,int approvalStatus1);
		
		
	}


