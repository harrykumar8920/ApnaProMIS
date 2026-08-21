package com.pams.controllers;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AppRoleDAO;
import com.pams.dao.AssignTaskDAO;
import com.pams.dao.PendingTaskForApprovalDAO;
import com.pams.dto.AssignTaskDTO;
import com.pams.dto.PageNoDTO;
import com.pams.dto.PendingTaskForApprovalDTO;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddCase;
import com.pams.entity.AddCourt;
import com.pams.entity.AddSubTask;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CreateTasks;
import com.pams.entity.HearingDetails;
import com.pams.entity.SfioAs;
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
import com.pams.service.CaseCompanyRepository;
import com.pams.service.CaseProcessingDatesRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.CreateTasksRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.SfioAsRepository;
import com.pams.service.SubTaskRepository;
import com.pams.service.UnitDetailsRepository;
import com.pams.service.UploadAdditionalFilesDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.utils.ProMisConstant;
import com.pams.utils.Utils;
import com.pams.validation.PuhValidation;

import jakarta.validation.Valid;

@Controller
public class PuhController {
	@Autowired
	private AssignTaskDAO assignTaskDAO;
	@Autowired
	private PendingTaskForApprovalDAO pendingTaskForApprovalDAO;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhAfterCOurtRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AddCaseRepository addCaseRepo;

	@Autowired
	private UserManagementCustom userMangCustom;

	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;

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
	private CreateTasksRepository createtasksRepo;
	@Autowired
	private SfioAsRepository sfioAsRepo;
	@Autowired
	private PairaviDetailsRepository pairaviRepo;
	@Autowired
	private HearingDetailsRepository hearingRepo;
	@Autowired
	private CaseCompanyRepository caseCompany;
	@Autowired
	private CaseProcessingDatesRepository caseProcessingDatesRepo;
	@Autowired
	private UploadAdditionalFilesDetailsRepository uploadAdditionalFilesDetailsRepo;
	@Autowired
	private ComplaintdetlRepository complaintdetlRepo;
	@Autowired
	private ComplaintReportRepository complaintReportRepo;

	@Autowired
	private AddAccusedRepository addAccusedRepo;

	@Autowired
	AddStatusRepository addStatusRepo;

	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private CourtTypeRepository courtTypeRepo;

	@Autowired
	private UnitDetailsRepository unitDetailsRepo;
	@Autowired
	private HearingDetailsRepository hearingDetailsRepo;

//    @RequestMapping(value = "/showCasesPuh")
//	public String showCasesPuh(Model model) {
//
//		return "caseDetails/viewAllCasesPuh";
//	}
//	

	@RequestMapping(value = "approvedRejectSanctionOrder")
	public String approvedRejectSanctionOrder(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 2000;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<AddCase> totalsendbackcase = addCaseRepo.findALLByFinalisationStatusOrFinalisationStatus(2, 3, pagable);
		long totalRow = totalsendbackcase.getTotalElements();
		int currentRow = 1;
		int lastRow = totalsendbackcase.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		modelMap.addAttribute("lstCourtCase", totalsendbackcase.getContent());
		int pageNo1 = totalsendbackcase.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", totalsendbackcase.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		return "caseDetails/ApprovedRejectSanctionOrderPage";
	}

	@RequestMapping(value = "pendingFinalizecourtCaseDtl11")
	public String pendingFinalizecourtCaseDtl11(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		List<ProCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAllIfByApproveStatusIsOne();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Page<AddCase> totalsendbackcase = addCaseRepo.findALLByFinalisationStatusOrFinalisationStatus(2, 3, pagable);
		long totalRow = totalsendbackcase.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + totalsendbackcase.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		int totalpage = totalsendbackcase.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", totalpage);
		modelMap.addAttribute("totalItems", totalsendbackcase.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("lstCourtCase", totalsendbackcase.getContent());
		return "caseDetails/ApprovedRejectSanctionOrderPage";
	}

	@RequestMapping(value = "/showsSubList", method = RequestMethod.GET)
	public @ResponseBody List<AddSubTask> getSecListReportByAct(@RequestParam("taskId") Long taskId) {

		// AddSubTask act = addActRepo.findById(taskId).get();

		CreateTasks task = createtasksRepo.findById(taskId).get();
		List<AddSubTask> subTasklist = subTaskRepo.findAllByTask(task);
		return subTasklist;
	}

	@RequestMapping(value = "ListOfCourtCase")
	public String ListOfCourtCase(ModelMap modelMap) {
		List<AssignTaskDTO> lst = assignTaskDAO.findSectionOrderDetailsAfterCourt();

		/*
		 * List<proCourtCaseDetails> courtCasedtl =
		 * proCourtCaseDetailsRepo.findALLByApproveStatusBetween(1, 3,
		 * Sort.by(Sort.Direction.DESC, "id")); List<AddCase> totalsendbackcase =
		 * addCaseRepo.findALLByFinalisationStatus(2, Sort.by(Sort.Direction.DESC,
		 * "id"));
		 */
		modelMap.addAttribute("lstCourtCase", lst);

		return "caseDetails/AddedCourtList";
	}

	@RequestMapping(value = "ListOfCourtCase1")
	public String ListOfCourtCase1(ModelMap modelMap) {
		List<AssignTaskDTO> lst = assignTaskDAO.findSectionOrderDetails();
		List<AddCase> allByFinalisationStatus = addCaseRepo.findALLByFinalisationStatus(2,
				Sort.by(Sort.Direction.DESC, "id"));

		List<Object[]> counts = assignedTaskPuhRepo.getTaskCountGroupByCase();

		HashMap<Long, Long> map = new HashMap<>();

		for (Object[] obj : counts) {

			Long caseId = ((Number) obj[0]).longValue();
			Long count = ((Number) obj[1]).longValue();

			map.put(caseId, count);

		}

		for (AddCase addCase : allByFinalisationStatus) {
			Long count = map.getOrDefault(addCase.getId(), 0L);
			addCase.setTaskCount(count);
		}

		allByFinalisationStatus.sort(Comparator.comparing(AddCase::getTaskCount));

//allByFinalisationStatus.sort(Comparator.comparing(AddCase::getTaskCount).reversed());

		modelMap.addAttribute("lstCourtCase", allByFinalisationStatus);

		return "caseDetails/AddedCourtList1";
	}

	@RequestMapping(value = "ListOfCourtCase2")
	public String ListOfCourtCase2(ModelMap modelMap) {

		// List<ProCourtCaseDetails> allByApproveStatus =
		// proCourtCaseDetailsRepo.findAllByApproveStatus(2,
		// Sort.by(Sort.Direction.DESC, "id"));

		List<ProCourtCaseDetails> allByApproveStatus = proCourtCaseDetailsRepo.findCourtCasesWithoutAssignedTask(2);

		modelMap.addAttribute("lstCourtCase", allByApproveStatus);

		return "caseDetails/AddedCourtList2";
	}

	@RequestMapping(value = "totalListOfCourtCase")
	public String totalListOfCourtCase(ModelMap modelMap) {

		List<ProCourtCaseDetails> allByApproveStatus = proCourtCaseDetailsRepo.findAllByApproveStatus(2,
				Sort.by(Sort.Direction.DESC, "id"));
//List<AddCase> allByFinalisationStatus = addCaseRepo.findALLByFinalisationStatus(2,Sort.by(Sort.Direction.DESC, "id"));

		modelMap.addAttribute("lstCourtCase", allByApproveStatus);

		return "caseDetails/totalListOfCourtCaseh";
	}

	@RequestMapping(value = "ListOfCourtCasePuh")
	public String ListOfCourtCasePuh(ModelMap modelMap) throws Exception {
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		List<ProCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
				Sort.by(Sort.Direction.DESC, "id"));
		modelMap.addAttribute("courtCasedtl", courtCasedtl);

		return "caseDetails/AddedCourtList";
	}

