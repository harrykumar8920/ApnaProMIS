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
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data



@Table(name = "CourtType", schema = "authentication")
/*
 * @Table(name = "CourtType", schema = "authentication", uniqueConstraints = {
 * 
 * @UniqueConstraint(name = "COURT_UK", columnNames = "courtName") })
 */
public class AddCourt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "serial")
	private Long id;
	@Pattern(regexp="^[a-zA-Z0-9\\s-&()._]{1,100}$",message="court must be in alphanumeric with length ranging 2-40")
	
	
	
	private String courtName;
	
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy; 
	@Column(name = "updatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	//old type 1, punishment 2 discharge 3, stay 4
	private Integer courtType;
	
	 @Column(name = "active")
	 private Boolean active = true; 
	
	
}
