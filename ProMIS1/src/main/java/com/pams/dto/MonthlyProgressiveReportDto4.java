package com.pams.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class MonthlyProgressiveReportDto4 {
	
	private List<String> accusedName; 
	private Date dateOfInstruction;
	private List<String> sections;
	private String ncltBenchName;
	private String status;
}
