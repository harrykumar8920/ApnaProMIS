package com.pams.validation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.validation.Errors;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AddCourt;
import com.pams.entity.AddSubSec;
import com.pams.entity.AddUnitlocation;
import com.pams.entity.CreateTasks;
import com.pams.entity.Status;
import com.pams.entity.UnitDetails;
import com.pams.entity.UserDetails;

import jakarta.validation.Valid;


public class UserValidation {
	
public void validateCreatetasks(@Valid CreateTasks createtasks, Errors errors) {
		
		String task = createtasks.getTask();
		ProMISValidator taskVal = new ProMISValidator();
		taskVal.isvalidTask( task, errors);
		

	}
	
public void validateAddCourt(@Valid AddCourt addCourt, Errors errors) {
		
		String courtName = addCourt.getCourtName();
		
		ProMISValidator snmsVal = new ProMISValidator();

		snmsVal.isvalidCourt("courtName", courtName, errors, "errmsg.courtName1", true);
		

	}
public void validateStatus(@Valid Status status, Errors errors) {
	String statusName = status.getStatusName();
	ProMISValidator snmsVal = new ProMISValidator();
	snmsVal.isvalidStatusName("statusName", statusName, errors, "errmsg.StatusName", true);
}

	public void validateUserRegComplete(UserDetails userRegistration, Errors errors, boolean isUniqueUser,
			boolean isUniquePrimaryMob, boolean isUniqueAlternateMob) {

		String salutation = userRegistration.getSalutation();
		String email = userRegistration.getEmail();
		String primaryMobile = userRegistration.getPrimaryMobile();
		String alternateNo = userRegistration.getAlternateNo();

		String firstName = userRegistration.getFirstName();
		String middleName = userRegistration.getMiddleName();
		String lastName = userRegistration.getLastName();

		Date uiDob = userRegistration.getUiDob();
		String sfioEmpId = userRegistration.getSfioEmpId();
		//Long designationId = userRegistration.getDesignationId();
		Date uiJoiningDate = userRegistration.getUiJoiningDate();
		Long unitId = userRegistration.getUnitId();

		Long roleId = userRegistration.getRoleId();

		ProMISValidator snmsVal = new ProMISValidator();

		snmsVal.isvalidSalutation("salutation", salutation, errors, "errmsg.salutation", true);
		snmsVal.isvalidPersonName("firstName", firstName, errors, "errmsg.fnames", true);
		snmsVal.isvalidPersonName("middleName", middleName, errors, "errmsg.mnames", false);
		snmsVal.isvalidPersonName("lastName", lastName, errors, "errmsg.lnames", false);
		snmsVal.isValidMobile("primaryMobile", primaryMobile, errors);
		if (!snmsVal.isBlank("alternateNo", alternateNo))
			snmsVal.isValidMobile("alternateNo", alternateNo, errors);
		snmsVal.isValidEmail("email", email, errors, true);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (uiDob != null && !uiDob.equals("")) {
			boolean isValid = snmsVal.validateDateFormat(sdf.format(uiDob));
			if (!isValid) {
				errors.rejectValue("uiDob", "errmsg.toDate");
			}
		} else if (uiDob == null || uiDob.equals("")) {
			errors.rejectValue("uiDob", "errmsg.required");
		}
		snmsVal.isvalidRegnum("sfioEmpId", sfioEmpId, errors, "errmsg.engage.reg", false);

		if (uiJoiningDate != null && !uiJoiningDate.equals("")) {
			boolean isValid = snmsVal.validateDateFormat(sdf.format(uiJoiningDate));
			if (!isValid) {
				errors.rejectValue("uiJoiningDate", "errmsg.toDate");
			}
		} else if (uiJoiningDate == null || uiJoiningDate.equals("")) {
			errors.rejectValue("uiJoiningDate", "errmsg.required");
		}

		if (!isUniqueUser)
			errors.rejectValue("email", "errmsg.uniqueUser");

		if (!isUniquePrimaryMob)
			errors.rejectValue("primaryMobile", "errmsg.uniqueMobile");

		if (!isUniqueAlternateMob)
			errors.rejectValue("alternateNo", "errmsg.uniqueMobile");

		//snmsVal.isValidDropDown("designation", Integer.parseInt(designationId.toString()), errors);
		snmsVal.isValidDropDown("unit", Integer.parseInt(unitId.toString()), errors);
		snmsVal.isValidDropDown("roleId", Integer.parseInt(roleId.toString()), errors);
	}

	public void validateUnitDetails(UnitDetails unitDetails, Errors errors) {
		String unitName = unitDetails.getUnitName();
		Long location = null;
		if (unitDetails.getLocation()!=null||unitDetails.getLocation().getId()!=0)
		{
			location = unitDetails.getLocation().getId();
		}
		String address = unitDetails.getAddress();
		String telephoneNo = unitDetails.getTelephoneNo();
		String faxNo = unitDetails.getFaxNo();
		String eMail = unitDetails.getEMail();
		ProMISValidator snmsVal = new ProMISValidator();
		snmsVal.isvalidUnit("unitName", unitName, errors, "errmsg.unitname", true);
		snmsVal.isValidDropDown("location", location.intValue(), errors);
		snmsVal.isvalidUserAddress("address", address, errors, "errmsg.fname", true);
		snmsVal.isValidFirmPhone("telephoneNo", telephoneNo, errors);
		snmsVal.isValidFax("faxNo", faxNo, errors);
		snmsVal.isValidEmail("eMail", eMail, errors, false);
	}

	public void validatelocationDetails(@Valid AddUnitlocation locationDetails, Errors errors) {
		String location = locationDetails.getLocation();
		String locAddress = locationDetails.getLocAddress();
		ProMISValidator snmsVal = new ProMISValidator();
		snmsVal.isvalidCompany("location", location, errors, "errmsg.location", true);
		snmsVal.isvalidUserAddress("locAddress", locAddress, errors, "errmsg.fname", true);
	}


	public void validateActandSections(@Valid AddActSec addActSec, Errors errors) {
		//String location = locationDetails.getLocation();
		String Section = addActSec.getSection();
		ProMISValidator snmsVal = new ProMISValidator();
		snmsVal.isvalidUserAddress("Section", Section, errors, "errmsg.fname", true);
	}

	public void validateAct(@Valid AddAct addAct, Errors errors) {
		//String location = locationDetails.getLocation();
		String act = addAct.getAct();
		ProMISValidator snmsVal = new ProMISValidator();
		snmsVal.isvalidUserAddress("act", act, errors, "errmsg.fname", true);
	}
	
	public void validateSubSections(@Valid AddSubSec addSubSec, Errors errors) {
		String SubSection = addSubSec.getSubSection();
		ProMISValidator snmsVal = new ProMISValidator();
		snmsVal.isvalidUserAddress("SubSection", SubSection, errors, "errmsg.fname", true);
	}
	
	
	
}
