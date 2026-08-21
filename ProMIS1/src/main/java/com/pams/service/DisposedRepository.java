package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Disposed;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.UserDetails;

public interface DisposedRepository extends JpaRepository<Disposed, Long>{
	
	List<Disposed> findByProcourtdtlAndCreatedBy(ProCourtCaseDetails procourtdtl,UserDetails createdBy);

}
