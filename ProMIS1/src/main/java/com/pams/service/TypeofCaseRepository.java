package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.TypeofCase;

import aj.org.objectweb.asm.Type;

public interface TypeofCaseRepository extends JpaRepository<TypeofCase, Long> {

	List<TypeofCase> findByTypeAndIdNot(com.pams.entity.Type type,Long id);

	 List<TypeofCase> findAllByIdNot(Long id);
		
	
	
}
