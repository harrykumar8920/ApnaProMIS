package com.pams.dto;


import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class PriorityCaseDTO {

    private String companyName;
    private String caseTitle;
    private String courtJurisdiction;
    private String briefOfTheCase;
    private String caseStatus;
    private String caseNo;
    private String companyAct;
    private String pairaviOfficer;
    private String counsel;
    private String counselNumber;
    private String officerFromProsecution;
    private String date;
    private String toDate;
    private String remerks;
    private String sec;
    private String name;
    private int status;
}
