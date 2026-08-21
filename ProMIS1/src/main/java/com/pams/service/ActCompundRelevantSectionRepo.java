package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pams.entity.ActCompundRelevantSection;
import com.pams.entity.AddAct;
import com.pams.entity.ChargeInstaceMain;

@Repository
public interface ActCompundRelevantSectionRepo extends JpaRepository<ActCompundRelevantSection, Long> {


	@Query(nativeQuery = true, value = "SELECT \r\n" + 
			" a.act AS actname, \r\n" + 
			"    a.id AS actid,\r\n" + 
			"c.id AS chargeactcompundrelevantsectionid\r\n" + 
			"FROM \r\n" + 
			"    prosecution.charge_instace_main AS m\r\n" + 
			"INNER JOIN \r\n" + 
			"    prosecution.charge_act_compund_relevant_section AS c \r\n" + 
			"    ON m.id = c.charge_instance_main_id\r\n" + 
			"INNER JOIN \r\n" + 
			"    authentication.act AS a \r\n" + 
			"    ON a.id = c.act_id\r\n" + 
			"INNER JOIN \r\n" + 
			"    authentication.punishment AS p \r\n" + 
			"    ON p.id = c.punishment_id\r\n" + 
			"WHERE  \r\n" + 
			"    m.accuse_name_accused_id = :accusedId\r\n" + 
			"    AND m.assigned_task_id = :assinTaskId\r\n" + 
			"    AND m.charge_id = :chargeId\r\n" + 
			"")
	public List<Object[]> findByChargeInstanceMainCostum(Long accusedId, Long assinTaskId, Long chargeId);

	List<ActCompundRelevantSection> findByChargeInstanceMain(ChargeInstaceMain chargeInstanceMain);

	ActCompundRelevantSection findByActAndChargeInstanceMain(AddAct aaa, ChargeInstaceMain main);

}
