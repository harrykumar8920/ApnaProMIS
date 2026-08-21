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
@Table(name="inspectorDetails",schema = "prosecution" )
public class Inspector {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id") 
	private PairaviOfficer inspectorName;
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "id") private AddDesignation
	 * inspectorDesignation;
	 */
	
private Integer approvalStatus=0;
	
	private String orderFileName;
	private String orderNumber; 
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date orderDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date approveDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "aprovedBy")
	private UserDetails aprovedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
	
	@Transient
	private MultipartFile orderFile;
}
