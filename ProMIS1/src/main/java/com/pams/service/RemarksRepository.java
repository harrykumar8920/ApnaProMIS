package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.RemarksEntity;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;

import java.util.List;

public interface RemarksRepository extends JpaRepository<RemarksEntity, Integer> {

	
	List<RemarksEntity> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedtask);
}
