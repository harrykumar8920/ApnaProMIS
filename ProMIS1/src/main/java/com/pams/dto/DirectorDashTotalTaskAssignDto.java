package com.pams.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DirectorDashTotalTaskAssignDto {

	private String proSectionOrderNumber;
	private Date proSanctionDate;
	private String proSanctionFileName;
	private String task;
	private String unitName;
	private String userName;

}
