package com.pams.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AddCase;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.transaction.Transactional;

public interface ProCourtCaseDetailsRepository extends JpaRepository<ProCourtCaseDetails, Long> {

//	@Transactional
//	@Query("select max(id) from proCourtCaseDetails")
//	public Long findMaxid();

	@Transactional
	@Query("SELECT MAX(p.id) FROM ProCourtCaseDetails p")
	Long findMaxid();

	public ProCourtCaseDetails findALLById(Long courtId);
	
	List<ProCourtCaseDetails> findByFillingDateIsNotNull();

	public ProCourtCaseDetails findByAssignedTask(AssignedTaskPuhAfterCOurt assignedTask);

	public ProCourtCaseDetails findByAssignedTask(AssignedTaskPuh assignedTask);

	@Query(value = "SELECT * FROM prosecution.prosecution_court_case_details WHERE add_case_pro_sanction_order_id = :addCaseId", nativeQuery = true)
	List<ProCourtCaseDetails> findByAddCaseId(@Param("addCaseId") Long addCaseId);

	public ProCourtCaseDetails findByAddCase(AddCase addCase);

	public ProCourtCaseDetails findAllByCourtCaseNo(String courtCaseNo);

	public List<ProCourtCaseDetails> findALLBySfioAs(String string);

	List<ProCourtCaseDetails> findByCreatedDateBetween(Date fromdate, Date todate);

	List<ProCourtCaseDetails> findByFillingDateBetween(Date fromdate, Date todate);

	List<ProCourtCaseDetails> findByCreatedDateBetweenAndIsMCAParty(Date fromdate, Date todate, Boolean isMCAParty);

	// public List<proCourtCaseDetails> findByproDateBetween(Date todate, Date
	// fromDate);

	public ProCourtCaseDetails findALLByIdAndIsMCAParty(Long courtId, Boolean isMCAParty);

	public List<ProCourtCaseDetails> findALLByApproveStatusBetweenAndCreatedBy(int approveStatus, int approveStatus2,
			UserDetails createdBy);

	public List<ProCourtCaseDetails> findALLByApproveStatusAndCreatedBy(int approveStatus, UserDetails createdBy);

	public List<ProCourtCaseDetails> findALLByApproveStatusBetween(int approveStatus, int approveStatus2, Sort sort);

	public List<ProCourtCaseDetails> findALLByApproveStatusBetween(int approveStatus, int approveStatus2);

	public Page<ProCourtCaseDetails> findALLByApproveStatusBetween(Pageable pageable, int approveStatus,
			int approveStatus2);

	public List<ProCourtCaseDetails> findALLByCreatedBy(UserDetails createdBy, Sort sort);

	public Page<ProCourtCaseDetails> findALLByCreatedBy(UserDetails createdBy, Pageable pageable);

	@Query("SELECT u FROM ProCourtCaseDetails u WHERE u.approveStatus = 1")
	List<ProCourtCaseDetails> findAllIfByApproveStatusIsOne();

	public List<ProCourtCaseDetails> findByApproveStatus(int approveStatus);

	public List<ProCourtCaseDetails> findAllByApproveStatus(int approveStatus, Sort sort);
	
	@Query("""
		    SELECT c FROM ProCourtCaseDetails c
		    WHERE c.approveStatus = :status
		    AND c.id NOT IN (
		        SELECT a.proCourtCaseDetails.id
		        FROM AssignedTaskPuhAfterCOurt a
		    )
		    ORDER BY c.id DESC
		""")
		List<ProCourtCaseDetails> 
		findCourtCasesWithoutAssignedTask(@Param("status") Integer status);


	public Page<ProCourtCaseDetails> findALLByApproveStatusBetweenAndCreatedBy(int approveStatus, int approveStatus2,
			UserDetails createdBy, Pageable pagable);

	public Page<ProCourtCaseDetails> findALLByApproveStatusAndCreatedBy(Pageable pageable, int approveStatus,
			UserDetails createdBy);

	
	
	    @Transactional
	    @Query(value = "SELECT prosecution.delete_court_case(:caseId)", nativeQuery = true)
	    String deleteCourtCaseById(@Param("caseId") Long caseId);
}
