package com.pams.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

import com.pams.entity.AuditTrail;

public interface AuditBeanDao {

	void saveAuditTrail(AuditTrail auditTrail);

	List<AuditTrail> findAllByActionDate(Date date1);
	List<AuditTrail> findAllByActionDate(Date date1,Sort sort);
	

}
