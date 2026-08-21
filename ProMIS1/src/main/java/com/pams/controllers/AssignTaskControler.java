package com.pams.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AppRoleDAO;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddCase;
import com.pams.entity.AddCourt;
import com.pams.entity.AddSubTask;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseForwardHistory;
import com.pams.entity.CreateTasks;
import com.pams.entity.SfioAs;
import com.pams.entity.Type;
import com.pams.entity.UnitDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddCaseRepository;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.AppRoleRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.CaseForwardHistoryRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.CreateTasksRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.SfioAsRepository;
import com.pams.service.SubTaskRepository;
import com.pams.service.UnitDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.utils.Utils;
import com.pams.validation.PuhValidation;

import jakarta.validation.Valid;

@Controller
public class AssignTaskControler {
	
	private final CaseForwardHistoryRepository caseForwardHistoryRepository;

    public AssignTaskControler(CaseForwardHistoryRepository caseForwardHistoryRepository) {
        this.caseForwardHistoryRepository = caseForwardHistoryRepository;
    }

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AddCaseRepository addCaseRepo;

	@Autowired
	private UserManagementCustom userMangCustom;

	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;

	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepoa;
	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private ActSecDetailsRepository actSecDetailsRepo;

	@Autowired
	private UserDetailsRepository useDetailRepo;

	@Autowired
	private AppRoleRepository appRoleRepository;

	@Autowired
	private AddDesignationRepository addDesignationRepository;

	@Autowired
	private SubTaskRepository subTaskRepo;
	@Autowired
	private AuditBeanBo auditBeanBo;

	@Autowired
	private Utils utils;
	@Autowired
	private CreateTasksRepository createtasksRepo;
	@Autowired
	private SfioAsRepository sfioAsRepo;

	@Autowired
	private AddAccusedRepository addAccusedRepo;

	@Autowired
	AddStatusRepository addStatusRepo;

	@Autowired
	private CourtTypeRepository courtTypeRepo;

	@Autowired
	private UnitDetailsRepository unitDetailsRepo;

	@Autowired
	private AddCaseRepository addCaseRepos;

	@Autowired
	private ProCourtCaseDetailsRepository ProCourtCaseDetailsRepos;

	@SuppressWarnings("null")
	@RequestMapping(value = "/assignTaskSec", params = "assignTaskSec")
	public String viewCaseDetailsSection(ModelMap modelmap,
			@RequestParam(value = "assignTaskSec", required = true) Long id, RedirectAttributes redirect)
			throws Exception {

		List<CreateTasks> TasksLst = null;
		AddCase addCase = addCaseRepos.findById(id).get();
		TasksLst = createtasksRepo.findTopOneData();
		AssignedTaskPuh assignedTaskPuh = new AssignedTaskPuh();
		assignedTaskPuh.setAddCase(addCase);
	
		List<SfioAs> sfiolst = sfioAsRepo.findAll(Sort.by(Sort.Direction.ASC, "SfioAs"));
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<UnitDetails> unitList = unitDetailsRepo.findAll();

		List<UnitDetails> UnitDetails1 = new ArrayList<UnitDetails>();
		int unitcount = 0;
		for (UnitDetails unitList2 : unitList) {
			if (unitList2.getUnitName().equalsIgnoreCase("Administrator Unit")) {

				unitList.remove(unitList2.getUnitId() - 1);
			} else {
				UnitDetails1.add(new UnitDetails(unitList2.getUnitId(), unitList2.getUnitName(),
						unitList2.getLocation(), unitList2.getAddress(), unitList2.getTelephoneNo(),
						unitList2.getFaxNo(), unitList2.getEMail(), unitList2.getCreatedDate()));
			}
			unitcount++;
		}
		List<AssignedTaskPuh> assignedTask = assignedTaskPuhRepo.findAllByAddCase(addCase);

		modelmap.addAttribute("assignedTaskPuh", assignedTaskPuh);
	
		modelmap.addAttribute("TasksLst", TasksLst);
		modelmap.addAttribute("courtType", courtType);

		modelmap.addAttribute("udetails", UnitDetails1);
		modelmap.addAttribute("addCase", addCase);
		modelmap.addAttribute("assignedTask", assignedTask);
		modelmap.addAttribute("sfiolst", sfiolst);

	
		return "caseDetails/assignTaskPuhSec";
	}
	
	

