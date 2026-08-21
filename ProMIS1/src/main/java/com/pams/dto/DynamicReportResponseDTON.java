package com.pams.dto;

import java.time.LocalDate;

public class DynamicReportResponseDTON {

    private String caseNo;
    private String hearingDetails;
    private String counselDetails;
    private String accusedDetails;
    private String prosecutorName;
    private String investigationName;
    private String pairaviOfficerName;
    private String complainantDetails;
    private String courtNo;
    private String companyAct;
    private LocalDate filingDate;
    private String cnrNo;

    // ===== GETTERS & SETTERS =====

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getHearingDetails() {
        return hearingDetails;
    }

    public void setHearingDetails(String hearingDetails) {
        this.hearingDetails = hearingDetails;
    }

    public String getCounselDetails() {
        return counselDetails;
    }

    public void setCounselDetails(String counselDetails) {
        this.counselDetails = counselDetails;
    }

    public String getAccusedDetails() {
        return accusedDetails;
    }

    public void setAccusedDetails(String accusedDetails) {
        this.accusedDetails = accusedDetails;
    }

    public String getProsecutorName() {
        return prosecutorName;
    }

    public void setProsecutorName(String prosecutorName) {
        this.prosecutorName = prosecutorName;
    }

    public String getInvestigationName() {
        return investigationName;
    }

    public void setInvestigationName(String investigationName) {
        this.investigationName = investigationName;
    }

    public String getPairaviOfficerName() {
        return pairaviOfficerName;
    }

    public void setPairaviOfficerName(String pairaviOfficerName) {
        this.pairaviOfficerName = pairaviOfficerName;
    }

    public String getComplainantDetails() {
        return complainantDetails;
    }

    public void setComplainantDetails(String complainantDetails) {
        this.complainantDetails = complainantDetails;
    }

    public String getCourtNo() {
        return courtNo;
    }

    public void setCourtNo(String courtNo) {
        this.courtNo = courtNo;
    }

    public String getCompanyAct() {
        return companyAct;
    }

    public void setCompanyAct(String companyAct) {
        this.companyAct = companyAct;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public String getCnrNo() {
        return cnrNo;
    }

    public void setCnrNo(String cnrNo) {
        this.cnrNo = cnrNo;
    }
}
