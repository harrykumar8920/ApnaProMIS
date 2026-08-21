package com.pams.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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
@Table(name = "proFreezerAssetOrder", schema = "prosecution")
public class FreezerAssetOrder {

	@Transient
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date deleteOrderDate;
	@Transient
	private MultipartFile orderFile;
	
	@Transient
	private Integer tempid;
	
	
	
	public Integer getTempid() {
		return tempid;
	}

	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}

	public Date getdeleteOrderDate() {
		return deleteOrderDate;
	}

	public void setdeleteOrderDate(Date deleteOrderDate) {
		this.deleteOrderDate = deleteOrderDate;
	}

	public MultipartFile getOrderFile() {
		return orderFile;
	}

	public void setOrderFile(MultipartFile orderFile) {
		this.orderFile = orderFile;
	}

	@Transient
	private String deleteRemarks;
	public String getDeleteRemarks() {
		return deleteRemarks;
	}

	public void setDeleteRemarks(String deleteRemarks) {
		this.deleteRemarks = deleteRemarks;
	}

	@Transient
	private Long deletedFreezerItemId;
	public Long getDeletedFreezerItemId() {
		return deletedFreezerItemId;
	}

	public void setDeletedFreezerItemId(Long deletedFreezerItemId) {
		this.deletedFreezerItemId = deletedFreezerItemId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(int approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	/*
	 * public String getFreezResponseType() { return freezResponseType; }
	 * 
	 * public void setFreezResponseType(String freezResponseType) {
	 * this.freezResponseType = freezResponseType; }
	 */

	public String getRejectRemarkFrezeAssets() {
		return rejectRemarkFrezeAssets;
	}

	public void setRejectRemarkFrezeAssets(String rejectRemarkFrezeAssets) {
		this.rejectRemarkFrezeAssets = rejectRemarkFrezeAssets;
	}

	public Date getFreezingOrderDate() {
		return freezingOrderDate;
	}

	public void setFreezingOrderDate(Date freezingOrderDate) {
		this.freezingOrderDate = freezingOrderDate;
	}

	public String getFreezerOrderFileName() {
		return freezerOrderFileName;
	}

	public void setFreezerOrderFileName(String freezerOrderFileName) {
		this.freezerOrderFileName = freezerOrderFileName;
	}

	public Date getComplianceOrderDate() {
		return ComplianceOrderDate;
	}

	public void setComplianceOrderDate(Date complianceOrderDate) {
		ComplianceOrderDate = complianceOrderDate;
	}

	public String getComplianceOrderFileName() {
		return complianceOrderFileName;
	}

	public void setComplianceOrderFileName(String complianceOrderFileName) {
		this.complianceOrderFileName = complianceOrderFileName;
	}

	public Date getIssueNoticeDate() {
		return issueNoticeDate;
	}

	public void setIssueNoticeDate(Date issueNoticeDate) {
		this.issueNoticeDate = issueNoticeDate;
	}

	public String getIssueNoticeFileName() {
		return issueNoticeFileName;
	}

	public void setIssueNoticeFileName(String issueNoticeFileName) {
		this.issueNoticeFileName = issueNoticeFileName;
	}

	public Date getFilingAffidavitDate() {
		return filingAffidavitDate;
	}

	public void setFilingAffidavitDate(Date filingAffidavitDate) {
		this.filingAffidavitDate = filingAffidavitDate;
	}

	public String getFilingAffidavitFileName() {
		return filingAffidavitFileName;
	}

	public void setFilingAffidavitFileName(String filingAffidavitFileName) {
		this.filingAffidavitFileName = filingAffidavitFileName;
	}

	public String getMovableAssets() {
		return movableAssets;
	}

	public void setMovableAssets(String movableAssets) {
		this.movableAssets = movableAssets;
	}

	public String getMovableAssetsFileName() {
		return movableAssetsFileName;
	}

	public void setMovableAssetsFileName(String movableAssetsFileName) {
		this.movableAssetsFileName = movableAssetsFileName;
	}

	public String getImmovableAssets() {
		return immovableAssets;
	}

	public void setImmovableAssets(String immovableAssets) {
		this.immovableAssets = immovableAssets;
	}

	public String getImmovableAssetsFileName() {
		return immovableAssetsFileName;
	}

	public void setImmovableAssetsFileName(String immovableAssetsFileName) {
		this.immovableAssetsFileName = immovableAssetsFileName;
	}

	public AssignedTaskPuhAfterCOurt getAssignedTask() {
		return assignedTask;
	}

	public void setAssignedTask(AssignedTaskPuhAfterCOurt assignedTask) {
		this.assignedTask = assignedTask;
	}

	public AddAccused getAddResponse() {
		return addResponse;
	}

	public void setAddResponse(AddAccused addResponse) {
		this.addResponse = addResponse;
	}

	public ProCourtCaseDetails getProcourtdtl() {
		return procourtdtl;
	}

	public void setProcourtdtl(ProCourtCaseDetails procourtdtl) {
		this.procourtdtl = procourtdtl;
	}

	public UserDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public UserDetails getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserDetails updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getViewFileTest() {
		return viewFileTest;
	}

	public void setViewFileTest(Boolean viewFileTest) {
		this.viewFileTest = viewFileTest;
	}

	public MultipartFile getFreezerOrderFile() {
		return freezerOrderFile;
	}

	public void setFreezerOrderFile(MultipartFile freezerOrderFile) {
		this.freezerOrderFile = freezerOrderFile;
	}

	public MultipartFile getComplianceOrderFile() {
		return complianceOrderFile;
	}

	public void setComplianceOrderFile(MultipartFile complianceOrderFile) {
		this.complianceOrderFile = complianceOrderFile;
	}

	public MultipartFile getIssueNoticeFile() {
		return issueNoticeFile;
	}

	public void setIssueNoticeFile(MultipartFile issueNoticeFile) {
		this.issueNoticeFile = issueNoticeFile;
	}

	public MultipartFile getFilingAffidavitFile() {
		return filingAffidavitFile;
	}

	public void setFilingAffidavitFile(MultipartFile filingAffidavitFile) {
		this.filingAffidavitFile = filingAffidavitFile;
	}

	public MultipartFile getMovableAssetsFile() {
		return movableAssetsFile;
	}

	public void setMovableAssetsFile(MultipartFile movableAssetsFile) {
		this.movableAssetsFile = movableAssetsFile;
	}

	public MultipartFile getImmovableAssetsFile() {
		return immovableAssetsFile;
	}

	public void setImmovableAssetsFile(MultipartFile immovableAssetsFile) {
		this.immovableAssetsFile = immovableAssetsFile;
	}

	public Set<FreezerAssetsItem> getFreezerAssetsItem() {
		return freezerAssetsItem;
	}

	public void setFreezerAssetsItem(Set<FreezerAssetsItem> freezerAssetsItem) {
		this.freezerAssetsItem = freezerAssetsItem;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	private int approvalStatus = 0;
	//private String freezResponseType;
	private String rejectRemarkFrezeAssets;

	public String[] getAccusedClause() {
		return accusedClause;
	}

	public void setAccusedClause(String[] accusedClause) {
		this.accusedClause = accusedClause;
	}

	@Transient
	private String[] accusedClause;
	@Transient
	private String[] accusedSection;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "freezingOrderDate")
	private Date freezingOrderDate;
	private String freezerOrderFileName;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "ComplianceOrderDate")
	private Date ComplianceOrderDate;
	private String complianceOrderFileName;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "issueNoticeDate")
	private Date issueNoticeDate;
	private String issueNoticeFileName;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "filingAffidavitDate")
	private Date filingAffidavitDate;
	private String filingAffidavitFileName;
	private String movableAssets;
	private String movableAssetsFileName;
	private String immovableAssets;
	private String immovableAssetsFileName;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddAccused addResponse;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;

	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy;

	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	@Transient
	private Boolean viewFileTest = false;
	@Transient
	private MultipartFile freezerOrderFile;
	@Transient
	private MultipartFile complianceOrderFile;
	@Transient
	private MultipartFile issueNoticeFile;
	@Transient
	private MultipartFile filingAffidavitFile;
	@Transient
	private MultipartFile movableAssetsFile;
	@Transient
	private MultipartFile immovableAssetsFile;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "freezerAssetOrder")
	private Set<FreezerAssetsItem> freezerAssetsItem = new HashSet<>();
	@Transient
	private String[] freezerAssetsTypeT;


	@Transient
	private String[] freezerAssetsNameT;

	public String[] getFreezerAssetsTypeT() {
		return freezerAssetsTypeT;
	}

	public void setFreezerAssetsTypeT(String[] freezerAssetsTypeT) {
		this.freezerAssetsTypeT = freezerAssetsTypeT;
	}

	public String[] getFreezerAssetsNameT() {
		return freezerAssetsNameT;
	}

	public void setFreezerAssetsNameT(String[] freezerAssetsNameT) {
		this.freezerAssetsNameT = freezerAssetsNameT;
	}
	@Transient
	private Long tempID;
	public Long getTempID() {
		return tempID;
	}

	public void setTempID(Long tempID) {
		this.tempID = tempID;
	}

	
	
	
	
}
