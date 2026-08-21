package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.UploadAdditionalFilesDetails;

import jakarta.transaction.Transactional;

public interface UploadAdditionalFilesDetailsRepository extends JpaRepository<UploadAdditionalFilesDetails, Long>{

	@Transactional
	@Query("select max(id) from UploadAdditionalFilesDetails")
	public Long findMaxid();
	List<UploadAdditionalFilesDetails> findByAssignedTaskPuhdtl(AssignedTaskPuhAfterCOurt  assignedTaskPuhdtl);
	
	List<UploadAdditionalFilesDetails> findByAssignedTaskPuhdtlAndApproveStatusBetween(AssignedTaskPuhAfterCOurt assignedTask,int approveStatus,int approveStatus1);
	
	@Query("SELECT COUNT(*) FROM UploadAdditionalFilesDetails p where p.approveStatus = 1")
			Integer findByApproveStatus();
	
}
