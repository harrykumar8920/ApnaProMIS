
package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;


@Entity
@Data
@Table(schema = "authentication", uniqueConstraints = {
		@UniqueConstraint(name = "PAIRAVI_TYPE_UK", columnNames = "pairaviType") })
public class PairaviType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	private String pairaviType;
}
