package com.pams.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pams.entity.AddCase;
import com.pams.entity.AddSubTask;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CreateTasks;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
@Repository
public interface AssignedTaskPuhAfterCOurtRepository extends JpaRepository<AssignedTaskPuhAfterCOurt, Long> {
	Optional<AssignedTaskPuhAfterCOurt> findById(Long id);
	List<AssignedTaskPuhAfterCOurt> findAllByProCourtCaseDetails(ProCourtCaseDetails pro);
	AssignedTaskPuhAfterCOurt findAllByCreateTaskAndSubtask(CreateTasks createTask,AddSubTask subtask);
	
	AssignedTaskPuhAfterCOurt findAllByCreateTaskAndSubtaskAndProCourtCaseDetails(CreateTasks createTask,AddSubTask subtask,ProCourtCaseDetails proCourtCaseDetails);
	@Query(value = "select * from prosecution.assigned_task_puh_after_court where user_id = :userId and create_task_id not in (1)", nativeQuery = true)
	public List<AssignedTaskPuhAfterCOurt> findAllIfByApproveStatusIsOne(@Param("userId") Long userId);
	Page<AssignedTaskPuhAfterCOurt> findAllByUserAndCreateTaskNot(UserDetails user, CreateTasks createTask, Pageable pagable);
	
	Page<AssignedTaskPuhAfterCOurt> findAllByUser(UserDetails user, Pageable pagable);
	
	List<AssignedTaskPuhAfterCOurt> findAllByUser(UserDetails user);
	
	Page<AssignedTaskPuhAfterCOurt> findAll( Pageable pagable);
	 List<AssignedTaskPuhAfterCOurt> findAll();
	
	
	
	
	
	
	AssignedTaskPuhAfterCOurt findByProCourtCaseDetails(ProCourtCaseDetails pro);
	
}
