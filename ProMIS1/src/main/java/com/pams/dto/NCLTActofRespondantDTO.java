package com.pams.dto;

import java.util.List;

import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NCLTActofRespondantDTO {
	
	  private Long id; // Primary key for NCLTActofRespondant
	@NotNull(message = "Accused is required")
    private List<AddAccused> accuseName; // List of accused IDsaccuseName
    
    @NotNull(message = "Act is required")
    private AddAct act; // Single selected act ID
    
    @NotNull(message = "Section is required")
    private List<AddActSec> section; // List of selected section IDs
    
    @NotNull(message = "Remarks are required")
    private String description; // Remarks text
    private AssignedTaskPuhAfterCOurt assignedTask;
    private ProCourtCaseDetails procourtdtl;
}
