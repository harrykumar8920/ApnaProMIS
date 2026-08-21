package com.pams.dto;

import java.util.List;

import com.pams.entity.AddAccused;
import com.pams.entity.AddCase;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.Complaintdetl;
import com.pams.entity.CouncilDetails;
import com.pams.entity.HearingDetails;
import com.pams.entity.Inspector;
import com.pams.entity.PairaviDetails;
import com.pams.entity.ProCourtCaseDetails;

import lombok.Data;

@Data
public class ReportByCINDTO {

	
	private ProCourtCaseDetails proCourtCaseDetails;
	private Complaintdetl complaintdetl;
	private List<CouncilDetails> councilDetails;
	private List<PairaviDetails> pairaviDetails;
	private List<Inspector> inspector;
	private CaseProcessingDates caseProcessingDates;
	private List<HearingDetails> hearingDetails;
	private List<AddAccused> addAccused;
	private ChargeInstaceMain chargeInstaceMain;
	
	
}
