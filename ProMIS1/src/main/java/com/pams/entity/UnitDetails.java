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
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor
@Table(name = "UnitDetails", schema = "authentication", uniqueConstraints = {
		@UniqueConstraint(name = "Unit_Details_UK", columnNames = "unitName") })
public class UnitDetails {
	
	public UnitDetails(Long unitId2, String unitName2, AddUnitlocation location2, String address2, String telephoneNo2,
			String faxNo2, String eMail2, Date createdDate2) {
		this.unitId = unitId2;
		this.unitName = unitName2;
		this.location =location2;
		this.address = address2;
		this.telephoneNo = telephoneNo2;
		this.faxNo = faxNo2;
		this.eMail = eMail2;
		this.createdDate = createdDate2;
	}


	public UnitDetails(Long unitId2) {
		this.unitId = unitId2;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unitId", columnDefinition = "serial")
	private Long unitId;

	@Column(name = "unitName", length = 20, nullable = false)
	private String unitName;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "location")
	private AddUnitlocation location;
	
	@Column(name = "address", length = 200, nullable = false)
	private String address;
	
	@Column(name = "telephoneNo", length = 30, nullable = false)
	private String telephoneNo;
	
	@Column(name = "faxNo", length = 30, nullable = false)
	private String faxNo;
	
	@Column(name = "eMail", length = 50, nullable = false)
	private String eMail;
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@Transient
	private Boolean editUnit;
//	@ManyToOne(fetch = FetchType.LAZY)
//	@PrimaryKeyJoinColumn(name = "createdBy")
//	private AppUser createdBy;

}
