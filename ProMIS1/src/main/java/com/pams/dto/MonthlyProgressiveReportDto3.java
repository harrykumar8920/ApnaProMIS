package com.pams.dto;

import java.util.List;

import com.pams.entity.ActCompundRelevantSection;

import lombok.Data;

@Data
public class MonthlyProgressiveReportDto3 {
private String causeTitle;
private String proSanctionDate;
private String threeMonth;
private String corrigendumDate;
private String corrigendumDateView;
private List<ActCompundRelevantSection> sec;
private List<ActCompundRelevantSection> sec1;
private List<ActCompundRelevantSection> sec2;
private List<ActCompundRelevantSection> sec3;
private List<ActCompundRelevantSection> sec4;
private List<ActCompundRelevantSection> sec5;
private String hearingDetailsstatus;

}
