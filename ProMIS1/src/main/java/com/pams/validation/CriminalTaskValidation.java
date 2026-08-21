package com.pams.validation;

import java.io.IOException;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.PairaviOfficer;

import jakarta.validation.Valid;

public class CriminalTaskValidation {
	
	
	public void hearingDescription(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException {
	
		String briefHD = criminalTaskDto.getBriefHD();
	
	String[] words = briefHD.trim().split("\\s+");
    
	int length = words.length;
	if(length>100) {
		
		errors.rejectValue("briefHD", "errors.briefHD");
	}
	
	}
	public void courtCaseNoValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException {
		String courtCaseName = criminalTaskDto.getCourtCaseName();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isValidCourtCase("courtCaseName", courtCaseName, errors, "errmsg.fnames", true);
		MultipartFile multipartFile = criminalTaskDto.getCourtCaseDtlFile();
		promisValid.isValidorderCopyForWithdrawnew("courtCaseDtlFile", multipartFile, errors);
	}
	public void courtCaseNoValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) throws IOException {
		String courtCaseName = criminalTaskDto.getCourtCaseName();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isValidCourtCase("courtCaseName", courtCaseName, errors, "errmsg.fnames", true);
		MultipartFile multipartFile = criminalTaskDto.getCourtCaseDtlFile();
		promisValid.isValidorderCopyForWithdrawnew("courtCaseDtlFile", multipartFile, errors);
	}
	
