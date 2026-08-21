
package com.pams.entity;



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
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Entity
@Data

@Table(name = "typeofCase", schema = "authentication")


public class TypeofCase implements Comparable<TypeofCase> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "typeOfCase", length = 40, nullable = false)
	@Pattern(regexp ="^[a-zA-Z.&/ ()-]{2,40}$", message = "TypeOfCase must be in alphabet with length ranging 2-40")
	private String typeOfCase;

	/* private String type; */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id")
	private Type type;
	
	@Transient
	private Boolean editcase = false;
	@Override
	public int compareTo(TypeofCase o) {
		
		return this.typeOfCase.compareTo(o.typeOfCase);
	}

	

}
