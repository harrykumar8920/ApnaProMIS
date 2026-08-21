package com.pams.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "UserDetails", schema = "authentication", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "primaryMobile", "email", "sfioEmpId" }) })
public class UserDetails {

	public UserDetails(Long i) {
	}

	public UserDetails(int count, String fullName, String designation2,  Long id) {
		this.Srno = count;
		this.fullName = fullName;
		this.designation2 = designation2;
		this.id = id;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "serial")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
	private AppUser userId;

	@Column(name = "firstName", length = 40, nullable = false)
	private String firstName;

	@Column(name = "middleName", length = 40, nullable = true)
	private String middleName;

	@Column(name = "lastName", length = 40, nullable = false)
	private String lastName;

	/*@Column(name = "fullName", length = 120)
	private String fullName;*/

	@Column(name = "salutation", length = 3, nullable = false)
	private String salutation;

	@Column(name = "sfioEmpId", length = 30, nullable = false)
	private String sfioEmpId;

	@Column(name = "primaryMobile", length = 12, nullable = false)
	private String primaryMobile;
	
	@Column(name = "alternateNo", length = 12, nullable = false)
	private String alternateNo;

	@Column(name = "email", length = 100, nullable = false)
	private String email;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "designation")
	private AddDesignation designation;
	
	@Transient
	//@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date uiDob;
	
	@Transient
	private Long unitId;
	
	@Transient
	private Long roleId;
	
	@Transient
	private Long designationId;
	
	@Transient
	//@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date uiJoiningDate;

//	@Column(name = "location", length = 50, nullable = false)
//	@NotNull
//	private String location;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "unit")
	private UnitDetails unit;
	
	@Column(name = "dob")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dob;
	
	@Column(name = "joiningDate")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date joiningDate;
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private AppUser createdBy;
	
	@Transient
	private String fullName;

	@Transient
	private String designation2;
	
	@Transient
	private int Srno;
}
