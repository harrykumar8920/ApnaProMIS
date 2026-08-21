package com.pams.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name="AccusedCompanyCaseDetails",schema = "prosecution")
public class AccusedCompCaseDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	private Long proCourtId;
	private Long CaseId;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "createdBy")
	private UserDetails createdBy;
	
	private Date createdDate;
	private Date updatedDate;
	
	@OneToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name="ENTITY_companySummon")
//	@OneToMany(mappedBy = "summonDetails", cascade = CascadeType.ALL)
	@Transient
	private List<CaseCompany> company=new ArrayList<CaseCompany>();
	
	@Transient
	private CaseCompany companyDto=new CaseCompany();
	@Transient
	private AddAccused AccusedDto=new AddAccused();
	
	@Transient
	private Long compId;
	
	@Transient
	private String type;
}
