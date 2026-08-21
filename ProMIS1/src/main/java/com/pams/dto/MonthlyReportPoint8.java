package com.pams.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MonthlyReportPoint8 {
	private String causTitle;
	private List<String> accusedName; 
	private List<String> sections;
	private Date lastDate;
	private Date nextDate;
	private String  status;


}
