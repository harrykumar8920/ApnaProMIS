
package com.pams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="clause",schema="authentication")
public class Clause {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//@Pattern(regexp = "^[a-zA-Z]", message="Enter only character")
	private String clause;
	//private Character clause;
}
