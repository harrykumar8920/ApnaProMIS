package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AccusedMaster;

public interface AccusedMasterRepository extends JpaRepository<AccusedMaster, Long>{

	AccusedMaster findAllByPanNumber(String panNumber);

}
