package com.pams.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.pams.entity.AddCase;
import com.pams.entity.UserDetails;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Data
public class AssignTaskDTO {
	
	//By Keyraj 25.05.2023
	  private Long id;
	  private String courtCaseNo;
	  private String courtType;
	  private Integer courtCaseApproveStatus;
	  private Integer totalcount;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @PrimaryKeyJoinColumn(name = "createdBy") 
	  private UserDetails createdBy;
	 
	private String caseTitle;
	private String causeTitle;
	
	private AddCase addCase;
	
	
	  @DateTimeFormat(pattern = "dd/MM/yyyy")
	  
	
	  private Date proSanctionDate;
	  
	  
	  private String proSectionOrderNumber;
	  
	  private String fileNumber; 
	  private String proSanctionFileName;
	  
	  private String task;
	  
	  private String firstName; 
	  private String middelName; 
	  private String lastName; 
	  private String unitName;
	  
	 

	
	
	
	
	

}
