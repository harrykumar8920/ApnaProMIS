package com.pams.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AccusedStatusNew;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;

import java.util.List;

public interface AccusedStatusNewRepository extends JpaRepository<AccusedStatusNew, Long> {
	
	List<AccusedStatusNew> findByAccusedIdAndAssignedTask(AddAccused accusedid,AssignedTaskPuhAfterCOurt assignedTask, Sort sort);
	
	List<AccusedStatusNew> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedtask);

}
