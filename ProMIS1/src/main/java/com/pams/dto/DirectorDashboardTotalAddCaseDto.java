package com.pams.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DirectorDashboardTotalAddCaseDto {
private String caseTitle;
private Date proSanctionDate;
private String proSectionOrderNumber;
private String fileNumber;
private String proSanctionFileName;
private Date createdDate;
private String createFirstName;
private Date approvedDate;
private String approveFirstName;
}
