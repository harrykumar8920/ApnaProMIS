package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.AddCourt;

import jakarta.transaction.Transactional;

public interface CourtTypeRepository extends JpaRepository<AddCourt, Long> {

AddCourt findAllById(Long id);
	
	List<AddCourt> findAllByIdGreaterThanAndActiveTrue(Long id);
	
	List<AddCourt> findByCourtNameStartsWithOrCourtNameStartsWith(String courtName,String courtName1);
	
	
	List<AddCourt> findByCourtNameNotContainingAndActiveTrue(String courtName);
	List<AddCourt> findByCourtNameStartsWithOrCourtNameStartsWith(String courtName,String courtName1,Sort sort);
	List<AddCourt> findByCourtNameStartsWithOrCourtNameStartsWithAndCourtType(String courtName,String courtName1,Integer courttype,Sort sort);
	List<AddCourt> findByCourtNameStartsWithOrCourtNameStartsWithAndCourtType(String courtName,String courtName1,Integer courttype);
	List<AddCourt> findByCourtNameNotContaining(String courtName,Sort sort);
	List<AddCourt> findByCourtNameNotContainingAndCourtType(String courtName,Integer courttype,Sort sort);
	List<AddCourt> findAll();
	//List<AddCourt> findByCourtType(Integer courttype);
	@Query(
		    value = "SELECT * FROM authentication.court_type c WHERE c.court_type = :courtType AND c.active = true",
		    nativeQuery = true
		)
		List<AddCourt> findByCourtType(@Param("courtType") Integer courtType);
	List<AddCourt> findByCourtType(Integer courttype,Sort sort);
	    @Modifying
	    @Transactional
	    @Query("update AddCourt c set c.active = false where c.id = :id")
	    int deactivateCourt(@Param("id") Long id);
	    @Modifying
	    @Transactional
	    @Query("update AddCourt c set c.active = true where c.id = :id")
	    int activateCourt(@Param("id") Long id);

	
	
	
	
 List<AddCourt> findAll(Sort sort);

List<AddCourt> findByCourtNameNotContainingAndCourtType(String string, int i);


	
}
