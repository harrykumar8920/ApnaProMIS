package com.pams.controllers;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.PerformaParty;
import com.pams.entity.UploadAdditionalFilesDetails;
import com.pams.entity.UserDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.MiscellaneousFileRepository;
import com.pams.service.PerformaPartyRepo;
import com.pams.service.UploadAdditionalFilesDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;

@Controller
public class UploadAdditionalFileController {

	private static final Logger logger = LoggerFactory.getLogger(OfficerController.class);

	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 200 MB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size

	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private MiscellaneousFileRepository misRepo;
	@Autowired
	private PerformaPartyRepo performaPartyRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired

	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	AddAccusedRepository addAccusedRepo;

	@Autowired

	OfficerController officerCont1;
	@Autowired

	OfficeControllerNCLT officerCont;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepo1;
	@Autowired
	UploadAdditionalFilesDetailsRepository uploadAdditionalFilesDetailsRepo;

	/*
	 * @RequestMapping(value = "/getaccusedforfileupload", method =
	 * RequestMethod.GET) public @ResponseBody List<AddAccused>
	 * getState(@RequestParam("company") CaseCompany casecompany,
	 * 
	 * @RequestParam("procourtdtlIDD") proCourtCaseDetails procourtdtlIDD) {
	 * List<AddAccused> accuseDtl =
	 * addAccusedRepo.findAllByProcourtdtlAndCompany(procourtdtlIDD, casecompany);
	 * return accuseDtl; }
	 */

