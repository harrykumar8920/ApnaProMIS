package com.pams.validation;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.pams.entity.AddCase;
import com.pams.entity.AddCourt;
import com.pams.entity.CaseStatus;
import com.pams.entity.PairaviDetails;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.validation.Valid;


public class CourtCaseValidator {
	private static final Logger logger = LoggerFactory.getLogger(CourtCaseValidator.class);
	   // Regular expressions to detect <script> tags and https URLs
    private static final String SCRIPT_REGEX = "(?i)<script.*?>.*?</script>";
    private static final String ANCHOR_REGEX = "(?i)<a\\s+.*?>.*?</a>";
    private static final String HTTPS_URL_REGEX = "(?i)https://[^\\s]+";
    private static final String ANGLE_BRACKETS_REGEX = ".*[<>].*";

    // Regex pattern for valid field values
   
    private static final String FIELD_VALUE_REGEX = "^[0-9a-zA-Z\\s._/&()||\\-]{1,70}$";


	public void validatecourtCase1(ProCourtCaseDetails proCourtCaseDetails, BindingResult error) throws IOException {

		// String sfioAs = proCourtCaseDetails.getSfioAs().getSfioAs();
		String courtCaseNo = proCourtCaseDetails.getCourtCaseNo();
		// Date fillingDate = proCourtCaseDetails.getFillingDate();
		// Date corrigendumDate = proCourtCaseDetails.getCorrigendumDate();
		String CauseTittle = proCourtCaseDetails.getCauseTitle();
		// String ProSanctionOrder = proCourtCaseDetails.getProSanctionOrder();
		// Date proDate = proCourtCaseDetails.getProDate();
		MultipartFile proSanctionFile = proCourtCaseDetails.getProSanctionFile();
		String CaseNo = proCourtCaseDetails.getCaseId();
		String CaseTittle = proCourtCaseDetails.getCaseTitle();
		// String brief = proCourtCaseDetails.getBrief();
		MultipartFile progistFile = proCourtCaseDetails.getProgistFile();

		ProMISValidator promisValid = new ProMISValidator();
		/*
		 * if(sfioAs.equalsIgnoreCase(null) || sfioAs.equalsIgnoreCase("")) {
		 * error.rejectValue("sfioAs", "errmsg.required"); }
		 */
		// promisValid.isvalidCaseBrife("brief", brief, error, "errmsg.brief", true);
		// promisValid.isvalidCourtNo("courtCaseNo", courtCaseNo, error, true);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		/*
		 * if (fillingDate != null && !fillingDate.equals("")) { boolean isValid =
		 * promisValid.validateDateFormat(sdf.format(fillingDate)); if (!isValid) {
		 * error.rejectValue("fillingDate", "errmsg.toDate"); } } else if (fillingDate
		 * == null || fillingDate.equals("")) { error.rejectValue("fillingDate",
		 * "errmsg.required"); }
		 */

		/*
		 * if (corrigendumDate != null && !corrigendumDate.equals("")) { boolean isValid
		 * = promisValid.validateDateFormat(sdf.format(corrigendumDate)); if (!isValid)
		 * { error.rejectValue("corrigendumDate", "errmsg.toDate"); } } else if
		 * (corrigendumDate == null || corrigendumDate.equals("")) {
		 * error.rejectValue("corrigendumDate", "errmsg.required"); }
		 */

		/*
		 * if (proDate != null && !proDate.equals("")) { boolean isValid =
		 * promisValid.validateDateFormat(sdf.format(proDate)); if (!isValid) {
		 * error.rejectValue("proDate", "errmsg.toDate"); } } else if (proDate == null
		 * || proDate.equals("")) { error.rejectValue("proDate", "errmsg.required"); }
		 */

		/*
		 * if(ProSanctionOrder.equalsIgnoreCase(null) ||
		 * ProSanctionOrder.equalsIgnoreCase("")) {
		 * error.rejectValue("proSanctionOrder", "errmsg.required"); }
		 */

		if (CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {
			error.rejectValue("causeTitle", "errmsg.required");
		}
		else
		{
			isvalidName("causeTitle", CauseTittle, error, "errmsg.required");
		}
		if (CaseTittle.equalsIgnoreCase(null) || CaseTittle.equalsIgnoreCase("")) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CaseTittle, error, "errmsg.required");
		}
		if (proSanctionFile != null) {
			if (proSanctionFile.getSize() > 0 || !proSanctionFile.isEmpty()) {
				promisValid.isValidFile(proSanctionFile, error, true, "proSanctionFile");
				if (!promisValid.isValidFileName(proSanctionFile.getOriginalFilename()))
					error.rejectValue("proSanctionFile", "errmsg.filename");
				if (!promisValid.isValidFileTikka(proSanctionFile.getOriginalFilename(), proSanctionFile))
					error.rejectValue("proSanctionFile", "errmsg.maliciousdata");
			} else {
				error.rejectValue("proSanctionFile", "errmsg.required");
			}
		} else {
			error.rejectValue("proSanctionFile", "errmsg.required");
		}
		if (progistFile != null) {
			if (progistFile.getSize() > 0 || !progistFile.isEmpty()) {
				promisValid.isValidFile(progistFile, error, true, "proSanctionFile");
				if (!promisValid.isValidFileName(progistFile.getOriginalFilename()))
					error.rejectValue("proSanctionFile", "errmsg.filename");
				if (!promisValid.isValidFileTikka(progistFile.getOriginalFilename(), progistFile))
					error.rejectValue("proSanctionFile", "errmsg.maliciousdata");
			} else {
				error.rejectValue("progistFile", "errmsg.required");
			}
		} else {
			error.rejectValue("progistFile", "errmsg.required");
		}
	}

	public void validatecourtCaseRespondent(ProCourtCaseDetails proCourtCaseDetails, BindingResult error)
			throws IOException {

		// String sfioAs = proCourtCaseDetails.getSfioAs().getSfioAs();
		// String courtCaseNo = proCourtCaseDetails.getCourtCaseNo();
		Date fillingDate = proCourtCaseDetails.getFillingDate();
		// String CauseTittle = proCourtCaseDetails.getCauseTitle();
		// String ProSanctionOrder = proCourtCaseDetails.getProSanctionOrder();
		// Date proDate = proCourtCaseDetails.getProDate();
		// MultipartFile proSanctionFile = proCourtCaseDetails.getProSanctionFile();

		// Date corrigendumDate = proCourtCaseDetails.getCorrigendumDate();
		// String CaseNo = proCourtCaseDetails.getCaseId();
		String CaseTittle = proCourtCaseDetails.getCaseTitle();
		// String brief = proCourtCaseDetails.getBrief();
		MultipartFile progistFile = proCourtCaseDetails.getProgistFile();

		MultipartFile backgroundFIle = proCourtCaseDetails.getBackgroundFile();
		MultipartFile brieffile = proCourtCaseDetails.getBriefFile();

		ProMISValidator promisValid = new ProMISValidator();
		if (!backgroundFIle.isEmpty()) {

			promisValid.isValidFile(backgroundFIle, error, true, "backgroundFile");
			if (!promisValid.isValidFileName(backgroundFIle.getOriginalFilename()))
				error.rejectValue("backgroundFile", "errmsg.filename");
			if (!promisValid.isValidFileTikka(backgroundFIle.getOriginalFilename(), backgroundFIle))
				error.rejectValue("backgroundFile", "errmsg.maliciousdata");
		} 
		if (!brieffile.isEmpty()) {

			promisValid.isValidFile(brieffile, error, true, "briefFile");
			if (!promisValid.isValidFileName(brieffile.getOriginalFilename()))
				error.rejectValue("briefFile", "errmsg.filename");
			if (!promisValid.isValidFileTikka(brieffile.getOriginalFilename(), brieffile)) {
				error.rejectValue("briefFile", "errmsg.maliciousdata");
			}
		} 
		/*
		 * if(sfioAs.equalsIgnoreCase(null) || sfioAs.equalsIgnoreCase("")) {
		 * error.rejectValue("sfioAs", "errmsg.required"); }
		 */
		// promisValid.isvalidCaseBrife("brief", brief, error, "errmsg.brief", true);
		// promisValid.isvalidCourtNo("courtCaseNo", courtCaseNo, error, true);

		if (!proCourtCaseDetails.getCourtTypeC().equals("NCLT")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (fillingDate != null && !fillingDate.equals("")) {
				boolean isValid = promisValid.validateDateFormat(sdf.format(fillingDate));
				if (!isValid) {
					error.rejectValue("fillingDate", "errmsg.toDate");
				}
			} else if (fillingDate == null || fillingDate.equals("")) {
				error.rejectValue("fillingDate", "errmsg.required");
			}
		}
		/*
		 * if (corrigendumDate != null && !corrigendumDate.equals("")) { boolean isValid
		 * = promisValid.validateDateFormat(sdf.format(corrigendumDate)); if (!isValid)
		 * { error.rejectValue("corrigendumDate", "errmsg.toDate"); } } else if
		 * (corrigendumDate == null || corrigendumDate.equals("")) {
		 * error.rejectValue("corrigendumDate", "errmsg.required"); }
		 * 
		 */

		/*
		 * if(CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {
		 * error.rejectValue("causeTitle", "errmsg.required"); }
		 */
		if (CaseTittle.equalsIgnoreCase(null) || CaseTittle.equalsIgnoreCase("")) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CaseTittle, error, "errmsg.required");
		}

		/*
		 * if(proSanctionFile!=null) { if(proSanctionFile.getSize() > 0 ||
		 * !proSanctionFile.isEmpty()) { promisValid.isValidFile(proSanctionFile, error,
		 * true,"proSanctionFile");
		 * if(!promisValid.isValidFileName(proSanctionFile.getOriginalFilename()))
		 * error.rejectValue("proSanctionFile", "errmsg.filename");
		 * if(!promisValid.isValidFileTikka(proSanctionFile.getOriginalFilename(),
		 * proSanctionFile)) error.rejectValue("proSanctionFile",
		 * "errmsg.maliciousdata"); }else{ error.rejectValue("proSanctionFile",
		 * "errmsg.required"); } }else { error.rejectValue("proSanctionFile",
		 * "errmsg.required"); }
		 */
		if (proCourtCaseDetails.getId() == null || proCourtCaseDetails.getId() == 0) {
			if (progistFile != null) {
				if (progistFile.getSize() > 0 || !progistFile.isEmpty()) {
					promisValid.isValidFile(progistFile, error, true, "proSanctionFile");
					if (!promisValid.isValidFileName(progistFile.getOriginalFilename()))
						error.rejectValue("progistFile", "errmsg.filename");
					if (!promisValid.isValidFileTikka(progistFile.getOriginalFilename(), progistFile))
						error.rejectValue("progistFile", "errmsg.maliciousdata");
				} else {
					error.rejectValue("progistFile", "errmsg.required");
				}
			} else {
				error.rejectValue("progistFile", "errmsg.required");
			}
		}
	}

	public void validatePairavi(@Valid PairaviDetails pairaviDetails, BindingResult errors) {
		// String Name = pairaviDetails.getName();
		// String email = pairaviDetails.getEmail();
		// String mobile = pairaviDetails.getMobile();
		Date fromDate = pairaviDetails.getFromDate();
		Date toDate = pairaviDetails.getToDate();
		ProMISValidator promisValid = new ProMISValidator();

	}

	public void courtMaterValidate(@Valid AddCourt addCourt, BindingResult error) {
		String CourtName = addCourt.getCourtName();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidCourtName("courtName", CourtName, error, "errmsg.courtName", true);
	}

	public void caseStatusValid(@Valid CaseStatus caseStatus, BindingResult errors) {

		Date date = caseStatus.getCaseStatusDate();
		Long caseStatusId = caseStatus.getCaseStatus().getId();
		Long disposedId = caseStatus.getDisposed().getId();
		ProMISValidator snmsVal = new ProMISValidator();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		if (date != null && !date.equals("")) {
			boolean isValid = snmsVal.validateDateFormat(sdf.format(date));
			if (!isValid) {
				errors.rejectValue("caseStatusDate", "errmsg.toDate");
			}
		} else if (date == null || date.equals("")) {
			errors.rejectValue("caseStatusDate", "errmsg.required");
		}

		if (caseStatus.getCaseStatus().getStatusName().equalsIgnoreCase("Disposed Off")) {
			if (disposedId == 0) {
				errors.rejectValue("disposed", "msg.wrongId");
			}
		}
	}

	public void validatecourtCase(ProCourtCaseDetails proCourtCaseDetails, BindingResult error) throws IOException {

		// String sfioAs = proCourtCaseDetails.getSfioAs().getSfioAs();
		String courtCaseNo = proCourtCaseDetails.getCourtCaseNo();
		// Date fillingDate = proCourtCaseDetails.getFillingDate();
		// Date corrigendumDate = proCourtCaseDetails.getCorrigendumDate();
		// String CauseTittle = proCourtCaseDetails.getCauseTitle();
		// String ProSanctionOrder = proCourtCaseDetails.getProSanctionOrder();
		// Date proDate = proCourtCaseDetails.getProDate();
		// MultipartFile proSanctionFile = proCourtCaseDetails.getProSanctionFile();
		// String CaseNo = proCourtCaseDetails.getCaseId();
		String CaseTittle = proCourtCaseDetails.getCaseTitle();
		// String brief = proCourtCaseDetails.getBrief();
		MultipartFile progistFile = proCourtCaseDetails.getProgistFile();
		MultipartFile backgroundFIle = proCourtCaseDetails.getBackgroundFile();
		MultipartFile brieffile = proCourtCaseDetails.getBriefFile();

		ProMISValidator promisValid = new ProMISValidator();
		if (!backgroundFIle.isEmpty()) {

			promisValid.isValidFile(backgroundFIle, error, true, "backgroundFile");
			if (!promisValid.isValidFileName(backgroundFIle.getOriginalFilename()))
				error.rejectValue("backgroundFile", "errmsg.filename");
			if (!promisValid.isValidFileTikka(backgroundFIle.getOriginalFilename(), backgroundFIle)) {
				error.rejectValue("backgroundFile", "errmsg.maliciousdata");}
		} 
		if (!brieffile.isEmpty()) {

			promisValid.isValidFile(brieffile, error, true, "briefFile");
			if (!promisValid.isValidFileName(brieffile.getOriginalFilename()))
				error.rejectValue("briefFile", "errmsg.filename");
			if (!promisValid.isValidFileTikka(brieffile.getOriginalFilename(), brieffile))
				error.rejectValue("briefFile", "errmsg.maliciousdata");
		} 
		
		
		
		

		/*
		 * if(sfioAs.equalsIgnoreCase(null) || sfioAs.equalsIgnoreCase("")) {
		 * error.rejectValue("sfioAs", "errmsg.required"); }
		 */
		// promisValid.isvalidCaseBrife("brief", brief, error, "errmsg.brief", true);
		// promisValid.isvalidCourtNo("courtCaseNo", courtCaseNo, error, true);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		/*
		 * if (fillingDate != null && !fillingDate.equals("")) { boolean isValid =
		 * promisValid.validateDateFormat(sdf.format(fillingDate)); if (!isValid) {
		 * error.rejectValue("fillingDate", "errmsg.toDate"); } } else if (fillingDate
		 * == null || fillingDate.equals("")) { error.rejectValue("fillingDate",
		 * "errmsg.required"); }
		 */

		/*
		 * if (corrigendumDate != null && !corrigendumDate.equals("")) { boolean isValid
		 * = promisValid.validateDateFormat(sdf.format(corrigendumDate)); if (!isValid)
		 * { error.rejectValue("corrigendumDate", "errmsg.toDate"); } } else if
		 * (corrigendumDate == null || corrigendumDate.equals("")) {
		 * error.rejectValue("corrigendumDate", "errmsg.required"); }
		 */

		/*
		 * if (proDate != null && !proDate.equals("")) { boolean isValid =
		 * promisValid.validateDateFormat(sdf.format(proDate)); if (!isValid) {
		 * error.rejectValue("proDate", "errmsg.toDate"); } } else if (proDate == null
		 * || proDate.equals("")) { error.rejectValue("proDate", "errmsg.required"); }
		 */

		/*
		 * if(ProSanctionOrder.equalsIgnoreCase(null) ||
		 * ProSanctionOrder.equalsIgnoreCase("")) {
		 * error.rejectValue("proSanctionOrder", "errmsg.required"); }
		 */

		/*
		 * if(CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {
		 * error.rejectValue("causeTitle", "errmsg.required"); }
		 */
		if (CaseTittle.equalsIgnoreCase(null) || CaseTittle.equalsIgnoreCase("")) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CaseTittle, error, "errmsg.required");
		}
		/*
		 * if(proSanctionFile!=null) { if(proSanctionFile.getSize() > 0 ||
		 * !proSanctionFile.isEmpty()) { promisValid.isValidFile(proSanctionFile, error,
		 * true,"proSanctionFile");
		 * if(!promisValid.isValidFileName(proSanctionFile.getOriginalFilename()))
		 * error.rejectValue("proSanctionFile", "errmsg.filename");
		 * if(!promisValid.isValidFileTikka(proSanctionFile.getOriginalFilename(),
		 * proSanctionFile)) error.rejectValue("proSanctionFile",
		 * "errmsg.maliciousdata"); }else{ error.rejectValue("proSanctionFile",
		 * "errmsg.required"); } }else { error.rejectValue("proSanctionFile",
		 * "errmsg.required"); }
		 */

		if (proCourtCaseDetails.getId() == null || proCourtCaseDetails.getId() == 0) {
			if (progistFile != null) {
				if (progistFile.getSize() > 0 || !progistFile.isEmpty()) {
					promisValid.isValidFile(progistFile, error, true, "proSanctionFile");
					if (!promisValid.isValidFileName(progistFile.getOriginalFilename()))
						error.rejectValue("progistFile", "errmsg.filename");
					if (!promisValid.isValidFileTikka(progistFile.getOriginalFilename(), progistFile))
						error.rejectValue("progistFile", "errmsg.maliciousdata");
				} else {
					error.rejectValue("progistFile", "errmsg.required");
				}
			} else {
				error.rejectValue("progistFile", "errmsg.required");
			}
		}
	}

	public void addCase(AddCase AddCases, BindingResult error) throws IOException {

		/*
		 * if (AddCases.getTypeOfCase().getId() == 0) { error.rejectValue("typeOfCase",
		 * "errmsg.required"); }
		 */

		if (AddCases.getInvestigationOrderNo().equals("")) {
			error.rejectValue("investigationOrderNo", "errmsg.required");
		}
		else
		{
			isvalidName("investigationOrderNo", AddCases.getInvestigationOrderNo(), error, "errmsg.required");
		}

		Date fillingDate = AddCases.getProSanctionDate();
		String proSectionOrderNumber = AddCases.getProSectionOrderNumber();
		String CauseTittle1 = AddCases.getCaseTitle();
		// MultipartFile proSanctionFile = AddCases.getProsectionSanctionOrderFile();

		ProMISValidator promisValid = new ProMISValidator();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (fillingDate != null && !fillingDate.equals("")) {
			boolean isValid = promisValid.validateDateFormat(sdf.format(fillingDate));
			if (!isValid) {
				error.rejectValue("proSanctionDate", "errmsg.toDate");
			}
		} else if (fillingDate == null || fillingDate.equals("")) {
			error.rejectValue("proSanctionDate", "errmsg.required");
		}

		// if(CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {

		if (proSectionOrderNumber == null || proSectionOrderNumber.isEmpty()) {
			error.rejectValue("proSectionOrderNumber", "errmsg.required");
		}
		
		else
		{
			isvalidName("proSectionOrderNumber", proSectionOrderNumber, error, "errmsg.required");
		}

		if (CauseTittle1 == null || CauseTittle1.isEmpty()) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CauseTittle1, error, "errmsg.required");
		}

		/*
		 * if(proSanctionFile!=null) { if(proSanctionFile.getSize() > 0 ||
		 * !proSanctionFile.isEmpty()) { promisValid.isValidFile(proSanctionFile, error,
		 * true,"prosectionSanctionOrderFile");
		 * if(!promisValid.isValidFileName(proSanctionFile.getOriginalFilename()))
		 * error.rejectValue("prosectionSanctionOrderFile", "errmsg.filename");
		 * if(!promisValid.isValidFileTikka(proSanctionFile.getOriginalFilename(),
		 * proSanctionFile)) error.rejectValue("prosectionSanctionOrderFile",
		 * "errmsg.maliciousdata"); }else{
		 * error.rejectValue("prosectionSanctionOrderFile", "errmsg.required"); } }else
		 * { error.rejectValue("prosectionSanctionOrderFile", "errmsg.required"); }
		 */

	}

	public void addCase3(AddCase AddCases, BindingResult error) throws IOException {

		/*
		 * if (AddCases.getTypeOfCase().getId() == 0) { error.rejectValue("typeOfCase",
		 * "errmsg.required"); }
		 */

		if (AddCases.getInvestigationOrderNo().equals("")) {
			error.rejectValue("investigationOrderNo", "errmsg.required");
		}
		else
		{
			isvalidName("investigationOrderNo", AddCases.getInvestigationOrderNo(), error, "errmsg.required");
		}

		// Date fillingDate = AddCases.getProSanctionDate();
		// proSectionOrderNumber = AddCases.getProSectionOrderNumber();
		String CauseTittle1 = AddCases.getCaseTitle();
		// MultipartFile proSanctionFile = AddCases.getProsectionSanctionOrderFile();

		ProMISValidator promisValid = new ProMISValidator();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		/*
		 * if (fillingDate != null && !fillingDate.equals("")) { boolean isValid =
		 * promisValid.validateDateFormat(sdf.format(fillingDate)); if (!isValid) {
		 * error.rejectValue("proSanctionDate", "errmsg.toDate"); } } else if
		 * (fillingDate == null || fillingDate.equals("")) {
		 * error.rejectValue("proSanctionDate", "errmsg.required"); }
		 */

		// if(CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {

		/*
		 * if (proSectionOrderNumber == null || proSectionOrderNumber.isEmpty()) {
		 * error.rejectValue("proSectionOrderNumber", "errmsg.required"); }
		 */
		if (CauseTittle1 == null || CauseTittle1.isEmpty()) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CauseTittle1, error, "errmsg.required");
		}
		

		/*
		 * if(proSanctionFile!=null) { if(proSanctionFile.getSize() > 0 ||
		 * !proSanctionFile.isEmpty()) { promisValid.isValidFile(proSanctionFile, error,
		 * true,"prosectionSanctionOrderFile");
		 * if(!promisValid.isValidFileName(proSanctionFile.getOriginalFilename()))
		 * error.rejectValue("prosectionSanctionOrderFile", "errmsg.filename");
		 * if(!promisValid.isValidFileTikka(proSanctionFile.getOriginalFilename(),
		 * proSanctionFile)) error.rejectValue("prosectionSanctionOrderFile",
		 * "errmsg.maliciousdata"); }else{
		 * error.rejectValue("prosectionSanctionOrderFile", "errmsg.required"); } }else
		 * { error.rejectValue("prosectionSanctionOrderFile", "errmsg.required"); }
		 */

	}

	public void addCase1(AddCase AddCases, BindingResult error) throws IOException {
		/*
		 * if (AddCases.getTypeOfCase().getId() == 0) { error.rejectValue("typeOfCase",
		 * "errmsg.required"); }
		 */

		if (AddCases.getInvestigationOrderNo().equals("")) {
			error.rejectValue("investigationOrderNo", "errmsg.required");
		}
		else
		{
			isvalidName("investigationOrderNo", AddCases.getInvestigationOrderNo(), error, "errmsg.required");
		}
		Date fillingDate = AddCases.getProSanctionDate();
		String CauseTittle = AddCases.getProSectionOrderNumber();
		String CauseTittle1 = AddCases.getCaseTitle();
		// MultipartFile proSanctionFile = AddCases.getProsectionSanctionOrderFile();

		ProMISValidator promisValid = new ProMISValidator();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (fillingDate != null && !fillingDate.equals("")) {
			boolean isValid = promisValid.validateDateFormat(sdf.format(fillingDate));
			if (!isValid) {
				error.rejectValue("proSanctionDate", "errmsg.toDate");
			}
		} else if (fillingDate == null || fillingDate.equals("")) {
			error.rejectValue("proSanctionDate", "errmsg.required");
		}

		if (CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {
			error.rejectValue("proSectionOrderNumber", "errmsg.required");
		}
		else
		{
			isvalidName("proSectionOrderNumber", CauseTittle, error, "errmsg.required");
		}
		
		if (CauseTittle1.equalsIgnoreCase(null) || CauseTittle1.equalsIgnoreCase("")) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CauseTittle1, error, "errmsg.required");
		}

		if (AddCases.getCourtType().getId() == 0l) {
			error.rejectValue("courtType", "errmsg.required");
		}
		if (AddCases.getState().getId() == 0L) {
			error.rejectValue("state", "errmsg.required");
		}
		if (AddCases.getCity().getId() == 0L) {
			error.rejectValue("city", "errmsg.required");
		}
		if (AddCases.getIsAccused() == null) {
			error.rejectValue("isAccused", "errmsg.required");
		}
		if (AddCases.getPetionerName().isEmpty()) {
			error.rejectValue("petionerName", "errmsg.required");
		}
		else
		{
			isvalidName("petionerName", AddCases.getPetionerName(), error, "errmsg.required");
		}

	}

	public void addCaseNCLT(AddCase AddCases, BindingResult error) throws IOException {

		if (AddCases.getBenchName().getId() == 0) {
			error.rejectValue("benchName", "errmsg.required");
		}

		if (AddCases.getCourtType().getId() == 0l) {
			error.rejectValue("courtType", "errmsg.required");
		}
		/*
		 * if(AddCases.getBenchName().equals("Choose")) { error.rejectValue("benchName",
		 * "errmsg.required"); }
		 */
		/*
		 * if (AddCases.getTypeOfCase().getId() == 0) { error.rejectValue("typeOfCase",
		 * "errmsg.required"); }
		 */

		if (AddCases.getInvestigationOrderNo().equals("")) {
			error.rejectValue("investigationOrderNo", "errmsg.required");
		}

		Date fillingDate = AddCases.getProSanctionDate();
		String CauseTittle = AddCases.getProSectionOrderNumber();
		String CauseTittle1 = AddCases.getCaseTitle();
		// MultipartFile proSanctionFile = AddCases.getProsectionSanctionOrderFile();

		ProMISValidator promisValid = new ProMISValidator();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (fillingDate != null && !fillingDate.equals("")) {
			boolean isValid = promisValid.validateDateFormat(sdf.format(fillingDate));
			if (!isValid) {
				error.rejectValue("proSanctionDate", "errmsg.toDate");
			}
		} else if (fillingDate == null || fillingDate.equals("")) {
			error.rejectValue("proSanctionDate", "errmsg.required");
		}

		if (CauseTittle.equalsIgnoreCase(null) || CauseTittle.equalsIgnoreCase("")) {
			error.rejectValue("proSectionOrderNumber", "errmsg.required");
		}
		else
		{
			isvalidName("proSectionOrderNumber", CauseTittle, error, "errmsg.required");
		}
		if (CauseTittle1.equalsIgnoreCase(null) || CauseTittle1.equalsIgnoreCase("")) {
			error.rejectValue("caseTitle", "errmsg.required");
		}
		else
		{
			isvalidName("caseTitle", CauseTittle1, error, "errmsg.required");
		}

		/*
		 * if (AddCases.getCourtType().getId()==0l) { error.rejectValue("courtType",
		 * "errmsg.required"); } if (AddCases.getState().getId() == 0L) {
		 * error.rejectValue("state", "errmsg.required"); } if
		 * (AddCases.getCity().getId() == 0L) { error.rejectValue("city",
		 * "errmsg.required"); }
		 */
		if (AddCases.getIsAccused() == null) {
			error.rejectValue("isAccused", "errmsg.required");
		}
		if (AddCases.getPetionerName().isEmpty()) {
			error.rejectValue("petionerName", "errmsg.required");
		}
		else
		{
			isvalidName("petionerName", AddCases.getPetionerName(), error, "errmsg.required");
		}

	}
	public void addCinNumber(AddCase AddCases, BindingResult error) throws IOException {
		String cinNumber = AddCases.getCinNumber();
		isValidCin("cinNumber", cinNumber,error);
	}
	public void checkCourtCaseNumber(AddCase AddCases, BindingResult error) throws IOException {
		String courtCase = AddCases.getCourtCaseNumber();
		isAdvanceNotice("courtCaseNumber", courtCase,error);
	}
	public void checkAdvanceNotice(AddCase AddCases, BindingResult error) throws IOException {
		String advanceNotice = AddCases.getAdvanceNotice();
		isAdvanceNotice("advanceNotice", advanceNotice,error);
	}
	public void checkCompanyName(AddCase AddCases, BindingResult error) throws IOException {
		String company = AddCases.getCaseTitle();
		isCaseTitle("caseTitle", company,error);
	}
	public void isCaseTitle(String fieldName, String fieldValue, Errors errors) {
	    if (fieldValue == null || fieldValue.trim().isEmpty() || "null".equalsIgnoreCase(fieldValue.trim())) {
	        errors.rejectValue(fieldName, "errmsg.required");
	        return;
	    }

	    String regex = "^[\\w\\d\\s.,_()/-]{2,70}$";
	    if (!fieldValue.matches(regex)) {
	        errors.rejectValue(fieldName, "errmsg.caseTitle");
	    }
	
	}
	public static void iscourtCaseNumber(String fieldName, String fieldValue, BindingResult errors) {
	    if (fieldValue == null || fieldValue.trim().isEmpty() || "null".equalsIgnoreCase(fieldValue.trim())) {
	        errors.rejectValue(fieldName, "errmsg.required");
	        return;
	    }
	    String cinRegex = "^[UL][0-9]{5}[A-Z]{2}[0-9]{4}[A-Z]{3}[0-9]{6}$";
	    if (!fieldValue.toUpperCase().matches(cinRegex)) {
	        errors.rejectValue(fieldName, "errmsg.courtCaseNumber");
	    }
	}
	public void isAdvanceNotice(String fieldName, String fieldValue, Errors errors) {
	    if (fieldValue == null || fieldValue.trim().isEmpty() || "null".equalsIgnoreCase(fieldValue.trim())) {
	        errors.rejectValue(fieldName, "errmsg.required");
	        return;
	    }

	    String regex = "^[\\w\\d\\s.,_()/-]{2,70}$";
	    if (!fieldValue.matches(regex)) {
	        errors.rejectValue(fieldName, "errmsg.advanceNotice");
	    }
	}

	
	
	
	
	
	public static void isValidCin(String fieldName, String fieldValue, BindingResult errors) {
	    if (fieldValue == null || fieldValue.trim().isEmpty() || "null".equalsIgnoreCase(fieldValue.trim())) {
	        errors.rejectValue(fieldName, "errmsg.required");
	        return;
	    }

	    String cinRegex = "^[UL][0-9]{5}[A-Z]{2}[0-9]{4}[A-Z]{3}[0-9]{6}$";

	    if (!fieldValue.toUpperCase().matches(cinRegex)) {
	        errors.rejectValue(fieldName, "errmsg.invalidCin");
	    }
	}
	public void addCasefile(AddCase AddCases, BindingResult error) throws IOException {

		MultipartFile proSanctionFile = AddCases.getProsectionSanctionOrderFile();

		ProMISValidator promisValid = new ProMISValidator();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		if (proSanctionFile != null) {
			if (proSanctionFile.getSize() > 0 || !proSanctionFile.isEmpty()) {
				promisValid.isValidFile(proSanctionFile, error, true, "prosectionSanctionOrderFile");
				if (!promisValid.isValidFileName(proSanctionFile.getOriginalFilename()))
					error.rejectValue("prosectionSanctionOrderFile", "errmsg.filename");
				if (!promisValid.isValidFileTikka(proSanctionFile.getOriginalFilename(), proSanctionFile))
					error.rejectValue("prosectionSanctionOrderFile", "errmsg.maliciousdata");
			} else {
				error.rejectValue("prosectionSanctionOrderFile", "errmsg.required");
			}
		} else {
			error.rejectValue("prosectionSanctionOrderFile", "errmsg.required");
		}

	}
	
	
	

	
	  public static void isvalidName(String fieldName, String fieldValue, BindingResult errors, String errMsg) {
	        if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
	            errors.rejectValue(fieldName, "errmsg.required");
	            return;
	        }

	        // Check for malicious patterns
	        if (containsMaliciousContent(fieldValue)) {
	            errors.rejectValue(fieldName, "errMsg.malicious");
	            return;
	        }

	        // Validate against the field value pattern
	        Pattern pattern = Pattern.compile(FIELD_VALUE_REGEX);
	        Matcher matcher = pattern.matcher(fieldValue);
	        if (!matcher.matches()) {
	            errors.rejectValue(fieldName, errMsg);
	        }
	    }

	    private static boolean containsMaliciousContent(String fieldValue) {
	        String lowerCaseValue = fieldValue.toLowerCase();
	        return lowerCaseValue.matches(SCRIPT_REGEX) || 
	               lowerCaseValue.matches(ANCHOR_REGEX) || 
	               lowerCaseValue.matches(HTTPS_URL_REGEX) || 
	               lowerCaseValue.matches(ANGLE_BRACKETS_REGEX);
	    }
	
	
	

}
