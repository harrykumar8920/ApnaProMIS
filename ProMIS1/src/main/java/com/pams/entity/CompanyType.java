

package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CompanyType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_type_id  ",columnDefinition = "serial")
	private int id ;
	@Column(name = "Company_type  ")
	private String name;
}
