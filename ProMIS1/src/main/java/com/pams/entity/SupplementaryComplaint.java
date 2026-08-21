package com.pams.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "prosecution_supplementary_complaint", schema = "prosecution")
public class SupplementaryComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplementaryInvestigationOrderNo;

    private LocalDate supplementaryInvestigationOrderDate;

    private LocalDate dateOfSubmissionOfSupplementaryInvestigationReport;

    private String supplementaryInvestigationSanctionNo;

    private LocalDate supplementaryInvestigationSanctionDate;

    private String supplementaryComplaintCaseNumber;

    private String investigationOfficer;

    private String investigationOfficerDesignation;

    private String complainant;

    private String complainantDesignation;

    private LocalDate dateOfFiling;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "supplementary_complaint_accused",   // 🔥 THIRD TABLE
        schema = "prosecution",
        joinColumns = @JoinColumn(name = "supplementary_complaint_id"),
        inverseJoinColumns = @JoinColumn(name = "accused_id")
    )
    private List<AddAccused> accusedList;
    
    
    
    @Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
    
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
    
}
