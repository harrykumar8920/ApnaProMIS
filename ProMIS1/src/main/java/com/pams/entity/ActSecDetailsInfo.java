package com.pams.entity;

import java.util.Date;

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
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ActSecDetails", schema = "prosecution") 
public class ActSecDetailsInfo{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "act")	
	private AddAct act;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "Section")	
	private AddActSec section;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "SubSection")	
	private AddSubSec subSection ;
	private Integer caseType;
	private String clause;
	

	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "id") private proCourtCaseDetails procourtdtl;
	 */
	
	private Long procourtdtlID;
	
	private String Description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy; 
	
	
	//private proCourtCaseDetails procrtdtl;

	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	
	@Column(name = "isActive", length = 1)
	private Integer isActive=1;
	  

	@Transient
	private Boolean isEditActSecDetailsInfo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "assignedTaskID")
	private AssignedTaskPuh assignedTask;

}