	@SuppressWarnings({ "null", "unused" })
	@RequestMapping(value = "/assignTaskCourt", params = "assignTaskCourt")
	public String viewCaseDetailsCourt(ModelMap modelmap,
			@RequestParam(value = "assignTaskCourt", required = true) Long id, RedirectAttributes redirect)
			throws Exception {

		List<CreateTasks> TasksLst = new ArrayList<>();

		ProCourtCaseDetails orElseThrow = ProCourtCaseDetailsRepos.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		AssignedTaskPuhAfterCOurt assignedTaskPuhAfterCOurt= new AssignedTaskPuhAfterCOurt();
		assignedTaskPuhAfterCOurt.setProCourtCaseDetails(orElseThrow);
		if(orElseThrow.getAssignedTask().getCreateTask().getId()==18) {
			assignedTaskPuhAfterCOurt.setCreateTask(createtasksRepo.findById(11l).get());
			TasksLst.add(createtasksRepo.findById(11l).get());
		}else if(orElseThrow.getAssignedTask().getCreateTask().getId()==19) {
			TasksLst.add(createtasksRepo.findById(2l).get());
		}else if(orElseThrow.getAssignedTask().getCreateTask().getId()==15) {
			TasksLst.add(createtasksRepo.findById(15l).get());
		}else if(orElseThrow.getAssignedTask().getCreateTask().getId()==16) {
			TasksLst.add(createtasksRepo.findById(16l).get());
		}
		List<AssignedTaskPuhAfterCOurt> allByProCourtCaseDetails = assignedTaskPuhRepoa.findAllByProCourtCaseDetails(orElseThrow);
		
		
		List<SfioAs> sfiolst = sfioAsRepo.findAll(Sort.by(Sort.Direction.ASC, "SfioAs"));
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<UnitDetails> unitList = unitDetailsRepo.findAll();
		Type type = orElseThrow.getType();
		
		
		
		List<UnitDetails> UnitDetails1 = new ArrayList<UnitDetails>();
		int unitcount = 0;
		for (UnitDetails unitList2 : unitList) {
			if (unitList2.getUnitName().equalsIgnoreCase("Administrator Unit")) {

				unitList.remove(unitList2.getUnitId() - 1);
			} else {
				UnitDetails1.add(new UnitDetails(unitList2.getUnitId(), unitList2.getUnitName(),
						unitList2.getLocation(), unitList2.getAddress(), unitList2.getTelephoneNo(),
						unitList2.getFaxNo(), unitList2.getEMail(), unitList2.getCreatedDate()));
			}
			unitcount++;
		}
		//List<AssignedTaskPuhAfterCOurt> assignedTask = assignedTaskPuhRepoa.findAllByProCourtCaseDetails(orElseThrow);

		modelmap.addAttribute("assignedTaskPuhAfterCOurt", assignedTaskPuhAfterCOurt);
		modelmap.addAttribute("pcrtdtls", orElseThrow);

		modelmap.addAttribute("TasksLst", TasksLst);
		modelmap.addAttribute("courtType", courtType);

		modelmap.addAttribute("udetails", UnitDetails1);
		modelmap.addAttribute("assignedTask", allByProCourtCaseDetails);
		modelmap.addAttribute("sfiolst", sfiolst);
		return "caseDetails/assignTaskPuhCourt";
	}
	
