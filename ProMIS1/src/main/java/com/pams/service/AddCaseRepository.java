package com.pams.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pams.entity.AddCase;
import com.pams.entity.Type;
import com.pams.entity.UserDetails;

import jakarta.transaction.Transactional;
@Repository
public interface AddCaseRepository extends CrudRepository<AddCase, Long> {
	@Transactional
	@Query("select max(id) from AddCase")
	public Long findMaxid();

    public AddCase findByCaseTitle(String caseTitle);
    public List<AddCase> findAllByCaseTitle(String caseTitle);
    public List<AddCase> findAllByCaseTitleContainingIgnoreCase(String caseTitle);
    boolean existsByCinNumber(String cinNumber);
    public AddCase findByCinNumber(String cinNumber);
	public List<AddCase> findByProSectionOrderNumber(String prosectionordernumber);

	public List<AddCase> findALLByCreatedBy(UserDetails createdBy, Sort sort);

	public List<AddCase> findAll(Sort sort);

	public Page<AddCase> findAll(Pageable pagable);
	
	
	
	
	
	
	
	
	@Query(value = """
		    SELECT *
		    FROM prosecution.prosecution_sanction_order_details
		    WHERE LOWER(pro_section_order_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(file_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(case_title) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(cin_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(investigation_order_no) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    """,
		    countQuery = """
		    SELECT COUNT(*)
		    FROM prosecution.prosecution_sanction_order_details
		    WHERE LOWER(pro_section_order_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(file_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(case_title) LIKE LOWER(CONCAT('%', :keyword, '%'))
		       OR LOWER(cin_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(investigation_order_no) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    """,
		    nativeQuery = true)
		Page<AddCase> search(
		        @Param("keyword") String keyword,
		        Pageable pageable);
	
	
	

	
	
	
	
	
	
	
	

	/* public Page<AddCase> findByType(Type type, Pageable pagable); */

	public Page<AddCase> findALLByCreatedByAndFinalisationStatus(Pageable pagable, UserDetails createdBy,
			Integer finalisationStatus);

	/*
	 * public Page<AddCase> findALLByCreatedByAndFinalisationStatusAndType(Pageable
	 * pagable, UserDetails createdBy, Integer finalisationStatus, Type type);
	 */

	/*
	 * public List<AddCase>
	 * findALLByCreatedByAndFinalisationStatusAndType(UserDetails createdBy, Integer
	 * finalisationStatus, Type type, Sort sort);
	 */

	public List<AddCase> findALLByCreatedByAndFinalisationStatus(UserDetails createdBy, Integer finalisationStatus,
			Sort sort);

	/* List<AddCase> findByType(Type type); */

	public List<AddCase> findALLByFinalisationStatus(Integer finalisationStatus, Sort sort);

	public Page<AddCase> findALLByFinalisationStatus(Integer finalisationStatus, Pageable pagable);

	public Page<AddCase> findALLByFinalisationStatusOrFinalisationStatus(Integer finalisationStatus1,
			Integer finalisationStatus2, Pageable pagable);

	public List<AddCase> findALLByFinalisationStatusOrFinalisationStatus(Integer finalisationStatus1,
			Integer finalisationStatus2);
	
	
	public AddCase findALLByInvestigationOrderNoAndInvestigationOrderDateAndCaseTitle(String investigationOrderNo,
			Date investigationOrderDate, String caseTitle);

}
