package com.pams.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.MiscellaneousFile;
import com.pams.entity.UploadAdditionalFilesDetails;
import com.pams.entity.UserDetails;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.MiscellaneousFileRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;

@Controller
public class MiscellaneousFileController {
	private static final Logger logger = LoggerFactory.getLogger(OfficerController.class);

	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 200 MB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size

	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private MiscellaneousFileRepository misFileRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepo1;
	@Autowired

	OfficeControllerNCLT officerCont;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired

	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UploadAdditionalFileController uploadController;
	
	@RequestMapping(value = "/saveSfioFile")
	public String saveUploadAdditionalFile(ModelMap modelMap,
			@ModelAttribute("nCLTTaskDTO") NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect,
			BindingResult bindResult) throws Exception {
		int tabId = 33;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		if (criminalTaskDto.getMisRespondent() == null) {
			bindResult.rejectValue("misRespondent", "errmsg.required");
		}
		if (criminalTaskDto.getMisFile1().isEmpty()) {
			bindResult.rejectValue("misFile1", "errmsg.required");
		}
		if (bindResult.hasErrors()) {
			officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/NCLTtaskPage";
		}
		MultipartFile file = criminalTaskDto.getMisFile1();

		MiscellaneousFile misFile = new MiscellaneousFile();

		long id1;
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if (criminalTaskDto.getMisUploadID() != null) {
			misFile.setId(criminalTaskDto.getMisUploadID());
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
			id1 = (misFileRepo.findMaxid() != null)
					? (misFileRepo.findMaxid() + 1)
					: 1;
		}
		String orignalfilenameOrderCopyOfTransfer = file.getOriginalFilename();
		String fileExt = orignalfilenameOrderCopyOfTransfer
				.substring(orignalfilenameOrderCopyOfTransfer.lastIndexOf("."));
		String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");
		String filename = id1 + "_" + result;
		uploadController.caseFileUpload(file, filename);
		misFile.setCreatedBy(userdet);
		misFile.setAssignedTask(assignedTaskPuh);
		misFile.setSfiorespondentfile(criminalTaskDto.getSfiorespondentfile());
		misFile.setMisRespondent(criminalTaskDto.getMisRespondent());
		misFile.setProcourtdtl(criminalTaskDto.getProCourtDtl());
		misFile.setFileName(filename);
		misFile.setApproveStatus(0);
		misFile.setCreatedDate(new Date());
		misFileRepo.save(misFile);
		redirect.addAttribute("tabId", tabId);
		redirect.addFlashAttribute("message", "SFIO / Other File saved Successfully  :");
		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);
		return "redirect:/proceedTask3";

	}
	@RequestMapping(value = "/addCriminalDtl", params = "editSfioFileUploade")
	public String editSfioFileUploade(@RequestParam(value = "editSfioFileUploade", required = true) Long id, ModelMap modelMap,
			@ModelAttribute NCLTTaskDTO criminalTaskDto, RedirectAttributes redirect, BindingResult bindResult)
			throws Exception {
		int tabId = 33;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		MiscellaneousFile uploadfile = misFileRepo.findById(id).get();
		criminalTaskDto.setMisUploadID(id);
		criminalTaskDto.setSfiorespondentfile(uploadfile.getSfiorespondentfile());
		criminalTaskDto.setMisRespondent(uploadfile.getMisRespondent());
		criminalTaskDto.setMisFile1(uploadfile.getFile());
		officerCont.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}
	
	@RequestMapping(value = "/addCriminalDtl", params = "confirmSfioFileUploade")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String confirmSfioFileUploade(
			@RequestParam(value = "confirmSfioFileUploade", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) throws Exception {
		int tabId = 33;
		
		MiscellaneousFile miscellaneousFile = misFileRepo.findById(id).get();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		miscellaneousFile.setApproveStatus(2);
		misFileRepo.save(miscellaneousFile);
		model.addAttribute("message", "Respondent SFIO File details Confirmed Successfully.");

		officerCont.modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTtaskPage";

	}
	
	
}
