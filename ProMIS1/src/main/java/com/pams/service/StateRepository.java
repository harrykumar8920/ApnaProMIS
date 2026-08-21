package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AddState;
import com.pams.entity.Status;

public interface StateRepository extends JpaRepository<AddState, Long> {

	List<AddState> findAllById(long stateid);
	
	List<AddState> findAllByOrderByStateAsc();
	
	

}
