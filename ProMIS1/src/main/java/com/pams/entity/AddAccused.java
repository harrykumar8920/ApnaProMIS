package com.pams.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;




@Entity

@Table(name = "prosecutionAccusedDetails", schema = "prosecution")
public class AddAccused {
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CaseCompany getCompany() {
		return company;
	}

	public void setCompany(CaseCompany company) {
		this.company = company;
	}

	public String getAccusedName() {
		return accusedName;
	}

	public void setAccusedName(String accusedName) {
		this.accusedName = accusedName;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAccusedType() {
		return accusedType;
	}

	public void setAccusedType(String accusedType) {
		this.accusedType = accusedType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AddDesignation getDesignation() {
		return designation;
	}

	public void setDesignation(AddDesignation designation) {
		this.designation = designation;
	}

	public ProCourtCaseDetails getProcourtdtl() {
		return procourtdtl;
	}

	public void setProcourtdtl(ProCourtCaseDetails procourtdtl) {
		this.procourtdtl = procourtdtl;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public UserDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}

	public AssignedTaskPuhAfterCOurt getAssignedTask() {
		return assignedTask;
	}

	public void setAssignedTask(AssignedTaskPuhAfterCOurt assignedTask) {
		this.assignedTask = assignedTask;
	}

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getRejectRemark() {
		return rejectRemark;
	}

	public void setRejectRemark(String rejectRemark) {
		this.rejectRemark = rejectRemark;
	}

	public Set<AccusedActAndSection> getActSection() {
		return actSection;
	}

	public void setActSection(Set<AccusedActAndSection> actSection) {
		this.actSection = actSection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HearingDetails getHearingDtl() {
		return hearingDtl;
	}

	public void setHearingDtl(HearingDetails hearingDtl) {
		this.hearingDtl = hearingDtl;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accused_id", columnDefinition = "serial")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)

	@PrimaryKeyJoinColumn(name = "id")
	private CaseCompany company;
	private String accusedName;
	private String panNumber;
	private String accusedType;
	private String address;
	private String cinNumber;
	private String individualRelateTo;
	private int ssId;
	public String getRespondentNumber() {
		return respondentNumber;
	}

	public void setRespondentNumber(String respondentNumber) {
		this.respondentNumber = respondentNumber;
	}

	public String getPerformaPartyRespondent() {
		return performaPartyRespondent;
	}

	public void setPerformaPartyRespondent(String performaPartyRespondent) {
		this.performaPartyRespondent = performaPartyRespondent;
	}

	private String respondentNumber;
	private String performaPartyRespondent;
	public int getSsId() {
		return ssId;
	}

	public void setSsId(int ssId) {
		this.ssId = ssId;
	}

	public String getCinNumber() {
		return cinNumber;
	}

	public void setCinNumber(String cinNumber) {
		this.cinNumber = cinNumber;
	}

	public String getIndividualRelateTo() {
		return individualRelateTo;
	}

	public void setIndividualRelateTo(String individualRelateTo) {
		this.individualRelateTo = individualRelateTo;
	}
	@Column(name = "loc_text")  
	private boolean locText;
	
	@Column(name = "arrest_during_inv") 
	private boolean arrestDuringInvestigation;
	public boolean isLocText() {
		return locText;
	}

	

	public boolean isArrestDuringInvestigation() {
		return arrestDuringInvestigation;
	}

	
	public void setLocText(boolean locText) {
		this.locText = locText;
	}

	public void setArrestDuringInvestigation(boolean arrestDuringInvestigation) {
		this.arrestDuringInvestigation = arrestDuringInvestigation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateofOpen;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateofDeletion;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateofArrest;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateofGrantClosing;
	
	
	public Date getDateofOpen() {
		return dateofOpen;
	}

	public void setDateofOpen(Date dateofOpen) {
		this.dateofOpen = dateofOpen;
	}

	public Date getDateofDeletion() {
		return dateofDeletion;
	}

	public void setDateofDeletion(Date dateofDeletion) {
		this.dateofDeletion = dateofDeletion;
	}

	public Date getDateofArrest() {
		return dateofArrest;
	}

	public void setDateofArrest(Date dateofArrest) {
		this.dateofArrest = dateofArrest;
	}

	public Date getDateofGrantClosing() {
		return dateofGrantClosing;
	}

	public void setDateofGrantClosing(Date dateofGrantClosing) {
		this.dateofGrantClosing = dateofGrantClosing;
	}

	@ManyToOne(fetch = FetchType.LAZY)

	@PrimaryKeyJoinColumn(name = "designation")
	private AddDesignation designation;

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
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;

	private int approveStatus = 0;
	private String rejectRemark;

	
	
	
	
	@OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "addAccused")
    private Set<AccusedActAndSection> actSection = new HashSet<>();
	 
	 
	@Transient
	private String type;

	@Transient
	private HearingDetails hearingDtl;

}
