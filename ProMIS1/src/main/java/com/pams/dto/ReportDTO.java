package com.pams.dto;

import java.util.Date;
import java.util.List;

import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.SecList;
import com.pams.entity.ProCourtCaseDetails;

import lombok.Data;

@Data
public class ReportDTO {
	
	private String name;
	private String proCaseNo;
	private String causeTitle;
	private String mobile;
	private Date lastHearingDetails;
	private Date nextHearingDate;
	private List<ActSecDetailsInfo> sec;
	private List<ActSecDetailsInfo> sec2;
	private List<ActSecDetailsInfo> sec3;
	private List<ActSecDetailsInfo> sec4;
	
	
	private ProCourtCaseDetails procourtdtl;
	private String pairaviName ;
	private String caseStatus;
	private String lastcaseStatus;
	private String prosecutorname;
	private String counselname;
	private String counselnameLast;
	private String counselname1;
	private String counselmob;
	private String counselmob1;
	private String fromdate1;
	private String strategy;
	
	
	
	
}
