package com.pams.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accuseddisposalqua", schema = "prosecution")
public class AccusedDisposalqua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fineimposed;
    private String imprisonment;
    private String senctioncompunded;
    private String forumcompounding;
    private String convictionaccusedguilty;
    @Column(length = 2000) 
    private String briefdetails;
    private String caseStatus;     
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accused_id")
    private AddAccused accused;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disposed_id")   
    private Disposed disposed;

}