	@RequestMapping(value = "/caseApprove", params = "ok")
	public String approvecase(ModelMap modelmap, @ModelAttribute("pcrtdtls") ProCourtCaseDetails pcrtdtls1)
			throws Exception {

		ProCourtCaseDetails pcrtdtls = proCourtCaseDetailsRepo.findALLById(pcrtdtls1.getId());
		pcrtdtls.setApproveStatus(2);
		pcrtdtls.setCasePosition(2);
		proCourtCaseDetailsRepo.save(pcrtdtls);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.addcourtcasedetailapproved") + " " + " and Investigation number is "
						+ pcrtdtls.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", pcrtdtls.getAddCase().getId());
		auditBeanBo.save();
		AssignedTaskPuh assignTask = assignedTaskPuhRepo.findById(pcrtdtls.getAssignedTask().getId()).get();
		assignTask.setIsApproved(true);
		assignedTaskPuhRepo.save(assignTask);

		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndIsActive(pcrtdtls.getId(), 1);
		modelmap.addAttribute("seclist", seclist);
		modelmap.addAttribute("pcrtdtls", pcrtdtls);
		return "caseDetails/viewCourtCaseDtl";
	}

	@RequestMapping(value = "/caseApprove", params = "sendBackCase")
	public String sendBackCase(ModelMap modelmap, @ModelAttribute("pcrtdtls") ProCourtCaseDetails pcrtdtls1)
			throws Exception {

		ProCourtCaseDetails pcrtdtls = proCourtCaseDetailsRepo.findALLById(pcrtdtls1.getId());
		pcrtdtls.setApproveStatus(3);
		pcrtdtls.setCasePosition(3);
		pcrtdtls.setSendBackRemarks(pcrtdtls1.getSendBackRemarks());
		proCourtCaseDetailsRepo.save(pcrtdtls);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.sendback"),
				utils.getMessage("log.login.addcourtcasedetailsendback") + " " + " and Investigation number is "
						+ pcrtdtls.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", pcrtdtls.getAddCase().getId());
		auditBeanBo.save();

		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndIsActive(pcrtdtls.getId(), 1);
		modelmap.addAttribute("seclist", seclist);
		modelmap.addAttribute("pcrtdtls", pcrtdtls);
		return "caseDetails/viewCourtCaseDtl";
	}

	@RequestMapping(value = "/CourtCaseDtl", params = "Courtdtl")
	public String courtCaseDtl(ModelMap modelmap, @RequestParam(value = "Courtdtl", required = true) Long caseId) {

		ProCourtCaseDetails pcrtdtls = proCourtCaseDetailsRepo.findALLById(caseId);
		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndIsActive(pcrtdtls.getId(), 1);
		modelmap.addAttribute("seclist", seclist);
		modelmap.addAttribute("pcrtdtls", pcrtdtls);
		if(pcrtdtls.getType().getId()==1)
			return "caseDetails/viewNcltCaseDtl";
		else 
			return "caseDetails/viewCourtCaseDtl";
		
		
	}

	@RequestMapping(value = "/CourtCaseDtl", params = "assignTask")
	public String AssignTaskListPuh(ModelMap modelmap,
			@RequestParam(value = "assignTask", required = true) Long caseId) {

		List<CreateTasks> TasksLst = createtasksRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		ProCourtCaseDetails pcrtdtls = proCourtCaseDetailsRepo.findALLById(caseId);
		AssignedTaskPuh assignedTaskPuh = new AssignedTaskPuh();
		// assignedTaskPuh.setProCourtCase(pcrtdtls);
		// assignedTaskPuh.setCourt(pcrtdtls.getCourtType());

		List<SfioAs> sfiolst = sfioAsRepo.findAll();
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
		// List<AssignedTaskPuh> assignedTask =
		// assignedTaskPuhRepo.findAllByProCourtCase(pcrtdtls);

		modelmap.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelmap.addAttribute("TasksLst", TasksLst);
		modelmap.addAttribute("courtType", courtType);

		modelmap.addAttribute("udetails", UnitDetails1);
		modelmap.addAttribute("pcrtdtls", pcrtdtls);
		// modelmap.addAttribute("assignedTask", assignedTask);
		modelmap.addAttribute("sfiolst", sfiolst);
		return "caseDetails/assignTaskPuh";
	}

