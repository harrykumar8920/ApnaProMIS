package com.pams.dto;

import java.util.List;

import com.pams.entity.AddAct;

import lombok.Data;

@Data
public class ActCompundRelevantSectionDto {
private Long id;
private String actName;
private Long actId;
private String compoundability;
private String releventSection;
private String punishment;
private Integer punishmentId;
private AddAct actList;
}
