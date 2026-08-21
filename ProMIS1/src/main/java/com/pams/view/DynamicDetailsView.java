package com.pams.view;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "dynamic_details", schema = "prosecution")
@Immutable
public class DynamicDetailsView {

    @Id
    @Column(name = "pro_sanction_order_id")
    private Long proSanctionOrderId;

    @Column(name = "case_title")
    private String caseTitle;

    @Column(name = "cin_number")
    private String cinNumber;

    @Column(name = "typeof_order")
    private String typeofOrder;

    @Column(name = "investigation_order_no")
    private String investigationOrderNo;

    @Column(name = "investigation_order_date")
    private Date investigationOrderDate;

    @Column(name = "corrigendum_date")
    private Date corrigendumDate;

    @Column(name = "court_case_no")
    private String courtCaseNo;

    @Column(name = "brief")
    private String brief;

    @Column(name = "backgroundofcase")
    private String backgroundOfCase;

    // getters only
}
