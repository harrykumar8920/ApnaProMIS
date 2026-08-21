package com.pams.dto;

import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;

import lombok.Data;

@Data
public class ViewAccusedStatusDTO {
	
	private AssignedTaskPuhAfterCOurt assignedTask;
	private AddAccused accuseName;
	private Boolean liststatus=false;

}
