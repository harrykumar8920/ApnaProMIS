
package com.pams.entity;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "App_User", schema = "authentication", //
		uniqueConstraints = { //
				@UniqueConstraint(name = "APP_USER_UK", columnNames = "User_Name") })
public class AppUser {

	public AppUser(Long userId) {
		this.userId = userId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "User_Id",columnDefinition = "serial")
	private Long userId;

	@Column(name = "User_Name", length = 36, nullable = false)
	private String userName;

	@Column(name = "Encryted_Password", length = 128, nullable = false)
	private String encrytedPassword;

	@Column(name = "validFrom")
	@Temporal(TemporalType.TIMESTAMP)
	private Date validFrom;

	@Column(name = "validUpto")
	@Temporal(TemporalType.TIMESTAMP)
	private Date validUpto;

	@Column(name = "Enabled", length = 1, nullable = false)
	private int enabled;
	
	@Column(columnDefinition="text")
	private String oldPassword;


	
	private Boolean passChanged ;

	private Boolean isApproved ;
   
}