	@RequestMapping(value = "/assignedTaskPuh1")
	public String assignedTaskPuh1(@ModelAttribute @Valid AssignedTaskPuh assignedTaskPuh, BindingResult binding,
			ModelMap modelmap, RedirectAttributes redirect, Long caseId) {
		AssignedTaskPuh assignTask = null;
		boolean errorr = false;
		if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() == null
				&& assignedTaskPuh.getId() == null) {
			// assignTask =
			// assignedTaskPuhRepo.findAllByProCourtCaseAndCreateTask(assignedTaskPuh.getProCourtCase(),assignedTaskPuh.getCreateTask());
			long id1 = 0;
			Optional<AddSubTask> tt11 = subTaskRepo.findById(id1);
			assignedTaskPuh.setSubtask(tt11.get());

			PuhValidation puh = new PuhValidation();
			puh.validatePuh(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		} else if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() != null
				&& assignedTaskPuh.getId() == null) {
			// assignTask =
			// assignedTaskPuhRepo.findAllByProCourtCaseAndCreateTaskAndSubtask(assignedTaskPuh.getProCourtCase(),
			// assignedTaskPuh.getCreateTask(), assignedTaskPuh.getSubtask());
			PuhValidation puh = new PuhValidation();

			puh.validatePuh1(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		} else if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() == null
				&& assignedTaskPuh.getId() != null) {
			long id1 = 0;
			Optional<AddSubTask> tt11 = subTaskRepo.findById(id1);
			assignedTaskPuh.setSubtask(tt11.get());

			AssignedTaskPuh assignTask1 = assignedTaskPuhRepo.findById(assignedTaskPuh.getId()).get();
			assignedTaskPuh.setSubtask(assignTask1.getSubtask());
			/*
			 * if (!(assignTask1.getCreateTask() == assignedTaskPuh.getCreateTask())) {
			 * assignTask =
			 * assignedTaskPuhRepo.findAllByProCourtCaseAndCreateTask(assignedTaskPuh.
			 * getProCourtCase(), assignedTaskPuh.getCreateTask()); }
			 */
			PuhValidation puh = new PuhValidation();
			puh.validatePuh(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		} else if (assignedTaskPuh.getCreateTask() != null && assignedTaskPuh.getSubtask() != null
				&& assignedTaskPuh.getId() != null) {
			AssignedTaskPuh assignTask1 = assignedTaskPuhRepo.findById(assignedTaskPuh.getId()).get();
			if (!(assignTask1.getCreateTask() == assignedTaskPuh.getCreateTask())) {
				/*
				 * assignTask =
				 * assignedTaskPuhRepo.findAllByProCourtCaseAndCreateTaskAndSubtask(
				 * assignedTaskPuh.getProCourtCase(), assignedTaskPuh.getCreateTask(),
				 * assignedTaskPuh.getSubtask());
				 */
			}
			PuhValidation puh = new PuhValidation();

			puh.validatePuh1(assignedTaskPuh, binding);
			errorr = binding.hasErrors();

		}

		if (errorr == true || assignTask != null) {

			if (assignTask != null) {
				String username = assignTask.getUser().getFirstName();
				String lastname = assignTask.getUser().getLastName();
				modelmap.addAttribute("message", "This Task is already assiged to " + username + " " + lastname);
			}

			List<CreateTasks> TasksLst = createtasksRepo.findAll();
			// proCourtCaseDetails pcrtdtls =
			// proCourtCaseDetailsRepo.findALLById(assignedTaskPuh.getProCourtCase().getId());
			// assignedTaskPuh.setProCourtCase(pcrtdtls);
			// assignedTaskPuh.setCourt(pcrtdtls.getCourtType());

			List<SfioAs> sfiolst = sfioAsRepo.findAll();
			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			List<UnitDetails> udetails = unitDetailsRepo.findAll();
			// List<AssignedTaskPuh> assignedTask =
			// assignedTaskPuhRepo.findAllByProCourtCase(pcrtdtls);

			CreateTasks task = assignedTaskPuh.getCreateTask();

			List<AddSubTask> subtask = subTaskRepo.findAllByTask(task);

			modelmap.addAttribute("subtask", subtask);

			modelmap.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelmap.addAttribute("TasksLst", TasksLst);
			modelmap.addAttribute("courtType", courtType);

			modelmap.addAttribute("udetails", udetails);
			// modelmap.addAttribute("pcrtdtls", pcrtdtls);
			// modelmap.addAttribute("assignedTask", assignedTask);
			modelmap.addAttribute("sfiolst", sfiolst);

			return "caseDetails/assignTaskPuh";
		}

		else {

			assignedTaskPuhRepo.save(assignedTaskPuh);

			// assignedTaskPuh.getProCourtCase().getId();
			// proCourtCaseDetails procourtDtls =
			// proCourtCaseDetailsRepo.findById(assignedTaskPuh.getProCourtCase().getId()).get();
			// procourtDtls.setCasePosition(4);
			// proCourtCaseDetailsRepo.save(procourtDtls);
			if (assignedTaskPuh.getId() == null) {
				redirect.addFlashAttribute("message", " Task assiged Successfully.");
			} else {
				redirect.addFlashAttribute("message", " Task assiged Updated Successfully.");
			}

			List<CreateTasks> TasksLst = createtasksRepo.findAll();
			// proCourtCaseDetails pcrtdtls =
			// proCourtCaseDetailsRepo.findALLById(assignedTaskPuh.getProCourtCase().getId());
			// assignedTaskPuh.setProCourtCase(pcrtdtls);
			// assignedTaskPuh.setCourt(pcrtdtls.getCourtType());

			List<SfioAs> sfiolst = sfioAsRepo.findAll();
			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			List<UnitDetails> udetails = unitDetailsRepo.findAll();
			// List<AssignedTaskPuh> assignedTask =
			// assignedTaskPuhRepo.findAllByProCourtCase(pcrtdtls);

			modelmap.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelmap.addAttribute("TasksLst", TasksLst);
			modelmap.addAttribute("courtType", courtType);

			modelmap.addAttribute("udetails", udetails);
			// modelmap.addAttribute("pcrtdtls", pcrtdtls);
			// modelmap.addAttribute("assignedTask", assignedTask);
			modelmap.addAttribute("sfiolst", sfiolst);
			return "redirect:/ListOfCourtCase";

		}
	}

