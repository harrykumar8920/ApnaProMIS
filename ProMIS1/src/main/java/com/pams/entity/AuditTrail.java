
package com.pams.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "AUDIT_TRAIL",schema="public")
public class AuditTrail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "ACTION_DATE")
	private Date actionDate;
	
	@Column(name = "ACTOR_ID")
	private int actorId;
	
	@Column(name = "ACTOR_NAME")
	private String actorName;
	
	@Column(name = "ACTOR_IP")
	private String actorIP;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "DOMAIN")
	private String domain;
	
	@Column(name = "OPERATION_TYPE")
	private String operationType;
	
	@Column(name = "OPERATION_DESC")
	private String operationDesc;
	@Column(name = "caseID")
    private Long caseID;
	
	
	
}
