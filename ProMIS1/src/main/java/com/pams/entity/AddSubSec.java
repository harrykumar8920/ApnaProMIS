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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "AddSubSec", schema = "authentication")
public class AddSubSec{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "act")	
	private AddAct act;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "section")	
	private AddActSec section;
	
	private String description;
	private String subSection;

	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	
	@Column(name = "isActive", length = 1)
	private Integer isActive;
	  

	@Transient
	private Boolean isEditSubSec=false;

	
}
