	
package com.pams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "AddInvestigaOrderDateSub", schema = "prosecution")
public class AddInvestigaOrderDateSub {
	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;
	
	private String additionalInvestigation;
	/*
	 * @DateTimeFormat(pattern = "dd/MM/yyyy")
	 * 
	 * @Column(name = "addInvestDate")
	 */
	private String additionalInvestigationDate;
	@ManyToOne
	private AddCase addCase;
	
}
