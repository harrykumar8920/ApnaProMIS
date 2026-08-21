package com.pams.validation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.BasicContentHandlerFactory.HANDLER_TYPE;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.RecursiveParserWrapperHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
/*import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.ContentHandlerFactory;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;*/
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTTaskDTO;

import jakarta.validation.Valid;


public class ProMISValidator {

	private static final Logger logger = LoggerFactory.getLogger(ProMISValidator.class);
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 1GB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size

	public void isValidDropDown(String fieldName, int fieldValue, Errors errors) {
		if (fieldValue == 0) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	

	public void isvalidAddress(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String fnameReg = "^[A-Za-z\\s]{2,35}$";
		String fnameReg = "^[\\w\\d\\s.,_()/-]{2,70}$";
		// String fnameReg="^[\\w\\d\\s.,_()/-]{2,100}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidTask(String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue("task", "errmsg.required");
			return;
		}
		String firmCity = "^[a-zA-Z0-9\\s&()._, ]{3,100}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!matcher.matches())
			errors.rejectValue("task", "errmsg.taskas");
	}

	public void isvalidCourt(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String firmCity = "^[a-zA-Z0-9\\s-&()._]{2,40}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidStatusName(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[a-zA-Z\\s.-_&()]{2,100}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.required");
	}

	public void isValidCourtCase(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg ="^([0-9a-zA-Z\\s\\/,_-]){3,35}$";
		String numberReg = "[0-9a-zA-Z\\s\\/,_-]{2,25}\\/[0-9]{1,7}\\/[0-9]{4}";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.courtCaseNo");
	}

	public void isValidEmail(String fieldName, String fieldValue, Errors errors, boolean required) {

		if (required && (fieldValue == null || "".equals(fieldValue.trim()))
				|| "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String emailReg = "^([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})?$";
		Pattern pattern = Pattern.compile(emailReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.email");
	}

	/**
	 * Check valid phone no
	 * 
	 * @param fieldName  - String variable for phone
	 * @param fieldValue -String variable for phone value
	 * @param errors     - Errors variable for containing field error
	 */
	public void isValidFirmPhone(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String numberReg = "^\\(?([0-9]{2,3})\\)?[-]?([0-9]{2,4})[-]?([0-9]{4,8})$";
//        String numberReg = "^([0-9/]){3,}$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.phone");
	}

	public void isValidPincode(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg = "^[0-9]{10,10}$";
		String numberReg = "^[1-9]{1}[0-9]{5}$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.pincode");
	}

	public void isValidZIPcode(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg = "^[0-9]{10,10}$";
		String numberReg = "^([A-Za-z0-9\\s,-]){3,8}$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.zipcode");
	}

	

	public void isValidComplanitdesignation1(String fieldName, NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getComplanitdesignation() == null
				|| criminalTaskDto.getComplanitdesignation().getId() == 0) {
			errors.rejectValue("complanitdesignation", "errmsg.required");
		}

	}

	public void isValidComplanitdesignation(String fieldName, CriminalTaskDto criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getComplanitdesignation() == null
				|| criminalTaskDto.getComplanitdesignation().getId() == 0) {
			errors.rejectValue("complanitdesignation", "errmsg.required");
		}

	}

	public void isValidpairaviofficer(String fieldName, CriminalTaskDto criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getPairaviType() == null || criminalTaskDto.getPairaviType().getId() == 0) {
			errors.rejectValue("pairaviType", "errmsg.required");
		}

	}

	public void isValidpairaviofficer1(String fieldName, NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getPairaviType() == null || criminalTaskDto.getPairaviType().getId() == 0) {
			errors.rejectValue("pairaviType", "errmsg.required");
		}

	}

	public void isValidpairavidesination(String fieldName, CriminalTaskDto criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getPairavidesignation() == null || criminalTaskDto.getPairavidesignation().getId() == 0) {
			errors.rejectValue("pairavidesignation", "errmsg.required");
		}

	}

	public void isValidpairavidesination1(String fieldName, NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getPairavidesignation() == null || criminalTaskDto.getPairavidesignation().getId() == 0) {
			errors.rejectValue("pairavidesignation", "errmsg.required");
		}

	}

	public void isValidCompanyID(String fieldName, CriminalTaskDto criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getCompID123() == 0) {
			errors.rejectValue(fieldName, "errmsg.required");
		}

	}

	public void isValidControlingofficer(String fieldName, CriminalTaskDto criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getIOName() == null || criminalTaskDto.getIOName().equalsIgnoreCase("0")) {
			errors.rejectValue("IOName", "errmsg.required");
		}

	}

	public void isValidControlingofficer1(String fieldName, NCLTTaskDTO criminalTaskDto, Errors errors)

	{

		if (criminalTaskDto.getIOName() == null || criminalTaskDto.getIOName().equalsIgnoreCase("0")) {
			errors.rejectValue("IOName", "errmsg.required");
		}

	}

	public void isValidMobile(String fieldName, String fieldValue, Errors errors) {

		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String numberReg = "^[0-9]{10,10}$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.mobile");
	}

	public void isValidpetitionNumber(String fieldName, String fieldValue, Errors errors, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	public void isValidaadhar(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String numberReg = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";

		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidpanNumber(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String numberReg = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidpasportNumber(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg = "^[0-9]{10,10}$";
		String numberReg = "^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$";

		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.passport");
	}

	public void isvalidcoyname(String fieldname, String fieldValue, Errors errors, boolean required) {

		if ("".equals(fieldValue) || fieldValue.isEmpty()) {
			errors.rejectValue(fieldname, "errmsg.required");
		}
	}

	public void isBlank(String fieldname, String fieldValue, Errors errors) {
		if (fieldValue == null) {
			errors.rejectValue(fieldname, "errmsg.invalid");
			return;
		}
		if ("".equals(fieldValue) || fieldValue.isEmpty()) {
			errors.rejectValue(fieldname, "errmsg.required");
		}
	}

	// gouthami 15/09/2020
	public boolean getValidInteger(long id) {
		boolean flag = true;
		if (id == 0) {

			flag = false;
		}

		if (String.valueOf(id) == null) {
			flag = false;
		} else {

			String numberReg = "[+]?[0-9][0-9]*";
//           String numberReg = "^([0-9/]){3,}$";
			Pattern pattern = Pattern.compile(numberReg);
			Matcher matcher = pattern.matcher(String.valueOf(id));
			if (!matcher.matches())
				flag = false;
		}
		return flag;
	}

	public boolean getValidClaus(StringBuilder Clause) {
		boolean flag = true;
		if (Clause == null) {

			flag = false;
		} else {
			String numberReg = "\\(([a-z)]+)\\)";
//      String numberReg = "^([0-9/]){3,}$";
			Pattern pattern = Pattern.compile(numberReg);
			Matcher matcher = pattern.matcher(String.valueOf(Clause));
			if (!matcher.matches())
				flag = false;
		}
		return flag;
	}

	public boolean isvalidCompanyName(String fieldValue) {
		boolean flag = true;
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {

			flag = false;
		}
		String firmCity = "^[a-zA-Z0-9\\s-&()._]{1,100}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!matcher.matches()) {
			flag = false;
		}
		return flag;
	}

	// gouthami 15/09/2020
	public boolean getValidZeroInteger(long id) {
		boolean flag = true;

		String numberReg = "[+]?[0-9][0-9]*";
//           String numberReg = "^([0-9/]){3,}$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(String.valueOf(id));
		if (!matcher.matches())
			flag = false;

		return flag;
	}

	
	public boolean isBlank(String fieldname, String fieldValue) {
		boolean flag = false;
		if (fieldValue == null || "".equals(fieldValue.trim())) {
			flag = true;
		}
		return flag;
	}

	public boolean isgetBlank(String fieldValue) {
		boolean flag = false;
		if (fieldValue == null || "".equals(fieldValue.trim())) {
			flag = true;
		}
		return flag;
	}

	
	public void isvalidDesignation(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^([A-Za-z\\s.]){2,50}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.designation");
	}

	public void isValidText200(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternRemark = "^([0-9a-zA-Z\\s\\/,_-]){3,200}$";
		Pattern pattern = Pattern.compile(patternRemark);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidFirmReg(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^([0-9a-zA-Z\\/]){1,20}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.firmReg");
	}

	public void isvalidFirmCity(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String fnameReg = "^(a-zA-Z\\()\\s]){3,70}$";
		String firmCity = "^([a-zA-Z\\(\\)\\s]){1,50}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidFirmName(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String firmCity = "^[a-zA-Z0-9\\s-&()._]{1,70}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidUserAddress(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,1-9._:()/-]{1,200}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidRadio(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	public void isRadioChecked(String fieldName, boolean fieldValue, Errors errors) {
		if (fieldValue == false) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	public void isvalidName(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[a-zA-Z\\s.-_&()]{5,100}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidorder(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[\\w\\d\\s._()/-]{5,50}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidPersonName(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg1 = "^[\\w\\s._()/-]{1,50}$";
		String fnameReg = "^[A-Za-z\\s]{2,50}$";

		Pattern pattern = Pattern.compile(fnameReg1);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}
	public void isvalidcomplaintPetioner(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg1 = "^[\\w\\s._()/-]{1,25}$";
		//String fnameReg = "^[A-Za-z\\s]{2,50}$";

		Pattern pattern = Pattern.compile(fnameReg1);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}
	

	public Boolean getvalidorder(String fieldValue) {

		if ((fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {

			return false;
		}
		String fnameReg = "^[\\w\\d\\s._()/-]{5,50}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isgetBlank(fieldValue) && !matcher.matches()) {

			return false;
		} else
			return true;
	}

	public void isvalidSalutation(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {

		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[a-zA-Z]{2,3}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidUserName(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[a-zA-Z\\s]{2,40}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public String getValidBoolean(String value) {
		if (("false").equalsIgnoreCase(value) || ("true").equalsIgnoreCase(value))
			return value;
		return "";
	}

	 public void isValidData(String fieldname, String fieldValue, Errors errors) {
		if (fieldValue == null)

		{
			errors.rejectValue(fieldname, "errmsg.invalid");
			return;
		}
		String dataReg = "^[\\w\\d\\s._!@+#$:()/=-]+$";
		Pattern pattern = Pattern.compile(dataReg);
		Matcher matcher = pattern.matcher(fieldValue.trim());
		if (!matcher.matches()) {
			errors.rejectValue(fieldname, "errmsg.malicious");
		}
	}

	public void isValidPassword(String fieldname, String fieldValue, Errors errors) {
		if (fieldValue == null)

		{
			errors.rejectValue(fieldname, "errmsg.invalid");
			return;
		}
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$&_.])[a-zA-Z0-9!@#$_.]{8,20}$";
		Pattern pattern = Pattern.compile(passwordPattern);
		Matcher matcher = pattern.matcher(fieldValue.trim());
		if (!matcher.matches()) {
			errors.rejectValue(fieldname, "errmsg.malicious");
		}
	}

	public boolean validateYear(String date) {
		Pattern pattern = Pattern.compile("((20)\\d\\d)");
		Matcher matcher = pattern.matcher(date);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	
	public void isvaliddate(String fieldName, Date pairavifromDate, Errors errors) {

		if (pairavifromDate == null)

		{
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
	}

	public void isValidDateCaseProcessing(String fieldName, Date one, Date two, Date three, Date four, Date five,
			Errors errors) {
		if (one == null && two == null && three == null && four == null && five == null) {
			errors.rejectValue(fieldName, "errmsg.required");
		}
	}

	public boolean validateDateFormat(final String date) {

		
		Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");
		Matcher matcher = pattern.matcher(date);
		if (matcher.matches()) {
			matcher.reset();
			if (matcher.find()) {
				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (day.equals("31") && (month.equals("4") || month.equals("6") || month.equals("9")
						|| month.equals("11") || month.equals("04") || month.equals("06") || month.equals("09"))) {
					return false; // only 1,3,5,7,8,10,12 has 31 days
				} else if (month.equals("2") || month.equals("02")) {
					// leap year
					if (year % 4 == 0) {
						if (day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					} else {
						if (day.equals("29") || day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

//    eFormNFRA Validation

	public void isvalidPan(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg = "^[0-9]{10,10}$";
		String patternPan = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
		Pattern pattern = Pattern.compile(patternPan);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.pan");
	}

	public void isValidAssignedBy(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg = "^[0-9]{10,10}$";
		String patternAssignedBy = "^[a-zA-Z0-9\\s-&,_.]{3,70}$";
		Pattern pattern = Pattern.compile(patternAssignedBy);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.assignedby");
	}

	public void isvalidRegnWithAgency(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^([0-9a-zA-Z\\/]){1,20}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.regnwithagency");
	}

	public void isvalidAddrLine7(String fieldName, String fieldValue, Errors errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s.,_()/-]{2,70}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.required");
	}

	public void isvalidAddrLine1(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s.,_()/-]{2,70}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidAddrLine2(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,_()/-]{3,50}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidCity(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String fnameReg = "^(a-zA-Z\\()\\s]){3,70}$";
		String firmCity = "^([a-zA-Z\\(\\)\\s]){1,50}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidWebsite(String fieldName, String fieldValue, Errors errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String numberReg = "^[0-9]{10,10}$";
		String numberReg = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.website");
	}

	public void isValidRatingScale(String fieldName, String fieldValue, Errors errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
//        String patternRating = "\\d+(\\.\\d{1,1})?";
		String patternRating = "^([A-Za-z\\s.-_]){1,25}$";
		Pattern pattern = Pattern.compile(patternRating);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.rating");
	}

	public void isValidRemark(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternRemark = "^\\w.+(?:\\s+\\w[0-9a-zA-Z\\s\\/,._-]+){0,99}$";
		Pattern pattern = Pattern.compile(patternRemark);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidDescription(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternRemark = "^\\w.+(?:\\s+\\w[0-9a-zA-Z'()\\s\\/,\\._-]+){0,499}$";
		Pattern pattern = Pattern.compile(patternRemark);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidCIN(String fieldName, String fieldValue, Errors errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternCIN = "^([L|U]{1})([0-9]{5})([A-Za-z]{2})([0-9]{4})([A-Za-z]{3})([0-9]{6})$";
		Pattern pattern = Pattern.compile(patternCIN);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.required");
	}

	public void isvalidGLN(String fieldName, String fieldValue, Errors errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternGLN = "^416(\\d{13})$";
		Pattern pattern = Pattern.compile(patternGLN);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.gln");
	}

	public void isvalidRegnum(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternRegnum = "^([0-9a-zA-Z\\/]){1,20}$";
		Pattern pattern = Pattern.compile(patternRegnum);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidNumeric(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required && (fieldValue == null || fieldValue.equals("0.0") || fieldValue.equals("0")
				|| "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternNumeric = "\\d+(\\.\\d{1,2})?";
		Pattern pattern = Pattern.compile(patternNumeric);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isValidNumericWithZero(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternNumeric = "\\d+(\\.\\d{1,2})?";
		Pattern pattern = Pattern.compile(patternNumeric);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}


	public void isvalidFCRN(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternFCRN = "";
		if (fieldValue.indexOf(0) == 'F')
			patternFCRN = "^[F]{1}[0-9]{5}$";
		else
			patternFCRN = "[A-Z0-9]{6,13}$";
		Pattern pattern = Pattern.compile(patternFCRN);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidFirmLLPIN(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternllpin = "^([0-9a-zA-Z\\/]){3,20}$";
		Pattern pattern = Pattern.compile(patternllpin);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.firmllpin");
	}

	public void isvalidUnit(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[a-zA-Z0-9\\s.-_ /()-]{2,20}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidLocation(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}

		String fnameReg = "^[a-zA-Z\\s.& -_,/() ]{2,40}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);

	}

	public void isvalidFinanceYear(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}

		String fnameReg = "(20)\\d{2}-(20)\\d{2}";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
		else {
			if (Integer.parseInt(fieldValue.substring(fieldValue.lastIndexOf('-') + 1))
					- Integer.parseInt(fieldValue.substring(0, fieldValue.lastIndexOf('-'))) != 1)
				errors.rejectValue(fieldName, "errmsg.finYearSeq");
		}
	}

	public void isValidFax(String fieldName, String fieldValue, Errors errors) {
		if (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim())) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
//	        String numberReg = "/^\\+?[0-9]{6,10}$/";
		String numberReg = "^[0-9-]{6,12}$";

//	        String numberReg = "^([0-9/]){3,}$";
		Pattern pattern = Pattern.compile(numberReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.fax");
	}

	public void isvalidCaseId(String fieldName, String fieldValue, BindingResult errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternCIN = "^[a-zA-Z0-9\\s/-]{5,100}$";
		Pattern pattern = Pattern.compile(patternCIN);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.caseid");
	}

	public void isvalidCourtNo(String fieldName, String fieldValue, BindingResult errors, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String patternCIN = "^[a-zA-Z0-9\\s/-]{5,100}$";
		Pattern pattern = Pattern.compile(patternCIN);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, "errmsg.courtCaseNo");
	}

	public void isvalidCompany(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String firmCity = "^[a-zA-Z0-9\\s&()._, ]{1,100}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	// file uploads validation
	public void isValidFile(MultipartFile file, Errors errors, boolean isRequired, String errFieldName) {
		if (isRequired && (file.isEmpty() || file == null)) {
			errors.rejectValue(errFieldName, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(errFieldName, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(errFieldName, "errmsg.invalid.file");
		}
	}

	public boolean isValidFileName(String fileName) {
	    if (fileName == null) {
	        return false;
	    }

	    fileName = fileName.trim().toLowerCase();

	    // Ensure there is at least one dot and it's not the first character
	    if (fileName.lastIndexOf(".") <= 0) {
	        return false;
	    }

	    String allowExt = "pdf";
	    // Updated regex to allow alphanumeric characters, spaces, and underscores, but not special characters before the extension
	    //String fileReg = "^[\\w\\s#^/,\\-]+\\.(?i)(" + allowExt + ")$";
	    String fileReg = "^[\\w\\s#^/,.\\-]+\\.(?i)(" + allowExt + ")$";
	    
	    Pattern pattern = Pattern.compile(fileReg);
	    Matcher matcher = pattern.matcher(fileName);

	    if (!matcher.matches()) {
	        return false;
	    }

	    return true;
	}

	public boolean isValidMime(String requestMime) {
		String allowedMime = "application/pdf";
		if (requestMime != null && !"".equals(requestMime) && requestMime.equalsIgnoreCase("pdf"))
			return true;
		String mimetype[] = allowedMime.split(",");
		for (int i = 0; i < mimetype.length; i++) {
			if (requestMime != null && !"".equals(requestMime) && requestMime.equalsIgnoreCase(mimetype[i]))
				return true;
		}
		return false;
	}
	


	
	
	
	public boolean isValidFileTikka(String filename, MultipartFile part) throws IOException {
	    boolean result = true;
	    String allowedMime = "application/pdf,application/PDF";
	    String[] mimetype = allowedMime.split(",");
	    List<String> mimelist = Arrays.asList(mimetype);

	    Tika tika = new Tika();
	    boolean correct = false;

	    InputStream filepart = part.getInputStream();
	    try {
	        String mediaType = tika.detect(filepart);
	        System.out.println(filename + " " + mediaType);

	        if (mimelist.contains(mediaType)) {
	            correct = true;
	        }

	        if (!correct) {
	            result = false;
	        }

	        if ("application/pdf".equalsIgnoreCase(mediaType)) {
	            // Use AutoDetectParser directly with BodyContentHandler
	            AutoDetectParser parser = new AutoDetectParser();
	            BodyContentHandler handler = new BodyContentHandler(-1);
	            Metadata metadata = new Metadata();
	            ParseContext context = new ParseContext();

	            try (InputStream inputStream = part.getInputStream()) {
	                // Parse directly using the parser
	                parser.parse(inputStream, handler, metadata, context);
	                System.out.println("PDF parsed successfully");
	                
	                // Get the extracted text if needed
	                String extractedText = handler.toString();
	                System.out.println("Extracted text length: " + extractedText.length());
	            }

	            String contentType = metadata.get(Metadata.CONTENT_TYPE);
	            System.out.println("Detected content type: " + contentType);
	        }

	        // Check for forbidden content in PDF
	        if (result) {
	            String regex = "<\\s*script\\b[^>]*>(.*?)<\\s*/\\s*script>";
	            String[] forbiddenStrings = {"捷", "迅", "辑", "movie", "mp3", "audio", "video"};
	            List<String> forbiddenStrings1 = List.of(
	                    "<script",
	                    "</script>",
	                    "javascript:",
	                    "onerror=",
	                    "onload=",
	                    "onclick=",
	                    "onmouseover="
	            );

	            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

	            try (PDDocument document = PDDocument.load(part.getInputStream())) {
	                PDFTextStripper pdfStripper = new PDFTextStripper();
	                String text = pdfStripper.getText(document);

	                // Check for <script> tags in the extracted text
	                Matcher matcher = pattern.matcher(text);
	                if (matcher.find()) {
	                    System.out.println("Malicious script tag found.");
	                    result = false;
	                }

	                // Check for forbidden strings
	                for (String forbidden : forbiddenStrings) {
	                    if (text.contains(forbidden)) {
	                        System.out.println("Forbidden string found: " + forbidden);
	                        result = false;
	                    }
	                }
	                
	                for (String forbidden : forbiddenStrings1) {
	                    if (text.contains(forbidden)) {
	                        System.out.println("Forbidden string found: " + forbidden);
	                        result = false;
	                    }
	                }

	            } catch (IOException e) {
	                e.printStackTrace();
	                result = false;
	            }
	        }

	    } catch (Exception e1) {
	        e1.printStackTrace();
	        result = false;
	    } finally {
	        if (filepart != null) {
	            safeClose(filepart);
	        }
	    }

	    return result;
	}
	
	
	
	    // Method to safely close the input stream
	    private void safeClose(InputStream filepart) {
	        if (filepart != null) {
	            try {
	                filepart.close();
	            } catch (IOException e) {
	                System.out.println("Error closing stream: " + e.getMessage());
	            }
	        }
	    }

	

	public String getSafeString(String value) {
		String status = "";
		try {
			status = ESAPI.validator().getValidInput("StringTest", value, "SafeString", Integer.MAX_VALUE, false);
		} catch (IntrusionException | ValidationException e) {
			logger.error(e.getMessage(), e);
		}
		return status;
	}

	public boolean getvalidCompany(String fieldValue, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {

			return false;
		}
		String firmCity = "^[a-zA-Z0-9\\s-&()._]{1,100}$";
		Pattern pattern = Pattern.compile(firmCity);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isgetBlank(fieldValue) && !matcher.matches())
			return false;
		else
			return true;
	}

	public Boolean isValidName(String fieldValue) {

		if ((fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {

			return false;
		}
		String fnameReg = "^[a-zA-Z\\s.-_&()]{1,35}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isgetBlank(fieldValue) && !matcher.matches())
			return false;
		else
			return true;
	}

	public void isValidDin(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		String fnameReg = "^[0-9]{10,10}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);
	}

	public void isvalidCaseBrife(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,._:()/-]{3,500}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);// TODO Auto-generated method stub

	}
	public void isvalidBrifeHD(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
	    // Required field validation
	    if (required && (fieldValue == null || fieldValue.trim().isEmpty() || "null".equalsIgnoreCase(fieldValue.trim()))) {
	        errors.rejectValue(fieldName, "errmsg.required");
	        return;
	    }

	    // Regex pattern for allowed characters (basic sanitization)
	    String address = "^[\\w\\d\\s,.'_:()/-]{3,1000}$";
	    Pattern pattern = Pattern.compile(address);
	    Matcher matcher = pattern.matcher(fieldValue);

	    // Check if fieldValue is not blank and doesn't match the pattern
	    if (!isBlank(fieldName, fieldValue) && !matcher.matches()) {
	        errors.rejectValue(fieldName, errMsg);
	        return;
	    }

	    // Word count validation: must not exceed 200 words
	    if (!isBlank(fieldName, fieldValue)) {
	        int wordCount = fieldValue.trim().split("\\s+").length;
	        if (wordCount > 200) {
	            errors.rejectValue(fieldName, "errmsg.maxWordLimitExceeded"); // Define this key in messages.properties
	        }
	    }
	}

	public void isvalidCourtName(String fieldName, String fieldValue, BindingResult errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
		// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[a-zA-Z0-9\\s-&()._]{3,35}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);// TODO Auto-genera

	}

// fOR hEARING Deatils 

	public void isValidcounselDesignation(String fieldName, @Valid CriminalTaskDto criminalTaskDto, Errors errors) {

	

	}

	public void isValidcounselDesignation12(String fieldName, @Valid NCLTTaskDTO criminalTaskDto, Errors errors) {

		

	}

	public void isValidcounselDesignation123(String fieldName, @Valid NCLTTaskDTO criminalTaskDto, Errors errors) {

	
	}

	public void isValidCounselDesignation1(String fieldName, @Valid CriminalTaskDto criminalTaskDto, Errors errors) {

		

	}

	
	public void isValidCaseStatus(String fieldName, @Valid CriminalTaskDto criminalTaskDto, Errors errors) {

		if (criminalTaskDto.getStatus() == null || criminalTaskDto.getStatus().getId() == 0) {
			errors.rejectValue("status", "errmsg.required");

		}

	}

	public void isValidCaseStatus1(String fieldName, @Valid NCLTTaskDTO criminalTaskDto, Errors errors) {

		if (criminalTaskDto.getStatus() == null || criminalTaskDto.getStatus().getId() == 0) {
			errors.rejectValue("status", "errmsg.required");

		}

	}

	public void isValidorderCopyOfDisposedOff(String string, MultipartFile orderCopyOfDisposedOff, Errors errors)
			throws IOException {

		if (orderCopyOfDisposedOff == null || orderCopyOfDisposedOff.isEmpty()) {

			errors.rejectValue("orderCopyOfDisposedOff", "errmsg.required");
		}

	}

	public void isValidorderCopyOfTransfer(String string, MultipartFile file, Errors errors) throws IOException {

		if (file.isEmpty() || file == null) {
			errors.rejectValue(string, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(string, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(string, "errmsg.invalid.file");
		}

	}

	public void isValidadditionalFile(String string, MultipartFile file, Errors errors) throws IOException {

		if (file.isEmpty() || file == null) {
			errors.rejectValue(string, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(string, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(string, "errmsg.invalid.file");
		}

	}

	public void isValidorderCopyForWithdraw(String string1, MultipartFile file, Errors errors) throws IOException {

		if (file.isEmpty() || file == null) {
			errors.rejectValue(string1, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(string1, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(string1, "errmsg.invalid.file");
		}

	}

	public void isValidorderCopyForWithdrawnew(String string, MultipartFile file, Errors errors) throws IOException {

		if (file.isEmpty() || file == null) {
			errors.rejectValue(string, "errmsg.required");
		}

		else if (!file.isEmpty()) {
			String orignalfilenameOrderCopyOfTransfer = file.getOriginalFilename();
			String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");
			   boolean validFileName = result.matches("^[a-zA-Z0-9._-]+$");

			if (validFileName == false) {

				errors.rejectValue(string, "errmsg.filename");
			}
		}

		else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(string, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(string, "errmsg.invalid.file");
		}

	}

	public void isValidstayOrderCopy(String string, MultipartFile file, Errors errors) throws IOException {

		if (file.isEmpty() || file == null) {
			errors.rejectValue(string, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(string, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(string, "errmsg.invalid.file");
		}

	}

	public void isValiddetailsOfOrderForWindingUp(String string, MultipartFile file, Errors errors) throws IOException {

		if (file.isEmpty() || file == null) {
			errors.rejectValue(string, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(string, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(string, "errmsg.invalid.file");
		}

	}

	public void isValidreasonnOfStay(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,._:()/-]{3,500}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);// TODO Auto-generated method stub

	}

	public void isValidstayRemark(String fieldName, String fieldValue, Errors errors, String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,._:()/-]{3,500}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);// TODO Auto-generated method stub

	}

	public void isValiddurattionOfStay(String fieldName, String fieldValue, Errors errors, String errMsg,
			boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,._:()/-]{3,500}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);// TODO Auto-generated method stub

	}

	public void isValiddetailsOfOfficeToWhichCaseisTransfered(String fieldName, String fieldValue, Errors errors,
			String errMsg, boolean required) {
		if (required
				&& (fieldValue == null || "".equals(fieldValue.trim()) || "null".equalsIgnoreCase(fieldValue.trim()))) {
			errors.rejectValue(fieldName, "errmsg.required");
			return;
		}
// String address="^(\\w*\\s*[-,.\\(\\)\\&]{3,200}$";
		String address = "^[\\w\\d\\s,._:()/-]{3,500}$";
		Pattern pattern = Pattern.compile(address);
		Matcher matcher = pattern.matcher(fieldValue);
		if (!isBlank(fieldName, fieldValue) && !matcher.matches())
			errors.rejectValue(fieldName, errMsg);// TODO Auto-generated method stub

	}

	public void isValidCompanyName(String companyName, String companyValue, Errors errors, String errMsg,
			boolean required) {

		if (required && (companyValue == null || "".equals(companyValue.trim())
				|| "null".equalsIgnoreCase(companyValue.trim()))) {
			errors.rejectValue(companyName, errMsg);
			return;
		}
		String fnameReg = "^[a-zA-Z\\s.-_&()]{1,35}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(companyValue);
		if (!isBlank(companyName, companyValue) && !matcher.matches())
			errors.rejectValue(companyName, errMsg);
	}

	public void isValidAccusedName(String accusedName, String accusedValue, Errors errors, String errMsg,
			boolean required) {

		if (required && (accusedValue == null || "".equals(accusedValue.trim())
				|| "null".equalsIgnoreCase(accusedValue.trim()))) {
			errors.rejectValue(accusedName, errMsg);
			return;
		}
		String fnameReg = "^[a-zA-Z\\s.-_&()]{1,35}$";
		Pattern pattern = Pattern.compile(fnameReg);
		Matcher matcher = pattern.matcher(accusedValue);
		if (!isBlank(accusedName, accusedValue) && !matcher.matches())
			errors.rejectValue(accusedName, errMsg);
	}

}
