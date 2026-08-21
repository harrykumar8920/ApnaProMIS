package com.pams.service;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AddSubSec;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import java.lang.Long;
import com.pams.entity.AssignedTaskPuh;


public interface ActSecDetailsRepository extends JpaRepository<ActSecDetailsInfo, Long> {


	
	
	public ActSecDetailsInfo findAllById(Long id);
	public List<ActSecDetailsInfo> findAllByProcourtdtlID(Long id);
	public List<ActSecDetailsInfo> findAllByProcourtdtlIDOrProcourtdtlIDAndCreatedBy(Long id,Long id1,UserDetails createdBy);
	public List<ActSecDetailsInfo> findAllByProcourtdtlIDOrProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive(Long id,Long id1,UserDetails createdBy,Integer caseType,Integer isActive);
	
	public List<ActSecDetailsInfo> findAllByProcourtdtlIDAndCreatedBy(Long id,UserDetails createdBy);
	public List<ActSecDetailsInfo> findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive(Long id,UserDetails createdBy,Integer caseType,Integer isActive);
	
	
	public List<ActSecDetailsInfo> findAllByProcourtdtlIDAndIsActive(Long id,Integer isActive);
	
	public List<ActSecDetailsInfo> findByProcourtdtlIDAndActAndSectionAndSubSectionAndIsActive(Long procourtdtlid,AddAct act,AddActSec section,AddSubSec subSection,Integer isActive);
	
	public List<ActSecDetailsInfo> findByProcourtdtlIDAndActAndSectionAndIsActive(Long procourtdtlid,AddAct act,AddActSec section,Integer isActive);
	
	
	public List<ActSecDetailsInfo> findByProcourtdtlIDAndAct(Long procourtdtlid,AddAct act);
	
	List<ActSecDetailsInfo> findByAssignedTaskAndIsActive(AssignedTaskPuh assignedtask, Integer isActive);
	
	public List<ActSecDetailsInfo> findByAssignedTaskAndActAndSectionAndSubSectionAndIsActive(AssignedTaskPuh assignedTask,AddAct act,AddActSec section,AddSubSec subSection,Integer isActive);
	

}
