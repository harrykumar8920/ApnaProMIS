package com.pams.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor
@Table(name = "App_Role", schema = "authentication", uniqueConstraints = {
		@UniqueConstraint(name = "APP_ROLE_UK", columnNames = "Role_Name") })
public class AppRole {

	public AppRole(Long roleId) {
		this.roleId = roleId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Role_Id",columnDefinition = "serial")
	private Long roleId;

	@Column(name = "Role_Name", length = 30, nullable = false)
	@NotNull
	@Pattern(regexp="^[a-zA-Z_]{2,40}",message="Role name must be in alphanumeric with length ranging 2-40")
	private String roleName;
	
}

