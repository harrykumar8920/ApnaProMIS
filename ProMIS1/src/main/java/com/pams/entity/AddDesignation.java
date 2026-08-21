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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Designation", schema = "authentication")
/*
 * @Table(name = "Designation", schema = "authentication", uniqueConstraints = {
 * 
 * @UniqueConstraint(name = "DESIGNATION_UK", columnNames = "designation") })
 */
public class AddDesignation {

	public AddDesignation(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;

	@Column(name = "designation", length = 100, nullable = false)
	@NotNull
//	@Size(min = 5, max = 39)
	//@Pattern(regexp="^[a-zA-Z ]{2,39}",message="Designation must be in alphanumeric with length ranging 2-40")
	@Pattern(regexp="^[a-zA-Z ]{2,39}",message="Designation must be in alphanumeric with length ranging 2-40")
	private String designation;
	


	@Column(name = "desig_order", length = 2, nullable = true)
	private Integer order;
     
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@Transient
	private Boolean editdesgi;
	
	private String deginationtype; 
}