	@SuppressWarnings({ "null", "unused" })
	@RequestMapping(value = "/assignTaskCourt", params = "assignTaskCourtForward")
	public String assignTaskCourtForward(ModelMap modelmap,
			@RequestParam(value = "assignTaskCourtForward", required = true) Long id, RedirectAttributes redirect)
			throws Exception {

		List<CreateTasks> TasksLst = new ArrayList<>();

		ProCourtCaseDetails orElseThrow = ProCourtCaseDetailsRepos.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		CaseForwardHistory assignedTaskPuhAfterCOurt= new CaseForwardHistory();
		
		assignedTaskPuhAfterCOurt.setProCourtCaseDetails(orElseThrow);
		
		
		List<SfioAs> sfiolst = sfioAsRepo.findAll(Sort.by(Sort.Direction.ASC, "SfioAs"));
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<UnitDetails> unitList = unitDetailsRepo.findAll();
	
		
		
		
		List<UnitDetails> UnitDetails1 = new ArrayList<UnitDetails>();
		int unitcount = 0;
		for (UnitDetails unitList2 : unitList) {
			if (unitList2.getUnitName().equalsIgnoreCase("Administrator Unit")) {

				unitList.remove(unitList2.getUnitId() - 1);
			} else {
				UnitDetails1.add(new UnitDetails(unitList2.getUnitId(), unitList2.getUnitName(),
						unitList2.getLocation(), unitList2.getAddress(), unitList2.getTelephoneNo(),
						unitList2.getFaxNo(), unitList2.getEMail(), unitList2.getCreatedDate()));
			}
			unitcount++;
		}
		
		modelmap.addAttribute("assignedTaskPuhAfterCOurt", assignedTaskPuhAfterCOurt);
		modelmap.addAttribute("pcrtdtls", orElseThrow);




		modelmap.addAttribute("udetails", UnitDetails1);
	
		modelmap.addAttribute("sfiolst", sfiolst);
		return "caseDetails/assignTaskCourtForwardh";
	}

	@GetMapping("/updateCasePriority2")
	public String updateCasePriority2(@RequestParam Long caseId,
	                                 @RequestParam Integer priority) {

		
		ProCourtCaseDetails proCourtCaseDetails = ProCourtCaseDetailsRepos.findById(caseId).get();
	   
	    proCourtCaseDetails.setCaseStatusCheck(priority);
	    ProCourtCaseDetailsRepos.save(proCourtCaseDetails);

	    return "redirect:/totalCourtCaseDtl"; 
	}
	
	

	@SuppressWarnings("null")
	@RequestMapping(value = "/assignTask", params = "assignTask")
	public String viewCaseDetails(ModelMap modelmap, @RequestParam(value = "assignTask", required = true) Long id,
			RedirectAttributes redirect) throws Exception {

		List<CreateTasks> TasksLst = null;
		AddCase addCase = addCaseRepos.findById(id).get();

		List<AssignedTaskPuh> assignTask = assignedTaskPuhRepo.findAllByAddCase(addCase);
		AssignedTaskPuh assignTask1 = null;

		int listsize = assignTask.size();
		if (assignTask.isEmpty()) {
			TasksLst = createtasksRepo.findTopOneData();

		} else if (listsize == 1) {
			assignTask1 = assignTask.get(0);

			if (assignTask1.getIsApproved() == true) {
				/*
				 * if (assignTask1.getAddCase().getType().getType().equals("NCLT")) { TasksLst =
				 * createtasksRepo.findNCLTtask(); } else { TasksLst =
				 * createtasksRepo.findNotNCLTtask(); }
				 */
			} else {
				TasksLst = createtasksRepo.findTopOneData1();
			}

		} else {
			assignTask1 = assignTask.get(0);
			/*
			 * if
			 * (assignTask1.getAddCase().getTypeOfCase().getTypeOfCase().equals("NCLT/NCLAT"
			 * )) { TasksLst = createtasksRepo.findNCLTtask();
			 * 
			 * } else { TasksLst = createtasksRepo.findNotNCLTtask(); }
			 */
		}

		// proCourtCaseDetails pcrtdtls = proCourtCaseDetailsRepo.findALLById(caseId);

		ProCourtCaseDetails pcrtdtls = ProCourtCaseDetailsRepos.findByAddCase(addCase);
		AssignedTaskPuh assignedTaskPuh = new AssignedTaskPuh();
		assignedTaskPuh.setAddCase(addCase);
		// assignedTaskPuh.setProCourtCase(pcrtdtls);
		// assignedTaskPuh.setCourt(pcrtdtls.getCourtType());

		List<SfioAs> sfiolst = sfioAsRepo.findAll(Sort.by(Sort.Direction.ASC, "SfioAs"));
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<UnitDetails> unitList = unitDetailsRepo.findAll();

		List<UnitDetails> UnitDetails1 = new ArrayList<UnitDetails>();
		int unitcount = 0;
		for (UnitDetails unitList2 : unitList) {
			if (unitList2.getUnitName().equalsIgnoreCase("Administrator Unit")) {

				unitList.remove(unitList2.getUnitId() - 1);
			} else {
				UnitDetails1.add(new UnitDetails(unitList2.getUnitId(), unitList2.getUnitName(),
						unitList2.getLocation(), unitList2.getAddress(), unitList2.getTelephoneNo(),
						unitList2.getFaxNo(), unitList2.getEMail(), unitList2.getCreatedDate()));
			}
			unitcount++;
		}
		List<AssignedTaskPuh> assignedTask = assignedTaskPuhRepo.findAllByAddCase(addCase);

		modelmap.addAttribute("assignedTaskPuh", assignedTaskPuh);
		modelmap.addAttribute("pcrtdtls", pcrtdtls);

		modelmap.addAttribute("TasksLst", TasksLst);
		modelmap.addAttribute("courtType", courtType);

		modelmap.addAttribute("udetails", UnitDetails1);
		modelmap.addAttribute("addCase", addCase);
		modelmap.addAttribute("assignedTask", assignedTask);
		modelmap.addAttribute("sfiolst", sfiolst);

		if (pcrtdtls != null) {
			List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndIsActive(pcrtdtls.getId(), 1);
			modelmap.addAttribute("seclist", seclist);
			return "caseDetails/assignTaskPuh";
		} else {
			return "caseDetails/assignTaskPuh1";
		}

	}

