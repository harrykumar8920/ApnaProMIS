package com.pams.entity;

import java.util.Date;

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
import lombok.Data;



	@Entity
	@Data
	@Table(name="Pro_AccusedResponse_Details",schema = "prosecution")
			
	public class AccusedResponse {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "accusedResponse_Id",columnDefinition = "serial")
		private Long id;
		
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date replyFiledDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date dateOfApplication;
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date orderDate;
		@Temporal(TemporalType.TIMESTAMP)
		private Date createdDate;
		@Column(name = "updatedDate")
		@Temporal(TemporalType.TIMESTAMP)
		private Date updatedDate;
		@Temporal(TemporalType.TIMESTAMP)
		private Date approvedDate;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "createdBy")
		private UserDetails createdBy;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "updatedBy")
		
		private UserDetails updatedBy;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "approvedBy")
		private UserDetails approvedBy; 
		
		private String applicationNumber;
		
		
		
		@ManyToOne(fetch = FetchType.LAZY)
		  
		@PrimaryKeyJoinColumn(name = "addCaseID") 
		private AddCase addCase;
		
		
		private Integer approvalStatus=0;
		private String remarksByPUH="";
		
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "typeofResponse")
		private TypeofResponse typeofResponse;
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "assignedTaskID")
		private AssignedTaskPuhAfterCOurt assignedTask;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "id")
		private AddCourt courtType;
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "id")
		private AddState state; 
		@ManyToOne(fetch = FetchType.LAZY)
		@PrimaryKeyJoinColumn(name = "id")
		private District city; 
		
		private Boolean replyfiled;
		private String orderType;
		
		@ManyToOne(fetch = FetchType.EAGER)
		@PrimaryKeyJoinColumn(name = "accusedDetails")
		private AddAccused accusedDetails;
		@Transient
		private Integer tabId;
		@Transient
		private	String typeOfCase;
		@Transient
		private	Long accusedDetailsID;
		
		@Transient
		private MultipartFile applicationOrderFile;
		private String applicationOrderFileName;
		@Transient
		private MultipartFile orderFile;
		private String orderFileName;
		@Transient
		private MultipartFile replyFiledOrder;
	
		private String replyOrderFiledName;
	}
