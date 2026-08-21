package com.pams.entity;

import java.util.ArrayList;
import java.util.Date;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ChargeInstaceMain", schema = "prosecution")
public class ChargeInstaceMain {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	private boolean deleteStatus = false;
	private Integer approvalStatus = 0;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private ProCourtCaseDetails procourtdtl;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Charge charge;

	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chargeInstanceMain")

	private java.util.List<ChargeInstanceAccused> chargeInstanceAccused = new ArrayList<>();

	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chargeInstanceMain")

	private java.util.List<ActCompundRelevantSection> actCompundRelevantSection = new ArrayList<>();
	
	

	@Column(length = 1000) // Specifies the column size
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "aprovedBy")
	private UserDetails aprovedBy;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date approveDate;

	@Transient
	private String[] instanceRemarksA;
	@Transient
	private Instance[] instanceIDA;
	@Transient
	private Punishment1[] punishmentIDA;

	@Transient
	private java.util.List<AddAccused> accuseName1 = new ArrayList<AddAccused>();

	@Transient
	private AddAct[] actA;
	@Transient
	private String[] compoundabilityA;
	
	@Transient
	private String releventSectionA;
	@Transient
	private String addActSecId;
	
	@Transient
	private AddAct act;
	@Transient
	private String compoundability;
	@Transient
	private String releventSection;

	private int samechargeType;

}
