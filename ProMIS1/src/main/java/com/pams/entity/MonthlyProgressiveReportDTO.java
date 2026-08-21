package com.pams.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;


@Data
public class MonthlyProgressiveReportDTO {
	
	private List<HearingDetails> hd;
	private SfioAs sfioAs;
	private String courtCaseNo;
	private String caseTitle;
	
	
	
	private Date fillingDate;
	
	private String proCaseNo;
	
	
	private Date proDate;
	
	private String proSanctionOrder;
	
	private String causeTitle;
	
	private String brief;
	
	
	
	
	
	private Date nextDateOfHearing;
	
	
	private UserDetails createdBy; 
	
	private AddState state; 
	
	private District city; 
	
	private String courtType;
	
	private AddSubSec subsection;

	private String fy;
	private List<AddAccused> accused;
	

}
