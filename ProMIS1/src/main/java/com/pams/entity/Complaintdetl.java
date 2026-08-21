package com.pams.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name="prosecutionComplaintDetails",schema = "prosecution" )
public class Complaintdetl {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_complanitId",columnDefinition = "serial")
	private Long complanitId;
	private String IOName;
	private Boolean isSPCourt=false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "designation")
	private AddDesignation complanitdesignation;
	private String complanitName;
	private String complanitEmail;
	private String complaintMobile;
	private String complaintPetinoner;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date complaintPetinonerDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;
	private int approve_status=0;
	private String rejectRemark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	private Date createdDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy;
	private Date updatedDate;@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "approveBy")
	private UserDetails approveBy;
	private Date approveDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "designation")
	private AddDesignation desigInvesOffi;
	
	

}
