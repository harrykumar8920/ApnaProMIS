package com.pams.dto;

import java.util.List;

import com.pams.entity.ActSecDetailsInfo;

import lombok.Data;

@Data
public class WeeklyCauseListReportDTO {
	private String caseTitle;
	private String courtName;
	private String state;
	private String bench;
	private String causeTitle;
	private List<ActSecDetailsInfo> sec;
	private List<ActSecDetailsInfo> sec2;
	private List<ActSecDetailsInfo> sec3;
	private List<ActSecDetailsInfo> sec4;
	private String brief;
	private String caseStatus;
	private String pairaviName;
	private String pairavimob;
	private String counselname;
	private String counselmob;
	private String lastHearingDetails;
	private String nextHearingDate;
	private String counselnameLast;
	private String lastcaseStatus;
	
	
	
	
	
	
	
}
