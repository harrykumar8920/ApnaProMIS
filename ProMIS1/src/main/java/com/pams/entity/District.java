

package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Entity
@Data
public class District {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "serial")
	private Long id;
	private String districtName;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private  AddState state;
	
}
