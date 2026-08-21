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
@Data
@Entity
@Table(name="prosecution_Accused_detail_master",schema = "prosecution" )
public class AccusedMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accused_id",columnDefinition = "serial")
	private Long id;
	
	private String accusedName;
	private  String panNumber;
	private String address;
	
	private String accDesination;
	
	
	

	
	
	
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	
	
}
