package com.pams.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "prosecutiondisposed", schema = "prosecution")
public class Disposed {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 private String orderfilename;
	 private String mannerDisposal;
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "counsel_id")
	 private PairaviOfficer counsel;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name="pairavi_officer_id")
	 private PairaviOfficer pairaviOfficer;
	
	    @Column(name = "createdDate")
		@Temporal(TemporalType.TIMESTAMP)
		private Date createdDate;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name="created_by")
	    private UserDetails createdBy;
	    
		@Column(name = "updatedDate")
		@Temporal(TemporalType.TIMESTAMP)
		private Date updatedDate;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="updated_by")
		private UserDetails updatedBy;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="assigned_task_id")
		private AssignedTaskPuhAfterCOurt assignedTask;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="procourtdtl_id")
		private ProCourtCaseDetails procourtdtl;
		
		@Column(length = 2000) 
		private String dismissalOrder;	
		
		@OneToMany(
		        mappedBy = "disposed",
		        cascade = CascadeType.ALL,
		        orphanRemoval = true)
		private List<AccusedDisposalqua> accusedDisposal = new ArrayList<>();
}