	@RequestMapping(value = "/addCriminalDtl", params = "editPerformaParty")
	public String editPerformaParty(@RequestParam(value = "editPerformaParty", required = true) Long id,
			ModelMap modelMap, @ModelAttribute NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 27;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		PerformaParty uploadfile = performaPartyRepo.findById(id).get();
		criminalTaskDto.setTypeofOrder(uploadfile.getTypeofOrder());
		criminalTaskDto.setPPCompany(uploadfile.getPPCompany());
		criminalTaskDto.setPPCompCin(uploadfile.getPPCompCin());
		criminalTaskDto.setPPRespondentName(uploadfile.getPPRespondentName());
		criminalTaskDto.setPPRespondentDesgination(uploadfile.getPPRespondentDesgination());
		criminalTaskDto.setPPAddress(uploadfile.getPPAddress());
		criminalTaskDto.setPerformaID(uploadfile.getId());
		System.out.println("ABC");

		officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardPerformaParty")
	public String forwardPerformaParty(@RequestParam(value = "forwardPerformaParty", required = true) Long id,
			ModelMap modelMap, @ModelAttribute NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 27;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		PerformaParty uploadfile = performaPartyRepo.findById(id).get();
		uploadfile.setApproveStatus(2);
		performaPartyRepo.save(uploadfile);

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.proformapartyforward") + " " + assignedTaskPuh.getUser().getSalutation()
						+ " " + assignedTaskPuh.getUser().getFirstName() + " "
						+ (assignedTaskPuh.getUser().getMiddleName().equals("") ? ""
								: assignedTaskPuh.getUser().getMiddleName() + "")
						+ assignedTaskPuh.getUser().getLastName() + " " + " and investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		modelMap.addAttribute("message", "Proforma Party Details Confirmed Successfully  :");
		officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "editUploadFile")
	public String editUploadFile(@RequestParam(value = "editUploadFile", required = true) Long id, ModelMap modelMap,
			@ModelAttribute NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect, BindingResult bindResult)
			throws Exception {
		int tabId = 28;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		UploadAdditionalFilesDetails uploadfile = uploadAdditionalFilesDetailsRepo.findById(id).get();
		criminalTaskDto.setAccusedIdtest(uploadfile.getAccusedId());
		criminalTaskDto.setUploadID(id);

		officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "editUploadFileCriminal")
	public String editUploadFileCriminal(@RequestParam(value = "editUploadFileCriminal", required = true) Long id,
			ModelMap modelMap, @ModelAttribute CriminalTaskDto criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 26;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		UploadAdditionalFilesDetails uploadfile = uploadAdditionalFilesDetailsRepo.findById(id).get();

		criminalTaskDto.setAccusedIdtest1(uploadfile.getAccusedId());
		criminalTaskDto.setUploadID(id);

		officerCont1.modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardUploadFile")
	public String forwardUploadFile(@RequestParam(value = "forwardUploadFile", required = true) Long id,
			ModelMap modelMap, @ModelAttribute NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 28;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		UploadAdditionalFilesDetails uploadfile = uploadAdditionalFilesDetailsRepo.findById(id).get();
		uploadfile.setApproveStatus(2);

		uploadAdditionalFilesDetailsRepo.save(uploadfile);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.Filingdetailforward") + " " + assignedTaskPuh.getUser().getSalutation()
						+ " " + assignedTaskPuh.getUser().getFirstName() + " "
						+ (assignedTaskPuh.getUser().getMiddleName().equals("") ? ""
								: assignedTaskPuh.getUser().getMiddleName() + "")
						+ assignedTaskPuh.getUser().getLastName() + " " + " and investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		modelMap.addAttribute("message", "Filing Details Confirmed Successfully  :");

		officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardUploadFileAccused")
	public String forwardUploadFileAccused(@RequestParam(value = "forwardUploadFileAccused", required = true) Long id,
			ModelMap modelMap, @ModelAttribute CriminalTaskDto criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 26;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		UploadAdditionalFilesDetails uploadfile = uploadAdditionalFilesDetailsRepo.findById(id).get();
		uploadfile.setApproveStatus(2);

		uploadAdditionalFilesDetailsRepo.save(uploadfile);
		modelMap.addAttribute("message", "Filing Details Confirmed Successfully  :");
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.Filingdetailforward") + " " + assignedTaskPuh.getUser().getSalutation()
						+ " " + assignedTaskPuh.getUser().getFirstName() + " "
						+ (assignedTaskPuh.getUser().getMiddleName().equals("") ? ""
								: assignedTaskPuh.getUser().getMiddleName() + "")
						+ assignedTaskPuh.getUser().getLastName() + " " + " and investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		officerCont1.modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/saveUploadAdditionalFileAccuse")
	public String saveUploadAdditionalFileAccuse(ModelMap modelMap,
			@ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 26;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UploadAdditionalFilesDetails uploadAdditionalFilesDetails = new UploadAdditionalFilesDetails();

		if (criminalTaskDto.getAccusedIdtest1() == null)

		{
			bindResult.rejectValue("accusedIdtest1", "errmsg.required");
		}

		else if (criminalTaskDto.getAccusedIdtest1().getId() == 0) {
			bindResult.rejectValue("accusedIdtest1", "errmsg.required");
		}

		if (criminalTaskDto.getFile1().isEmpty()) {
			bindResult.rejectValue("file1", "errmsg.required");
		} else {
			isValidFile("file1", criminalTaskDto.getFile1(), bindResult);
		}

		if (bindResult.hasErrors()) {
			officerCont1.modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/CriminalTaskPage";
		}
		AddAccused accusedId = criminalTaskDto.getAccusedIdtest1();
		MultipartFile file = criminalTaskDto.getFile1();
		Long id1;
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if (criminalTaskDto.getUploadID() != null) {
			uploadAdditionalFilesDetails.setId(criminalTaskDto.getUploadID());
			id1 = criminalTaskDto.getUploadID();
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.Filingdetailupdate") + " " + " and Investigation Number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

		} else {
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.Filingdetailsave") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			id1 = (uploadAdditionalFilesDetailsRepo.findMaxid() != null)
					? (uploadAdditionalFilesDetailsRepo.findMaxid() + 1)
					: 1;
		}

		String orignalfilenameOrderCopyOfTransfer = file.getOriginalFilename();

		String fileExt = orignalfilenameOrderCopyOfTransfer
				.substring(orignalfilenameOrderCopyOfTransfer.lastIndexOf("."));
		String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");

		String filename = id1 + "_" + result;

		caseFileUpload(file, filename);

		uploadAdditionalFilesDetails.setUser(userdet);
		uploadAdditionalFilesDetails.setAccusedId(accusedId);

		uploadAdditionalFilesDetails.setFileName(filename);

		uploadAdditionalFilesDetails.setAssignedTaskPuhdtl(assignedTaskPuh);
		uploadAdditionalFilesDetailsRepo.save(uploadAdditionalFilesDetails);

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);
		redirect.addFlashAttribute("message", "Filing Details saved Successfully  :");
		return "redirect:/proceedTask2";

	}

	@RequestMapping(value = "/saveUploadAdditionalFile")
	public String saveUploadAdditionalFile(ModelMap modelMap,
			@ModelAttribute("nCLTTaskDTO") NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 28;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		if (criminalTaskDto.getAccusedIdtest() == null) {
			bindResult.rejectValue("accusedIdtest", "errmsg.required");
		}

		if (criminalTaskDto.getFile1().isEmpty()) {
			bindResult.rejectValue("file1", "errmsg.required");
		}

		AddAccused accusedId = criminalTaskDto.getAccusedIdtest();

		if (bindResult.hasErrors()) {
			officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/NCLTtaskPage";
		}
		MultipartFile file = criminalTaskDto.getFile1();

		UploadAdditionalFilesDetails uploadAdditionalFilesDetails = new UploadAdditionalFilesDetails();

		Long id1;
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if (criminalTaskDto.getUploadID() != null) {
			uploadAdditionalFilesDetails.setId(criminalTaskDto.getUploadID());
			id1 = criminalTaskDto.getUploadID();
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.Filingdetailupdate") + " " + " and Investigation Number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		} else {
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.Filingdetailsave") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			id1 = (uploadAdditionalFilesDetailsRepo.findMaxid() != null)
					? (uploadAdditionalFilesDetailsRepo.findMaxid() + 1)
					: 1;
		}

		/*
		 * Long id1 = (uploadAdditionalFilesDetailsRepo.findMaxid() != null) ?
		 * (uploadAdditionalFilesDetailsRepo.findMaxid() + 1) : 1;
		 */

		String orignalfilenameOrderCopyOfTransfer = file.getOriginalFilename();
		String fileExt = orignalfilenameOrderCopyOfTransfer
				.substring(orignalfilenameOrderCopyOfTransfer.lastIndexOf("."));
		String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");
		String filename = id1 + "_" + result;
		caseFileUpload(file, filename);
		uploadAdditionalFilesDetails.setUser(userdet);

		// AddAccused addAccused = addAccusedRepo.findById(0l).get();
		uploadAdditionalFilesDetails.setAccusedId(accusedId);
		// uploadAdditionalFilesDetails.setMisRespondent(criminalTaskDto.getMisRespondent());
		uploadAdditionalFilesDetails.setFileName(filename);
		uploadAdditionalFilesDetails.setAssignedTaskPuhdtl(assignedTaskPuh);

		uploadAdditionalFilesDetailsRepo.save(uploadAdditionalFilesDetails);
		redirect.addAttribute("tabId", tabId);
		redirect.addFlashAttribute("message", "Filing Details saved Successfully  :");
		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);
		return "redirect:/proceedTask3";
	}

	public void caseFileUpload(@RequestParam("file") MultipartFile file, String name) {
		BufferedOutputStream stream = null;

		try {
			// Get the parent directory of the filePath (use whatever base path you're
			// working with)
			File parent = new File(filePath).getParentFile().getCanonicalFile();

			// 1. Validate directory path - Ensure the parent directory exists and is valid
			String directory = validateDirectoryPath(filePath, parent);

			// 2. Validate the file name - Ensure the name is valid (no special characters
			// or invalid characters for filenames)
			if (!isValidFileName(name)) {
				throw new IllegalArgumentException("Invalid file name: " + name);
			}

			// Construct the complete file path (using the parent directory and file name)
			String filepath = filePath + File.separator + name.trim();

			// Save the file locally
			stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(file.getBytes());
			stream.close();

		} catch (Exception e) {
			// Log the error
			logger.info(e.getMessage());
		} finally {
			// Ensure the stream is safely closed
			if (stream != null) {
				safeClose(stream);
			}
		}
	}

	// Validate the directory path
	private String validateDirectoryPath(String filePath, File parent) throws IllegalArgumentException {
		Path path = parent.toPath(); // Convert File to Path

		// Ensure the parent directory exists
		if (!Files.exists(path)) {
			throw new IllegalArgumentException("Parent directory does not exist: " + parent.getAbsolutePath());
		}

		// If the path exists and is a directory, return it; otherwise, throw an error
		if (Files.isDirectory(path)) {
			return path.toString(); // Return the valid directory
		} else {
			throw new IllegalArgumentException("Invalid directory path: " + filePath);
		}
	}

	// Validate the file name using a regular expression (to check if it contains
	// invalid characters)
	private boolean isValidFileName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return false;
		}

		// Regex to check if the file name contains invalid characters like / \ : * ? "
		// < > |
		String fileNamePattern = "^[^<>:\"/\\|?*]+$";
		Pattern pattern = Pattern.compile(fileNamePattern);
		Matcher matcher = pattern.matcher(name.trim());
		return matcher.matches();
	}

	// Utility to safely close streams
	private void safeClose(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			// Handle or log the error during closing
			logger.info("Error while closing stream: " + e.getMessage());
		}
	}

	public void isValidFile(String string, MultipartFile file, Errors errors) throws IOException {

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

}