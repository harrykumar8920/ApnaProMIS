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
@Table(name="prosecutionCompanyDetailsMaster",schema = "prosecution" )
public class AddCompany {

	public AddCompany(String cin, String companyName, String address) {
		this.cin = cin;
		this.companyName = companyName;
		this.address = address;
	}
	public AddCompany() {
		// TODO Auto-generated constructor stub
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "serial")
	private Long id;
	
	
	private String companyName;

	private String cin;
	private String address;
	//private int approveStatus=0;

	
}
