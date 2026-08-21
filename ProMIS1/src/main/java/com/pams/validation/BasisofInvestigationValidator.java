package com.pams.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.pams.entity.BasisofIO;
import com.pams.service.BasisofIORepository;

@Component
public class BasisofInvestigationValidator {
	@Autowired
	BasisofIORepository BasisofIORepo;
	public void investigationValidator(BasisofIO basisofIO, BindingResult bindingResult)
	{
	
		if (basisofIO.getBasisOfInvestigationOrder().isEmpty()) {
		    bindingResult.rejectValue("basisOfInvestigationOrder", "errmsg.required");
		} else {
		    // Check for duplicates
		    boolean isDuplicate = false;
		    for (BasisofIO existingOrder : BasisofIORepo.findAll()) {
		        if (existingOrder.getBasisOfInvestigationOrder().equals(basisofIO.getBasisOfInvestigationOrder())) {
		            isDuplicate = true;
		            break;
		        }
		    }
		    if (isDuplicate) {
		        bindingResult.rejectValue("basisOfInvestigationOrder", "errmsg.duplicate");
		    } else {
		        // Validate alphabet characters
		        String pattern = "^[a-zA-Z ]+$";
		        if (!basisofIO.getBasisOfInvestigationOrder().matches(pattern)) {
		            bindingResult.rejectValue("basisOfInvestigationOrder", "errmsg.alphabetonly");
		        }
		    }
		}

	}

}
