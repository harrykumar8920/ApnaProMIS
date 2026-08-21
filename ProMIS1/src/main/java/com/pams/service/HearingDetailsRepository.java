package com.pams.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.HearingDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.UserDetails;

import jakarta.transaction.Transactional;

public interface HearingDetailsRepository extends JpaRepository<HearingDetails, Long> {

	@Transactional
	@Query("select max(id) from HearingDetails")
	public Long findMaxid();

	public HearingDetails findAllById(Long id);
	
	
	
	@Modifying
	@Transactional
	@Query(
	    value = """
	        UPDATE prosecution.pro_hearing_details
	        SET latesthdstatus = false
	        WHERE procourtdtl_court_case_id = :caseId
	          AND latesthdstatus = true
	    """,
	    nativeQuery = true
	)
	int unsetLatestHearing(@Param("caseId") Long caseId);
	
	
	@Modifying

	@Transactional

	@Query(value = """

				        SELECT DISTINCT ON (procourtdtl_court_case_id) *

			FROM prosecution.pro_hearing_details

			WHERE last_hearing_date IS NOT NULL

			ORDER BY procourtdtl_court_case_id, last_hearing_date DESC;

				    """, nativeQuery = true)

	List<HearingDetails> findAllByQuery();

	
	
	
	
	List<HearingDetails> findByNextHearingDateBetweenOrderByNextHearingDateAsc(
	        Date fromDate,
	        Date toDate);
	List<HearingDetails> findByNextHearingDateBetweenOrderByNextHearingDateDesc(
	        Date fromDate,
	        Date toDate);
	
	
	List<HearingDetails> findByNextHearingDateGreaterThanOrderByNextHearingDateDesc(Date hearingdate);

	List<HearingDetails> findByNextHearingDateBetween(Date fromdate,Date todate);
	List<HearingDetails> findByProcourtdtl(ProCourtCaseDetails procourtdtl);
	List<HearingDetails> findByProcourtdtlAndNextHearingDateAndCurrentStatus(ProCourtCaseDetails procourtdtl,Date todayDate,boolean currentStatus);
	
	@Query(value="SELECT DISTINCT ON (h.procourtdtl_court_case_id) h.*\r\n"
			+ "FROM prosecution.pro_hearing_details h\r\n"
			+ "WHERE h.user_id = :userId\r\n"
			+ "  AND h.next_hearing_date <= :todayDate\r\n"
			+ "  AND NOT EXISTS (\r\n"
			+ "      SELECT 1\r\n"
			+ "      FROM prosecution.pro_hearing_details h2\r\n"
			+ "      WHERE h2.procourtdtl_court_case_id = h.procourtdtl_court_case_id\r\n"
			+ "        AND h2.next_hearing_date > :todayDate\r\n"
			+ "  )\r\n"
			+ "ORDER BY h.procourtdtl_court_case_id, h.next_hearing_date DESC;", nativeQuery=true)
		List<HearingDetails> findLatestHearingPerCase(@Param("userId") Long userId, @Param("todayDate") Date todayDate);
	
