package com.pams.dto;

import lombok.Data;

@Data
public class GetDataFromSNMSByCaseNumberDTO {
	
	public int gDFSBCId;
	public String caseNumber;
	public int individualType;
	public String companyName;
	public String cin;
	public String registrationNumber;
	public String name;
	public String email;
	public String relationWithCompany;
	public String designation;
	
	public String panNumber;
	
}
