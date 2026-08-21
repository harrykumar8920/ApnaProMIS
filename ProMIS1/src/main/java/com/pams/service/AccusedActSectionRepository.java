package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AccusedActAndSection;
import com.pams.entity.AddAccused;
import java.util.List;

public interface AccusedActSectionRepository extends JpaRepository<AccusedActAndSection, Long> {
	
	List<AccusedActAndSection> findByAddAccused(AddAccused addaccused);

}