	@RequestMapping(value = "/editPuh", params = "editPuh")
	public String AssignTaskListPuh1(ModelMap modelmap, @RequestParam(value = "editPuh", required = true) Long caseId) {

		System.out.println("value====================" + caseId);
		AssignedTaskPuh at = assignedTaskPuhRepo.findById(caseId).get();

		// at.setCourt(at.getCourt());
		// at.setProCourtCase(at.getProCourtCase());
		at.setSfioAs(at.getSfioAs());
		at.setSubtask(at.getSubtask());
		at.setUnit(at.getUnit());
		at.setUser(at.getUser());

		// Long t = at.getProCourtCase().getId();

		// proCourtCaseDetails pcd =
		// proCourtCaseDetailsRepo.findById(at.getProCourtCase().getId()).get();
		// pcd.setCaseTitle(pcd.getCaseTitle());
		// pcd.setCourtCaseNo(pcd.getCourtCaseNo());
		// pcd.setCauseTitle(pcd.getCauseTitle());
		// pcd.setFillingDate(pcd.getFillingDate());

		List<SfioAs> sfiolst = sfioAsRepo.findAll();
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<UnitDetails> unitList = unitDetailsRepo.findAll();
		// List<AddSubTask> subtask = subTaskRepo.findAll();

		// List<AssignedTaskPuh> assignedTask =
		// assignedTaskPuhRepo.findAllByProCourtCase(at.getProCourtCase().getCaseId());

//		List<AssignedTaskPuh> assignedTask = assignedTaskPuhRepo.findAllByProCourtCase(at.getProCourtCase());

//		modelmap.addAttribute("assignedTask", assignedTask);
		List<CreateTasks> TasksLst = createtasksRepo.findAll();
		CreateTasks task = at.getCreateTask();

		List<AddSubTask> subtask = subTaskRepo.findAllByTask(task);

		modelmap.addAttribute("subtask", subtask);
		modelmap.addAttribute("TasksLst", TasksLst);
		modelmap.addAttribute("courtType", courtType);

		modelmap.addAttribute("udetails", unitList);

		modelmap.addAttribute("sfiolst", sfiolst);

		modelmap.addAttribute("assignedTaskPuh", at);
		// modelmap.addAttribute("pcrtdtls", pcd);

		return "caseDetails/assignTaskPuh";
	}

	@RequestMapping(value = "/getInspectorListReportByUnitId", method = RequestMethod.GET)
	public @ResponseBody List<UserDetails> getInspectorListReportByUnitId(@RequestParam("unitId") Long unitId,
			Model model) {

		List<UserDetails> unituserList1 = userMangCustom.findByRole(ProMisConstant.Role_USER, new UnitDetails(unitId));
		unituserList1.sort(Comparator.comparing(UserDetails::getFirstName, String.CASE_INSENSITIVE_ORDER));

		List<UserDetails> unituserList = unituserList1.stream().filter(f -> f.getUserId().getEnabled() == 1)
				.collect(Collectors.toList());

		int count = 1;
		List<UserDetails> pList = new ArrayList<UserDetails>();
		for (UserDetails user : unituserList) {
			pList.add(new UserDetails(count,
					user.getSalutation() + " " + userDetailsService.getFullName(user) + " ("
							+ user.getUnit().getUnitName() + ")",
					user.getDesignation().getDesignation(), user.getUserId().getUserId()));

			count++;
		}
		return pList;
	}

	@RequestMapping(value = "/showsCourtDtl", method = RequestMethod.GET)
	public @ResponseBody ProCourtCaseDetails showsCourtDtl(@RequestParam("courtID") Long courtID, Model model) {

		ProCourtCaseDetails courtDtl = proCourtCaseDetailsRepo.findALLById(courtID);

		return courtDtl;
	}

	@RequestMapping(value = "/AssignedTaskListPuh")
	public String CreateTaskListPuh(ModelMap modelmap) {

		List<AssignedTaskPuh> assignedTasklst = assignedTaskPuhRepo.findAll();

		modelmap.addAttribute("assignedTasklst", assignedTasklst);
		return "caseDetails/AssignedTaskListPuh";
	}

