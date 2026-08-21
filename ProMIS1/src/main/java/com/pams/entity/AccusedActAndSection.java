package com.pams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity

@Table(name = "AccusedActAndSection", schema = "prosecution")
public class AccusedActAndSection {

	@Override
	public String toString() {
		return "AccusedActAndSection [id=" + id + ", clause=" + clause + ", act=" + act + ", section=" + section
				+ ", compatability=" + compatability + ", subSection=" + subSection + ", punishment=" + punishment
				+ ", description=" + description + ", addAccused=" + addAccused + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public AddAct getAct() {
		return act;
	}

	public void setAct(AddAct act) {
		this.act = act;
	}

	public AddActSec getSection() {
		return section;
	}

	public void setSection(AddActSec section) {
		this.section = section;
	}

	public String getCompatability() {
		return compatability;
	}

	public void setCompatability(String compatability) {
		this.compatability = compatability;
	}

	public AddSubSec getSubSection() {
		return subSection;
	}

	public void setSubSection(AddSubSec subSection) {
		this.subSection = subSection;
	}

	public Punishment1 getPunishment() {
		return punishment;
	}

	public void setPunishment(Punishment1 punishment) {
		this.punishment = punishment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AddAccused getAddAccused() {
		return addAccused;
	}

	public void setAddAccused(AddAccused addAccused) {
		this.addAccused = addAccused;
	}

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	private String clause;
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "act")
	private AddAct act;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "Section")
	private AddActSec section;
	
	private String compatability; 
	
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @PrimaryKeyJoinColumn(name = "SubSection") private AddSubSec subSection;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @PrimaryKeyJoinColumn(name = "punishment") private Punishment1 punishment;
	 
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)

	@JoinColumn(name = "ida")
	private AddAccused addAccused;

}
