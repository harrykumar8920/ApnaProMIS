package com.pams.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PendingTaskForApprovalDTO {

	private Long asssignTaskID;

	private String caseTitle;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "proSanctionDate")
	private Date proSanctionDate;

	private String proSectionOrderNumber;

	private String proSanctionFileName;

	private String task;

	private String sfioAs;
	
	int  a1Hearing; 
    int  a2Pairavi; 
    int  a3CompDtl; 
    int  a4CaseCompany; 
    int  a5Accused; 
    int  a6AdditionalFile; 
    int  a7ReportTemplate; 
    int  a8CourtCase; 
    int  a9CaseProsessingDate;
    int  a10freezer;
    int  a11respondant;
    
	
	
	

}
