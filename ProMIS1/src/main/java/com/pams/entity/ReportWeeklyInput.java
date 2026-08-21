
package com.pams.entity;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class ReportWeeklyInput {
	public String date;
	public String toDate;
	@Transient
	public String fromdate;
	@Transient
	public String toDate1;
	private Status accusedstatus;
	
	
	

}
