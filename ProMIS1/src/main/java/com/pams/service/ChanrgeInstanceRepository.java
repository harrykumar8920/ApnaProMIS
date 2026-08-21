package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Charge;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.CreateTasks;
import com.pams.entity.ProCourtCaseDetails;

public interface ChanrgeInstanceRepository extends JpaRepository<ChargeInstaceMain, Long> {
	
	
	List<ChargeInstaceMain> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedtask);
	List<ChargeInstaceMain> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedtask,Sort sort);	
	List<ChargeInstaceMain> findALLByAssignedTaskAndCharge(AssignedTaskPuh assignedtask,Charge charge);
	
	//List<ChargeInstaceMain> findByAssignedTaskAndAccuseName(AssignedTaskPuh assignedtask,AddAccused accuseName);
	List<ChargeInstaceMain> findByProcourtdtl(ProCourtCaseDetails procourtdtl);
	List<ChargeInstaceMain> findByAssignedTaskAndSamechargeType(AssignedTaskPuhAfterCOurt assignedtask,int samechargeType,Sort sort);
	/*
	 * @Query(nativeQuery = true, value =
	 * "SELECT DISTINCT ON (charge_id) id, charge_id " +
	 * "FROM prosecution.charge_instace_main " +
	 * "WHERE accuse_name_accused_id = :accuseName " +
	 * "  AND assigned_task_id = :assignedTask " + "ORDER BY charge_id DESC, id;")
	 */
	
	
	
	
	
	

	
	@Query(nativeQuery = true, value = "SELECT DISTINCT ON (m.charge_id) \r\n" + 
			"    m.id, \r\n" + 
			"    m.charge_id, \r\n" + 
			"    c.charge_name\r\n" + 
			"FROM \r\n" + 
			"    prosecution.charge_instace_main AS m\r\n" + 
			"INNER JOIN \r\n" + 
			"    authentication.charge_details AS c \r\n" + 
			"    ON c.id = m.charge_id\r\n" + 
			"WHERE \r\n" + 
			"    m.accuse_name_accused_id = :accuseName\r\n" + 
			"    AND m.assigned_task_id = :assignedTask\r\n" + 
			"ORDER BY \r\n" + 
			"    m.charge_id DESC, m.id;")
	
	public List<Object[]> findCustomChargeByAssignedTaskAndAccuseName(Long accuseName, Long assignedTask);

	
	
	
	
}
