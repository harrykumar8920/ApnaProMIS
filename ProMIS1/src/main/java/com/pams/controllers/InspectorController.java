package com.pams.controllers;

import java.util.Date;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Inspector;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.InspectorRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
import com.pams.validation.InspectorValidation;

@Controller
public class InspectorController {

	@Autowired
	private InspectorRepository inspectorRepository;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private AddDesignationRepository designationRepo;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;
	@Autowired
	private OfficerController officerControl;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private OfficeControllerNCLT officerControl1;

	@RequestMapping(value = "backFromInspector")
	public String backtomain(@ModelAttribute(value = "inspector") Inspector inspector, BindingResult bindResult,
			ModelMap model, RedirectAttributes redirect) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = inspector.getAssignedTask();
		int tabId = 21;

		redirect.addAttribute("tabId", tabId);
		//redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);
		if(assignedTaskPuh.getProCourtCaseDetails().getType().getId()==1) {
			redirect.addAttribute("assignTaskIDNCLT", assignedTaskPuh.getId());
		}else {
			redirect.addAttribute("assignTaskID", assignedTaskPuh.getId());
		}
		
		return "redirect:/additionalDetails";
	}

	@PostMapping("/addinspector")
	public String getCourtCase(Model model, @ModelAttribute CriminalTaskDto criminalTaskDto) {

		Inspector inspector = new Inspector();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		// List<AddDesignation> sfioOfficerDesignation =
		// designationRepo.findByDeginationtype("SFIO Officer");
		inspector.setProcourtdtl(procasedetails);
		inspector.setAssignedTask(assignedTaskPuh);
		model.addAttribute("inspector", inspector);
		// model.addAttribute("insDesignation", sfioOfficerDesignation);
		model.addAttribute("pairaviOfficerList", pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		model.addAttribute("inspectorList",
				inspectorRepository.findByAssignedTask(assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id")));

		return "Prosecutor/Inspectors";

	}

	@GetMapping("/inspectorAdd")
	public String addinspector1(Model model, @ModelAttribute(value = "inspector") Inspector inspector1) {

		Inspector inspector = new Inspector();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = inspector1.getAssignedTask();
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		// List<AddDesignation> sfioOfficerDesignation =
		// designationRepo.findByDeginationtype("SFIO Officer");
		inspector.setProcourtdtl(procasedetails);
		inspector.setAssignedTask(assignedTaskPuh);
		model.addAttribute("inspector", inspector);
		// model.addAttribute("insDesignation", sfioOfficerDesignation);
		model.addAttribute("pairaviOfficerList", pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		model.addAttribute("inspectorList",
				inspectorRepository.findByAssignedTask(assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id")));

		return "Prosecutor/Inspectors";

	}

	@RequestMapping(value = "/saveInspector", method = RequestMethod.GET)
	public String editInspector(@RequestParam("editInspector") Long id, Model model) {
		Inspector inspector = inspectorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid inspector Id:" + id));
		model.addAttribute("inspector", inspector);
		model.addAttribute("pairaviOfficerList", pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		model.addAttribute("inspectorList", inspectorRepository.findByAssignedTask(inspector.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id")));
		return "Prosecutor/Inspectors";
	}

	@RequestMapping(value = "/confirmInspector", method = RequestMethod.GET)
	public String confirmInspector(@RequestParam("conInspector") Long id, Model model) {
		Inspector inspector = inspectorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid inspector Id:" + id));

		inspector.setApprovalStatus(2);
		inspectorRepository.save(inspector);
		Inspector inspector1 = new Inspector();
		inspector1.setProcourtdtl(inspector.getProcourtdtl());
		inspector1.setAssignedTask(inspector.getAssignedTask());
		model.addAttribute("inspector", inspector1);
		model.addAttribute("pairaviOfficerList", pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		model.addAttribute("inspectorList", inspectorRepository.findByAssignedTask(inspector.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id")));
		model.addAttribute("message", "Confirmed Successfully");
		return "Prosecutor/Inspectors";
	}

	@PostMapping("/saveInspector")
	public String saveInspectorDet(@ModelAttribute Inspector inspector, BindingResult bindResult, Model model,
			RedirectAttributes redirect) throws Exception {

		Long id;
		if (inspector.getId() == null) {
			id = inspectorRepository.findMaxid();
		} else {
			id = inspector.getId();
		}

		InspectorValidation inspValid = new InspectorValidation();
		inspValid.inspectorValid(inspector, bindResult);

		if (bindResult.hasErrors()) {
			model.addAttribute("inspector", inspector);

			// model.addAttribute("insDesignation",
			// designationRepo.findByDeginationtype("SFIO Officer"));
			model.addAttribute("pairaviOfficerList",
					pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
			model.addAttribute("inspectorList", inspectorRepository.findByAssignedTask(inspector.getAssignedTask(),
					Sort.by(Sort.Direction.DESC, "id")));
			return "Prosecutor/Inspectors";
		}

		if (!inspector.getOrderFile().isEmpty()) {
			String orderFileName = "ispectororder" + id + ".pdf";
			MultipartFile orderfile = inspector.getOrderFile();
			inspector.setOrderFileName(orderFileName);

			officerControl.caseFileUpload(orderfile, orderFileName);
		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		inspector.setCreatedBy(userdet);
		inspector.setAprovedBy(userdet);
		inspector.setCreatedDate(new Date());
		inspectorRepository.save(inspector);
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Save"),
				utils.getMessage("log.login.addcasesInspectorsaved") + " " + " and Investigation number is "
						+ inspector.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", inspector.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		redirect.addFlashAttribute("message", "Save Successfully");
		// redirect.addAttribute("inspector", inspector);
		redirect.addFlashAttribute("inspector", inspector);

		return "redirect:/inspectorAdd";

	}

}
