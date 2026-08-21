package com.pams.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.pams.entity.AddSubTask;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CreateTasks;
import com.pams.entity.SfioAs;
import com.pams.entity.UnitDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AssignedTasksPuhRepository;

@Service
public class PuhValidation {
	
	@Autowired
	AssignedTasksPuhRepository assignedTasksPuhRepository;

	public void validatePuh(AssignedTaskPuh assignedTaskPuh, Errors errors) {

		

		PuhValidation puh = new PuhValidation();
		puh.isvalidUnit("unit", assignedTaskPuh.getUnit(), errors, "errmsg.unitname", true);
		puh.isvalidsfioAs("sfioAs", assignedTaskPuh.getSfioAs(), errors, "errmsg.unitname", true);
		puh.isvalidcreatetask("createTask", assignedTaskPuh.getCreateTask(), errors, "errmsg.unitname", true);
		//puh.isvalidSubtask("Subtask", assignedTaskPuh.getSubtask(), errors, "errmsg.unitname", true);
		puh.isvaliduser("user", assignedTaskPuh.getUser(), errors, "errmsg.unitname", true);
		
		
		

}
public void validatePuhAfterCourt(AssignedTaskPuhAfterCOurt assignedTaskPuh, Errors errors) {

		PuhValidation puh = new PuhValidation();
		puh.isvalidUnit("unit", assignedTaskPuh.getUnit(), errors, "errmsg.unitname", true);
		puh.isvalidsfioAs("sfioAs", assignedTaskPuh.getSfioAs(), errors, "errmsg.unitname", true);
		puh.isvalidcreatetask("createTask", assignedTaskPuh.getCreateTask(), errors, "errmsg.unitname", true);
		//puh.isvalidSubtask("Subtask", assignedTaskPuh.getSubtask(), errors, "errmsg.unitname", true);
		puh.isvaliduser("user", assignedTaskPuh.getUser(), errors, "errmsg.unitname", true);

}
	
	
	
public void validatePuh1(AssignedTaskPuh assignedTaskPuh, Errors errors) {

		

		PuhValidation puh = new PuhValidation();
		puh.isvalidUnit("unit", assignedTaskPuh.getUnit(), errors, "errmsg.unitname", true);
		puh.isvalidsfioAs("sfioAs", assignedTaskPuh.getSfioAs(), errors, "errmsg.unitname", true);
		puh.isvalidcreatetask("createTask", assignedTaskPuh.getCreateTask(), errors, "errmsg.unitname", true);
		//puh.isvalidSubtask("Subtask", assignedTaskPuh.getSubtask(), errors, "errmsg.unitname", true);
		///puh.isvalidSubtask("Subtask", assignedTaskPuh.getSubtask(), assignedTaskPuh.getCreateTask(), errors, "errmsg.unitname", true);
		///
		String remark = assignedTaskPuh.getRemark();
		puh.isValidRemarks("remark", assignedTaskPuh.getRemark(), errors, null, true);
		
	puh.isvaliduser("user", assignedTaskPuh.getUser(), errors, "errmsg.unitname", true);
	
		//puh.isvaliduser("user", assignedTaskPuh, errors, "errmsg.unitname", true,tt);
	
	 

		
		

}



public void isValidRemarks(String fieldName, String remarks,
        Errors errors, String errMsg, boolean required) {
	
	
	
	try {
		int number = Integer.parseInt(remarks);
	    System.out.println(number);
	} catch (NumberFormatException e) {
		
	    System.out.println("Invalid number");
	    
	    
	    errors.rejectValue(fieldName, "Invalidnumber", errMsg); 
	}

// required check

	/*
	 * // if not required and empty → skip further validation if (remarks == null ||
	 * remarks.trim().isEmpty()) { return; }
	 * 
	 * String value = remarks.trim();
	 * 
	 * // length check if (value.length() < 3 || value.length() > 100) {
	 * errors.rejectValue(fieldName, "errmsg.length", errMsg); return; }
	 * 
	 * // deny HTML / script tags if (value.matches(".*<[^>]+>.*")) {
	 * errors.rejectValue(fieldName, "errmsg.required", errMsg); return; }
	 * 
	 * // allowed characters check if (!value.matches("^[A-Za-z0-9 .,\\-_/()]+$")) {
	 * errors.rejectValue(fieldName, "errmsg.required", errMsg); }
	 */
}














	public void isvaliduser(String fieldName, UserDetails userDetails, Errors errors, String errMsg, boolean required) {
		 if (userDetails == null ||userDetails.getId()==0){
		        errors.rejectValue(fieldName, "errmsg.required");
		        return;
		      }
	}
	
	public void isvaliduser(String fieldName, AssignedTaskPuh assignedTaskPuh, Errors errors, String errMsg, boolean required,List<AssignedTaskPuh> tt) {
		 
	
		//UserDetails user = assignedTaskPuh.getUser();
		//proCourtCaseDetails currentcasenumbe = assignedTaskPuh.getProCourtCase();
		//String currentcasenumber = currentcasenumbe.getCourtCaseNo();
		String currenttask = assignedTaskPuh.getCreateTask().getTask();
		
		String caseid = null;
		String  task = null;
		
		//List<AssignedTaskPuh> tt = assignedTasksPuhRepository.findAllByUser(user);
		
		
	for (AssignedTaskPuh assignedTaskPuh2 : tt) {
		
	//caseid = assignedTaskPuh2.getProCourtCase().getCourtCaseNo();
	task = assignedTaskPuh2.getCreateTask().getTask();
	
	/*
	 * if (currentcasenumber.equals(caseid) && currenttask.equals(task)) {
	 * errors.rejectValue(fieldName, "errmsg.required1"); break; }
	 */
	
	}
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	public void isvalidSubtask(String fieldName, AddSubTask addSubTask, Errors errors, String errMsg, boolean required) {
		 if (addSubTask == null ||addSubTask.getId()==0){
		        errors.rejectValue(fieldName, "errmsg.required");
		        return;
		      }
	}
	
	public void isvalidUnit(String fieldName, UnitDetails unitDetails, Errors errors, String errMsg, boolean required) {
		 if (unitDetails == null ) {
		        errors.rejectValue(fieldName, "errmsg.required");
		        return;
		      }
		      
	}

	public void isvalidsfioAs(String fieldName, SfioAs sfioAs, Errors errors, String errMsg, boolean required) {
		 if (sfioAs == null ) {
		        errors.rejectValue(fieldName, "errmsg.required");
		        return;
		      }}

	public void isvalidcreatetask(String fieldName, CreateTasks createTasks, Errors errors, String errMsg, boolean required) {
		 if (createTasks == null || createTasks.getId()==0 ) {
		        errors.rejectValue(fieldName, "errmsg.required");
		        return;
		      }}

}
