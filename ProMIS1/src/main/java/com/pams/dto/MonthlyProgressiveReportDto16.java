package com.pams.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MonthlyProgressiveReportDto16 {
private String caseTitle;
private List<String> lst;
private String caseNumber;
private String causeTitle;
private Date fillingDate;

private String financialYear;
private String courtName;
private Date nextdateHearing;
private List<String> statusName;
}
