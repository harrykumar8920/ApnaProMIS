package com.pams.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AddCase;
import com.pams.entity.AddSubTask;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.CreateTasks;
import com.pams.entity.UserDetails;

import jakarta.transaction.Transactional;

@Transactional
public interface AssignedTasksPuhRepository extends JpaRepository<AssignedTaskPuh, Long> {

	@Query(value = "SELECT *\r\n" + "FROM prosecution.assigned_task_puh\r\n"
			+ "WHERE id = ( SELECT MAX(id) FROM prosecution.assigned_task_puh where add_case_pro_sanction_order_id =:id )", nativeQuery = true)

	public AssignedTaskPuh findbyID1(@Param("id") AddCase addCase);

	@Query("Select COUNT (DISTINCT c.addCase) from AssignedTaskPuh c")
	Integer findCountByCase();
	
	
	@Query(value = "SELECT a.add_case_pro_sanction_order_id, COUNT(a.id) " +
            "FROM prosecution.assigned_task_puh a " +
            "GROUP BY a.add_case_pro_sanction_order_id",
    nativeQuery = true)
List<Object[]> getTaskCountGroupByCase();


	Optional<AssignedTaskPuh> findById(Long id);

	// AssignedTaskPuh findAllByProCourtCaseAndCreateTask(proCourtCaseDetails
	// proCourtCase,CreateTasks createTask);
	// AssignedTaskPuh
	// findAllByProCourtCaseAndCreateTaskAndSubtask(proCourtCaseDetails
	// proCourtCase,CreateTasks createTask,AddSubTask subtask);

	AssignedTaskPuh findAllByAddCaseAndCreateTaskAndSubtask(AddCase addCase, CreateTasks createTask,
			AddSubTask subtask);

	List<AssignedTaskPuh> findAllByIsApproved(boolean b);
	Page<AssignedTaskPuh> findAllByIsApproved(boolean b,Pageable pageable);
	List<AssignedTaskPuh> findAllByUser(UserDetails user);

	Page<AssignedTaskPuh> findAllByUser(UserDetails user, Pageable pageable);

//	List<AssignedTaskPuh> findAllByProCourtCase(proCourtCaseDetails pcrtdtls);

	List<AssignedTaskPuh> findAllByAddCase(AddCase addCase);

	List<AssignedTaskPuh> findAllByUserAndIsApproved(UserDetails user, boolean b);

	List<AssignedTaskPuh> findAllByUserAndIsApprovedAndApprovalStatus(UserDetails user, boolean b,
			Integer approvalStatus);

	Page<AssignedTaskPuh> findAllByUserAndIsApprovedAndApprovalStatus(Pageable pageable, UserDetails user, boolean b,
			Integer approvalStatus);

	List<AssignedTaskPuh> findAllByUserAndIsApprovedAndApprovalStatus(UserDetails user, boolean b,
			Integer approvalStatus, Sort sort);

	List<AssignedTaskPuh> findAllByUserAndIsApproved(UserDetails user, boolean b, Sort sort);

	// AssignedTaskPuh findAllByUserAndProCourtCaseAndCreateTask(UserDetails
	// user,proCourtCaseDetails proCourtCase,CreateTasks createTask);
	// AssignedTaskPuh
	// findAllByUserAndProCourtCaseAndCreateTaskAndSubtask(UserDetails
	// user,proCourtCaseDetails proCourtCase,CreateTasks createTask,AddSubTask
	// subtask);

	// AssignedTaskPuh findAllByUserAndProCourtCase(UserDetails
	// user,proCourtCaseDetails proCourtCase);

	// List<AssignedTaskPuh> findAllByUserAndIsApprovedAndApprovalStatus(UserDetails
	// user, boolean b, long l);
	List<AssignedTaskPuh> findAllByUserAndApprovalStatus(UserDetails user, long l);

	List<AssignedTaskPuh> findAllByUserAndIsApprovedAndApprovalStatus(UserDetails user, boolean b, long l, Sort sort);

	List<AssignedTaskPuh> findAllByUserAndApprovalStatus(UserDetails user, long l, Sort sort);

	List<AssignedTaskPuh> findAllByUserAndCreateTaskBetween(UserDetails user, CreateTasks createTask,
			CreateTasks createTask1);

	@Query(value = "select * from prosecution.assigned_task_puh where user_id = :userId and create_task_id not in (1)", nativeQuery = true)
	public List<AssignedTaskPuh> findAllIfByApproveStatusIsOne(@Param("userId") Long userId);

	
	  @Query(value =
	  "select * from prosecution.assigned_task_puh where user_id= :user   and create_task_id not in( 1)"
	  , nativeQuery = true) public Page<AssignedTaskPuh>
	  findAllByIsApproveStatusOne(@Param("user") UserDetails user, Pageable
	  pagable);
	 
	
	

	@Query(value = "select * from prosecution.assigned_task_puh where create_task_id not in( 1)", nativeQuery = true)

	public List<AssignedTaskPuh> findAllIfByApproveStatus();

	Page<AssignedTaskPuh> findAllByUserAndCreateTaskNot(UserDetails user, CreateTasks createTask, Pageable pagable);

}
