package com.pams.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.PairaviOfficer;
import java.lang.Integer;
import java.util.List;

public interface PairaviOfficerRepository extends JpaRepository<PairaviOfficer, Long> {
	
	List<PairaviOfficer> findByType(Integer type,Sort sort);
	
	
	boolean existsByEmail(String email);
	 boolean existsByEmailAndIdNot(String email, Long id);
	

}