	private static LocalDate toLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@RequestMapping(value = "/puhHome")
	public String puhHome(ModelMap modelMap) throws Exception {

		List<AddCase> totalsendbackcase = addCaseRepo.findALLByFinalisationStatus(1,
				Sort.by(Sort.Direction.DESC, "id"));
		List<ProCourtCaseDetails> TotalCase = proCourtCaseDetailsRepo.findAll();
		List<AssignedTaskPuh> TotalassignCase = assignedTaskPuhRepo.findAll();
		List<AddCase> listAddCase = addCaseRepo.findALLByFinalisationStatusOrFinalisationStatus(2, 3);
		List<ProCourtCaseDetails> list = proCourtCaseDetailsRepo.findAllIfByApproveStatusIsOne();
		List<ProCourtCaseDetails> proCourtDtls = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(1, 1,
				Sort.by(Sort.Direction.DESC, "id"));
		List<ProCourtCaseDetails> notSendproCourtDtls = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(0, 0,
				Sort.by(Sort.Direction.DESC, "id"));
		int size = list.size();
		// List<AssignedTaskPuh> pendingAssignedTask =
		// assignedTaskPuhRepo.findAllByIsApproved(false);
		modelMap.addAttribute("totalAssignedTask", assignedTaskPuhRepo.count());
		modelMap.addAttribute("pendingAssignedTask", notSendproCourtDtls.size());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		Date nextdate1 = last7days();

		Date fromDate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

		List<HearingDetails> hearingdata = hearingDetailsRepo.findByNextHearingDateBetween(nextdate1, fromDate1);

		modelMap.addAttribute("totalCourtCaseDtl", TotalCase.size());
		modelMap.addAttribute("TotalCase", totalsendbackcase.size());
		modelMap.addAttribute("TotalCase1", TotalassignCase.size());
		modelMap.addAttribute("listAddCase", listAddCase.size());

		List<AddCase> totalcasewhichisAssigned1 = (List<AddCase>) addCaseRepo.findAll();

		modelMap.addAttribute("totalcase12", totalcasewhichisAssigned1.size());
		modelMap.addAttribute("totleDesignation", addDesignationRepository.count());
		modelMap.addAttribute("totleRole", appRoleRepository.count());

		List<PendingTaskForApprovalDTO> approvalPendinglist = pendingTaskForApprovalDAO.approveAndReject();

		modelMap.addAttribute("approveAndReject", approvalPendinglist.size());

		Integer n = pairaviRepo.findByApproveStatus();
		Integer n1 = hearingRepo.findByApproveStatus();
		Integer n2 = caseCompany.findByApproveStatus();
		Integer n3 = caseProcessingDatesRepo.findByApproveStatus();
		Integer n4 = uploadAdditionalFilesDetailsRepo.findByApproveStatus();
		Integer n5 = complaintdetlRepo.findByApproveStatus();
		Integer n6 = complaintReportRepo.findByApproveStatus();
		Integer n7 = addAccusedRepo.findByApproveStatus();
		Integer sum = n + n1 + n2 + n3 + n4 + n5 + n6 + n7;

		List<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtRepository.findAll();
		modelMap.addAttribute("viewadditionalDetails", ApprovedTask.size());
		modelMap.addAttribute("totalpendingApproval", sum);
		modelMap.addAttribute("totalpendingCourtCase", proCourtDtls.size());

		modelMap.addAttribute("size", size);

		ReportController rc = new ReportController();
		Date nextdate = rc.next7days();

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
		List<HearingDetails> totaltodaycase = hearingDetailsRepo.findByNextHearingDateBetween(fromDate, nextdate);

		modelMap.addAttribute("totaltodaycase1", hearingdata.size());
		modelMap.addAttribute("totaltodaycase", totaltodaycase.size());

		// For Graph

		//List<HearingDetails> allByApproveStatus = hearingDetailsRepo.findAll();
		
		List<HearingDetails> allByApproveStatus = hearingDetailsRepo.findAllByQuery();

		LocalDate fromLocalDate = toLocalDate(fromDate);

		Map<String, Integer> dayWiseCount = IntStream.rangeClosed(1, 7).boxed()
				.collect(Collectors.toMap(i -> "DAY" + i, i -> {
					LocalDate currentDay = fromLocalDate.plusDays(i - 1);
					return (int) totaltodaycase.stream().map(HearingDetails::getNextHearingDate)
							.filter(Objects::nonNull).map(PuhController::toLocalDate) // Use helper safely
							.filter(hearingDate -> hearingDate.equals(currentDay)).count();
				}, (a, b) -> b, LinkedHashMap::new));

		modelMap.addAttribute("dayWiseCountUpdate", dayWiseCount);
		// 1. Last 7 Days ka count nikalne ke liye logic (Chronological Order: DAY1 = 6
		// Days ago, DAY7 = Today)
		Map<String, Integer> Last7DaysCount = IntStream.rangeClosed(1, 7).boxed()
				.collect(Collectors.toMap(i -> "DAY" + i, i -> {
					// DAY1 -> 6 days ago, DAY2 -> 5 days ago ... DAY7 -> Today (0 days ago)
					LocalDate currentDay = fromLocalDate.minusDays(7 - i);

					return (int) allByApproveStatus.stream().map(HearingDetails::getNextHearingDate) // NOTE: Agar past
																										// hearing ki
																										// date koi
																										// doosra field
																										// hai to wo use
																										// karein
							.filter(Objects::nonNull).map(PuhController::toLocalDate) // Apka helper method
							.filter(hearingDate -> hearingDate.equals(currentDay)).count();
				}, (a, b) -> b, LinkedHashMap::new));

		// 2. Aapka existing model mapping (Ise waise hi rehne dein)

		List<AddCase> Totalsanctionorder = addCaseRepo.findALLByFinalisationStatus(2,
				Sort.by(Sort.Direction.DESC, "id"));
		modelMap.addAttribute("Totalsanctionorder", Totalsanctionorder.size());

		

		Map<String, Integer> statusCountMapNCLT = allByApproveStatus.stream()
		        .filter(h -> h.getStatus() != null
		                && h.getProcourtdtl() != null
		                && h.getProcourtdtl().getType().getId() == 1)
		        .collect(Collectors.groupingBy(
		                h -> h.getStatus().getStatusName(),
		                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
		        ));
		Map<String, Integer> statusCountMap = allByApproveStatus.stream()
		        .filter(h -> h.getStatus() != null
		                && h.getProcourtdtl() != null
		                && h.getProcourtdtl().getType().getId() == 2)
		        .collect(Collectors.groupingBy(
		                h -> h.getStatus().getStatusName(),
		                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
		        ));

		
		List<ProCourtCaseDetails> byFillingDateIsNotNull =
		        proCourtCaseDetailsRepo.findByFillingDateIsNotNull();


		// Filling Date wise sorting
		byFillingDateIsNotNull.sort(
		        Comparator.comparing(ProCourtCaseDetails::getFillingDate)
		);


		// Financial Year wise grouped count
		Map<String, Integer> financialYearCountMap =
		        byFillingDateIsNotNull.stream()
		                .filter(caseDtl -> caseDtl.getFinancialYear() != null)
		                .collect(Collectors.groupingBy(
		                        ProCourtCaseDetails::getFinancialYear,
		                        LinkedHashMap::new, // insertion order preserve
		                        Collectors.summingInt(e -> 1)
		                ));
		// 3. UI (Thymeleaf) par bhejne ke liye model mein add karein
		modelMap.addAttribute("financialYearCountMap", financialYearCountMap);
		modelMap.addAttribute("statusCountMap", statusCountMap);
		modelMap.addAttribute("statusCountMapNCLT", statusCountMapNCLT);
		modelMap.addAttribute("Last7DaysCount", Last7DaysCount);
		modelMap.addAttribute("totaltodaycase", totaltodaycase.size());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		
		
		List<HearingDetails> byUserAndLessThanNextHearingDate = 
				hearingRepo.findLatestHearingPerCase(oldcurrent);
		
		modelMap.addAttribute("byUserAndLessThanNextHearingDate", byUserAndLessThanNextHearingDate.size());
		
		

		return "puhHome";
	}

