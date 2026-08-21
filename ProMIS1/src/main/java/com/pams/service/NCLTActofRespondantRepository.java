package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AddActSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.NCLTActofRespondant;
import java.util.List;


public interface NCLTActofRespondantRepository extends JpaRepository<NCLTActofRespondant, Long>{
	
	List<NCLTActofRespondant> findByAssignedTaskAndApprovalStatus(AssignedTaskPuh assignedTask,Integer approvalStatus);
	
	

	List<NCLTActofRespondant> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedTask);
	
	 @Query("SELECT DISTINCT s FROM NCLTActofRespondant n JOIN n.section s WHERE n.assignedTask.id = :taskId")
	    List<AddActSec> findDistinctSectionsByAssignedTask(@Param("taskId") Long taskId);
	 
	 
	 @Query("SELECT DISTINCT s FROM NCLTActofRespondant n " +
		       "JOIN n.section s " +
		       "WHERE n.assignedTask.id = :assignedTaskId " +
		       "AND s.id IN :sectionIds")
		List<AddActSec> findDistinctSectionsByAssignedTask1(
		    @Param("assignedTaskId") Long assignedTaskId,
		    @Param("sectionIds") List<Long> sectionIds);

		  

}
