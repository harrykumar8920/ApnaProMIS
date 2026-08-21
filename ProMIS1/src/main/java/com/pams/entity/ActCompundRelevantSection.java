package com.pams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "ChargeActCompundRelevantSection", schema = "prosecution")
public class ActCompundRelevantSection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AddAct act;
	private String compoundability;
	private String releventSection;
	private Long addActSecId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "charge_instance_main_id", nullable = false)
	private ChargeInstaceMain chargeInstanceMain;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Punishment1 punishment;

	@Transient
	private Integer actId;

	public ActCompundRelevantSection(Integer actId, String releventSection) {

		this.releventSection = releventSection;
		this.actId = actId;
	}

	public ActCompundRelevantSection() {
		super();

	}
}
