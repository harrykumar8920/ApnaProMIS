package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.CourtCaseName;

public interface CourtCaseNameRepository extends JpaRepository<CourtCaseName, Long> {
	
	List<CourtCaseName> findByTypeCase(String typeCase);
}
