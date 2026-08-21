package com.pams.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
@Table(name = "prosecutionAccusedStatusNew", schema = "prosecution")
public class AccusedStatusNew {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "accusedId")
	private AddAccused accusedId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Charge chargeName;
	//for old instancew status 
	private Long releventSectionId;

	private String instanceRemarks;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn(name = "id")
	private ChargeInstaceSub instanceId;
	
	private String punishmentOrder;//ok
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date punishmentDate;//ok

	//Charge not made-1,Death-2,Quashed-3,Cognizance not taken-4
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private Discharge discharge;
	
	
	
	private boolean stayed;
	
	private String punishmentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "updatedBy")
	private UserDetails updatedBy;
	
	@CreationTimestamp
	private LocalDateTime creationDate;
	
	@UpdateTimestamp
	private LocalDateTime updationDate;
	private boolean checkStatus = true;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "punishmenid")
	private Punishment1 punishmenttId;//ok
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "courtid")
	private AddCourt courtList;//ok
	@Column(length = 600)
	private String remarksCheck;
	private int statusCheck;
	@Transient
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddAct actList;
	@Transient
	private Long adddActTableId;
	private String CompoundabilityText;
	private String relevantText;
	private String punishmentText;
	/* private String chargeNameText; */
}
