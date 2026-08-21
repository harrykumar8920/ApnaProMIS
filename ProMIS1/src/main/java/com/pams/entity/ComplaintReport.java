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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;


@Entity
@Data
@Table(name="proComplaintReportTemplate",schema = "prosecution")
public class ComplaintReport {

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "compID",columnDefinition = "serial")
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user")
	private UserDetails user;
	
	
	
	
	
	 
	
	
	/*
	 * @Lob private String sectionPara1;
	 */
	
	@Lob
	private String mpara1;
	
	@Lob 
	private String backPara;
	
	@Lob 
	private String descPara;
	
	
	@Lob 
	private String  prayerPara;
	
	private String place ;
	
	private Boolean isFinal = false;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTaskPuh;
	
	
	private String complaintReportUpload;
	private int typeOfReport;
	
	private int approveStatus=0;
	private String rejectRemark;
	
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "userId") private AppUser appUser;
	 */
	
    @Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Transient
	private CaseCompany caseCompany;
	
	@Transient
	private List<AddAccused> addAccused;
	
	@Transient
	private List<ActSecDetailsInfo> actsecDtl;
	

	@Transient
	private AccusedCompCaseDtl accComp;
	
	 @Transient
	private String counsel;
	 @Transient
		private String counsel2;
	 
	 @Transient
		private String pairaviofficer;
}
