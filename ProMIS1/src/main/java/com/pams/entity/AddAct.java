package com.pams.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "Act", schema = "authentication")
public class AddAct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	
	@Column(name = "Act", length = 100, nullable = true)
	private String act;
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	private String actType;
	private String description;
	
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@Transient
	private Boolean isEditAct;
	
	@Column(name = "isActive", length = 1)
	private Integer isActive;
	
}
