package com.pams.dto;

import lombok.Data;

@Data
public class PageNoDTO {
	
	int pageno;
	int totalPage;
	Integer totalList;
	//1 NCLT & 2 CRIMINCAl
	Integer  caseType;
	String str;


}