	public void companyPerformaPartyValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) {
		String cin = criminalTaskDto.getPPCompCin();
		String compName = criminalTaskDto.getPPCompany();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidCIN("pPCompCin", cin, errors, true);
		promisValid.isvalidcoyname("pPCompany", compName, errors, true);
	}
	public void companyPerformaPartyValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) {
		String cin = criminalTaskDto.getPPCompCin();
		String compName = criminalTaskDto.getPPCompany();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidCIN("pPCompCin", cin, errors, true);
		promisValid.isvalidcoyname("pPCompany", compName, errors, true);
	}
	


	public void complaintValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) {

		String complaintName = criminalTaskDto.getComplanitName();
		String complanitEmail = criminalTaskDto.getComplanitEmail();
		String complaintMobile = criminalTaskDto.getComplaintMobile();
		String complaintPetioner = criminalTaskDto.getComplaintPetinoner();
		// String complaintPetinoner = criminalTaskDto.getComplaintPetinoner();
		String ioname = criminalTaskDto.getIOName();

		// testcommit testing

		ProMISValidator promisValid = new ProMISValidator();

		promisValid.isvalidPersonName("complanitName", complaintName, errors, "errmsg.complaintName", true);
		promisValid.isvalidPersonName("IOName", ioname, errors, "errmsg.complaintName", true);

		promisValid.isValidEmail("complanitEmail", complanitEmail, errors, true);
		promisValid.isValidMobile("complaintMobile", complaintMobile, errors);

		// promisValid.isValidpetitionNumber("complaintPetinoner", complaintPetinoner,
		// errors, true);

		promisValid.isValidComplanitdesignation("complanitdesignation", criminalTaskDto, errors);

		promisValid.isValidControlingofficer("pairavidesignation", criminalTaskDto, errors);

	}
	public void complaintValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) {

		String complaintName = criminalTaskDto.getComplanitName();
		String complanitEmail = criminalTaskDto.getComplanitEmail();
		String complaintMobile = criminalTaskDto.getComplaintMobile();
		String complaintPetioner = criminalTaskDto.getComplaintPetinoner();
		// String complaintPetinoner = criminalTaskDto.getComplaintPetinoner();
		String ioname = criminalTaskDto.getIOName();

		// testcommit testing

		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidcomplaintPetioner("complaintPetinoner", complaintPetioner, errors, "errmsg.complaintPetioner", true);
		promisValid.isvalidPersonName("complanitName", complaintName, errors, "errmsg.fnames", true);
		promisValid.isvalidPersonName("IOName", ioname, errors, "errmsg.fnames", true);

		promisValid.isValidEmail("complanitEmail", complanitEmail, errors, true);
		promisValid.isValidMobile("complaintMobile", complaintMobile, errors);

		// promisValid.isValidpetitionNumber("complaintPetinoner", complaintPetinoner,
		// errors, true);

		promisValid.isValidComplanitdesignation1("complanitdesignation", criminalTaskDto, errors);

		promisValid.isValidControlingofficer1("pairavidesignation", criminalTaskDto, errors);

	}

	public void addtionalFileUploadValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{
		String companyname=criminalTaskDto.getCompanyName();
		String accusedName=criminalTaskDto.getAccusedNameForFileUpload();
		MultipartFile file=criminalTaskDto.getFile1();

		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isValidCompanyName("companyName", companyname, errors, "errmsg.fnames", true);
		promisValid.isValidAccusedName("accusedName", accusedName, errors, "errmsg.fnames", true);
		promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		//promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		
			
			promisValid.isValidorderCopyForWithdraw("file1", file, errors);
	
		
		

	}
	public void addtionalFileUploadValidationID(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{
		//String companyname=criminalTaskDto.getCompanyName();
		String accusedName=criminalTaskDto.getAccusedNameForFileUpload();
		
	MultipartFile file=criminalTaskDto.getFile1();

		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		//promisValid.isValidCompanyName("companyName", companyname, errors, "errmsg.fnames", true);
		//promisValid.isValidAccusedName("accusedName", accusedName, errors, "errmsg.fnames", true);
		promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		//promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		
			
			promisValid.isValidorderCopyForWithdraw("file1", file, errors);
				
		

	}
	

	public void addtionalFileUploadValidationCompany(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{
		//String companyname=criminalTaskDto.getCompanyName();
		//String accusedName=criminalTaskDto.getAccusedNameForFileUpload();
		MultipartFile file=criminalTaskDto.getFile1();

		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isValidCompanyName("companyName", companyname, errors, "errmsg.fnames", true);
		//promisValid.isValidAccusedName("accusedName", accusedName, errors, "errmsg.fnames", true);
		promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		//promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
	
			
			promisValid.isValidorderCopyForWithdraw("file1", file, errors);
		
		
		

	}
	public void addtionalFileUploadValidationCompany1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) throws IOException

	{
		//String companyname=criminalTaskDto.getCompanyName();
		//String accusedName=criminalTaskDto.getAccusedNameForFileUpload();
		
		
		
		MultipartFile multipartFile=criminalTaskDto.getFile1();

		ProMISValidator promisValid = new ProMISValidator();

		
		//promisValid.isValidCompanyName("companyName", companyname, errors, "errmsg.fnames", true);
		//promisValid.isValidAccusedName("accusedName", accusedName, errors, "errmsg.fnames", true);
		//promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		//promisValid.isValidCompanyID("compID123", criminalTaskDto, errors);
		//for (MultipartFile multipartFile : file) {
			
			promisValid.isValidorderCopyForWithdraw("file1", multipartFile, errors);
		}
		
		

	

	
	/*
	 * public void forFileSave(@Valid MultipartFile file, Errors errors) throws
	 * IOException
	 * 
	 * {
	 * 
	 * ProMISValidator promisValid = new ProMISValidator();
	 * 
	 * promisValid.isValidorderCopyForWithdraw("file1", file, errors);
	 * 
	 * }
	 */

	public void companyValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) {
		String cin = criminalTaskDto.getCin();
		String compName = criminalTaskDto.getCompName();
		String compAddess = criminalTaskDto.getCompAddess();

		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidCIN("cin", cin, errors, true);
		promisValid.isvalidcoyname("compName", compName, errors, true);
		// promisValid.isvalidAddrLine7("compAddess", compAddess, errors, true);

	}
	public void companyValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) {
		String cin = criminalTaskDto.getCin();
		String compName = criminalTaskDto.getCompName();
		String compAddess = criminalTaskDto.getCompAddess();

		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidCIN("cin", cin, errors, true);
		promisValid.isvalidcoyname("compName", compName, errors, true);
		// promisValid.isvalidAddrLine7("compAddess", compAddess, errors, true);

	}

	public void pairaviOfficerEarliar(@Valid CriminalTaskDto criminalTaskDto, Errors errors)

	{

		PairaviOfficer pOff = criminalTaskDto.getPairaviOfficer();
		if(pOff==null || pOff.getId()==0) {
			errors.rejectValue("pairaviOfficer", "errmsg.required");
		}
		//String pairaviName = criminalTaskDto.getPairaviName();
		//String pairaviemail = criminalTaskDto.getPairaviemail();
		//AddDesignation pairavidesignation = criminalTaskDto.getPairavidesignation();
		Date pairavifromDate = criminalTaskDto.getPairavifromDate();

		Date pairavitoDate = criminalTaskDto.getPairavitoDate();

		String pairaviMobile = criminalTaskDto.getPairaviMobile();
		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isValidpairavidesination("pairavidesignation", criminalTaskDto, errors);
		promisValid.isValidpairaviofficer("PairavidetailsType", criminalTaskDto, errors);

		//promisValid.isvalidPersonName("pairaviName", pairaviName, errors, "errmsg.fnames", true);
		//promisValid.isValidEmail("pairaviemail", pairaviemail, errors, true);
		//promisValid.isValidMobile("pairaviMobile", pairaviMobile, errors);

		promisValid.isvaliddate("pairavifromDate", pairavifromDate, errors);
		promisValid.isvaliddate("pairavitoDate", pairavitoDate, errors);

	}
	
	
	
	public void pairaviOfficerEarliar1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		//String pairaviName = criminalTaskDto.getPairaviName();
		//String pairaviemail = criminalTaskDto.getPairaviemail();
		//AddDesignation pairavidesignation = criminalTaskDto.getPairavidesignation();
		PairaviOfficer pOff = criminalTaskDto.getPairaviOfficer();
		if(pOff==null || pOff.getId()==0) {
			errors.rejectValue("pairaviOfficer", "errmsg.required");
		}
		Date pairavifromDate = criminalTaskDto.getPairavifromDate();

		Date pairavitoDate = criminalTaskDto.getPairavitoDate();

		//String pairaviMobile = criminalTaskDto.getPairaviMobile();
		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isValidpairavidesination1("pairavidesignation", criminalTaskDto, errors);
		promisValid.isValidpairaviofficer1("PairavidetailsType", criminalTaskDto, errors);

		//promisValid.isvalidPersonName("pairaviName", pairaviName, errors, "errmsg.fnames", true);
		//promisValid.isValidEmail("pairaviemail", pairaviemail, errors, true);
		//promisValid.isValidMobile("pairaviMobile", pairaviMobile, errors);

		promisValid.isvaliddate("pairavifromDate", pairavifromDate, errors);
		promisValid.isvaliddate("pairavitoDate", pairavitoDate, errors);

	}

	public void pairaviOfficercurrent(@Valid CriminalTaskDto criminalTaskDto, Errors errors)

	{
		PairaviOfficer pOff = criminalTaskDto.getPairaviOfficer();
		if(pOff==null || pOff.getId()==0) {
			errors.rejectValue("pairaviOfficer", "errmsg.required");
		}
		//String pairaviName = criminalTaskDto.getPairaviName();
		//String pairaviemail = criminalTaskDto.getPairaviemail();
		////AddDesignation pairavidesignation = criminalTaskDto.getPairavidesignation();
		Date pairavifromDate = criminalTaskDto.getPairavifromDate();
	
		// Date pairavitoDate = criminalTaskDto.getPairavitoDate();

	//	String pairaviMobile = criminalTaskDto.getPairaviMobile();
		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isValidpairavidesination("pairavidesignation", criminalTaskDto, errors);
		promisValid.isValidpairaviofficer("PairavidetailsType", criminalTaskDto, errors);

		//promisValid.isvalidPersonName("pairaviName", pairaviName, errors, "errmsg.fnames", true);
		//promisValid.isValidEmail("pairaviemail", pairaviemail, errors, true);
		//promisValid.isValidMobile("pairaviMobile", pairaviMobile, errors);

		promisValid.isvaliddate("pairavifromDate", pairavifromDate, errors);
		// promisValid.isvaliddate("pairavitoDate", pairavitoDate, errors);

	}
	
	
	public void pairaviOfficercurrent1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		//String pairaviName = criminalTaskDto.getPairaviName();
	//	String pairaviemail = criminalTaskDto.getPairaviemail();
	//	AddDesignation pairavidesignation = criminalTaskDto.getPairavidesignation();
		Date pairavifromDate = criminalTaskDto.getPairavifromDate();
		PairaviOfficer pOff = criminalTaskDto.getPairaviOfficer();
		
		if(pOff==null || pOff.getId()==0) {
			errors.rejectValue("pairaviOfficer", "errmsg.required");
		}
		
		// Date pairavitoDate = criminalTaskDto.getPairavitoDate();

		//String pairaviMobile = criminalTaskDto.getPairaviMobile();
		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isValidpairavidesination1("pairavidesignation", criminalTaskDto, errors);
		
		promisValid.isValidpairaviofficer1("PairavidetailsType", criminalTaskDto, errors);

		//promisValid.isvalidPersonName("pairaviName", pairaviName, errors, "errmsg.fnames", true);
		//promisValid.isValidEmail("pairaviemail", pairaviemail, errors, true);
		//promisValid.isValidMobile("pairaviMobile", pairaviMobile, errors);

		promisValid.isvaliddate("pairavifromDate", pairavifromDate, errors);
		// promisValid.isvaliddate("pairavitoDate", pairavitoDate, errors);

	}

	public void caseProcessingDate(@Valid CriminalTaskDto criminalTaskDto, Errors errors)

	{

		Date invOrder = criminalTaskDto.getInvOrder();
		Date suppInv = criminalTaskDto.getSuppInv();
		Date invReport = criminalTaskDto.getInvReport();
		Date suppInvReport = criminalTaskDto.getSuppInvReport();
		Date dateFilling = criminalTaskDto.getDateFilling();

		ProMISValidator promisValid = new ProMISValidator();

		promisValid.isvaliddate("invOrder", invOrder, errors);

		promisValid.isvaliddate("suppInv", suppInv, errors);

		promisValid.isvaliddate("invReport", invReport, errors);

		promisValid.isvaliddate("suppInvReport", suppInvReport, errors);

		promisValid.isvaliddate("dateFilling", dateFilling, errors);

	}
	
	public void caseProcessingAllDate(@Valid CriminalTaskDto criminalTaskDto, Errors errors)

	{

		Date invOrder = criminalTaskDto.getInvOrder();
		Date suppInv = criminalTaskDto.getSuppInv();
		Date invReport = criminalTaskDto.getInvReport();
		Date suppInvReport = criminalTaskDto.getSuppInvReport();
		Date dateFilling = criminalTaskDto.getDateFilling();

		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isvaliddate("invOrder", invOrder, errors);
		promisValid.isValidDateCaseProcessing("invOrder", invOrder, suppInv, invReport, suppInvReport, dateFilling, errors);


	}
	
	public void caseProcessingDate1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		Date invOrder = criminalTaskDto.getInvOrder();
		Date suppInv = criminalTaskDto.getSuppInv();
		Date invReport = criminalTaskDto.getInvReport();
		//Date suppInvReport = criminalTaskDto.getSuppInvReport();
		Date dateFilling = criminalTaskDto.getDateFilling();

		ProMISValidator promisValid = new ProMISValidator();

		promisValid.isvaliddate("invOrder", invOrder, errors);

		//promisValid.isvaliddate("suppInv", suppInv, errors);

		promisValid.isvaliddate("invReport", invReport, errors);

		//promisValid.isvaliddate("suppInvReport", suppInvReport, errors);

		promisValid.isvaliddate("dateFilling", dateFilling, errors);

	}

	public void financeyear(@Valid CriminalTaskDto criminalTaskDto, Errors errors)

	{

		String dateFilling = criminalTaskDto.getInstructionFy();

		ProMISValidator promisValid = new ProMISValidator();

		String errMsg = "Please enter date";
		promisValid.isvalidFinanceYear("instructionFy", dateFilling, errors, errMsg, true);

	}

	// FOr Hearing Deatils

	public void HearingDetailsValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1();
		 */

		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 */
		/*
		 * promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto,
		 * errors); promisValid.isValidCounselDesignation1("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidCaseStatus("status", criminalTaskDto, errors);

	}
	public void HearingDetailsValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) throws IOException

	{


		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		//Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();

		ProMISValidator promisValid = new ProMISValidator();

		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		//promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidCaseStatus1("status", criminalTaskDto, errors);

	}


	public void HearingDetailsOngoingValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors)
			throws IOException

	{

		// Ongoing

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 */
		/*
		 * promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto,
		 * errors); promisValid.isValidCounselDesignation1("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);

	}

	// DISPOSED OFF

	public void HearingDetailsDisposedOffValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		Date dateOfDisposed = criminalTaskDto.getDateOfDisposed();
		MultipartFile orderCopyOfDisposedOff = criminalTaskDto.getOrderCopyOfDisposedOff();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 */
		promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto, errors);
		promisValid.isValidCounselDesignation1("counselDesignation1", criminalTaskDto, errors);
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isvaliddate("dateOfDisposed", dateOfDisposed, errors);
		promisValid.isValidorderCopyOfDisposedOff("OrderCopyOfDisposedOff", orderCopyOfDisposedOff, errors);

	}
	
	public void HearingDetailsDisposedOffValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		Date dateOfDisposed = criminalTaskDto.getDateOfDisposed();
		MultipartFile orderCopyOfDisposedOff = criminalTaskDto.getOrderCopyOfDisposedOff();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation",
		 * criminalTaskDto, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isvaliddate("dateOfDisposed", dateOfDisposed, errors);
		promisValid.isValidorderCopyOfDisposedOff("OrderCopyOfDisposedOff", orderCopyOfDisposedOff, errors);

	}
	
	//othe
	public void HearingDetailsother(@Valid CriminalTaskDto criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		//Date dateOfDisposed = criminalTaskDto.getDateOfDisposed();
		//MultipartFile orderCopyOfDisposedOff = criminalTaskDto.getOrderCopyOfDisposedOff();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto,
		 * errors); promisValid.isValidCounselDesignation1("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		//promisValid.isvaliddate("dateOfDisposed", dateOfDisposed, errors);
		//promisValid.isValidorderCopyOfDisposedOff("OrderCopyOfDisposedOff", orderCopyOfDisposedOff, errors);

	}
	
	public void HearingDetailsother1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		//Date dateOfDisposed = criminalTaskDto.getDateOfDisposed();
		//MultipartFile orderCopyOfDisposedOff = criminalTaskDto.getOrderCopyOfDisposedOff();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation",
		 * criminalTaskDto, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		//promisValid.isvaliddate("dateOfDisposed", dateOfDisposed, errors);
		//promisValid.isValidorderCopyOfDisposedOff("OrderCopyOfDisposedOff", orderCopyOfDisposedOff, errors);

	}


	// TRANSFER

	public void HearingDetailsTransferValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1();
		 */

		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile orderCopyOfTransfer = criminalTaskDto.getOrderCopyOfTransfer();
		Date transferDateOfOrder = criminalTaskDto.getTransferDateOfOrder();
		Date transferDateOfTransfer = criminalTaskDto.getTransferDateOfTransfer();
		String detailsOfOfficeToWhichCaseisTransfered = criminalTaskDto.getDetailsOfOfficeToWhichCaseisTransfered();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto,
		 * errors); promisValid.isValidCounselDesignation1("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidorderCopyOfTransfer("orderCopyOfTransfer", orderCopyOfTransfer, errors);
		promisValid.isvaliddate("transferDateOfOrder", transferDateOfOrder, errors);
		promisValid.isvaliddate("transferDateOfTransfer", transferDateOfTransfer, errors);
		promisValid.isValiddetailsOfOfficeToWhichCaseisTransfered("detailsOfOfficeToWhichCaseisTransfered",
				detailsOfOfficeToWhichCaseisTransfered, errors, "errmsg.fnames", true);

	}
	public void HearingDetailsTransferValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile orderCopyOfTransfer = criminalTaskDto.getOrderCopyOfTransfer();
		Date transferDateOfOrder = criminalTaskDto.getTransferDateOfOrder();
		Date transferDateOfTransfer = criminalTaskDto.getTransferDateOfTransfer();
		String detailsOfOfficeToWhichCaseisTransfered = criminalTaskDto.getDetailsOfOfficeToWhichCaseisTransfered();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation",
		 * criminalTaskDto, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidorderCopyOfTransfer("orderCopyOfTransfer", orderCopyOfTransfer, errors);
		promisValid.isvaliddate("transferDateOfOrder", transferDateOfOrder, errors);
		promisValid.isvaliddate("transferDateOfTransfer", transferDateOfTransfer, errors);
		promisValid.isValiddetailsOfOfficeToWhichCaseisTransfered("detailsOfOfficeToWhichCaseisTransfered",
				detailsOfOfficeToWhichCaseisTransfered, errors, "errmsg.fnames", true);

	}

	// WITHDRAW

	public void HearingDetailsWithdrawValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors)
			throws IOException

	{
		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile orderCopyForWithdraw = criminalTaskDto.getOrderCopyForWithdraw();
		Date withdrawDateOfOrder = criminalTaskDto.getWithdrawDateOfOrder();
		Date withdrawDateOfWithdraw = criminalTaskDto.getWithdrawDateOfWithdraw();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 */
		promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto, errors);
		promisValid.isValidCounselDesignation1("counselDesignation1", criminalTaskDto, errors);
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidorderCopyForWithdraw("orderCopyForWithdraw", orderCopyForWithdraw, errors);
		promisValid.isvaliddate("withdrawDateOfOrder", withdrawDateOfOrder, errors);
		promisValid.isvaliddate("withdrawDateOfWithdraw", withdrawDateOfWithdraw, errors);

	}
	
	public void HearingDetailsWithdrawValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors)
			throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile orderCopyForWithdraw = criminalTaskDto.getOrderCopyForWithdraw();
		Date withdrawDateOfOrder = criminalTaskDto.getWithdrawDateOfOrder();
		Date withdrawDateOfWithdraw = criminalTaskDto.getWithdrawDateOfWithdraw();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation",
		 * criminalTaskDto, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidorderCopyForWithdraw("orderCopyForWithdraw", orderCopyForWithdraw, errors);
		promisValid.isvaliddate("withdrawDateOfOrder", withdrawDateOfOrder, errors);
		promisValid.isvaliddate("withdrawDateOfWithdraw", withdrawDateOfWithdraw, errors);

	}


	// STAY

	public void HearingDetailsStayValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile stayOrderCopy = criminalTaskDto.getStayOrderCopy();
		Date stayDateOfOrder = criminalTaskDto.getStayDateOfOrder();
		String durattionOfStay = criminalTaskDto.getDurattionOfStay();
		String reasonnOfStay = criminalTaskDto.getReasonnOfStay();
		String stayRemark = criminalTaskDto.getStayRemark();

		ProMISValidator promisValid = new ProMISValidator();
		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 */
		/*
		 * promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto,
		 * errors); promisValid.isValidCounselDesignation1("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidstayOrderCopy("stayOrderCopy", stayOrderCopy, errors);
		promisValid.isvaliddate("stayDateOfOrder", stayDateOfOrder, errors);
		promisValid.isValidreasonnOfStay("reasonnOfStay", reasonnOfStay, errors, "errmsg.fnames", true);
		promisValid.isValidstayRemark("stayRemark", stayRemark, errors, "errmsg.fnames", true);
		promisValid.isValiddurattionOfStay("durattionOfStay", durattionOfStay, errors, "errmsg.fnames", true);

	}
	public void HearingDetailsStayValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile stayOrderCopy = criminalTaskDto.getStayOrderCopy();
		Date stayDateOfOrder = criminalTaskDto.getStayDateOfOrder();
		String durattionOfStay = criminalTaskDto.getDurattionOfStay();
		String reasonnOfStay = criminalTaskDto.getReasonnOfStay();
		String stayRemark = criminalTaskDto.getStayRemark();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation",
		 * criminalTaskDto, errors);
		 * promisValid.isValidcounselDesignation12("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isValidstayOrderCopy("stayOrderCopy", stayOrderCopy, errors);
		promisValid.isvaliddate("stayDateOfOrder", stayDateOfOrder, errors);
		promisValid.isValidreasonnOfStay("reasonnOfStay", reasonnOfStay, errors, "errmsg.fnames", true);
		promisValid.isValidstayRemark("stayRemark", stayRemark, errors, "errmsg.fnames", true);
		promisValid.isValiddurattionOfStay("durattionOfStay", durattionOfStay, errors, "errmsg.fnames", true);

	}

	// WINDUP

	public void HearingDetailsWindUpValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile detailsOfOrderForWindingUp = criminalTaskDto.getDetailsOfOrderForWindingUp();
		Date windingUpClosingDate = criminalTaskDto.getWindingUpClosingDate();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 * promisValid.isValidcounselDesignation("counselDesignation", criminalTaskDto,
		 * errors); promisValid.isValidCounselDesignation1("counselDesignation1",
		 * criminalTaskDto, errors);
		 */
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isvaliddate("windingUpClosingDate", windingUpClosingDate, errors);
		promisValid.isValiddetailsOfOrderForWindingUp("detailsOfOrderForWindingUp", detailsOfOrderForWindingUp, errors);

	}
	
	public void HearingDetailsWindUpValidation1(@Valid NCLTTaskDTO criminalTaskDto, Errors errors) throws IOException

	{

		/*
		 * String counselName = criminalTaskDto.getCounselName(); String counselEmail =
		 * criminalTaskDto.getCounselEmail(); String counselMobileNo =
		 * criminalTaskDto.getCounselMobileNo(); String counselName1 =
		 * criminalTaskDto.getCounselName1(); String counselEmail1 =
		 * criminalTaskDto.getCounselEmail1(); String counselMobileNo1 =
		 * criminalTaskDto.getCounselMobileNo1(); AddDesignation counselDesignation =
		 * criminalTaskDto.getCounselDesignation(); AddDesignation counselDesignation1 =
		 * criminalTaskDto.getCounselDesignation1();
		 */
		Date lastHearingDate = criminalTaskDto.getLastHearingDate();
		Date nextHearingDate = criminalTaskDto.getNextHearingDate();
		Date dateofCaseStatusUpdate = criminalTaskDto.getDateofCaseStatusUpdate();
		MultipartFile detailsOfOrderForWindingUp = criminalTaskDto.getDetailsOfOrderForWindingUp();
		Date windingUpClosingDate = criminalTaskDto.getWindingUpClosingDate();

		ProMISValidator promisValid = new ProMISValidator();

		/*
		 * promisValid.isvalidPersonName("counselName", counselName, errors,
		 * "errmsg.fnames", true); promisValid.isValidEmail("counselEmail",
		 * counselEmail, errors, true); promisValid.isValidMobile("counselMobileNo",
		 * counselMobileNo, errors); promisValid.isvalidPersonName("counselName1",
		 * counselName1, errors, "errmsg.fnames", true);
		 * promisValid.isValidEmail("counselEmail1", counselEmail1, errors, true);
		 * promisValid.isValidMobile("counselMobileNo1", counselMobileNo1, errors);
		 */
		promisValid.isValidcounselDesignation12("counselDesignation", criminalTaskDto, errors);
		promisValid.isValidcounselDesignation12("counselDesignation1", criminalTaskDto, errors);
		promisValid.isvaliddate("lastHearingDate", lastHearingDate, errors);
		promisValid.isvaliddate("nextHearingDate", nextHearingDate, errors);
		promisValid.isvaliddate("dateofCaseStatusUpdate", dateofCaseStatusUpdate, errors);
		promisValid.isvaliddate("windingUpClosingDate", windingUpClosingDate, errors);
		promisValid.isValiddetailsOfOrderForWindingUp("detailsOfOrderForWindingUp", detailsOfOrderForWindingUp, errors);

	}

	public void fileuploadValidation(@Valid CriminalTaskDto criminalTaskDto, Errors errors) throws IOException

	{

		//String counselName = criminalTaskDto.getCounselName();

		MultipartFile orderCopyForWithdraw = criminalTaskDto.getOrderCopyForWithdraw();
		Date withdrawDateOfOrder = criminalTaskDto.getWithdrawDateOfOrder();

		ProMISValidator promisValid = new ProMISValidator();

		//promisValid.isvalidPersonName("counselName", counselName, errors, "errmsg.fnames", true);

		promisValid.isValidorderCopyForWithdraw("orderCopyForWithdraw", orderCopyForWithdraw, errors);
		promisValid.isvaliddate("withdrawDateOfOrder", withdrawDateOfOrder, errors);

	}

}
