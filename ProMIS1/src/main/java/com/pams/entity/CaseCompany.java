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
import lombok.Data;



@Entity
@Data
@Table(name="prosecutionCaseCompanyDetails",schema = "prosecution" )
public class CaseCompany {

	
	
	
	public CaseCompany(AddCompany company, ProCourtCaseDetails procourtdtl) {
		this.company = company;
		this.procourtdtl = procourtdtl;	// TODO Auto-generated constructor stub
	}
	public CaseCompany() {
		// TODO Auto-generated constructor stub
	}
	public CaseCompany(AddCompany company, ProCourtCaseDetails procourtdtl, UserDetails userdet, Date date) {
		this.company = company;
		this.procourtdtl = procourtdtl;
		this.createdBy = userdet;
		this.createdDate = date;
	}
	
	public CaseCompany(AddCompany company, ProCourtCaseDetails procourtdtl, Long compId, UserDetails userdet, 
			Date date) {
		this.company = company;
		this.procourtdtl = procourtdtl;
		this.invCompId = compId;
		this.createdBy = userdet;
		this.createdDate = date;
	}
	
	
	public CaseCompany(AddCompany company, ProCourtCaseDetails procourtdtl, Long compId, UserDetails userdet, 
			Date date,AssignedTaskPuhAfterCOurt assignedTask,int approveStatus) {
		this.company = company;
		this.procourtdtl = procourtdtl;
		this.invCompId = compId;
		this.createdBy = userdet;
		this.updatedBy = userdet;
		this.approveBy = userdet;
		this.createdDate = date;
		this.assignedTask=assignedTask;
		this.approveStatus=approveStatus;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "serial")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddCompany company;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy;
	private Date approvedDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "approveBy")
	private UserDetails approveBy;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	private Long   invCompId;
	private Long updateBy;
	private int approveStatus=0;
	private String rejectRemark;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "assignedTaskID")
	private AssignedTaskPuhAfterCOurt assignedTask;
	
	/*
	 * @Lob private String compRemark;
	 */
}
