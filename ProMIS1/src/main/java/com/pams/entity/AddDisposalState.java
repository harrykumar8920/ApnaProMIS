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
@Table(name = "prosecution_Disposal_State",schema = "authentication")
public class AddDisposalState {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_court_disposal_state_id  ", columnDefinition = "serial")
	private Long id;
	
    private String disposalState;
	
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	@Transient
    private Boolean editstatus;
}
