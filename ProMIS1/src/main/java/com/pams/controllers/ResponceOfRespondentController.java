package com.pams.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTActofRespondantDTO;
import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.FreezerAssetOrder;
import com.pams.entity.ResponseOfRespondent;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.ResponseOfRespondentRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
import com.pams.validation.ProMISValidator;

@Controller
public class ResponceOfRespondentController {
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 200 MB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size

	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	ResponseOfRespondentRepository responseOfRespondentRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private OfficeControllerNCLT officerControl;
	@Autowired
	private AddAccusedRepository addAccusedRepository;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;

	@PostMapping("/responceOfRespondent")
	public String getCourtCase(Model model, @ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) {
		ResponseOfRespondent responseOfRespondent = new ResponseOfRespondent();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		List<ResponseOfRespondent> responselist = responseOfRespondentRepo.findAllByProcourtdtlAndAssignedTask(
				procasedetails, assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		List<AddAccused> findAll = addAccusedRepository.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		responseOfRespondent.setAssignedTask(assignedTaskPuh);
		responseOfRespondent.setProcourtdtl(procasedetails);
		model.addAttribute("responseOfRespondent", responseOfRespondent);
		model.addAttribute("responselist", responselist);
		return "Prosecutor/responceRespondent";
	}

	@RequestMapping(value = "saveResponseOfRespond")
	public String updateResponseOfRespond(
			@ModelAttribute(value = "responseOfRespondent") ResponseOfRespondent responseOfRespondent, ModelMap model,
			RedirectAttributes redirect, BindingResult bindResult) throws Exception {

		Long id1 = responseOfRespondent.getId();
		ProCourtCaseDetails procourtdtl = responseOfRespondent.getProcourtdtl();
		AssignedTaskPuhAfterCOurt assignedTask = responseOfRespondent.getAssignedTask();
		// ResponseOfRespondent responseOfRespondent = new ResponseOfRespondent();
		List<ResponseOfRespondent> responselist = responseOfRespondentRepo
				.findAllByProcourtdtlAndAssignedTask(procourtdtl, assignedTask, Sort.by(Sort.Direction.DESC, "id"));
		ProMISValidator proValidation = new ProMISValidator();
		if (responseOfRespondent.isReplyFiled() == true && responseOfRespondent.getDateRecept() == null) {
			// bindResult.rejectValue("replyFiled", "errmsg.required");
			bindResult.rejectValue("dateRecept", "errmsg.required");
			if (!responseOfRespondent.getRemarks().trim().equals("")) {
				proValidation.isvalidBrifeHD("remarks", responseOfRespondent.getRemarks(), bindResult, "errmsg.remarks", false);
			}
		}

		if (responseOfRespondent.getAddResponse() == null) {
			bindResult.rejectValue("addResponse", "errmsg.required");
		}

		/*
		 * if (responseOfRespondent.getApplicationType().isEmpty()) {
		 * bindResult.rejectValue("applicationType", "errmsg.required"); }
		 */
		if (responseOfRespondent.getDateOfApplication() == null) {
			bindResult.rejectValue("dateOfApplication", "errmsg.required");
		}
		if (responseOfRespondent.getDateOfRecieptApp() == null) {
			bindResult.rejectValue("dateOfRecieptApp", "errmsg.required");
		}
		if (responseOfRespondent.getDateOfReply() == null) {
			bindResult.rejectValue("dateOfReply", "errmsg.required");
		}
		if (id1 == null || (!responseOfRespondent.getRRFileName1().isEmpty())) {
			isValidFile(responseOfRespondent.getRRFileName1(), bindResult, true, "rRFileName1");
		}
		if (bindResult.hasErrors()) {
			List<AddAccused> findAll = addAccusedRepository.findAllByAssignedTask(assignedTask,
					Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("allRespondentList", findAll);
			model.addAttribute("responseOfRespondent", responseOfRespondent);
			model.addAttribute("responselist", responselist);
			return "Prosecutor/responceRespondent";
		}
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		String filename = "";
		if ((!responseOfRespondent.getRRFileName1().isEmpty())) {
			MultipartFile file = responseOfRespondent.getRRFileName1();
			String orignalfilenameOrderCopyOfTransfer = file.getOriginalFilename();
			String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");
			Long id2 = (responseOfRespondentRepo.findMaxid() != null) ? (responseOfRespondentRepo.findMaxid() + 1) : 1;
			if (id1 != null) {
				responseOfRespondent.setId(id1);
				filename = id1 + "_" + result;
			} else {
				filename = id2 + "_" + result;
			}
			officerControl.caseFileUpload(file, filename);
			responseOfRespondent.setFileName(filename);
		}
		responseOfRespondent.setCreatedBy(userdet);
		responseOfRespondent.setCreatedDate(new java.util.Date());
		responseOfRespondent.setUpdatedBy(userdet);
		responseOfRespondent.setUpdatedDate(new java.util.Date());
		responseOfRespondent.setApprovalStatus(0);
		if (id1 == null) {

			redirect.addFlashAttribute("message", "ResponseOfRespondent Added Successfully :");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.responseofrespondentsave") + " " + " and Investigation number is "
							+ assignedTask.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTask.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		} else {

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.responseofrespondentupdate") + " " + " and Investigation Number is "
							+ assignedTask.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTask.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			redirect.addFlashAttribute("message", "ResponseOfRespondent Updated Successfully :");
		}
		responseOfRespondentRepo.save(responseOfRespondent);

		redirect.addFlashAttribute("responseOfRespondent", responseOfRespondent);

		return "redirect:/respondant";

	}

	@GetMapping("/respondant")
	public String responseOfRespondent(Model model,
			@ModelAttribute("responseOfRespondent") ResponseOfRespondent responseOfRespondent1) {

		ResponseOfRespondent responseOfRespondent = new ResponseOfRespondent();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = responseOfRespondent1.getAssignedTask();

		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		List<ResponseOfRespondent> responselist = responseOfRespondentRepo.findAllByProcourtdtlAndAssignedTask(
				procasedetails, assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		List<AddAccused> findAll = addAccusedRepository.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);

		responseOfRespondent.setAssignedTask(assignedTaskPuh);
		responseOfRespondent.setProcourtdtl(procasedetails);
		model.addAttribute("responseOfRespondent", responseOfRespondent);
		model.addAttribute("responselist", responselist);

		return "Prosecutor/responceRespondent";
	}

	@RequestMapping(value = "forwardResponcOfRespondent")
	public String forwardResponcOfRespondent(
			@RequestParam(value = "forwardResponcOfRespondent", required = true) Long id, ModelMap model)
			throws Exception {
		ResponseOfRespondent responseOfRespondent = responseOfRespondentRepo.findById(id).get();
		responseOfRespondent.setApprovalStatus(2);
		responseOfRespondentRepo.save(responseOfRespondent);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.responseofrespondentforward") + " "
						+ responseOfRespondent.getAssignedTask().getUser().getSalutation() + " "
						+ responseOfRespondent.getAssignedTask().getUser().getFirstName() + " "
						+ (responseOfRespondent.getAssignedTask().getUser().getMiddleName().equals("") ? ""
								: responseOfRespondent.getAssignedTask().getUser().getMiddleName() + "")
						+ responseOfRespondent.getAssignedTask().getUser().getLastName() + " "
						+ " and investigation number is "
						+ responseOfRespondent.getAssignedTask().getProCourtCaseDetails().getAddCase()
								.getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", responseOfRespondent.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		List<ResponseOfRespondent> responselist1 = responseOfRespondentRepo.findAllByProcourtdtlAndAssignedTask(
				responseOfRespondent.getProcourtdtl(), responseOfRespondent.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		ResponseOfRespondent responseOfRespondent1 = new ResponseOfRespondent();
		responseOfRespondent1.setAssignedTask(responseOfRespondent.getAssignedTask());
		responseOfRespondent1.setProcourtdtl(responseOfRespondent.getProcourtdtl());
		List<AddAccused> findAll = addAccusedRepository.findAllByAssignedTask(responseOfRespondent.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("message", " ResponseOfRespondent Forwarded Successfully : ");
		model.addAttribute("responseOfRespondent", responseOfRespondent1);
		model.addAttribute("responselist", responselist1);
		return "Prosecutor/responceRespondent";
	}

	@RequestMapping(value = "editResponcOfRespondent")

	public String editResponcOfRespondent(@RequestParam(value = "editResponcOfRespondent", required = true) Long id,
			ModelMap model) {

		ResponseOfRespondent responseofresponse = responseOfRespondentRepo.findById(id).get();
		List<AddAccused> findAll = addAccusedRepository.findAllByAssignedTask(responseofresponse.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);

		List<ResponseOfRespondent> findAllByProcourtdtlAndAssignedTaskId = responseOfRespondentRepo
				.findAllByProcourtdtlAndAssignedTask(responseofresponse.getProcourtdtl(),
						responseofresponse.getAssignedTask(), Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("responseOfRespondent", responseofresponse);
		model.addAttribute("responselist", findAllByProcourtdtlAndAssignedTaskId);
		return "Prosecutor/responceRespondent";
	}

	@RequestMapping(value = "backToMainPage")
	public String backToAccused(ModelMap model, @ModelAttribute ResponseOfRespondent responseOfRespondent,
			BindingResult errors) {

		int tabId = 21;
		officerControl.modelAttributeObject(responseOfRespondent.getAssignedTask(), model, tabId, new NCLTTaskDTO());

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "backToMain")
	public String backToMain(ModelMap model, @ModelAttribute NCLTActofRespondantDTO nCLTActofRespondantDTO,
			BindingResult errors) {

		int tabId = 21;
		officerControl.modelAttributeObject(nCLTActofRespondantDTO.getAssignedTask(), model, tabId, new NCLTTaskDTO());

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "backfromFreezer")
	public String backfromFreezer(ModelMap model, @ModelAttribute FreezerAssetOrder responseOfRespondent,
			BindingResult errors) {

		int tabId = 21;
		officerControl.modelAttributeObject(responseOfRespondent.getAssignedTask(), model, tabId, new NCLTTaskDTO());

		return "Task/NCLTtaskPage";

	}

	public void isValidFile(MultipartFile file, BindingResult errors, boolean isRequired, String errFieldName) {
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

}
