package com.pams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="punishment",schema="authentication")
public class Punishment1 {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String punishment1;

}

