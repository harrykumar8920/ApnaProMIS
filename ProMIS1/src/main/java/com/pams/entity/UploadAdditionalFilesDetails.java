package com.pams.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "UploadAdditionalFilesDetails", schema = "prosecution")
public class UploadAdditionalFilesDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	
	

	private String fileName;
	/* private String misRespondent; */
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user")
	private UserDetails user;

	@ManyToOne(fetch = FetchType.LAZY)

	@PrimaryKeyJoinColumn(name = "id")
	private AssignedTaskPuhAfterCOurt assignedTaskPuhdtl;

	
	@ManyToOne(fetch = FetchType.LAZY)

	@PrimaryKeyJoinColumn(name = "accusedId")
	private AddAccused accusedId;
	private int approveStatus = 0;
	private String rejectRemark;
	
	@Transient
	private MultipartFile file;

}
