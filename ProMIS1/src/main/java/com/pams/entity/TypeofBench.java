
package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
@Table(name = "typeOfBench", schema = "authentication")
public class TypeofBench {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="typeofbench", nullable=false)
	
	//@Pattern(regexp="^[a-zA-Z ]{2,39}",message="type of bench only in alphabets")
	private String bench;
}
