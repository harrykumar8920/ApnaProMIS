package com.pams.service;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;

public interface AddActSecRepository extends JpaRepository<AddActSec, Long> {

	public AddActSec findAllBySection(String Section);

	public List<AddActSec> findAllByAct(AddAct act);
	
	@Query(nativeQuery = true, value = "select * from authentication.add_act_sec where act_id=6 and section in ('277','278','279','280','281','282','283','284','285','286','287','288','289','290','291','292','293','294','295','296','297','298','299','300','301','302','303','304','305','306','307','308','309','310','253','254','255','256','257','248','249','219','221','253','346','218','239','237','238','240','241','242','243','244','245','246')")
	public List<AddActSec> findAllNCLTCaseAndAct2013();
	
	
	
}
