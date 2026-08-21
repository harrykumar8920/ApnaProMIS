package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.PerformaParty;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;

import java.util.List;

public interface PerformaPartyRepo extends JpaRepository<PerformaParty, Long> {
	
	List<PerformaParty> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedtask);

}