	@RequestMapping(value = "/assignedTaskPuh")
	public String assignedTaskPuh(@ModelAttribute @Valid AssignedTaskPuh assignedTaskPuh, BindingResult binding,
			ModelMap modelmap, RedirectAttributes redirect, Long caseId) throws Exception {
		AssignedTaskPuh assignTask = null;
		boolean errorr = false;
	

		AddCase addCase = addCaseRepos.findById(assignedTaskPuh.getAddCase().getId()).get();
		List<CreateTasks> TasksLst = null;

		CreateTasks createTask = assignedTaskPuh.getCreateTask();

		if (createTask == null) {
			System.out.println("jai mata dei");
			modelmap.addAttribute("message", "This Task is must not be empty or null");
			return "caseDetails/assignTaskPuh1";
		}

		
			PuhValidation puh = new PuhValidation();
			puh.validatePuh1(assignedTaskPuh, binding);
			errorr = binding.hasErrors();	

		if (errorr == true ) {
			TasksLst = createtasksRepo.findTopOneData();

			//ProCourtCaseDetails pcrtdtls = ProCourtCaseDetailsRepos.findByAddCase(addCase);			
			List<SfioAs> sfiolst = sfioAsRepo.findAll(Sort.by(Sort.Direction.ASC, "SfioAs"));
			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			List<UnitDetails> udetails = unitDetailsRepo.findAll();
			List<AssignedTaskPuh> assignedTask = assignedTaskPuhRepo.findAllByAddCase(assignedTaskPuh.getAddCase());
			modelmap.addAttribute("addCase", assignedTaskPuh.getAddCase());

			CreateTasks task = assignedTaskPuh.getCreateTask();

			List<AddSubTask> subtask = subTaskRepo.findAllByTask(task);

			modelmap.addAttribute("subtask", subtask);

			modelmap.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelmap.addAttribute("TasksLst", TasksLst);
			modelmap.addAttribute("courtType", courtType);
			//modelmap.addAttribute("pcrtdtls", pcrtdtls);
			modelmap.addAttribute("udetails", udetails);
		
			modelmap.addAttribute("assignedTask", assignedTask);
			modelmap.addAttribute("sfiolst", sfiolst);

		

				//List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndIsActive(pcrtdtls.getId(),1);
				//modelmap.addAttribute("seclist", seclist);
			
				return "caseDetails/assignTaskPuhSec";
			

			
		}

		else {

			if (assignedTaskPuh.getId() != null) {
				redirect.addFlashAttribute("message",
						" Task assiged Successfully to  " + assignedTaskPuh.getUser().getSalutation() + " "
								+ assignedTaskPuh.getUser().getFirstName() + " "
								+ assignedTaskPuh.getUser().getLastName());
				UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.assign"),
						utils.getMessage("log.login.addcaseassign") + assignedTaskPuh.getUser().getSalutation() + " "
								+ assignedTaskPuh.getUser().getFirstName() + " "
								+ assignedTaskPuh.getUser().getLastName() + " and investigation number is "
								+ addCase.getInvestigationOrderNo(),
						userdet.getFullName(), "true", assignedTaskPuh.getAddCase().getId());

				auditBeanBo.save();

			} else {
				redirect.addFlashAttribute("message", " Task assiged save Successfully.");
			}

			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			assignedTaskPuh.setCreatedBy(userdet);
			assignedTaskPuh.setUpdatedBy(userdet);
			assignedTaskPuh.setCreatedDate(new Date());

			if (assignedTaskPuh.getCreateTask().getId() != 1) {
				assignedTaskPuh.setIsApproved(false);
			}

			//assignedTaskPuhRepo.save(assignedTaskPuh);
			
			List<AssignedTaskPuh> newList =new ArrayList<AssignedTaskPuh>();
			
			int number = Integer.parseInt(assignedTaskPuh.getRemark());
			assignedTaskPuh.setRemark("");
			
			for (int i = 1; i <= number; i++) {

			    AssignedTaskPuh obj = new AssignedTaskPuh();
			    BeanUtils.copyProperties(assignedTaskPuh, obj);

			    obj.setId(null); // Important

			    newList.add(obj);
			}
			
	
			
			assignedTaskPuhRepo.saveAll(newList);

			//return "redirect:/ListOfCourtCase1";
			
			Long id=assignedTaskPuh.getAddCase().getId();
			
			   redirect.addAttribute("assignTaskSec", id);

			    return "redirect:/assignTaskSec";

		}
	}
	@RequestMapping(value = "/assignedTaskPuhAfterCourt")
	public String assignedTaskPuhAfterCourtCase(@ModelAttribute @Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, BindingResult binding,
			ModelMap modelmap,RedirectAttributes redirect) throws Exception {
		boolean errorr = false;
		AssignedTaskPuhAfterCOurt assignTask=null;
		CreateTasks createTask = assignedTaskPuh.getCreateTask();

		if (createTask == null) {
			//System.out.println("jai mata dei");
			modelmap.addAttribute("message", "This Task is must not be empty or null");
			return "caseDetails/assignTaskPuhCourt";
		}
		if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() == null
				&& assignedTaskPuh.getId() == null) {
			
			assignedTaskPuh.setSubtask(subTaskRepo.findById(0l).get());
			PuhValidation puh = new PuhValidation();
			puh.validatePuhAfterCourt(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		} else if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() != null
				&& assignedTaskPuh.getId() == null) {
			assignTask = assignedTaskPuhRepoa.findAllByCreateTaskAndSubtaskAndProCourtCaseDetails(assignedTaskPuh.getCreateTask(), assignedTaskPuh.getSubtask(),assignedTaskPuh.getProCourtCaseDetails());
			PuhValidation puh = new PuhValidation();

			puh.validatePuhAfterCourt(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		} else if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() == null
				&& assignedTaskPuh.getId() != null)  {
			long id1 = 0;
			Optional<AddSubTask> tt11 = subTaskRepo.findById(id1);
			assignedTaskPuh.setSubtask(tt11.get());

			AssignedTaskPuhAfterCOurt assignTask11 = assignedTaskPuhRepoa.findById(assignedTaskPuh.getId()).get();
			assignedTaskPuh.setSubtask(assignTask11.getSubtask());

			if (!(assignTask11.getCreateTask() == assignedTaskPuh.getCreateTask())) {
				assignTask = assignedTaskPuhRepoa.findAllByCreateTaskAndSubtask(assignedTaskPuh.getCreateTask(), assignedTaskPuh.getSubtask());
			}

			PuhValidation puh = new PuhValidation();
			puh.validatePuhAfterCourt(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		} else if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() != null
				&& assignedTaskPuh.getId() != null) {
			AssignedTaskPuhAfterCOurt assignTask111 = assignedTaskPuhRepoa.findById(assignedTaskPuh.getId()).get();
			if (!(assignTask111.getCreateTask() == assignedTaskPuh.getCreateTask())) {

				assignTask = assignedTaskPuhRepoa.findAllByCreateTaskAndSubtaskAndProCourtCaseDetails(
						assignedTaskPuh.getCreateTask(), assignedTaskPuh.getSubtask(),assignedTaskPuh.getProCourtCaseDetails());

			}
			PuhValidation puh = new PuhValidation();

			puh.validatePuhAfterCourt(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		}
		
		
		if (errorr == true || assignTask != null) {

			if (assignTask != null) {
				String username = assignTask.getUser().getFirstName();
				String lastname = assignTask.getUser().getLastName();
				String surname = assignTask.getUser().getSalutation();
				redirect.addFlashAttribute("message",
						"This Task is already assiged to " + surname + " " + username + " " + lastname);
				return "redirect:/assignTaskCourt?assignTaskCourt=" + assignedTaskPuh.getProCourtCaseDetails().getId();
			}

			ProCourtCaseDetails pcrtdtls =assignedTaskPuh.getProCourtCaseDetails();
			List<CreateTasks> TasksLst=new ArrayList<>();
			AssignedTaskPuhAfterCOurt assignedTaskPuhAfterCOurt= new AssignedTaskPuhAfterCOurt();
			if(assignedTaskPuh.getCreateTask().getId()==18 || assignedTaskPuh.getCreateTask().getId()==11) {
				assignedTaskPuhAfterCOurt.setCreateTask(createtasksRepo.findById(11l).get());
				TasksLst.add(createtasksRepo.findById(11l).get());
			}else if(assignedTaskPuh.getCreateTask().getId()==19||assignedTaskPuh.getCreateTask().getId()==2) {
				TasksLst.add(createtasksRepo.findById(2l).get());
			}
			List<UnitDetails> unitList = unitDetailsRepo.findAll();
			List<UnitDetails> UnitDetails1 = new ArrayList<UnitDetails>();
			int unitcount = 0;
			for (UnitDetails unitList2 : unitList) {
				if (unitList2.getUnitName().equalsIgnoreCase("Administrator Unit")) {

					unitList.remove(unitList2.getUnitId() - 1);
				} else {
					UnitDetails1.add(new UnitDetails(unitList2.getUnitId(), unitList2.getUnitName(),
							unitList2.getLocation(), unitList2.getAddress(), unitList2.getTelephoneNo(),
							unitList2.getFaxNo(), unitList2.getEMail(), unitList2.getCreatedDate()));
				}
				unitcount++;
			}
			List<SfioAs> sfiolst = sfioAsRepo.findAll(Sort.by(Sort.Direction.ASC, "SfioAs"));
			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			
			List<AssignedTaskPuhAfterCOurt> assignedTask = assignedTaskPuhRepoa.findAllByProCourtCaseDetails(pcrtdtls);
			
			modelmap.addAttribute("assignedTaskPuhAfterCOurt", assignedTaskPuh);
			modelmap.addAttribute("pcrtdtls", pcrtdtls);

			modelmap.addAttribute("TasksLst", TasksLst);
			modelmap.addAttribute("courtType", courtType);

			modelmap.addAttribute("udetails", UnitDetails1);
			modelmap.addAttribute("assignedTask", assignedTask);
			modelmap.addAttribute("sfiolst", sfiolst);

			if (pcrtdtls != null) {

				List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndIsActive(pcrtdtls.getId(),
						1);
				modelmap.addAttribute("seclist", seclist);
				return "caseDetails/assignTaskPuhCourt";
			} else {
				return "caseDetails/assignTaskPuhCourt";
			}

			// return "caseDetails/assignTaskCourt";
		}

		else {

			if (assignedTaskPuh.getId() != null) {
				redirect.addFlashAttribute("message",
						" Task assiged Successfully to  " + assignedTaskPuh.getUser().getSalutation() + " "
								+ assignedTaskPuh.getUser().getFirstName() + " "
								+ assignedTaskPuh.getUser().getLastName());
				UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.assign"),
						utils.getMessage("log.login.addcaseassign") + assignedTaskPuh.getUser().getSalutation() + " "
								+ assignedTaskPuh.getUser().getFirstName() + " "
								+ assignedTaskPuh.getUser().getLastName() + " and investigation number is "
								+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
						userdet.getFullName(), "true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());

				auditBeanBo.save();

			} else {
				redirect.addFlashAttribute("message", " Task assiged Created after Court case Details Successfully.");
			}

			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			assignedTaskPuh.setCreatedBy(userdet);
			assignedTaskPuh.setUpdatedBy(userdet);
			assignedTaskPuh.setCreatedDate(new Date());

			if (assignedTaskPuh.getCreateTask().getId() != 1) {
				assignedTaskPuh.setIsApproved(false);
			}

			assignedTaskPuhRepoa.save(assignedTaskPuh);
			return "redirect:/ListOfCourtCase2";

		}	
		}
	
	@PostMapping(value = "/assignedToOtherProsecutor")
	public String assignedTaskPuhAfterCourtCase(
	        @ModelAttribute ("assignedTaskPuhAfterCOurt") CaseForwardHistory caseForwardHistory,
	        BindingResult binding,
	        ModelMap modelmap,
	        RedirectAttributes redirect) throws Exception {
		
		UserDetails forwardedTo1 = caseForwardHistory.getForwardedTo();
		  if (forwardedTo1 == null) {
		        binding.rejectValue("forwardedTo","errmsg.required");
		    }
		
		
		
		
	    if (binding.hasErrors()) {

			List<UnitDetails> unitList = unitDetailsRepo.findAll();
			List<UnitDetails> UnitDetails1 = new ArrayList<UnitDetails>();
			int unitcount = 0;
			for (UnitDetails unitList2 : unitList) {
				if (unitList2.getUnitName().equalsIgnoreCase("Administrator Unit")) {

					unitList.remove(unitList2.getUnitId() - 1);
				} else {
					UnitDetails1.add(new UnitDetails(unitList2.getUnitId(), unitList2.getUnitName(),
							unitList2.getLocation(), unitList2.getAddress(), unitList2.getTelephoneNo(),
							unitList2.getFaxNo(), unitList2.getEMail(), unitList2.getCreatedDate()));
				}
				unitcount++;
			}
			
			modelmap.addAttribute("assignedTaskPuhAfterCOurt", caseForwardHistory);
			modelmap.addAttribute("pcrtdtls", caseForwardHistory.getProCourtCaseDetails());
			modelmap.addAttribute("udetails", UnitDetails1);
		
			return "caseDetails/assignTaskCourtForwardh";
	    }

	   
	    
	    List<CaseForwardHistory> findallbyProCourtCaseDetails = caseForwardHistoryRepository.findAllByProCourtCaseDetails(caseForwardHistory.getProCourtCaseDetails());
	    if (findallbyProCourtCaseDetails.isEmpty())


	    {
	    	CaseForwardHistory caseForwardHistory2 = new CaseForwardHistory();
	    	 caseForwardHistory2.setFromDate(caseForwardHistory.getProCourtCaseDetails().getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	    	 caseForwardHistory2.setUserName(caseForwardHistory.getProCourtCaseDetails().getCreatedBy());
	    	 caseForwardHistory2.setToDate(LocalDate.now());
	    	 caseForwardHistory2.setProCourtCaseDetails(caseForwardHistory.getProCourtCaseDetails());
	 	   caseForwardHistoryRepository.save(caseForwardHistory2);
	    	 
	    }
	    else
	    {
	    	CaseForwardHistory latest = 
	    		    findallbyProCourtCaseDetails.stream()
	    		        .max(Comparator.comparing(CaseForwardHistory::getId)).orElse(null);
	    	
	    	latest.setToDate(LocalDate.now());
		 	   caseForwardHistoryRepository.save(latest);

	    }
	    
	   
	    ProCourtCaseDetails proCourtCaseDetails = caseForwardHistory.getProCourtCaseDetails();	   
	    proCourtCaseDetails.setCreatedBy(caseForwardHistory.getForwardedTo());	    
	    ProCourtCaseDetailsRepos.save(proCourtCaseDetails);

	  
	    AssignedTaskPuhAfterCOurt assignedTask =
	            assignedTaskPuhRepoa.findByProCourtCaseDetails(proCourtCaseDetails);

	    if (assignedTask != null) {
	        assignedTask.setUser(caseForwardHistory.getForwardedTo());
	        assignedTaskPuhRepoa.save(assignedTask);
	    }

	    caseForwardHistory.setUserName(caseForwardHistory.getForwardedTo());
	    caseForwardHistory.setToDate(LocalDate.now());
	    caseForwardHistory.setFromDate(LocalDate.now());
	    caseForwardHistoryRepository.save(caseForwardHistory);

	  
	    String forwardedUserName = caseForwardHistory.getForwardedTo().getFullName(); 
	    redirect.addFlashAttribute("successMessage",
	            "Case is forwarded to Mr. " + forwardedUserName);

	    return "redirect:/totalListOfCourtCase";
	}

}
