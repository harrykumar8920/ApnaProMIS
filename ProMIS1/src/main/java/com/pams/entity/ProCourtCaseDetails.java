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
@Table(name="prosecution_Court_CaseDetails",schema = "prosecution")
public class ProCourtCaseDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "court_Case_Id",columnDefinition = "serial")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private SfioAs sfioAs;
	private String courtCaseNo;
	@Transient
	private String courtCaseNo1;
	@Transient
	private String courtCaseNo2;
	@Transient
	private String courtCaseNo3;
	
	private Integer caseType;
	private String fileNumber;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "benchName")
	private TypeofBench bench_Name; 
	
	private String cnrNumber;
	
	private int caseStatusCheck=1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private InvCaseDetails invCaseDetail;
 
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date corrigendumDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fillingDate;
	
	
	
	@Column(name = "prosecution_case_no")
	private String proCaseNo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	
	/*
	 * 
	 * 
	 * @Column(name = "prosecution_Sanction_Order") private String proSanctionOrder;
	 * private String proSanctionOrderFile;
	 */
	//@Column(name = "prosecution_Case_Date")
	//private Date proDate;
	
	private String causeTitle;
	private String CourtCaseDtlFile;
	
	

	@Size(max = 5000)
	private String brief;
	
	@Size(max = 5000)
	private String backgroundofcase;
	
	
	private String gistFile;
	private String yearOfViolation;
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "approveBy")
	private UserDetails approveBy; 
	@Column(name = "approveDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddState state; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private District city; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddCourt courtType;
	@Transient
	private String courtTypeC;
	private String financialYear;
	//private long updatedBy;
	@Transient
	private MultipartFile proSanctionFile;
	@Transient
	private MultipartFile backgroundFile;
	@Transient
	private MultipartFile briefFile;
	private String briefFileName;
	private String backgroundFileName;
	@Transient
	private MultipartFile progistFile;
	@Transient 
	private String mcaorderno;
	private Boolean isMCAParty=false;
	private Integer isWhetherreplyfiled=0;
	@Transient
	private int typeofOrder;
	@Transient
	
	private String caseId;
	
	@Transient
	private String caseTitle;
	
	@Transient
	
	private Long actSectionID;
	@Transient
	private Long invCaseId;
	
	@Transient
	private AddAct act;
	
	@Transient
	private AddSubSec subsection;
	@Transient
	private String clause;
	
	@Transient
	private List<ActSecDetailsInfo> actSecDetailsInfo;
	
	
	
	@Transient
	private AddActSec section;
	@Transient
	private String description;
	@Transient
	private List<ActCompundRelevantSection> sec;
	
	@Transient
	private List<ActCompundRelevantSection> sec1;
	
	@Transient
	private List<ActCompundRelevantSection> sec2;
	
	@Transient
	private List<ActCompundRelevantSection> sec3;
	
	@Transient
	private List<ActCompundRelevantSection> sec4;
	@Transient
	private List<ActCompundRelevantSection> sec5;
	
	
	
	@Transient
	private String fy;
	
	@Transient
	private int acterror;
	
	
	@Transient
	private String compname;
	
	@Transient
	private String threeMonth;
	@Transient
	private String corrigendumDateView;
	@Transient
	private String counselOfficer;
	@Transient
	private String counselOfficerPhone;
	@Transient
	private String hearingDetailsstatus;
	@Transient
	private List<AssignedTaskPuh> assignedTaskPuh;
	@Transient
	private List<CreateTasks> TasksLst;
	
	
	private int approveStatus=0;
	private int casePosition=0;
	
	private String sendBackRemarks;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn(name = "assignedTaskID")
	private AssignedTaskPuh assignedTask;
	@Transient
	private Long assignedTaskID;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	  
	@PrimaryKeyJoinColumn(name = "addCaseID") 
	private AddCase addCase;
	
	@Transient
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date proSanctionDate;
	@Transient
	private String proSectionOrderNumber;
	
	@Transient
	private Boolean viewFile=false;
	
	@Transient
	private String petionerName;
	


	@ManyToOne(fetch = FetchType.LAZY) 
	@PrimaryKeyJoinColumn(name = "type") 
	private Type type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id") 
	private TypeofCase typeOfCase;
	
	
	
	

}
