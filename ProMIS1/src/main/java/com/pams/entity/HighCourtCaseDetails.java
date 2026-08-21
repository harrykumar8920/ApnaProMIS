package com.pams.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name="high_Court_CaseDetails",schema = "prosecution")
public class HighCourtCaseDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "court_Case_Id",columnDefinition = "serial")
	private Long id;
	
	@Transient
	private String proSectionOrderNumber;
	@Transient
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date proSanctionDate;
	
	@Transient
	private String caseTitle;
	@Transient
	private String petionerName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn(name = "assignedTaskID")
	private AssignedTaskPuh assignedTask;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn(name = "addCaseID") 
	private AddCase addCase;
	@Transient
	private Long assignedTaskIda;
	@Transient
	private Long addCaseIda;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private SfioAs sfioAs;
	
	private String highCourtType;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddState state; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private District city; 
	private String courtCaseNo;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fillingDate;
	private String causeTitle;
	private String cnrNumber;
	
	private String assignOutOf;
	@Size(max = 5000)
	private String brief;
	@Transient
	private MultipartFile progistFile;
	private String gistFile;
	
	
	
	
	

	
}
