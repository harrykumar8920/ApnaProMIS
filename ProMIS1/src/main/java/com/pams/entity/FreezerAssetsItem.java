
package com.pams.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "proFreezerAssetsItem", schema = "prosecution")
public class FreezerAssetsItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date deletedDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date orderDate;
	private String regionRemarks;
	private String uploadOrderFileNamee;
	public String getFreezerAssetsName() {
		return freezerAssetsName;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	

	public String getRegionRemarks() {
		return regionRemarks;
	}

	public void setRegionRemarks(String regionRemarks) {
		this.regionRemarks = regionRemarks;
	}

	public String getuploadOrderFileNamee() {
		return uploadOrderFileNamee;
	}

	public void setuploadOrderFileNamee(String uploadOrderFileNamee) {
		this.uploadOrderFileNamee = uploadOrderFileNamee;
	}

	public void setFreezerAssetsName(String freezerAssetsName) {
		this.freezerAssetsName = freezerAssetsName;
	}


	


	private String freezerAssetsType;
	private String freezerAssetsName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idtest")
	private FreezerAssetOrder freezerAssetOrder;

	public String getFreezerAssetsType() {
		return freezerAssetsType;
	}

	public void setFreezerAssetsType(String freezerAssetsType) {
		this.freezerAssetsType = freezerAssetsType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FreezerAssetOrder getFreezerAssetOrder() {
		return freezerAssetOrder;
	}

	public void setFreezerAssetOrder(FreezerAssetOrder freezerAssetOrder) {
		this.freezerAssetOrder = freezerAssetOrder;
	}
	
	
	//Remarks- 0 freez 1 unfreez 2 delete
	
	private Integer allStatus=0;
	public Integer getAllStatus() {
		return allStatus;
	}

	public void setAllStatus(Integer allStatus) {
		this.allStatus = allStatus;
	}







	
}
