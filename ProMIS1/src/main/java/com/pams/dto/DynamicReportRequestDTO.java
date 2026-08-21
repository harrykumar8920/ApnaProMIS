package com.pams.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicReportRequestDTO {

	private LocalDate fromDate;
	private LocalDate toDate;

	/**
	 * HEARING_DATE FILING_DATE SANCTION_ORDER_DATE
	 */
	private String dateType;

	/**
	 * Selected columns from UI Example: CASE_NO, ACCUSED_DETAILS, CNR_NO
	 */
	private java.util.List<String> selectedFields;

}
