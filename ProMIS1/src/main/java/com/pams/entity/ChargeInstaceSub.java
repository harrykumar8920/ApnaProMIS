
package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ChargeInstaceSub", schema = "prosecution")
public class ChargeInstaceSub {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Instance instance;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "punishment")
	private Punishment1 punishment;
	@Column(length = 1000) // Specifies the column size
	private String instanceRemarks;

	@ManyToOne
	//@JoinColumn(name = "charge_instance_main_id")
	private ChargeInstaceMain chargeInstanceMain;
	
	private Boolean punishmentDone=false;

}
