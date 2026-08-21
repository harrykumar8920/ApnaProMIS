package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JaiUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "proSanctionOrderId",columnDefinition = "serial")
	private Long id;
	
	private String name;
	private String email;
	
}
