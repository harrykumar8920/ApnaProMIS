package com.pams.validation;

import org.springframework.validation.BindingResult;

import com.pams.entity.AccusedCompCaseDtl;
import com.pams.entity.AccusedResponse;

public class AccusedCompValidation {

	public void accusedValidation(AccusedCompCaseDtl accusedCompCaseDtl, BindingResult errors) {
		//String accusedName = accusedCompCaseDtl.getAccusedDto().getAccusedMaster().getAccusedName();
		//String panNumber = accusedCompCaseDtl.getAccusedDto().getAccusedMaster().getPanNumber();
		ProMISValidator promisValid = new ProMISValidator();
		//promisValid.isvalidPersonName("AccusedDto.accusedMaster.accusedName", accusedName, errors, "errmsg.fnames",		true);
		//promisValid.isValidpanNumber("AccusedDto.accusedMaster.panNumber", panNumber, errors, "errmsg.pan", true);
	}

	public void companydtls(AccusedCompCaseDtl accusedCompCaseDtl, BindingResult errors) {
		String compName = accusedCompCaseDtl.getCompanyDto().getCompany().getCompanyName();
		String cin = accusedCompCaseDtl.getCompanyDto().getCompany().getCin();
		String address = accusedCompCaseDtl.getCompanyDto().getCompany().getAddress();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidCompany("companyDto.company.companyName", compName, errors, "errmsg.company", true);
		promisValid.isvalidCIN("companyDto.company.cin", cin, errors, true);
		promisValid.isvalidUserAddress("companyDto.company.address", address, errors, "errmsg.address", true);
	}

	public void accusedResponse(AccusedResponse accusedResponse, BindingResult errors) {

		if (accusedResponse.getState().getId() == 0L) {
			errors.rejectValue("state", "msg.wrongId");
		}
		if (accusedResponse.getCity().getId() == 0L) {
			errors.rejectValue("city", "msg.wrongId");
		}

		if (accusedResponse.getTypeofResponse().getId() == 0L) {
			errors.rejectValue("typeofResponse", "msg.wrongId");
		}

		if (accusedResponse.getOrderType().equals("0")) {
			errors.rejectValue("orderType", "msg.wrongId");
		}
		if (accusedResponse.getCourtType().getId() == 0l) {
			errors.rejectValue("courtType", "msg.wrongId");
		}

		if (accusedResponse.getApplicationNumber() == null || accusedResponse.getApplicationNumber().equals("")) {
			errors.rejectValue("applicationNumber", "msg.wrongId");
		}

		if (accusedResponse.getOrderDate() == null)

		{
			errors.rejectValue("orderDate", "errmsg.required");

		}

		if (accusedResponse.getReplyfiled() == true) {
			if (accusedResponse.getReplyFiledDate() == null){
				errors.rejectValue("replyFiledDate", "errmsg.required");
			}
			if (accusedResponse.getReplyFiledOrder().isEmpty()) {
				errors.rejectValue("replyFiledOrder", "errmsg.required");
			}
		}
		if (accusedResponse.getDateOfApplication() == null) {
			errors.rejectValue("dateOfApplication", "errmsg.required");
		}
		if (accusedResponse.getApplicationOrderFile().isEmpty()) {
			errors.rejectValue("applicationOrderFile", "errmsg.required");
		}

		if (accusedResponse.getOrderFile().isEmpty()) {
			errors.rejectValue("orderFile", "errmsg.required");
		}

	}
}
