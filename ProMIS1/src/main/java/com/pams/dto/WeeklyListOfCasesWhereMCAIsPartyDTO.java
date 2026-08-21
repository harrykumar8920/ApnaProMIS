package com.pams.dto;

import java.util.Date;
import java.util.List;

import com.pams.entity.ActCompundRelevantSection;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.HearingDetails;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class WeeklyListOfCasesWhereMCAIsPartyDTO {
	private String caseTitle;
	private String caseNo;
	
	private String courtType;
	private String causeTitle;
	private String backgroundofcase;
	private Integer isWhetherreplyfiled;
	private String brief;
	private String pairaviofficer;
	private String ePairaviofficer;
	private Date  filingDate;
	private String fy;
	private String counselOfficer;
	private String counselOfficerPhone;
	@Transient
	private List<ActSecDetailsInfo> sec;
	
	
	@Transient
	private List<ActSecDetailsInfo> sec1;

	@Transient
	private List<ActSecDetailsInfo> sec2;
	@Transient
	private List<ActCompundRelevantSection> secq;
	
	
	@Transient
	private List<ActCompundRelevantSection> sec1q;

	@Transient
	private List<ActCompundRelevantSection> sec2q;
	
	private HearingDetails hd1;
	
	
	private List<HearingDetails> hd;
	
	
	
	
	
	
	
	
	
	
	
}
