package com.pams.entity;

import java.util.Date;

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
@Table(name = "performaPartyDetails", schema = "prosecution")
public class PerformaParty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pp_id", columnDefinition = "serial")
	private Long id;
	private int typeofOrder;
	private String pPCompany;
	private String pPCompCin;
	private String pPRespondentName;
	private String pPRespondentDesgination;
	private String pPAddress;
	private String rejectRemark;
	
	private Integer approveStatus=0; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
}
