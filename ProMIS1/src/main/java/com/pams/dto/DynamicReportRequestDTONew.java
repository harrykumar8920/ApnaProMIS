package com.pams.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.pams.entity.AddAct;
import com.pams.entity.UserDetails;

import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Data
public class DynamicReportRequestDTONew {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    private String dateType;

    private List<String> selectedFields;
    
    private Long status;
    private Long actId;
    private String compoundability;
    private Long stateID;
    
    
	private UserDetails createdBy;
	
	 private Long caseType;
   
}