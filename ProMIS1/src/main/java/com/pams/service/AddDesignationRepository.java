package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.AddDesignation;
import java.lang.String;
import java.util.List;


//@Repository
public interface AddDesignationRepository extends JpaRepository<AddDesignation, Long> {

	AddDesignation findAllByDesignation(String accDesgi);
	
	List<AddDesignation> findByDeginationtype(String deginationtype);
	List<AddDesignation> findByDesignationAndDeginationtype(String degination,String deginationtype);

}
