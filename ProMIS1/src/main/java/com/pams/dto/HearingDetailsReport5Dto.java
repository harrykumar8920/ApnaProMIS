package com.pams.dto;

import java.util.Date;

import lombok.Data;

@Data
public class HearingDetailsReport5Dto {
private String caseTitle;
private Date dateOfOrder;
private Date nextHearingDate;
private String reasonofStay;

}
