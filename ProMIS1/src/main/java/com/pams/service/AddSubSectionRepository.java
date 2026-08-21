package com.pams.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AddSubSec;
@Repository
public interface AddSubSectionRepository extends JpaRepository<AddSubSec, Long>{
	
	
	
	public List<AddSubSec> findAllBySection(Optional<AddActSec> optional);
	public List<AddSubSec> findAllBySection(Optional<AddActSec> optional,Sort sort);
	public List<AddSubSec> findAllBySection(AddActSec optional);
	
	
	
	
	

}
