package com.pams.dto;

import java.util.Date;
import java.util.List;

import com.pams.entity.ActSecDetailsInfo;

import lombok.Data;
@Data
public class ReportWeeklyMCAIsPartyDto {
	private String caseTitle;
	private String caseNo;
	private String courtType;
	private String causeTitle;
	private String backgroundofcase;
	private Boolean isWhetherreplyfiled;
	private String brief;
	private String pairaviofficer;
	private String ePairaviofficer;
	private Date  filingDate;
	private String fy;
	private List<ActSecDetailsInfo> sec;
	private List<ActSecDetailsInfo> sec1;
	private List<ActSecDetailsInfo> sec2;
}
