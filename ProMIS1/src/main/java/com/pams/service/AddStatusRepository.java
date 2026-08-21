package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.Status;



@Repository
public interface AddStatusRepository extends JpaRepository<Status, Long>  {

	Status findAllByStatusName(String statusName);

	//List<Status> findAllByType(String string);
	List<Status> findAllByTypeAndIsActive(String string,Boolean isActive);
	
	List<Status> findAllByTypeAndIsActiveOrderByStatusNameAsc(String string,Boolean isActive);
	
	


}
