package com.pams.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "NCLTActofRespondant")
public class NCLTActofRespondant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary key for NCLTActofRespondant

	@NotNull(message = "Accused is required")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "nclt_respondant_accused", joinColumns = @JoinColumn(name = "nclt_respondant_id"), inverseJoinColumns = @JoinColumn(name = "accused_id"))
	private List<AddAccused> accuseName1; // List of accused entities

	@ManyToOne
	@JoinColumn(name = "act_id")
	@NotNull(message = "Act is required")
	private AddAct act; // Single selected act ID

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "nclt_respondant_section", joinColumns = @JoinColumn(name = "nclt_respondant_id"), inverseJoinColumns = @JoinColumn(name = "section_id"))
	@NotNull(message = "Section is required")
	private List<AddActSec> section; // List of selected section IDs

	@NotNull(message = "Remarks are required")
	private String description; // Remarks text

	@ManyToOne
	@JoinColumn(name = "assigned_task_id")
	private AssignedTaskPuhAfterCOurt assignedTask; // Associated task with the respondent

	@ManyToOne
	@JoinColumn(name = "court_case_details_id")
	private ProCourtCaseDetails procourtdtl; // Associated court case details

	private Integer approvalStatus = 0;
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

}
