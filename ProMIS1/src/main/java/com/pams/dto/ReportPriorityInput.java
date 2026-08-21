package com.pams.dto;

import java.util.Date;

import lombok.Data;
@Data
public class ReportPriorityInput {
	public String date;
	public String toDate;
	
	private int casePriority;
	
	private Long stateId;
	private Long userId;

	
	
}
