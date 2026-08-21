package com.pams.validation;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;

import com.pams.entity.AccusedStatusNew;
import com.pams.entity.Charge;

public class AccusedStatusValidation {

	public void isValidAccused(AccusedStatusNew accusedStatusNew, Errors errors) {
		isValidDropDown2("statusCheck",  accusedStatusNew, errors);
		isValidDropDown2("accusedId",  accusedStatusNew, errors);
		isValidDropDown4("chargeName", accusedStatusNew.getChargeName(), errors);
		isValidDropDown("actList",  (int) (long) accusedStatusNew.getAdddActTableId(), errors);
		
		isValidCompPunisReleSection("CompoundabilityText", accusedStatusNew.getCompoundabilityText(), errors, "errMsg.CompoundabilityText", true);
		isValidCompPunisReleSection("relevantText", accusedStatusNew.getRelevantText(), errors, "errMsg.relevantText", true);
		isValidCompPunisReleSection("punishmentText", accusedStatusNew.getPunishmentText(), errors, "errMsg.punishmentText", true);
		//isValidInstanceRemarks("punishmentId", accusedStatusNew.getPunishmentId(), errors, "errmsg.punishmentId", true);
		
		/*
		 * if (accusedStatusNew.getInstanceId() == null) {
		 * errors.rejectValue("InstanceId", "errmsg.required"); } else {
		 * isValidDropDown("instanceId", (int) (long)
		 * accusedStatusNew.getInstanceId().getId(), errors); }
		 */
		//isValidInstanceRemarks("instanceRemarks", accusedStatusNew.getInstanceRemarks(), errors,"errmsg.instanceRemarks", true);
		
		if (accusedStatusNew.getStatusCheck() == 1) {
			isValidDropDown("punishmenttId", accusedStatusNew.getPunishmenttId().getId(), errors);
			isValidDropDown("courtList", (int) (long) accusedStatusNew.getCourtList().getId(), errors);
		} else if (accusedStatusNew.getStatusCheck() == 2) {
			isValidDropDown("discharge", (int) (long) accusedStatusNew.getDischarge().getId(), errors);
			isValidDropDown("courtList", (int) (long) accusedStatusNew.getCourtList().getId(), errors);
		} else if (accusedStatusNew.getStatusCheck() == 3 & accusedStatusNew.isStayed()) {
			
				isValidDropDown("courtList", (int) (long) accusedStatusNew.getCourtList().getId(), errors);
			
		}

		isValidInstanceRemarks("punishmentOrder", accusedStatusNew.getPunishmentOrder(), errors,"errmsg.punishmentOrder", true);
		isValidDate("punishmentDate", accusedStatusNew.getPunishmentDate(), errors);
	}
	public void isValidCompPunisReleSection(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}

		String address = "^[\\w\\d\\s,1-9._:()/-]{2,50}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}
	public void isValidRemarksCheck(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		String address = "^[\\w\\d\\s,1-9._:()/-]{1,500}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidInstanceRemarks(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}

		String address = "^[\\w\\d\\s,1-9._:()/-]{2,50}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public boolean isBlank(String fieldname, String fieldValue) {
		boolean flag = false;
		if (fieldValue == null || "".equals(fieldValue.trim())) {
			flag = true;
		}
		return flag;
	}

	public void isValidDropDown(String fieldName, int fieldValue, Errors errors) {
		if (fieldValue == 0) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	public void isValidDropDown2(String fieldName, AccusedStatusNew accusedStatusNew, Errors errors) {
		if (accusedStatusNew.getAccusedId() == null) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}
	
	
	
	public void isValidTrueFalseInDropDown(String fieldName, boolean fieldValue, Errors errors) {
		if (fieldValue != true & fieldValue != false) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	public void isValidDate(String fieldName, Date fieldValue, Errors errors) {
		if (fieldValue == null)

		{
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}
	
	public void isValidDropDown4(String fieldName, Charge charge, Errors errors) {
		if (charge == null) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}}
	
}
