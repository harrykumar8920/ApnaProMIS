package com.pams.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DirectorDashboardNext7DaysDto {
private String causeTitle;
private String courtName;
private String courtCaseNo;
private String type;
private String bench;
private String state;
private String officerName;
private String mobile;
private Date lastHearingDate;
private Date nextHearingDate;

}
