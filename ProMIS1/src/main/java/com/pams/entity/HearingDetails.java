package com.pams.entity;

import java.util.Date;
import java.util.List;

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
import jakarta.persistence.Transient;
import lombok.Data;


@Entity
@Data
@Table(name="proHearingDetails",schema = "prosecution" )
public class HearingDetails {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	
	private boolean latestHDStatus = true;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private PairaviOfficer counselName;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private PairaviOfficer officer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date nextHearingDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date lastHearingDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Status status;
	@Column(length = 1100)
	private String briefHD;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateofCaseStatusUpdate;
	
	
	/*
	 * private String counselName; private String counselEmail;
	 * 
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "id") private AddDesignation counselDesignation;
	 * private String counselMobileNo;
	 */
	
	
	/*
	 * private String counselName1; private String counselEmail1;
	 * 
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "id") private AddDesignation
	 * counselDesignation1; private String counselMobileNo1;
	 */
	
	 private boolean currentStatus=true; 
	
	
	//For common DB field
	
	
	
	private String orderCopyFN;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateOfOrder;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateOfTransferWithdrawClosing;
	
	private String reasonofStay;
	private String remarks;
	private String durationofStayTransfer;
	
	private int approveStatus=2;
	private String rejectRemark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user")
	private UserDetails user;
	
	private String additionalDocFileName;

	@Column(name = "dstatus", columnDefinition = "integer default 0")
	private Integer dstatus = 0;
	
	@Transient
	private List<CaseCompany> company;
	
	@Transient
	List<AddAccused> accused1;
	
	@Transient
	private List<ActSecDetailsInfo> actSecDetailsInfo;
	
	@Transient
	private	List<AccusedStatus> accusedwithStatus;
	
	@Transient
	private List<AccusedStatus> lst;
	
	@Transient
	private List<AddAccused> companyAccused;
	
	@Transient
	List<AddActSec> sections;
	
	private boolean effectiveBoolean=false;
	
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @PrimaryKeyJoinColumn(name = "id") private AddState state;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @PrimaryKeyJoinColumn(name = "id") private District city;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @PrimaryKeyJoinColumn(name = "id") private AddCourt courtType;
	 
	


}
