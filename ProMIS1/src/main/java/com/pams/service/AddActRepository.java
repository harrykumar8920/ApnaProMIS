package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AddAct;
@Repository
public interface AddActRepository extends JpaRepository<AddAct, Long> {
	
	
	public boolean findAllByAct(String act);
	List<AddAct> findAllByOrderByIdAsc();
	
	boolean existsByActAndActType(String act, String actType);
	
	
	
	 boolean existsByAct(String act);


	public List<AddAct> findAllById(long l);
	public List<AddAct> findAllByIdOrId(long l,long ll);
	 
	
	

}
