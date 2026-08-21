package com.pams.validation;

import java.io.IOException;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.pams.entity.Inspector;

public class InspectorValidation {
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 200 MB file size
	public void inspectorValid(Inspector inspector, Errors errors) throws IOException {

		ProMISValidator promisValid = new ProMISValidator();
		MultipartFile multipartFile = inspector.getOrderFile();
		if (inspector.getOrderFile().isEmpty()) {
			promisValid.isValidorderCopyForWithdrawnew("orderFile", multipartFile, errors);
		}
		else
		{
			isValidFile(multipartFile, errors, true, "orderFile");
		}
		/*
		 * if (inspector.getInspectorDesignation().getId() == 0) {
		 * errors.rejectValue("inspectorDesignation", "errmsg.required"); }
		 */

		if (inspector.getInspectorName()==null) {
			errors.rejectValue("inspectorName", "errmsg.required");
		}	
		
		
		
		 
		
		  if (inspector.getOrderDate()==null) 
		  { errors.rejectValue("orderDate", "errmsg.required");		  
		  }
		  
		  if (inspector.getOrderNumber().trim().equals("")) {
		  errors.rejectValue("orderNumber", "errmsg.required");
		  
		  }
		 
	}
	public void isValidFile(MultipartFile file, Errors errors, boolean isRequired, String errFieldName) throws IOException {
		if (isRequired && (file.isEmpty() || file == null)) {
			errors.rejectValue(errFieldName, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(errFieldName, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(errFieldName, "errmsg.invalid.file");
		}
		ProMISValidator promisValid = new ProMISValidator();
		if (!promisValid.isValidFileTikka(errFieldName, file)) {
			errors.rejectValue(errFieldName, "errmsg.maliciousdata");
	} 
	}

}
