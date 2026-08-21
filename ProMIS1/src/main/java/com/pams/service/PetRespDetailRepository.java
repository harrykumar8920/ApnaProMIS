package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.DetailsType;
import com.pams.entity.PetRespDetail;
import com.pams.entity.ProCourtCaseDetails;

public interface PetRespDetailRepository  extends JpaRepository<PetRespDetail, Long>{

	List<PetRespDetail> findAllByProcourtdtl(ProCourtCaseDetails procourtdt);

	PetRespDetail findAllById(Long id);

	PetRespDetail findAllByProcourtdtlAndDetailsType(ProCourtCaseDetails courtdtl, DetailsType petType);

	

	

}
