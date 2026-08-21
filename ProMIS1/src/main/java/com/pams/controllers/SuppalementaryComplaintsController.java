package com.pams.controllers;

import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.AppRoleDAO;
import com.pams.dao.ChargeInstaceSubDAO;
import com.pams.dto.CriminalTaskDto;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Disposed;
import com.pams.entity.PairaviOfficer;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.SupplementaryComplaint;
import com.pams.entity.UserDetails;
import com.pams.service.AccusedStatusNewRepository;
import com.pams.service.ActCompundRelevantSectionRepo;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.ChanrgeInstanceRepository;
import com.pams.service.ChargeInstaceSubRepository;
import com.pams.service.ChargeRepository;
import com.pams.service.CouncilDetailsRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.CreateTasksRepository;
import com.pams.service.DischargeRepository;
import com.pams.service.DisposedRepository;
import com.pams.service.InstanceRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.PunishmentRepository;
import com.pams.service.StateRepository;
import com.pams.service.SupplementaryComplaintRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;

@Controller
public class SuppalementaryComplaintsController {

	@Autowired
	private PairaviOfficerRepository officersRepo;
	@Autowired
	private CouncilDetailsRepository councelRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepository;
	@Autowired
	private ChanrgeInstanceRepository chargeInstanceRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhAfterCOurtRepository;
	@Autowired
	private SupplementaryComplaintRepository supCompRepo;

	@Autowired
	private InstanceRepository instanceRepository;
	@Autowired
	PunishmentRepository punishmentRepository;
	@Autowired
	CourtTypeRepository courtTypeRepository;
	@Autowired
	private AccusedCompDAO accusedCompDAO;
	@Autowired
	private AddActRepository addActRepo;
	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private AccusedStatusNewRepository accusedStatusNewRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepos;
	@Autowired
	private ActCompundRelevantSectionRepo actCompundRelevantSectionRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private CreateTasksRepository createTasksRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private OfficerController officerControl;
	@Autowired
	private ChargeInstaceSubRepository chargeInstaceSubRepository;
	@Autowired
	private ChargeRepository chargeRepository;
	@Autowired
	private DischargeRepository dischargeRepository;
	@Autowired
	private ChargeInstaceSubDAO subDAO;
	@Autowired
	SupplementaryComplaintRepository suppCompRepo;
	@Autowired
	private DisposedRepository disposedRepo;

