package com.pams.dto;

import lombok.Data;
@Data
public class ChargeInstaceSubDto {
	
	private Integer punishmentId;

	private Long instanceId;

	private String instanceRemarks;
	
	private String punishmentName;
	private String instanceName;
	private Long subTableId;
}