	@Query(value="SELECT DISTINCT ON (h.procourtdtl_court_case_id) h.*\r\n"
			+ "FROM prosecution.pro_hearing_details h\r\n"
			+ "WHERE h.dstatus=0 and h.next_hearing_date <= :todayDate\r\n"
			+ "  AND NOT EXISTS (\r\n"
			+ "      SELECT 1\r\n"
			+ "      FROM prosecution.pro_hearing_details h2\r\n"
			+ "      WHERE h2.procourtdtl_court_case_id = h.procourtdtl_court_case_id\r\n"
			+ "        AND h2.next_hearing_date > :todayDate\r\n"
			+ "  )\r\n"
			+ "ORDER BY h.procourtdtl_court_case_id, h.next_hearing_date DESC;", nativeQuery=true)
		List<HearingDetails> findLatestHearingPerCase(@Param("todayDate") Date todayDate);
	
	
	List<HearingDetails> findByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdtl,AssignedTaskPuh assignedTask);
	List<HearingDetails> findByProcourtdtlAndAssignedTask(ProCourtCaseDetails procourtdtl,AssignedTaskPuhAfterCOurt assignedTask,Sort sort);
	List<HearingDetails> findByProcourtdtl(ProCourtCaseDetails procourtdtl,Sort sort);
	
	//@Query("SELECT * FROM HearingDetails p where p.approveStatus = 1 or p.approveStatus = 2 or p.approveStatus = 3")
	//@Query(value = "SELECT * FROM prosecution.pro_hearing_details where approve_status=3 or approve_status=1 or approve_status=2 and procourtdtl_court_case_id=id and assigned_task_id=id1" , nativeQuery = true)
	List<HearingDetails> findByProcourtdtlAndAssignedTaskAndApproveStatusBetween(ProCourtCaseDetails procourtdtl,AssignedTaskPuhAfterCOurt assignedTask,int approveStatus,int approveStatus1);
	
	HearingDetails findByProcourtdtlAndCurrentStatus(ProCourtCaseDetails procourtdtl,boolean currentStatus);
	
	HearingDetails findByProcourtdtlAndLatestHDStatus(ProCourtCaseDetails procourtdtl,boolean latestHDStatus);
	List<HearingDetails> findByNextHearingDateBetweenAndUser(Date fromdate,Date todate,UserDetails user);
	
	HearingDetails findByProcourtdtlAndCurrentStatusAndAssignedTask(ProCourtCaseDetails procourtdtl,boolean currentStatus,AssignedTaskPuhAfterCOurt assignedTask);
	@Query("SELECT COUNT(*) FROM HearingDetails p where p.approveStatus = 1")
	Integer findByApproveStatus();
	
	List<HearingDetails> findByNextHearingDate(Date todayDate);
	List<HearingDetails> findByNextHearingDateAndApproveStatus(Date todayDate,int approveStatus);
	
	List<HearingDetails> findByNextHearingDateBetweenAndApproveStatus(Date fromdate,Date todate,int approveStatus);
	List<HearingDetails> findByNextHearingDateBetweenAndApproveStatus(Date fromdate,Date todate,int approveStatus,Sort sort);
	 
	HearingDetails findByProcourtdtlAndCurrentStatusAndApproveStatus(ProCourtCaseDetails procourtdtl,boolean currentStatus,int approveStatus);


	@Query("""
		    SELECT h
		    FROM HearingDetails h
		    WHERE h.nextHearingDate = (
		        SELECT MAX(h2.nextHearingDate)
		        FROM HearingDetails h2
		        WHERE h2.procourtdtl = h.procourtdtl
		    )
		    AND h.nextHearingDate BETWEEN :fromDate AND :toDate
		""")
		List<HearingDetails> findLatestHearingBetweenDates(
		        @Param("fromDate") Date fromDate,
		        @Param("toDate") Date toDate);
	
	
	
	
	
	
	
	
	@Query(value = """
	        SELECT phd.*
	        FROM prosecution.pro_hearing_details phd
	        INNER JOIN prosecution.prosecution_court_case_details pccd
	                ON phd.procourtdtl_court_case_id = pccd.court_case_id
	        WHERE  (phd.procourtdtl_court_case_id, phd.next_hearing_date) IN (
    SELECT procourtdtl_court_case_id, MAX(next_hearing_date)
    FROM prosecution.pro_hearing_details
    GROUP BY procourtdtl_court_case_id
) and
	        pccd.filling_date >= CURRENT_DATE - INTERVAL '1 year' * :years
	          AND phd.status_pro_status_id = :statusId
	        """, nativeQuery = true)
	List<HearingDetails> findAllByFillingDateNotNull(
	        @Param("statusId") Long statusId,
	        @Param("years") int years);	
	
	
	@Query(value = """
SELECT phd.*
FROM prosecution.pro_hearing_details phd
INNER JOIN prosecution.prosecution_court_case_details pccd
	                ON phd.procourtdtl_court_case_id = pccd.court_case_id
WHERE  (phd.procourtdtl_court_case_id, phd.next_hearing_date) IN (
    SELECT procourtdtl_court_case_id, MAX(next_hearing_date)
    FROM prosecution.pro_hearing_details
    GROUP BY procourtdtl_court_case_id
)

  """, nativeQuery = true)
	List<HearingDetails> findAllByFillingDateNotNull();
	        
	     
	
	@Query(value = """
	        SELECT phd.*
	        FROM prosecution.pro_hearing_details phd
	        INNER JOIN prosecution.prosecution_court_case_details pccd
	                ON phd.procourtdtl_court_case_id = pccd.court_case_id
	        WHERE (phd.procourtdtl_court_case_id, phd.next_hearing_date) IN (
    SELECT procourtdtl_court_case_id, MAX(next_hearing_date)
    FROM prosecution.pro_hearing_details
    GROUP BY procourtdtl_court_case_id
) and
	         phd.status_pro_status_id = :statusId
	        """, nativeQuery = true)
	List<HearingDetails> findAllByFillingDateNotNull(
	        @Param("statusId") Long statusId);	
	
	@Query(value = """
	        SELECT phd.*
	        FROM prosecution.pro_hearing_details phd
	        INNER JOIN prosecution.prosecution_court_case_details pccd
	                ON phd.procourtdtl_court_case_id = pccd.court_case_id
	        WHERE (phd.procourtdtl_court_case_id, phd.next_hearing_date) IN (
    SELECT procourtdtl_court_case_id, MAX(next_hearing_date)
    FROM prosecution.pro_hearing_details
    GROUP BY procourtdtl_court_case_id
) and
	          pccd.filling_date >= CURRENT_DATE - INTERVAL '1 year' * :years
	        """, nativeQuery = true)
	List<HearingDetails> findAllByFillingDateNotNull(
	        @Param("years") int years);	
	
	
	
	
	
}
