package com.pams.entity;

import java.util.ArrayList;
import java.util.Date;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ProsecutionSanctionOrderDetails",schema = "prosecution")
		
public class AddCase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "proSanctionOrderId",columnDefinition = "serial")
	private Long id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date oversightCommitteeMeetingDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "basisInvestigationOrder")
	private BasisofIO basisInvestigationOrder;
	
	//private String additionalInvestigation;
	/*
	 * @DateTimeFormat(pattern = "dd/MM/yyyy")
	 * 
	 * @Column(name = "additionalInvestigationDate") private Date
	 * additionalInvestigationDate;
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "benchName")
	private TypeofBench benchName; 
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "type") private Type type;
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy;
	
	private String cinNumber;
	private String courtCaseNumber;
	private String advanceNotice;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "approvedBy")
	private UserDetails approvedBy; 
	
	private String caseTitle;
	private String caseNo;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "id") private TypeofCase typeOfCase;
	 */
	@Transient
	private String typeOfCaseT;
	private String investigationOrderNo;
	
	
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "proSanctionDate")
	private Date proSanctionDate;
	
	//@UniqueElements
	 //@Column(unique = true)
	private String proSectionOrderNumber;

	private String fileNumber;
	private String proSanctionFileName;
	
	@Transient
	private MultipartFile prosectionSanctionOrderFile;
	
	private Integer finalisationStatus=0;
	private String remarksByPUH="";
	
	private int typeofOrder;
	private Boolean isAccused;
	private String petionerName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddCourt courtType;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddState state; 
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private District city; 
	
	//New Three Field
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "investigationOrderDate")
	private Date investigationOrderDate;
	private String supplimentoryOrderNo;
	


	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "supplimentoryOrderDate")
	private Date supplimentoryOrderDate;
	@Transient
	private String[] additionalInvestigation;
	
	@Transient
	
	private String testError;
	  @Transient
	    private Long taskCount; 
	
	@Transient
	private String[] additionalInvestigationDate;
@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "addCase")
	
	private java.util.List<AddInvestigaOrderDateSub> addInvestigaOrderDateSub = new ArrayList<>();
	

}