	public static Date last7days() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.DAY_OF_MONTH, -6);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		Date todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(currentdate2);
		return todate;

	}

	@RequestMapping(value = "totalPendingCourtCaseDtlForApproval1")
	public String totalPendingCourtCaseDtlForApproval(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap)
			throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 1,
				1);
		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + proCourtCaeDetails.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", proCourtDtls);

		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		return "caseDetails/totalPendingCourtCaseDtlForApproval";
	}

	@RequestMapping(value = "totalPendingCourtCaseDtlForApproval")
	public String totalPendingCourtCaseDtlForApproval(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 1,
				1);

		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = 1;
		int lastRow = proCourtCaeDetails.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", proCourtDtls);

		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		return "caseDetails/totalPendingCourtCaseDtlForApproval";
	}

	/*
	 * @RequestMapping(value = "totalPendingCourtCaseDtlForApproval1") public String
	 * totalPendingCourtCaseDtlForApproval(@ModelAttribute PageNoDTO pageDTO,
	 * ModelMap modelMap) throws Exception {
	 * 
	 * int pageNo; if (pageDTO.getPageno() > 1) { pageNo = pageDTO.getPageno() - 1;
	 * } else { pageNo = 0; }
	 * 
	 * int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id"));
	 * 
	 * Page<proCourtCaseDetails> proCourtCaeDetails =
	 * proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 1, 1);
	 * List<proCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
	 * 
	 * String userrole =
	 * appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
	 * 
	 * modelMap.addAttribute("userRole", userrole);
	 * modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
	 * modelMap.addAttribute("lstCourtCase", proCourtDtls);
	 * 
	 * int pageNo1 = proCourtCaeDetails.getTotalPages();
	 * modelMap.addAttribute("currentPage", pageNo + 1);
	 * modelMap.addAttribute("totalPages", pageNo1);
	 * modelMap.addAttribute("totalItems", proCourtCaeDetails.getSize());
	 * modelMap.addAttribute("pageNoDTO", new PageNoDTO());
	 * 
	 * return "caseDetails/totalPendingCourtCaseDtlForApproval"; }
	 * 
	 * @RequestMapping(value = "totalPendingCourtCaseDtlForApproval") public String
	 * totalPendingCourtCaseDtlForApproval(ModelMap modelMap) throws Exception {
	 * 
	 * int pageNo = 0; int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id"));
	 * 
	 * Page<proCourtCaseDetails> proCourtCaeDetails =
	 * proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 1, 1);
	 * List<proCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
	 * 
	 * String userrole =
	 * appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
	 * 
	 * modelMap.addAttribute("userRole", userrole);
	 * modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
	 * modelMap.addAttribute("lstCourtCase", proCourtDtls);
	 * 
	 * int pageNo1 = proCourtCaeDetails.getTotalPages();
	 * modelMap.addAttribute("currentPage", pageNo + 1);
	 * modelMap.addAttribute("totalPages", pageNo1);
	 * modelMap.addAttribute("totalItems", proCourtCaeDetails.getSize());
	 * modelMap.addAttribute("pageNoDTO", new PageNoDTO());
	 * 
	 * return "caseDetails/totalPendingCourtCaseDtlForApproval"; }
	 */

	@RequestMapping(value = "totalCourtCaseDtl")
	public String totalCourtCaseDtl(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 2000;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0,
				6);
		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = 1;
		int lastRow = proCourtCaeDetails.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", proCourtDtls);

		return "caseDetails/totalCourtCaseDetails";

	}

	@RequestMapping(value = "totalCourtCaseDetails")
	public String totalCourtCaseDetails(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0,
				6);

		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + proCourtCaeDetails.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", proCourtDtls);

		return "caseDetails/totalCourtCaseDetails";

	}

	/*
	 * @RequestMapping(value = "pendingFinalizecourtCaseDtl1") public String
	 * pendingFinalizecourtCaseDtl1(@ModelAttribute PageNoDTO pageDTO, ModelMap
	 * modelMap) throws Exception {
	 * 
	 * int pageNo; if (pageDTO.getPageno() > 1) { pageNo = pageDTO.getPageno() - 1;
	 * } else { pageNo = 0; } int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id"));
	 * 
	 * List<proCourtCaseDetails> courtCasedtl =
	 * proCourtCaseDetailsRepo.findAllIfByApproveStatusIsOne(); UserDetails userdet
	 * =
	 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
	 * )); Page<AddCase> totalsendbackcase =
	 * addCaseRepo.findALLByFinalisationStatus(1, pagable);
	 * 
	 * int totalpage = totalsendbackcase.getTotalPages();
	 * 
	 * modelMap.addAttribute("lstCourtCase", totalsendbackcase.getContent());
	 * 
	 * PageNoDTO pageDto = new PageNoDTO();
	 * 
	 * modelMap.addAttribute("currentPage", pageNo + 1);
	 * modelMap.addAttribute("totalPages", totalpage);
	 * modelMap.addAttribute("totalItems", totalsendbackcase.getSize());
	 * modelMap.addAttribute("pageNoDTO", pageDto);
	 * 
	 * return "caseDetails/pendingFinalizecourtCaseDtl"; }
	 * 
	 * @RequestMapping(value = "pendingFinalizecourtCaseDtl") public String
	 * pendingFinalizecourtCaseDtl(ModelMap modelMap) throws Exception {
	 * 
	 * int pageNo = 0;
	 * 
	 * int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id"));
	 * 
	 * List<proCourtCaseDetails> courtCasedtl =
	 * proCourtCaseDetailsRepo.findAllIfByApproveStatusIsOne(); UserDetails userdet
	 * =
	 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
	 * )); Page<AddCase> totalsendbackcase =
	 * addCaseRepo.findALLByFinalisationStatus(1, pagable);
	 * 
	 * int totalpage = totalsendbackcase.getTotalPages();
	 * 
	 * modelMap.addAttribute("lstCourtCase", totalsendbackcase.getContent());
	 * 
	 * PageNoDTO pageDto = new PageNoDTO();
	 * 
	 * modelMap.addAttribute("currentPage", pageNo + 1);
	 * modelMap.addAttribute("totalPages", totalpage);
	 * modelMap.addAttribute("totalItems", totalsendbackcase.getSize());
	 * modelMap.addAttribute("pageNoDTO", pageDto);
	 * 
	 * return "caseDetails/pendingFinalizecourtCaseDtl"; }
	 */

	@RequestMapping(value = "pendingFinalizecourtCaseDtl1")
	public String pendingFinalizecourtCaseDtl1(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		List<ProCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAllIfByApproveStatusIsOne();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Page<AddCase> totalsendbackcase = addCaseRepo.findALLByFinalisationStatus(1, pagable);
		long totalRow = totalsendbackcase.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + totalsendbackcase.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		int totalpage = totalsendbackcase.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", totalpage);
		modelMap.addAttribute("totalItems", totalsendbackcase.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("lstCourtCase", totalsendbackcase.getContent());
		return "caseDetails/pendingFinalizecourtCaseDtl";
	}

	@RequestMapping(value = "pendingFinalizecourtCaseDtl")
	public String pendingFinalizecourtCaseDtl(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<AddCase> totalsendbackcase = addCaseRepo.findALLByFinalisationStatus(1, pagable);
		long totalRow = totalsendbackcase.getTotalElements();
		int currentRow = 1;
		int lastRow = totalsendbackcase.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		modelMap.addAttribute("lstCourtCase", totalsendbackcase.getContent());
		int pageNo1 = totalsendbackcase.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", totalsendbackcase.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		return "caseDetails/pendingFinalizecourtCaseDtl";
	}

	@RequestMapping(value = "/totalAssignedTaskList")
	public String totalAssignedTaskList(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		// modelMap.addAttribute("totalAssignedTask",
		// assignedTaskPuhRepo.findAll(Sort.by(Sort.Direction.DESC, "addCase")));

		int limitValue = 20;
		int totalpage = pageDTO.getTotalPage();
		Integer listSize1 = pageDTO.getTotalList();
		int pageNo = pageDTO.getPageno();
		int offsetValue = (pageNo - 1) * limitValue;

		List<AssignTaskDTO> totalTask = assignTaskDAO.findAllSectionOrderDetailsWithAssignTask(offsetValue, limitValue);

		int listSize = totalTask.size();

		PageNoDTO pageDto = new PageNoDTO();
		pageDto.setTotalList(listSize1);
		pageDto.setTotalPage(totalpage);
		modelMap.addAttribute("currentPage", pageNo);
		modelMap.addAttribute("totalPages", totalpage);
		modelMap.addAttribute("totalItems", listSize);
		modelMap.addAttribute("pageNoDTO", pageDto);

		modelMap.addAttribute("totalRow", listSize1);
		modelMap.addAttribute("currentRow", offsetValue + 1);
		modelMap.addAttribute("lastRow", offsetValue + listSize);

		modelMap.addAttribute("totalAssignedTask", totalTask);

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);

		return "caseDetails/viewAllCasesPuh";
	}

	// Show all cases for Puh (PUH Login)
	@RequestMapping(value = "/AssignedTaskList")
	public String showCasesPuh(ModelMap modelMap) throws Exception {

		// modelMap.addAttribute("totalAssignedTask",
		// assignedTaskPuhRepo.findAll(Sort.by(Sort.Direction.DESC, "addCase")));
		int offsetValue = 0;
		int limitValue = 2000;
		int totalpage = 0;
		int pageNo = 1;

		List<AssignTaskDTO> totalTask = assignTaskDAO.findAllSectionOrderDetailsWithAssignTask(offsetValue, limitValue);
		Integer totalcount = null;
		int listSize = totalTask.size();
		if (!totalTask.isEmpty()) {

			for (AssignTaskDTO assignTaskDTO : totalTask) {

				totalcount = assignTaskDTO.getTotalcount();
				if (totalcount % limitValue == 0) {
					totalpage = totalcount / limitValue;
					System.out.println(totalpage);
				} else {
					totalpage = (totalcount / limitValue) + 1;
					System.out.println(totalpage);
				}

				break;
			}
		}

		PageNoDTO pageDto = new PageNoDTO();
		pageDto.setTotalList(totalcount);
		pageDto.setTotalPage(totalpage);

		modelMap.addAttribute("totalRow", totalcount);
		modelMap.addAttribute("currentRow", offsetValue + 1);
		modelMap.addAttribute("lastRow", offsetValue + listSize);
		modelMap.addAttribute("currentPage", pageNo);
		modelMap.addAttribute("totalPages", totalpage);
		modelMap.addAttribute("totalItems", listSize);
		modelMap.addAttribute("pageNoDTO", pageDto);

		modelMap.addAttribute("totalAssignedTask", totalTask);

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);

		return "caseDetails/viewAllCasesPuh";
	}

	/*
	 * @RequestMapping(value = "/pendingAssignedTask1") public String
	 * pendingAssignedTask1(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap)
	 * throws Exception {
	 * 
	 * int pageNo; if (pageDTO.getPageno() > 1) { pageNo = pageDTO.getPageno() - 1;
	 * } else { pageNo = 0; } int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id"));
	 * 
	 * Page<proCourtCaseDetails> proCourtCaeDetails =
	 * proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0, 0);
	 * List<proCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent(); int
	 * pageNo1 = proCourtCaeDetails.getTotalPages();
	 * modelMap.addAttribute("currentPage", pageNo + 1);
	 * modelMap.addAttribute("totalPages", pageNo1);
	 * modelMap.addAttribute("totalItems", proCourtCaeDetails.getSize());
	 * modelMap.addAttribute("pageNoDTO", new PageNoDTO());
	 * 
	 * String userrole =
	 * appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
	 * 
	 * modelMap.addAttribute("userRole", userrole);
	 * modelMap.addAttribute("totalAssignedTask", proCourtDtls);
	 * 
	 * return "caseDetails/viewAllCasesPuh1"; }
	 * 
	 * @RequestMapping(value = "/pendingAssignedTask") public String
	 * pendingAssignedTask(ModelMap modelMap) throws Exception {
	 * 
	 * // List<AssignedTaskPuh> pendingAssignedTask = //
	 * assignedTaskPuhRepo.findAllByIsApproved(false); // Date 06.06.2023 By Keyraj
	 * // List<proCourtCaseDetails> proCourtDtls //
	 * =proCourtCaseDetailsRepo.findALLByApproveStatusBetween(0,0,Sort.by(Sort.
	 * Direction.DESC,"id")); int pageNo = 0; int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id"));
	 * 
	 * Page<proCourtCaseDetails> proCourtCaeDetails =
	 * proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0, 0);
	 * List<proCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent(); int
	 * pageNo1 = proCourtCaeDetails.getTotalPages();
	 * modelMap.addAttribute("currentPage", pageNo + 1);
	 * modelMap.addAttribute("totalPages", pageNo1);
	 * modelMap.addAttribute("totalItems", proCourtCaeDetails.getSize());
	 * modelMap.addAttribute("pageNoDTO", new PageNoDTO());
	 * 
	 * String userrole =
	 * appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
	 * 
	 * modelMap.addAttribute("userRole", userrole);
	 * modelMap.addAttribute("totalAssignedTask", proCourtDtls);
	 * 
	 * return "caseDetails/viewAllCasesPuh1"; }
	 */

	@RequestMapping(value = "/pendingAssignedTask1")
	public String pendingAssignedTask1(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0,
				0);
		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + proCourtCaeDetails.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("totalAssignedTask", proCourtDtls);

		return "caseDetails/viewAllCasesPuh1";
	}

	@RequestMapping(value = "/pendingAssignedTask")
	public String pendingAssignedTask(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		/*
		 * Page<AssignedTaskPuh> proCourtCaeDetails =
		 * assignedTaskPuhRepo.findAllByIsApproved(false,pagable);
		 */

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0,
				0);
		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = 1;
		int lastRow = proCourtCaeDetails.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("totalAssignedTask", proCourtDtls);

		return "caseDetails/viewAllCasesPuh1";
	}

	@RequestMapping(value = "listOfCourtCases1")
	public String listOfCourtCases1(ModelMap modelMap) {

		/*
		 * String cdate="24-03-2022";
		 * 
		 * String[] split = cdate.split("-"); System.out.println(); String daaa =
		 * split[2]+"-"+split[1]+"-"+split[0];
		 */

		List<AssignedTaskPuh> assignLst = assignedTaskPuhRepo.findAll();

		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", assignLst);
		return "caseDetails/listOfCourtCases1";
	}

	@RequestMapping(value = "listOfCourtCases")
	public String listOfCourtCases(ModelMap modelMap) throws Exception {
		List<AssignedTaskPuh> assignLst = assignedTaskPuhRepo.findAll();
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("lstCourtCase", assignLst);
		return "caseDetails/viewlistOfAssignTask";
	}

	@RequestMapping(value = "listOfCourtCasesView")
	public String listOfCourtCasesView(ModelMap modelMap) throws Exception {
		List<ProCourtCaseDetails> lstCourt = proCourtCaseDetailsRepo.findAll();
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", lstCourt);
		return "caseDetails/listOfCourtCasesView";
	}
}

/*
 * @RequestMapping(value="listOfCourtCases") public String listOfCourtCases
 * (ModelMap modelMap) { List<proCourtCaseDetails> lstCourt =
 * proCourtCaseDetailsRepo.findAll(); modelMap.addAttribute("lstCourtCase",
 * lstCourt); return "caseDetails/listOfCourtCases"; } }
 */
