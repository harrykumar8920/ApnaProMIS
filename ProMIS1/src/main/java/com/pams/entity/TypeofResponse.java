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
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
@Entity
@Table(name = "TypeofResponse", schema = "authentication")
public class TypeofResponse {
	

	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id", columnDefinition = "serial")
		
		private Long id;
		@Column(name = "response", length = 40)
		
		@Pattern(regexp="^[a-zA-Z ]{2,39}",message="Response must be in alphanumeric with length ranging 2-40")
		private String response;
		
	     
		@Column(name = "createdDate")
		@Temporal(TemporalType.TIMESTAMP)
		private Date createdDate;
		
		
	
		
	}


