package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "prosecution_status", schema = "authentication")
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_status_id", columnDefinition = "serial")
	private Long id;
	@Column(name = "pro_status")
	private String statusName;
	@Column(name = "status_type")
	private String type;
	@Transient
	private Boolean editstatus;
	
	@Column(name = "is_active",columnDefinition = "boolean default true")
	private Boolean isActive = true;
	
}
