package com.pams.dto;

import java.util.List;

import com.pams.entity.AccusedCompCaseDtl;
import com.pams.entity.AddAccused;
import com.pams.entity.CaseCompany;
import com.pams.entity.CaseStatus;
import com.pams.entity.PairaviDetails;
import com.pams.entity.PetRespDetail;
import com.pams.entity.ProCourtCaseDetails;

import lombok.Data;

@Data
public class ProCourtDtlDto {

	
	private ProCourtCaseDetails proCourtdtl;
	private List<PetRespDetail>  petRespDetail;
	private List<PairaviDetails>  pairaviDetails;
	private CaseStatus  caseStatus;
	private AddAccused  addAccused;
	private CaseCompany  caseCompany;
}