	@PostMapping("/supplimentaryStatuscc")
	public String supplimentaryStatuscc(Model modelMap,
			@ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTask = criminalTaskDto.getAssignedTask();
		// System.out.println(assignedTask);
		ProCourtCaseDetails proCourtCaseDetails = assignedTask.getProCourtCaseDetails();
		modelMap.addAttribute("assignedDtl", assignedTask);
		modelMap.addAttribute("procourtdtl", proCourtCaseDetails);

		SupplementaryComplaint supplementaryComplaint = new SupplementaryComplaint();
		supplementaryComplaint.setAssignedTask(assignedTask);
		supplementaryComplaint.setProcourtdtl(proCourtCaseDetails);
		modelMap.addAttribute("supplementaryComplaint", supplementaryComplaint);

		List<AddAccused> allByAssignedTask = addAccusedRepository.findAllByAssignedTask(assignedTask);
		List<SupplementaryComplaint> byAssignedTask = suppCompRepo.findByAssignedTask(assignedTask);
		modelMap.addAttribute("accusedList1", allByAssignedTask);
		modelMap.addAttribute("byAssignedTask", byAssignedTask);
		
		return "Prosecutor/SupplementaryNewPage";
	}
	@PostMapping("/disposedStatuscriminal")
	public String disposedStatuscrimi(Model modelMap,@ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) throws Exception {
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		AssignedTaskPuhAfterCOurt assignedTask = criminalTaskDto.getAssignedTask();
		ProCourtCaseDetails proCourtCaseDetails = assignedTask.getProCourtCaseDetails();
		modelMap.addAttribute("assignedDtl", assignedTask);
		modelMap.addAttribute("procourtdtl", proCourtCaseDetails);
		Disposed disposed = new Disposed();
		disposed.setAssignedTask(assignedTask);
		disposed.setProcourtdtl(proCourtCaseDetails);
		List<AddAccused> accusedList = addAccusedRepos.findAllByProcourtdtlAndAssignedTask(proCourtCaseDetails,
				assignedTask, Sort.by(Sort.Direction.DESC, "id"));
		
		modelMap.addAttribute("personList", accusedList);
		
		modelMap.addAttribute("councelRepo", officersRepo.findByType(2, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("pairaviOfficerList",officersRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("disposed", disposed);
		List<AddAccused> allByAssignedTask = addAccusedRepository.findAllByAssignedTask(assignedTask);
		List<SupplementaryComplaint> byAssignedTask = suppCompRepo.findByAssignedTask(assignedTask);
		modelMap.addAttribute("accusedList1", allByAssignedTask);
		modelMap.addAttribute("byAssignedTask", byAssignedTask);
		List<Disposed> byAssignedTaskAndProcourtdtlAndCreatedBy = disposedRepo.findByProcourtdtlAndCreatedBy(proCourtCaseDetails, userdet);
		modelMap.addAttribute("byAssignedTaskAndProcourtdtlAndCreatedBy", byAssignedTaskAndProcourtdtlAndCreatedBy);
		return "Prosecutor/disposedNewPage";
	}
	
	@GetMapping("/disposedStatuscriminalView")
	public String disposedStatusView(Model modelMap, @RequestParam("assignedTaskId") Long assignedTaskId) throws Exception {
	 
		//AssignedTaskPuhAfterCOurt assignedTask = criminalTaskDto.getAssignedTask();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		AssignedTaskPuhAfterCOurt assignedTask = assignedTaskPuhAfterCOurtRepository.findById(assignedTaskId).get();
		ProCourtCaseDetails proCourtCaseDetails = assignedTask.getProCourtCaseDetails();
		modelMap.addAttribute("assignedDtl", assignedTask);
		modelMap.addAttribute("procourtdtl", proCourtCaseDetails);
		Disposed disposed = new Disposed();
		disposed.setAssignedTask(assignedTask);
		disposed.setProcourtdtl(proCourtCaseDetails);
		List<AddAccused> accusedList = addAccusedRepos.findAllByProcourtdtlAndAssignedTask(proCourtCaseDetails,
				assignedTask, Sort.by(Sort.Direction.DESC, "id"));
		
		modelMap.addAttribute("personList", accusedList);
		
		modelMap.addAttribute("councelRepo", officersRepo.findByType(2, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("pairaviOfficerList",officersRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("disposed", disposed);
		
		List<AddAccused> allByAssignedTask = addAccusedRepository.findAllByAssignedTask(assignedTask);
		List<SupplementaryComplaint> byAssignedTask = suppCompRepo.findByAssignedTask(assignedTask);
		modelMap.addAttribute("accusedList1", allByAssignedTask);
		modelMap.addAttribute("byAssignedTask", byAssignedTask);
		List<Disposed> byAssignedTaskAndProcourtdtlAndCreatedBy = disposedRepo.findByProcourtdtlAndCreatedBy(proCourtCaseDetails, userdet);
		modelMap.addAttribute("byAssignedTaskAndProcourtdtlAndCreatedBy", byAssignedTaskAndProcourtdtlAndCreatedBy);
		
		
		return "Prosecutor/disposedNewPage";
		
		
	}
	
	
	
	
	
	
	
	@GetMapping("/supplimentaryStatusRedirect")
	public String supplimentaryStatusccAASss(Model modelMap,
	        @ModelAttribute("supplementaryComplaint") SupplementaryComplaint supplementaryComplaint) throws Exception {

	    AssignedTaskPuhAfterCOurt assignedTask = supplementaryComplaint.getAssignedTask();

	    List<AddAccused> accusedList = addAccusedRepository.findAllByAssignedTask(assignedTask);
	    List<SupplementaryComplaint> byAssignedTask = suppCompRepo.findByAssignedTask(assignedTask);

	    modelMap.addAttribute("accusedList1", accusedList);
	    modelMap.addAttribute("byAssignedTask", byAssignedTask);
	    modelMap.addAttribute("supplementaryComplaint", supplementaryComplaint);
	    return "Prosecutor/SupplementaryNewPage";
	}
	
	@GetMapping("/afterSaveSupplementory")
	public String afterSaveSupplimentaryStatuscc(
	        @RequestParam("id") Long id,
	        Model modelMap) throws Exception {

	    SupplementaryComplaint supplementaryComplaint =
	            supCompRepo.findById(id).orElseThrow();

	    AssignedTaskPuhAfterCOurt assignedTask =
	            supplementaryComplaint.getAssignedTask();

	    ProCourtCaseDetails proCourtCaseDetails =
	            assignedTask.getProCourtCaseDetails();

	    modelMap.addAttribute("assignedDtl", assignedTask);
	    modelMap.addAttribute("procourtdtl", proCourtCaseDetails);
	    modelMap.addAttribute("supplementaryComplaint", supplementaryComplaint);

	    List<AddAccused> allByAssignedTask =
	            addAccusedRepository.findAllByAssignedTask(assignedTask);

	    modelMap.addAttribute("accusedList", allByAssignedTask);

	    return "Prosecutor/SupplementaryNewPage";
	}

	@RequestMapping(value = "backFromSupplimentory")
	public String backToCriminalaFromSupplimentory(
			@ModelAttribute(value = "supplementaryComplaint") SupplementaryComplaint complaint,
			BindingResult bindResult, ModelMap model, RedirectAttributes redirect) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = complaint.getAssignedTask();
		int tabId = 21;

		redirect.addAttribute("tabId", tabId);
		redirect.addAttribute("assignTaskID", assignedTaskPuh.getId());

		return "redirect:/additionalDetails";
	}

	@PostMapping("/saveSupplementary")
	public String saveSupplementary(
	        @Valid @ModelAttribute("supplementaryComplaint")
	        SupplementaryComplaint complaint,
	        BindingResult result,
	        Model model,RedirectAttributes redirect) throws Exception {

	    if (result.hasErrors()) {

	        model.addAttribute("accusedList",
	                addAccusedRepository.findAllByAssignedTask(
	                        complaint.getAssignedTask()));

	        return "Prosecutor/SupplementaryNewPage";
	    }

	    complaint.setCreatedDate(new Date());
	    complaint.setUpdatedDate(new Date());

	    UserDetails userdet =
	            useDetailRepo.findAllByEmail(
	                    userDetailsService.getUserDetails().getUserName());

	    complaint.setCreatedBy(userdet);
	    complaint.setUpdatedBy(userdet);

	    supCompRepo.save(complaint);

	    // 👇 Direct model me object bhej do
	    model.addAttribute("supplementaryComplaint", new SupplementaryComplaint());

	    AssignedTaskPuhAfterCOurt assignedTask =
	            complaint.getAssignedTask();

	    ProCourtCaseDetails proCourtCaseDetails =
	            assignedTask.getProCourtCaseDetails();

	    model.addAttribute("assignedDtl", assignedTask);
	    model.addAttribute("procourtdtl", proCourtCaseDetails);
	    SupplementaryComplaint supplementaryComplaint = new SupplementaryComplaint();
	    
	    supplementaryComplaint.setAssignedTask(assignedTask);
	    supplementaryComplaint.setProcourtdtl(proCourtCaseDetails);
	    model.addAttribute("supplementaryComplaint", supplementaryComplaint);
	    List<AddAccused> allByAssignedTask =
	            addAccusedRepository.findAllByAssignedTask(assignedTask);

	   //model.addAttribute("accusedList", allByAssignedTask);
	    redirect.addFlashAttribute("supplementaryComplaint", supplementaryComplaint); 
	    redirect.addFlashAttribute("message",
	            "Supplementary Complaint Saved Successfully");
	    //List<AddAccused> allByAssignedTask = addAccusedRepository.findAllByAssignedTask(assignedTask);
		List<SupplementaryComplaint> byAssignedTask = suppCompRepo.findByAssignedTask(assignedTask);
		model.addAttribute("accusedList1", allByAssignedTask);
		model.addAttribute("byAssignedTask", byAssignedTask);
		return "redirect:/supplimentaryStatusRedirect";
	    //return "Prosecutor/SupplementaryNewPage";
	}

}
