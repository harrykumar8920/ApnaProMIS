package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Inspector;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.transaction.Transactional;
@Repository
public interface InspectorRepository extends JpaRepository<Inspector, Long> {
	
	@Transactional
	@Query("select max(id) from Inspector")
	public Long findMaxid();
	
	 List<Inspector> findAll(Sort sort);
	 List<Inspector> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedtask,Sort sort);
		
		List<Inspector> findByProcourtdtl(ProCourtCaseDetails proCourtCaseDetails);
}
