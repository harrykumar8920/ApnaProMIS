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
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "prosecutionCaseStatus",schema = "prosecution")
public class CaseStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_court_case_status_id  ", columnDefinition = "serial")
	private Long id;
	public CaseStatus(Long id) {
		this.id = id;
	}
	@Column(name = "caseStatusDate")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date caseStatusDate;
	
	
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	private Long updateBy;
	private Boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Status caseStatus;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddDisposalState disposed;
}
