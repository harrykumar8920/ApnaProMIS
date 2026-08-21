package com.pams.entity;

import org.hibernate.annotations.GeneratorType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "ChargeInstanceAccused", schema = "prosecution")
public class ChargeInstanceAccused {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "charge_instance_main_id", nullable = false)
	private ChargeInstaceMain chargeInstanceMain;
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @PrimaryKeyJoinColumn(name = "accuseId") private AddAccused accuseName;
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accuseId", nullable = false)
	private AddAccused accuseName;
}
