package com.pams.dto;

import java.util.Date;

import lombok.Data;

@Data
public class HearingDetailsDto {
private String caseTitle;
private String courtCaseNo;
private String causeTitle;

private Date lastHearingDate;
private Date nextHearingDate;

private String counselOfficerName;
private String statusName;
}
