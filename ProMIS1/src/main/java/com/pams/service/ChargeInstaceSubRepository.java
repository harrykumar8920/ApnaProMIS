package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pams.entity.Charge;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.ChargeInstaceSub;
import com.pams.entity.Instance;

public interface ChargeInstaceSubRepository extends JpaRepository<ChargeInstaceSub, Long> {
	
	@Query("SELECT u FROM ChargeInstaceSub u WHERE u.chargeInstanceMain = 'id'")
	List<ChargeInstaceSub> findByChargeInstanceMain(ChargeInstaceMain id);

	@Query(nativeQuery = true, value = "SELECT * FROM prosecution.charge_instace_sub WHERE prosecution.charge_instance_main_id = :id")
	List<ChargeInstaceSub> findCustomByChargeId(@Param("id") Long id);

	@Query(nativeQuery = true, value = "select * from prosecution.charge_instace_sub where charge_instance_main_id='2'")
	List<ChargeInstaceSub> findCustomByChargeId();
	List<ChargeInstaceSub> findByInstance(Instance instance);
	
}
