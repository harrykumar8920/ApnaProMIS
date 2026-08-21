package com.pams.dto;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class MonthlyReportPoint9 {
private String causeTitle;

@Transient
private List<String> companyAccused;

private boolean statusFilled;
private Date lastHearingDate;
private Date nextHearingDate;


}
