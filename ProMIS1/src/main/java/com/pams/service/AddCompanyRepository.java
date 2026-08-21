package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AddCompany;
import com.pams.entity.ProCourtCaseDetails;

public interface AddCompanyRepository  extends JpaRepository<AddCompany, Long>{

	AddCompany findAllByCin(String cin);
	AddCompany findAllByCinAndCompanyName(String cin,String companyName);

	

	

}
