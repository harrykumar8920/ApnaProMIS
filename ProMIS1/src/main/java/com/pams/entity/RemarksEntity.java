

package com.pams.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;


	
	
	
	@Entity
	@Data
	@Table(name="Remarks",schema="prosecution")
	public class RemarksEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String remarksNote;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTask;

}
