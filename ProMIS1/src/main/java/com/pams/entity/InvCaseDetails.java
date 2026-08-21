
package com.pams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="InvCaseDetails",schema = "prosecution" )
public class InvCaseDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "serial")
	private Long id;
	private String caseId;
	//private String caseTitle;
    private String mcaOrder;
    private String fy;
	private Long   invcaseDetailsId;  
}
