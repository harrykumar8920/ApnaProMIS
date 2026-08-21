package com.pams.controllers;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.AppUserDAO;
import com.pams.dto.CriminalTaskDto;
import com.pams.dto.PageNoDTO;
import com.pams.entity.AccusedActAndSection;
import com.pams.entity.AccusedMaster;
import com.pams.entity.AccusedResponse;
import com.pams.entity.AccusedStatus;
import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AddCase;
import com.pams.entity.AddCompany;
import com.pams.entity.AddCourt;
import com.pams.entity.AddDesignation;
import com.pams.entity.AddState;
import com.pams.entity.AddSubSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.ComplaintReport;
import com.pams.entity.Complaintdetl;
import com.pams.entity.CouncilDetails;
import com.pams.entity.CreateTasks;
import com.pams.entity.DetailsType;
import com.pams.entity.District;
import com.pams.entity.HearingDetails;
import com.pams.entity.InvCaseDetails;
import com.pams.entity.PairaviDetails;
import com.pams.entity.PairaviOfficer;
import com.pams.entity.Punishment1;
import com.pams.entity.RemarksEntity;
import com.pams.entity.Status;
import com.pams.entity.TypeofResponse;
import com.pams.entity.UploadAdditionalFilesDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AccusedActSectionRepository;
import com.pams.service.AccusedCompCaseDtlRepository;
import com.pams.service.AccusedMasterRepository;
import com.pams.service.AccusedResponseRepository;
import com.pams.service.AccusedStatusRepository;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AddCompanyRepository;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.AddSubSectionRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.CaseCompanyRepository;
import com.pams.service.CaseProcessingDatesRepository;
import com.pams.service.ClauseRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.CouncilDetailsRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.CreateTasksRepository;
import com.pams.service.DetailsTypeRespository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.InvCaseDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.PairaviTypeRepository;
import com.pams.service.PerformaPartyRepo;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.PunishmentRepository;
import com.pams.service.RemarksRepository;
import com.pams.service.StateRepository;
import com.pams.service.TypeofResponseRepository;
import com.pams.service.UploadAdditionalFilesDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.service.addDisposalRepository;
import com.pams.service.districtRepository;
import com.pams.utils.Utils;
import com.pams.validation.AccusedCompValidation;
import com.pams.validation.CriminalTaskValidation;
import com.pams.validation.ProMISValidator;

import jakarta.validation.Valid;

@Controller
public class OfficerController {
	private static final Logger logger = LoggerFactory.getLogger(OfficerController.class);

	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private TypeofResponseRepository typeofResponseRepo;
	@Autowired
	private AccusedActSectionRepository accusedActSectionRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhAfterCOurtRepository;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;

	@Autowired
	private ClauseRepository clauseRepo;
	@Autowired
	private PunishmentRepository punishmentRepo;

	@Autowired
	private PerformaPartyRepo performaPartyRepo;
	@Autowired
	private AccusedResponseRepository accusedResponseRepo;

	@Autowired
	private ComplaintReportRepository comprepo;
	@Autowired
	UploadAdditionalFilesDetailsRepository uploadAdditionalFilesDetailsRepo;

	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private DetailsTypeRespository detailsTypeRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired

	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private PairaviDetailsRepository pairaviDetailRepo;
	@Autowired
	private PairaviTypeRepository pairaviTypeRepo;
	@Autowired
	private AccusedCompDAO accusedComdao;
	@Autowired
	private AccusedCompCaseDtlRepository accusedCompCaseDtlRepo;
	@Autowired
	private HearingDetailsRepository hearingdtlRepo;
	@Autowired
	private InvCaseDetailsRepository invCaseDtlRepo;
	@Autowired
	private AddCompanyRepository addCompanyRepo;
	@Autowired
	private CourtTypeRepository courtTypeRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepo1;
	@Autowired
	AddStatusRepository addStatusRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepo;
	@Autowired
	private AddDesignationRepository designationRepo;

	@Autowired
	private addDisposalRepository disposalRepo;
	@Autowired
	private CaseCompanyRepository caseCompanyRepo;
	@Autowired
	private CreateTasksRepository createTasksRepo;
	@Autowired
	private AppUserDAO appUserDAO;
	@Autowired
	private AddSubSectionRepository addsubsecRepo;
	@Autowired
	private AddActRepository addActRepo;
	@Autowired
	private AddActSecRepository addactsecRepo;
	@Autowired
	private UserManagementCustom userMangCustom;
	@Autowired
	private ActSecDetailsRepository actsecdetailsRepo;
	@Autowired
	private InvCaseDetailsRepository InvCaseDtlRepo;

	@Autowired
	private CaseProcessingDatesRepository caseProcessingRepo;

	@Autowired
	private ComplaintdetlRepository complaintdetlRepo;

	@Autowired
	private AccusedMasterRepository accusedMasterRepo;

	@Autowired
	private CouncilDetailsRepository councilDetailsRepo;

	@Autowired
	private AccusedStatusRepository AccusedStatusRepo;

	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private districtRepository districtRepo;
	@Autowired
	private RemarksRepository remarksRepo;

	// ProsecutorTaskList for (Prosecutor Login)

	@RequestMapping(value = "/addCriminalDtl", params = "saveRemarks")
	public String saveRemarks(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		criminalTaskDto.setAddAccused(null);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		int tabId = 29;
		if (criminalTaskDto.getRemarksNote().isEmpty()) {
			bindResult.rejectValue("remarksNote", "errmsg.required");

			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/CriminalTaskPage";
		}

		if (criminalTaskDto.getRemarksNote().length() > 200) {
			bindResult.rejectValue("remarksNote", "remarksNote");
			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/CriminalTaskPage";
		}

		Date date = new Date();

		RemarksEntity re = new RemarksEntity();
		re.setRemarksNote(criminalTaskDto.getRemarksNote());
		re.setCreatedDate(date);
		re.setAssignedTask(assignedTaskPuh);
		remarksRepo.save(re);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Save"),
				utils.getMessage("log.login.Compliancenotesave") + " " + " and Investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		redirect.addFlashAttribute("message", "Remarks saved Successfully");
		redirect.addAttribute("tabId", tabId);
		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);
		return "redirect:/proceedTask2";
		// return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardUpdateCourtcaseDetails")
	public String forwardUpdateCourtcaseDetails(ModelMap modelMap,
			@ModelAttribute @Valid CriminalTaskDto criminalTaskDto, BindingResult bindResult) throws Exception {
		criminalTaskDto.setAddAccused(null);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		int tabId = 24;
		ProCourtCaseDetails proCourtCaseDtl =assignedTaskPuh.getProCourtCaseDetails();

		proCourtCaseDtl.setApproveStatus(2);
		proCourtCaseDetailsRepo.save(proCourtCaseDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.updatecourtcasenumberforward") + " "
						+ assignedTaskPuh.getUser().getSalutation() + " " + assignedTaskPuh.getUser().getFirstName()
						+ " "
						+ (assignedTaskPuh.getUser().getMiddleName().equals("") ? ""
								: assignedTaskPuh.getUser().getMiddleName() + "")
						+ assignedTaskPuh.getUser().getLastName() + " " + " and investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/prosecutorTaskList1")
	public String prosecutorTaskList1(@ModelAttribute PageNoDTO pageDTO, ModelMap model) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		UserDetails user = userDetailsService.getUserDetailssss();
		Page<AssignedTaskPuh> task = assignedTaskPuhRepo.findAllByUser(user, pagable);
		long totalRow = task.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + task.getNumberOfElements();
		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);
		List<AssignedTaskPuh> lst = assignedTaskPuhRepo.findAllByUser(user);
		model.addAttribute("listAssinedTask", task.getContent());
		int pageNo1 = task.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", task.getNumberOfElements());
		model.addAttribute("pageNoDTO", new PageNoDTO());

		return "caseDetails/ProsecutorTaskList";
	}

	@RequestMapping(value = "/prosecutorTaskList")
	public String prosecutorTaskList(Model model) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		UserDetails user = userDetailsService.getUserDetailssss();
		Page<AssignedTaskPuh> task = assignedTaskPuhRepo.findAllByUser(user, pagable);
		long totalRow = task.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + task.getNumberOfElements();
		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);

		List<AssignedTaskPuh> lst = assignedTaskPuhRepo.findAllByUser(user);
		model.addAttribute("listAssinedTask", task.getContent());
		int pageNo1 = task.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", task.getNumberOfElements());
		model.addAttribute("pageNoDTO", new PageNoDTO());

		return "caseDetails/ProsecutorTaskList";
	}

	/*
	 * @RequestMapping(value = "/prosecutorPendingTaskList") public String
	 * prosecutorPendingTaskList(Model model) throws Exception {
	 * 
	 * UserDetails user = userDetailsService.getUserDetailssss();
	 * List<AssignedTaskPuh> PendingTask =
	 * assignedTaskPuhRepo.findAllByUserAndIsApprovedAndApprovalStatus(user, false,
	 * 0);
	 * 
	 * // List<AssignedTaskPuh> //
	 * PendingTask=assignedTaskPuhRepo.findAllByUserAndIsApprovedAndApprovalStatus(
	 * user, // false, 0, Sort.by(Sort.Direction.ASC, "id"));
	 * 
	 * model.addAttribute("PendingTask", PendingTask); return
	 * "caseDetails/ProsecutorPendingTaskList1"; }
	 */

	@RequestMapping(value = "/prosecutorPendingTaskList")
	public String prosecutorPendingTaskList(Model model) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails user = userDetailsService.getUserDetailssss();
		Page<AssignedTaskPuh> PendingTask = assignedTaskPuhRepo.findAllByUserAndIsApprovedAndApprovalStatus(pagable,
				user, false, 0);
		long totalRow = PendingTask.getTotalElements();
		int currentRow = 1;
		int lastRow = PendingTask.getNumberOfElements();
		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);
		int pageNo1 = PendingTask.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", PendingTask.getNumberOfElements());
		model.addAttribute("pageNoDTO", new PageNoDTO());

		model.addAttribute("PendingTask", PendingTask);
		return "caseDetails/ProsecutorPendingTaskList1";
	}

	@RequestMapping(value = "/prosecutorPendingTaskList5")
	public String prosecutorPendingTaskList5(@ModelAttribute PageNoDTO pageDTO, Model model) throws Exception {
		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails user = userDetailsService.getUserDetailssss();
		Page<AssignedTaskPuh> PendingTask = assignedTaskPuhRepo.findAllByUserAndIsApprovedAndApprovalStatus(pagable,
				user, false, 0);
		long totalRow = PendingTask.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + PendingTask.getNumberOfElements();
		int pageNo1 = PendingTask.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", PendingTask.getNumberOfElements());
		model.addAttribute("pageNoDTO", new PageNoDTO());
		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);
		model.addAttribute("PendingTask", PendingTask);
		return "caseDetails/ProsecutorPendingTaskList1";
	}

	/*
	 * @RequestMapping(value = "/ApproveproTaskList") public String
	 * ApproveproTaskList(Model model) throws Exception { int pageNo = 0; int
	 * noOfrecord = 20; CreateTasks taskA = createTasksRepo.findById((long)
	 * 1).get(); Pageable pagable = PageRequest.of(pageNo, noOfrecord,
	 * Sort.by(Sort.Direction.DESC, "id")); UserDetails user =
	 * userDetailsService.getUserDetailssss(); // List<AssignedTaskPuh>
	 * ApprovedTask1 = // assignedTaskPuhRepo.findAllIfByApproveStatusIsOne(user);
	 * Page<AssignedTaskPuhAfterCOurt> ApprovedTask =
	 * assignedTaskPuhAfterCOurtRepository .findAllByUser(user, pagable); long
	 * totalRow = ApprovedTask.getTotalElements(); int currentRow = 1; int lastRow =
	 * ApprovedTask.getNumberOfElements(); int pageNo1 =
	 * ApprovedTask.getTotalPages(); model.addAttribute("currentPage", pageNo + 1);
	 * model.addAttribute("totalPages", pageNo1); model.addAttribute("totalItems",
	 * ApprovedTask.getNumberOfElements()); model.addAttribute("pageNoDTO", new
	 * PageNoDTO()); model.addAttribute("totalRow", totalRow);
	 * model.addAttribute("currentRow", currentRow); model.addAttribute("lastRow",
	 * lastRow); model.addAttribute("listAssinedTask", ApprovedTask); return
	 * "caseDetails/ProsecutorTaskList1"; }
	 */

	@RequestMapping(value = "/ApproveproTaskList")
	public String ApproveproTaskList(Model model) throws Exception {
	
	//	Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		UserDetails user = userDetailsService.getUserDetailssss();
		// List<AssignedTaskPuh> ApprovedTask1 =
		// assignedTaskPuhRepo.findAllIfByApproveStatusIsOne(user);
		List<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtRepository
				.findAllByUser(user);
		
		
		
		
		model.addAttribute("pageNoDTO", new PageNoDTO());
	
		model.addAttribute("listAssinedTask", ApprovedTask);
		return "caseDetails/ProsecutorTaskList1";
	}
	
	
	@RequestMapping(value = "/ApproveproTaskList5")
	public String ApproveproTaskList5(@ModelAttribute PageNoDTO pageDTO, Model model) throws Exception {
		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		UserDetails user = userDetailsService.getUserDetailssss();
		//Page<AssignedTaskPuh> ApprovedTask = assignedTaskPuhRepo.findAllByIsApproveStatusOne(user, pagable);
		
		
		
		//Page<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtRepository.findAll(pagable);
		Page<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtRepository
				.findAllByUser(user, pagable);
		
		
		/*
		 * long totalRow = ApprovedTask.getTotalElements(); int currentRow =
		 * (noOfrecord*pageNo)+1; int lastRow = ApprovedTask.getSize();
		 */
		long totalRow = ApprovedTask.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + ApprovedTask.getNumberOfElements();
		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);
		int pageNo1 = ApprovedTask.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", ApprovedTask.getNumberOfElements());
		model.addAttribute("pageNoDTO", new PageNoDTO());

		model.addAttribute("listAssinedTask", ApprovedTask);
		return "caseDetails/ProsecutorTaskList1";
	}

	@RequestMapping(value = "/additionalDetails", params = "assignTaskID")
	public String ApproveproTaskList(ModelMap modelMap, @RequestParam(value = "assignTaskID", required = true) Long id)
			throws Exception {

		int tabId = 21;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository.findById(id).get();

		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();

		modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/additionalDetails", params = "assignTaskForward")
	public String assignTaskForward(ModelMap modelMap,
			@RequestParam(value = "assignTaskForward", required = true) Long id, RedirectAttributes redirect)
			throws Exception {

		int tabId = 21;
		String message = "";

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();

		ProCourtCaseDetails findByAssignedTask = assignedTaskPuh.getProCourtCaseDetails();
		List<CaseProcessingDates> caseprosesingLst = caseProcessingRepo.findAllByAssignedTask(assignedTaskPuh);
		List<Complaintdetl> compdtls = complaintdetlRepo.findAllByAssignedTask(assignedTaskPuh);
		List<AddAccused> accusedLst = addAccusedRepo.findAllByAssignedTask(assignedTaskPuh);
		// List<CaseCompany> caseCompany =
		// caseCompanyRepo.findAllIbyAssignTask(assignedTaskPuh);

		if (!caseprosesingLst.isEmpty() && !compdtls.isEmpty() && !accusedLst.isEmpty())

		{
			assignedTaskPuh.setForwardedStatus(1);
			assignedTaskPuhRepo1.save(assignedTaskPuh);

			/*
			 * for (CaseCompany caseCompany2 : caseCompany) {
			 * caseCompany2.setApproveStatus(1); caseCompanyRepo.save(caseCompany2); }
			 */

			for (AddAccused accusedLst1 : accusedLst) {
				accusedLst1.setApproveStatus(1);
				addAccusedRepo.save(accusedLst1);
			}

			for (Complaintdetl accusedLst1 : compdtls) {
				accusedLst1.setApprove_status(1);
				complaintdetlRepo.save(accusedLst1);
			}

			for (CaseProcessingDates accusedLst1 : caseprosesingLst) {
				accusedLst1.setApproveStatus(1);
				caseProcessingRepo.save(accusedLst1);
			}

			CriminalTaskDto criminalTaskDto = new CriminalTaskDto();

			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

			/* if (assignedTaskPuh.getAddCase().getType().getId() == 2) { */
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " " + userdet.getMiddleName() + " "
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.forward"),
					utils.getMessage("log.login.Complainantaccusedcaseprocessingforwarded") + " "
							+ assignedTaskPuh.getUser().getSalutation() + " " + assignedTaskPuh.getUser().getFirstName()
							+ " " + assignedTaskPuh.getUser().getLastName() + " " + "and investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getFullName(), "true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			/*
			 * } else {
			 * auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails()
			 * .getUserId().toString()), userdet.getSalutation() + " " +
			 * userdet.getFirstName() + " " + userdet.getMiddleName() + " " +
			 * userdet.getLastName(), "ProMIS",
			 * Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
			 * utils.getMessage("log.login.forward"),
			 * utils.getMessage("log.login.ComplainantaccusedcaseprocessingforwardedNCLT") +
			 * " " + assignedTaskPuh.getUser().getSalutation() + " " +
			 * assignedTaskPuh.getUser().getFirstName() + " " +
			 * assignedTaskPuh.getUser().getLastName() + " " +
			 * "and investigation number is " +
			 * assignedTaskPuh.getAddCase().getInvestigationOrderNo(),
			 * userdet.getFullName(), "true", assignedTaskPuh.getAddCase().getId());
			 * auditBeanBo.save();
			 * 
			 * }
			 */

			redirect.addFlashAttribute("message", "Forwarded Successfully");
			return "redirect:/ApproveproTaskList";
		}

		if (compdtls.isEmpty()) {
			if (findByAssignedTask.getCourtType().getId() == 3 || findByAssignedTask.getCourtType().getId() == 4)
				message = " Petitoner's details, ";
			else
				message = " Complainant Details, ";

		}

		/*
		 * if (caseCompany.isEmpty()) { message = message + ", company details "; }
		 */
		if (caseprosesingLst.isEmpty()) {
			message = message + " Case processing details, ";
		}
		if (accusedLst.isEmpty()) {
			if (findByAssignedTask.getCourtType().getId() == 3 || findByAssignedTask.getCourtType().getId() == 4)
				message = message + " Respondent details, ";
			else
				message = message + "  Accused Details, ";

		}

		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		redirect.addFlashAttribute("message", "Please add" + message + "before forwarding");

		// redirect.addFlashAttribute("message","Please add Complainant Details, company
		// details,Case processing dates and Accused Details before forwarding");
		return "redirect:/ApproveproTaskList";
	}

	public void modelAttributeObject(@Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, ModelMap modelMap, int tabId,
			CriminalTaskDto criminalTaskDto) {

		modelMap.addAttribute("rejectremarksList", remarksRepo.findByAssignedTask(assignedTaskPuh));
		modelMap.addAttribute("performaPartyList", performaPartyRepo.findByAssignedTask(assignedTaskPuh));
		criminalTaskDto.setAddAccused(null);
		List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("pairaviOfficerList",
				pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("counselOfficerList",
				pairaviofficerRepo.findByType(2, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("punishmentlist", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		criminalTaskDto.setInvCaseNo(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo());
		String tt = assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo();
		// criminalTaskDto.setInvCaseNo(InvCaseNo);
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("courtType", courtType);
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("addActList", addactlist);
		criminalTaskDto.setInvCaseNo(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo());
		criminalTaskDto.setInvOrder(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderDate());
		criminalTaskDto.setSuppInv(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getSupplimentoryOrderDate());
		criminalTaskDto.setDateFilling(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getProSanctionDate());

		List<AddAccused> addaccusedList = addAccusedRepo.findAllByAssignedTaskAndAccusedTypeNot(assignedTaskPuh,
				"Individual");
		modelMap.addAttribute("addaccusedList", addaccusedList);
		modelMap.addAttribute("seclst", addactsecRepo.findAll());
		modelMap.addAttribute("subseclst",
				addsubsecRepo.findAllBySection(addactsecRepo.findById((long) 0), Sort.by(Sort.Direction.ASC, "id")));

		criminalTaskDto.setTabId(tabId);
		// List<AddAccused> accuseDtlForStatus =
		// addAccusedRepo.findAllByAssignedTask(assignedTaskPuh);
		// modelMap.addAttribute("accuseDtlForStatus", accuseDtlForStatus);
		modelMap.addAttribute("accuseDtlForStatus1", new AccusedStatus());

		List<AccusedStatus> lstAccusedStatus = null;
		if (criminalTaskDto.getEditHearing() == null) {
			lstAccusedStatus = AccusedStatusRepo.findByAssignedTaskAndApproveStatusAndStatus(assignedTaskPuh, 0, true,
					Sort.by(Sort.Direction.DESC, "id"));

		} else {
			Long hearingID = criminalTaskDto.getHearingEditId();
			lstAccusedStatus = AccusedStatusRepo.findByHearingDetailsAndStatus(criminalTaskDto.getHearingEditId(),
					true);
		}
		modelMap.addAttribute("lstAccusedStatus", lstAccusedStatus);

		assignedTaskPuh.getId();

		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		if (procasedetails.getApproveStatus() == 5) {
			criminalTaskDto.setCourtCaseName(procasedetails.getCourtCaseNo());
		}
		criminalTaskDto.setProCourtDtl(procasedetails);

		List<CaseProcessingDates> caseproseingdates = caseProcessingRepo
				.findAllByProcourtdtlAndAssignedTask(procasedetails, assignedTaskPuh);
		CaseProcessingDates caseproseingdates1 = null;
		if (!caseproseingdates.isEmpty()) {

			caseproseingdates1 = caseproseingdates.get(0);
		} else {
			caseproseingdates1 = new CaseProcessingDates();
		}

		modelMap.addAttribute("caseproseingdates", caseproseingdates1);

		InvCaseDetails invcaseDtl = InvCaseDtlRepo.findAllById(procasedetails.getInvCaseDetail().getId());
		criminalTaskDto.setAssignedTask(assignedTaskPuh);

		/*
		 * if (assignedTaskPuh.getAddCase().getTypeOfCase().getTypeOfCase().equals(
		 * "NCLT/NCLAT")) { criminalTaskDto.setTypeOfCase("NCLT"); } else {
		 * criminalTaskDto.setTypeOfCase("NCLT11"); }
		 */

		// modelMap.addAttribute("desilst",
		// designationRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));

		List<AddDesignation> sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Officer");
		List<AddDesignation> sfioCounslorDesignation = designationRepo.findByDeginationtype("SFIO Counsel");
		List<AddDesignation> CompanyEmployeeDesignation = designationRepo.findByDeginationtype("Company Employee");
		modelMap.addAttribute("desilst", sfioOfficerDesignation);
		modelMap.addAttribute("desilstC", sfioCounslorDesignation);
		modelMap.addAttribute("desilstE", CompanyEmployeeDesignation);

		modelMap.addAttribute("assignedDtl", procasedetails);
		modelMap.addAttribute("ptypelst", pairaviTypeRepo.findAll());
		modelMap.addAttribute("criminalTaskDto", criminalTaskDto);
		modelMap.addAttribute("invCasedtl", invcaseDtl);

		List<HearingDetails> hearinglist = hearingdtlRepo.findByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));

		if (!hearinglist.isEmpty()) {
			for (HearingDetails hearingDetails : hearinglist) {

				List<AccusedStatus> accusedwithStatus = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());
				hearingDetails.setAccusedwithStatus(accusedwithStatus);
			}
		}

		HearingDetails currenthearingDtl = hearingdtlRepo
				.findByProcourtdtlAndCurrentStatusAndAssignedTask(procasedetails, true, assignedTaskPuh);

		if (currenthearingDtl != null) {
			criminalTaskDto.setLastHearingDate(currenthearingDtl.getNextHearingDate());
		}

		List<Status> StatusList = addStatusRepo.findAllByTypeAndIsActive("A",true);
		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActive("C",true);
		ComplaintReport genreport = comprepo.findByAssignedTaskPuh(assignedTaskPuh);

		if (genreport != null) {
			criminalTaskDto.setTypeofreport(genreport.getTypeOfReport());
			criminalTaskDto.setGenreportID(genreport.getId());
			criminalTaskDto.setApproveStatusGenReport(genreport.getApproveStatus());
			criminalTaskDto.setRejectRemarkGenReport(genreport.getRejectRemark());
		}

		List<UploadAdditionalFilesDetails> uploadadditionalfile = uploadAdditionalFilesDetailsRepo
				.findByAssignedTaskPuhdtl(assignedTaskPuh);

		modelMap.addAttribute("uploadadditionalfile", uploadadditionalfile);
		modelMap.addAttribute("genreport", genreport);

		modelMap.addAttribute("statusLst", StatusList);
		modelMap.addAttribute("statusLst1", StatusList1);
		modelMap.addAttribute("hearinglist", hearinglist);

		/*
		 * List<AccusedCompCaseDtl> accCompanyList =
		 * accusedCompCaseRepository.findByProCourtId(procasedetails.getId());
		 * AccusedCompCaseDtl savedAccusedCompCaseDtl=accCompanyList.get(0);
		 * 
		 * AccusedCompCaseDtl savedAccusedCompCaseDtl = accusedComdao
		 * .findByProCourtId(procasedetails.getId());
		 */

		Complaintdetl compDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(procasedetails, assignedTaskPuh);

		if (compDtl != null) {
			criminalTaskDto.setRejectRemark(compDtl.getRejectRemark());
			criminalTaskDto.setApprove_status(compDtl.getApprove_status());
			criminalTaskDto.setComplanitId(compDtl.getComplanitId());
			criminalTaskDto.setComplanitEmail(compDtl.getComplanitEmail());
			criminalTaskDto.setComplanitName(compDtl.getComplanitName());
			criminalTaskDto.setComplaintMobile(compDtl.getComplaintMobile());
			criminalTaskDto.setComplanitdesignation(compDtl.getComplanitdesignation());
			criminalTaskDto.setComplaintPetinoner(compDtl.getComplaintPetinoner());
			criminalTaskDto.setIOName(compDtl.getIOName());
			criminalTaskDto.setComplaintPetinonerDate(compDtl.getComplaintPetinonerDate());
			criminalTaskDto.setIsSPCourt(compDtl.getIsSPCourt());
			// criminalTaskDto.setInvCaseNo(compDtl.getProcourtdtl().getInvCaseDetail().getMcaOrder());
			criminalTaskDto.setDesigInvesOffi(compDtl.getDesigInvesOffi());

		} else {
			criminalTaskDto.setComplaintPetinoner(procasedetails.getCnrNumber());
			criminalTaskDto.setComplaintPetinonerDate(procasedetails.getFillingDate());

		}

		List<PairaviDetails> pairvidtl = pairaviDetailRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh);
		List<CouncilDetails> councildtl = councilDetailsRepo.findAllByProcourtdtl(procasedetails);
		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		ProCourtCaseDetails t = procasedetails;
		modelMap.addAttribute("personList", accusedList);
		modelMap.addAttribute("personList1", accusedList);
		List<CaseCompany> companyList = caseCompanyRepo.findAllIbyAssignTask(assignedTaskPuh.getId());

		modelMap.addAttribute("companyList", companyList);

		/*
		 * if (savedAccusedCompCaseDtl != null) {
		 * 
		 * System.out.println(t);
		 * 
		 * modelMap.addAttribute("companyList", savedAccusedCompCaseDtl.getCompany());
		 * modelMap.addAttribute("personList", accusedList);
		 * 
		 * modelMap.addAttribute("companyList", companyList);
		 * criminalTaskDto.setAccusedCompId(savedAccusedCompCaseDtl.getId());
		 * modelMap.addAttribute("pairvidtl", pairvidtl);
		 * modelMap.addAttribute("councildtl", councildtl);
		 * 
		 * System.out.println(savedAccusedCompCaseDtl.getCompany()); }
		 */

		modelMap.addAttribute("pairvidtl", pairvidtl);
		modelMap.addAttribute("councildtl", councildtl);
	}

	public void modelAttributeObjectAfterCourt(@Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, ModelMap modelMap,
			int tabId, CriminalTaskDto criminalTaskDto) {
		modelMap.addAttribute("rejectremarksList", remarksRepo.findByAssignedTask(assignedTaskPuh));
		modelMap.addAttribute("performaPartyList", performaPartyRepo.findByAssignedTask(assignedTaskPuh));
		criminalTaskDto.setAddAccused(null);
		List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("pairaviOfficerList",
				pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("counselOfficerList",
				pairaviofficerRepo.findByType(2, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("punishmentlist", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		criminalTaskDto.setInvCaseNo(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo());
		String tt = assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo();
		// criminalTaskDto.setInvCaseNo(InvCaseNo);
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("courtType", courtType);
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("addActList", addactlist);
		criminalTaskDto.setInvCaseNo(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo());
		criminalTaskDto.setInvOrder(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderDate());
		criminalTaskDto.setSuppInv(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getSupplimentoryOrderDate());
		criminalTaskDto.setDateFilling(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getProSanctionDate());

		List<AddAccused> addaccusedList = addAccusedRepo.findAllByAssignedTaskAndAccusedTypeNot(assignedTaskPuh,
				"Individual");
		modelMap.addAttribute("addaccusedList", addaccusedList);
		modelMap.addAttribute("seclst", addactsecRepo.findAll());
		modelMap.addAttribute("subseclst",
				addsubsecRepo.findAllBySection(addactsecRepo.findById((long) 0), Sort.by(Sort.Direction.ASC, "id")));

		criminalTaskDto.setTabId(tabId);

		modelMap.addAttribute("accuseDtlForStatus1", new AccusedStatus());

		List<AccusedStatus> lstAccusedStatus = null;
		if (criminalTaskDto.getEditHearing() == null) {
			lstAccusedStatus = AccusedStatusRepo.findByAssignedTaskAndApproveStatusAndStatus(assignedTaskPuh, 0, true,
					Sort.by(Sort.Direction.DESC, "id"));

		} else {
			Long hearingID = criminalTaskDto.getHearingEditId();
			lstAccusedStatus = AccusedStatusRepo.findByHearingDetailsAndStatus(criminalTaskDto.getHearingEditId(),
					true);
		}
		modelMap.addAttribute("lstAccusedStatus", lstAccusedStatus);

		assignedTaskPuh.getId();

		// proCourtCaseDetails procasedetails =
		// assignedTaskPuh.getProCourtCaseDetails();

		// proCourtCaseDetails
		// procasedetails=proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		if (procasedetails.getApproveStatus() == 5) {
			criminalTaskDto.setCourtCaseName(procasedetails.getCourtCaseNo());
		}
		criminalTaskDto.setProCourtDtl(procasedetails);

		List<CaseProcessingDates> caseproseingdates = caseProcessingRepo
				.findAllByProcourtdtlAndAssignedTask(procasedetails, assignedTaskPuh);
		CaseProcessingDates caseproseingdates1 = null;
		if (!caseproseingdates.isEmpty()) {

			caseproseingdates1 = caseproseingdates.get(0);
		} else {
			caseproseingdates1 = new CaseProcessingDates();
		}

		modelMap.addAttribute("caseproseingdates", caseproseingdates1);

		InvCaseDetails invcaseDtl = InvCaseDtlRepo.findAllById(procasedetails.getInvCaseDetail().getId());
		criminalTaskDto.setAssignedTask(assignedTaskPuh);

		List<AddDesignation> sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Officer");
		List<AddDesignation> sfioCounslorDesignation = designationRepo.findByDeginationtype("SFIO Counsel");
		List<AddDesignation> CompanyEmployeeDesignation = designationRepo.findByDeginationtype("Company Employee");
		modelMap.addAttribute("desilst", sfioOfficerDesignation);
		modelMap.addAttribute("desilstC", sfioCounslorDesignation);
		modelMap.addAttribute("desilstE", CompanyEmployeeDesignation);

		modelMap.addAttribute("assignedDtl", procasedetails);
		modelMap.addAttribute("ptypelst", pairaviTypeRepo.findAll());
		modelMap.addAttribute("criminalTaskDto", criminalTaskDto);
		modelMap.addAttribute("invCasedtl", invcaseDtl);

		List<HearingDetails> hearinglist = hearingdtlRepo.findByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));

		if (!hearinglist.isEmpty()) {
			for (HearingDetails hearingDetails : hearinglist) {

				List<AccusedStatus> accusedwithStatus = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());
				hearingDetails.setAccusedwithStatus(accusedwithStatus);
			}
		}

		/*
		 * HearingDetails currenthearingDtl = hearingdtlRepo
		 * .findByProcourtdtlAndCurrentStatusAndAssignedTask(procasedetails, true,
		 * assignedTaskPuh);
		 * 
		 * if (currenthearingDtl != null) {
		 * criminalTaskDto.setLastHearingDate(currenthearingDtl.getNextHearingDate()); }
		 */

		List<Status> StatusList = addStatusRepo.findAllByTypeAndIsActive("A",true);
		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActive("C",true);
		ComplaintReport genreport = comprepo.findByAssignedTaskPuh(assignedTaskPuh);

		if (genreport != null) {
			criminalTaskDto.setTypeofreport(genreport.getTypeOfReport());
			criminalTaskDto.setGenreportID(genreport.getId());
			criminalTaskDto.setApproveStatusGenReport(genreport.getApproveStatus());
			criminalTaskDto.setRejectRemarkGenReport(genreport.getRejectRemark());
		}

		List<UploadAdditionalFilesDetails> uploadadditionalfile = uploadAdditionalFilesDetailsRepo
				.findByAssignedTaskPuhdtl(assignedTaskPuh);

		modelMap.addAttribute("uploadadditionalfile", uploadadditionalfile);
		modelMap.addAttribute("genreport", genreport);

		modelMap.addAttribute("statusLst", StatusList);
		modelMap.addAttribute("statusLst1", StatusList1);
		modelMap.addAttribute("hearinglist", hearinglist);

		Complaintdetl compDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(procasedetails, assignedTaskPuh);

		if (compDtl != null) {
			criminalTaskDto.setRejectRemark(compDtl.getRejectRemark());
			criminalTaskDto.setApprove_status(compDtl.getApprove_status());
			criminalTaskDto.setComplanitId(compDtl.getComplanitId());
			criminalTaskDto.setComplanitEmail(compDtl.getComplanitEmail());
			criminalTaskDto.setComplanitName(compDtl.getComplanitName());
			criminalTaskDto.setComplaintMobile(compDtl.getComplaintMobile());
			criminalTaskDto.setComplanitdesignation(compDtl.getComplanitdesignation());
			criminalTaskDto.setComplaintPetinoner(compDtl.getComplaintPetinoner());
			criminalTaskDto.setIOName(compDtl.getIOName());
			criminalTaskDto.setComplaintPetinonerDate(compDtl.getComplaintPetinonerDate());
			criminalTaskDto.setIsSPCourt(compDtl.getIsSPCourt());
			// criminalTaskDto.setInvCaseNo(compDtl.getProcourtdtl().getInvCaseDetail().getMcaOrder());
			criminalTaskDto.setDesigInvesOffi(compDtl.getDesigInvesOffi());

		} else {
			criminalTaskDto.setComplaintPetinoner(procasedetails.getCnrNumber());
			criminalTaskDto.setComplaintPetinonerDate(procasedetails.getFillingDate());

		}

		List<PairaviDetails> pairvidtl = pairaviDetailRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh);
		List<CouncilDetails> councildtl = councilDetailsRepo.findAllByProcourtdtl(procasedetails);
		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		ProCourtCaseDetails t = procasedetails;
		modelMap.addAttribute("personList", accusedList);
		modelMap.addAttribute("personList1", accusedList);
		List<CaseCompany> companyList = caseCompanyRepo.findAllIbyAssignTask(assignedTaskPuh.getId());

		modelMap.addAttribute("companyList", companyList);

		modelMap.addAttribute("pairvidtl", pairvidtl);
		modelMap.addAttribute("councildtl", councildtl);
	}

	@RequestMapping(value = "/addCriminalDtl", params = "complainDtlSave")
	public String ComplainDtlSave(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		int tabId = 21;

		if (criminalTaskDto.getComplanitdesignation() == null
				|| criminalTaskDto.getComplanitdesignation().getId() == 0) {
			bindResult.rejectValue("complanitdesignation", "msg.wrongId");
		}

		if (criminalTaskDto.getIOName() == null || criminalTaskDto.getIOName().equalsIgnoreCase("0")) {
			bindResult.rejectValue("IOName", "msg.wrongId");
		}

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.complaintValidation(criminalTaskDto, bindResult);
		if (bindResult.hasErrors()) {
			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/CriminalTaskPage";
		}

		Complaintdetl complaintDtl1 = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(proCourtCaseDtl,
				assignedTaskPuh);
		if (complaintDtl1 == null) {

			Complaintdetl complaintDtl = new Complaintdetl();
			complaintDtl.setProcourtdtl(criminalTaskDto.getProCourtDtl());
			complaintDtl.setComplaintPetinoner(criminalTaskDto.getComplaintPetinoner());
			complaintDtl.setComplanitdesignation(criminalTaskDto.getComplanitdesignation());
			complaintDtl.setDesigInvesOffi(criminalTaskDto.getDesigInvesOffi());
			complaintDtl.setComplanitEmail(criminalTaskDto.getComplanitEmail());
			complaintDtl.setComplanitName(criminalTaskDto.getComplanitName());
			complaintDtl.setComplaintMobile(criminalTaskDto.getComplaintMobile());
			complaintDtl.setIOName(criminalTaskDto.getIOName());
			complaintDtl.setAssignedTask(assignedTaskPuh);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			complaintDtl.setCreatedBy(userdet);
			complaintDtl.setUpdatedBy(userdet);
			complaintDtl.setApproveBy(userdet);
			complaintDtl.setCreatedDate(new Date());
			complaintDtl.setComplaintPetinonerDate(criminalTaskDto.getComplaintPetinonerDate());
			complaintDtl.setIsSPCourt(criminalTaskDto.getIsSPCourt());
			complaintDtl.setApprove_status(3);

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.addcasesComplainantSave") + " " + " and Investigation number is "
							+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", proCourtCaseDtl.getAddCase().getId());
			auditBeanBo.save();

			complaintdetlRepo.save(complaintDtl);
			modelMap.addAttribute("message", "Complainant Details saved Successfully  :");
			CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

			return "Task/CriminalTaskPage";
		} else {
			complaintDtl1.setProcourtdtl(criminalTaskDto.getProCourtDtl());
			complaintDtl1.setComplaintPetinoner(criminalTaskDto.getComplaintPetinoner());
			complaintDtl1.setComplanitdesignation(criminalTaskDto.getComplanitdesignation());
			complaintDtl1.setDesigInvesOffi(criminalTaskDto.getDesigInvesOffi());
			complaintDtl1.setComplanitEmail(criminalTaskDto.getComplanitEmail());
			complaintDtl1.setComplanitName(criminalTaskDto.getComplanitName());
			complaintDtl1.setComplaintMobile(criminalTaskDto.getComplaintMobile());
			complaintDtl1.setIOName(criminalTaskDto.getIOName());
			complaintDtl1.setIsSPCourt(criminalTaskDto.getIsSPCourt());
			complaintDtl1.setAssignedTask(assignedTaskPuh);
			complaintDtl1.setApprove_status(3);

			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

			complaintDtl1.setUpdatedBy(userdet);

			complaintDtl1.setUpdatedDate(new Date());

			complaintdetlRepo.save(complaintDtl1);
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.addcasesComplainantUpdate") + " " + " and Investigation Number is "
							+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", proCourtCaseDtl.getAddCase().getId());
			auditBeanBo.save();

			CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
			modelMap.addAttribute("message", "Complainant Details updated Successfully  :");

			return "Task/CriminalTaskPage";
		}
	}

	@RequestMapping(value = "AddCompanyDetails")
	public String addCompany(ModelMap modelMap, @Valid @ModelAttribute CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 22;

		AddCompany companylst = new AddCompany();
		companylst.setId(criminalTaskDto.getCompanyId());

		String compName = criminalTaskDto.getCompName();
		String compCin = criminalTaskDto.getCin();
		String coyadd = criminalTaskDto.getCompAddess();
		Long compId = criminalTaskDto.getCompanyId();

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.companyValidation(criminalTaskDto, bindResult);

		if (bindResult.hasErrors()) {

			CriminalTaskDto crim = new CriminalTaskDto();
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, crim);
			return "Task/CriminalTaskPage";
		}

	
		
		List<Status> StatusList = addStatusRepo.findAllByTypeAndIsActive("A",true);


		modelMap.addAttribute("statusLst", StatusList);

		// proCourtCaseDetails courtdtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);
		ProCourtCaseDetails courtdtl = assignedTaskPuh.getProCourtCaseDetails();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		companylst = addCompanyRepo.findAllByCin(compCin);

		if (companylst != null) {
			CaseCompany compdtl = caseCompanyRepo.findAllByCompanyAndProcourtdtl(companylst, courtdtl);
			if (compdtl != null) {
				modelMap.addAttribute("message", " Duplicate CIN is not allowed");
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
				return "Task/CriminalTaskPage";

			}
		}

		AddCompany addCompany = new AddCompany();

		if (companylst == null) {
			addCompany.setId(compId);
			addCompany.setCin(compCin);
			addCompany.setCompanyName(compName);
			addCompany.setAddress(coyadd);

			addCompany = addCompanyRepo.save(addCompany);
		}

		addCompany = addCompanyRepo.findAllByCin(compCin);
		int approveStatus1 = 0;
		if (assignedTaskPuh.getForwardedStatus() == 0) {
			approveStatus1 = 5;
		}

		CaseCompany caseCompany = new CaseCompany(addCompany, courtdtl, compId, userdet, new Date(), assignedTaskPuh,
				approveStatus1);

		caseCompanyRepo.save(caseCompany);

		modelMap.addAttribute("message", " Company  is added successfully ");

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "addCriminalDtl", params = "editcompany")
	public String editcompany(@RequestParam(value = "editcompany", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) {
		AddCompany AddCompanyDetails = addCompanyRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		criminalTaskDto.setCompanyId(AddCompanyDetails.getId());
		criminalTaskDto.setCompName(AddCompanyDetails.getCompanyName());
		criminalTaskDto.setCompAddess(AddCompanyDetails.getAddress());
		criminalTaskDto.setCin(AddCompanyDetails.getCin());

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "editCompanyDetails")
	public String editCompany(ModelMap modelMap, @Valid @ModelAttribute CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 22;

		AddCompany companylst = new AddCompany();
		companylst.setId(criminalTaskDto.getCompanyId());

		String compName = criminalTaskDto.getCompName();
		String compCin = criminalTaskDto.getCin();
		String coyadd = criminalTaskDto.getCompAddess();
		Long compId = criminalTaskDto.getCompanyId();

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.companyValidation(criminalTaskDto, bindResult);

		if (bindResult.hasErrors()) {

			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/CriminalTaskPage";
		}

		List<Status> StatusList = addStatusRepo.findAllByTypeAndIsActive("A",true);
		

		modelMap.addAttribute("statusLst", StatusList);

		AddCompany addCompany1 = addCompanyRepo.findAllByCin(compCin);

		AddCompany addCompany = addCompanyRepo.findById(compId).get();
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();

		if (addCompany1 != null) {
			if (addCompany1.getCin() == addCompany.getCin() && addCompany1.getId() == addCompany.getId()) {
				addCompany.setId(compId);
				addCompany.setCin(compCin);
				addCompany.setCompanyName(compName);
				addCompany.setAddress(coyadd);

				AddCompany tt = addCompanyRepo.save(addCompany);

				CaseCompany casecomany = caseCompanyRepo.findAllByCompanyAndProcourtdtl(tt, proCourtCaseDtl);
				casecomany.setApproveStatus(0);
				if (assignedTaskPuh.getForwardedStatus() == 0) {
					casecomany.setApproveStatus(5);
				}
				casecomany.setRejectRemark("");
				caseCompanyRepo.save(casecomany);

				modelMap.addAttribute("message", " Company  is updated successfully ");

				CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

				return "Task/CriminalTaskPage";
			} else {
				modelMap.addAttribute("message", " Company CIN is already added");
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
				return "Task/CriminalTaskPage";

			}
		}

		addCompany.setId(compId);
		addCompany.setCin(compCin);
		addCompany.setCompanyName(compName);
		addCompany.setAddress(coyadd);

		AddCompany tt = addCompanyRepo.save(addCompany);
		CaseCompany casecomany = caseCompanyRepo.findAllByCompanyAndProcourtdtl(tt, proCourtCaseDtl);
		casecomany.setApproveStatus(0);
		casecomany.setUpdatedDate(new Date());
		caseCompanyRepo.save(casecomany);
		modelMap.addAttribute("message", " Company  is updated successfully ");

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPage";

	}

	@PostMapping(value = "/addCriminalDtl", params = "savePairavi")
	public String savePairavi(ModelMap modelMap,
	        @ModelAttribute("criminalTaskDto") @Valid CriminalTaskDto criminalTaskDto, 
	        BindingResult bindResult,
	        RedirectAttributes redirect) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAddCase(assignedTaskPuh.getAddCase());
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		Long paraviid = criminalTaskDto.getPairaviId();

		int tabId = 22;
		PairaviDetails pairaviDetails = new PairaviDetails();

		pairaviDetails.setId(paraviid);
		PairaviDetails pairavidtl = pairaviDetailRepo.findAllByProcourtdtlAndIsActive(proCourtCaseDtl, true);

		if (pairavidtl != null && criminalTaskDto.getPairavifromDate() != null) {
			Date fromDate = criminalTaskDto.getPairavifromDate();

			if (paraviid == null) {
				if (pairavidtl.getToDate() != null) {
					if (fromDate.compareTo(pairavidtl.getToDate()) < 0) {

						bindResult.rejectValue("pairavifromDate", "errmsg.PairaviDate");
					}
				} else {
					if (fromDate.compareTo(pairavidtl.getFromDate()) < 0) {

						bindResult.rejectValue("pairavifromDate", "errmsg.PairaviDate1");
					}

				}
			}

		}

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		if (criminalTaskDto.getPairaviType() == null || criminalTaskDto.getPairaviType().getId() == 0) {

			criminalTaskVal.pairaviOfficerEarliar(criminalTaskDto, bindResult);
		}

		else {

			if (criminalTaskDto.getPairaviType().getPairaviType().equals("Earlier")) {

				criminalTaskVal.pairaviOfficerEarliar(criminalTaskDto, bindResult);
			}

			if (criminalTaskDto.getPairaviType().getPairaviType().equals("Current")) {

				criminalTaskVal.pairaviOfficercurrent(criminalTaskDto, bindResult);
			}

		}
		if (bindResult.hasErrors()) {

			// CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/CriminalTaskPage";

		} else {

			if (pairavidtl != null) {
				pairavidtl.setIsActive(false);

				if (criminalTaskDto.getPairaviatoDate() != null && pairavidtl.getToDate() == null) {
					pairavidtl.setToDate(criminalTaskDto.getPairaviatoDate());
				} else if (criminalTaskDto.getPairaviatoDate() == null && pairavidtl.getToDate() == null) {
					pairavidtl.setToDate(criminalTaskDto.getPairavifromDate());
				}
				pairaviDetailRepo.save(pairavidtl);

			}

			DetailsType dt = detailsTypeRepo.findAllById(4L);
			pairaviDetails.setId(paraviid);
			pairaviDetails.setDetailsType(dt);
			pairaviDetails.setPairaviType(criminalTaskDto.getPairaviType());
			pairaviDetails.setPairaviOfficer(criminalTaskDto.getPairaviOfficer());
			// pairaviDetails.setName(criminalTaskDto.getPairaviName());
			// pairaviDetails.setEmail(criminalTaskDto.getPairaviemail());
			// pairaviDetails.setMobile(criminalTaskDto.getPairaviMobile());
			// pairaviDetails.setDesignation(criminalTaskDto.getPairavidesignation());
			pairaviDetails.setFromDate(criminalTaskDto.getPairavifromDate());
			pairaviDetails.setToDate(criminalTaskDto.getPairavitoDate());
			pairaviDetails.setProcourtdtl(proCourtCaseDtl);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			pairaviDetails.setCreatedBy(userdet);
			pairaviDetails.setCreatedDate(new Date());
			pairaviDetails.setAssignedTask(assignedTaskPuh);

			pairaviDetailRepo.save(pairaviDetails);

			if (paraviid == null) {
				modelMap.addAttribute("message", " pairavi officer Added Successfully  : ");
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Save"),
						utils.getMessage("log.login.addcasesPairaviSave") + " " + " and Investigation number is "
								+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();

			} else {
				modelMap.addAttribute("message", " pairavi officer Updated Successfully  : ");
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Update"),
						utils.getMessage("log.login.addcasesPairaviupdated") + " " + " and Investigation Number is "
								+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();
			}

		}

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

		modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		// return "Task/CriminalTaskPage";

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		// return "redirect:/proceedTask";
		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "editPairaviOfficer")

	public String editTask(@RequestParam(value = "editPairaviOfficer", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) {
		PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid"));
		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// criminalTaskDto.setPairaviemail(pofficeredit.getEmail());
		// criminalTaskDto.setPairavidesignation(pofficeredit.getDesignation());
		criminalTaskDto.setPairaviOfficer(pofficeredit.getPairaviOfficer());
		criminalTaskDto.setPairaviId(pofficeredit.getId());
		criminalTaskDto.setPairaviType(pofficeredit.getPairaviType());
		// criminalTaskDto.setPairaviMobile(pofficeredit.getMobile());
		criminalTaskDto.setPairavifromDate(pofficeredit.getFromDate());
		criminalTaskDto.setPairavitoDate(pofficeredit.getToDate());

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "editAccused")

	public String editAccused(@RequestParam(value = "editAccused", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		Set<AccusedActAndSection> actSection = accusedDetails.getActSection();
		Long[] accusedACT = new Long[actSection.size()];
		Long[] accusedSection = new Long[actSection.size()];
		Long[] accusedSubSection = new Long[actSection.size()];
		String[] clause = new String[actSection.size()];
		String[] actName = new String[actSection.size()];
		String[] sectionName = new String[actSection.size()];
		String[] subsectionName = new String[actSection.size()];

		Integer[] punishmentID = new Integer[actSection.size()];
		String[] punishmentName = new String[actSection.size()];
		String[] compoundability = new String[actSection.size()];

		String disc = null;
		String actName1 = null;
		String sectionName1 = null;
		String subsectionName1 = null;
		if (!actSection.isEmpty()) {

			int i = 0;
			for (AccusedActAndSection actSection15 : actSection) {
				Long accusedACT132 = actSection15.getAct().getId();
				String actName2 = actSection15.getAct().getAct();

				Long section = actSection15.getSection().getId();
				String sectionName12 = actSection15.getSection().getSection();
				Long subsection = actSection15.getSubSection().getId();
				String subsectionName2 = actSection15.getSubSection().getSubSection();
				String disc1 = actSection15.getDescription();
				String claus = actSection15.getClause();
				Integer punishment1ID = actSection15.getPunishment().getId();
				String punishmentName1 = actSection15.getPunishment().getPunishment1();
				accusedACT[i] = accusedACT132;
				punishmentID[i] = punishment1ID;
				accusedSection[i] = section;
				accusedSubSection[i] = subsection;
				punishmentName[i] = punishmentName1;
				compoundability[i] = actSection15.getCompatability();

				if (subsectionName1 == null) {
					subsectionName1 = subsectionName2;
				} else {
					subsectionName1 = subsectionName1 + "~" + subsectionName2;
				}

				subsectionName[i] = subsectionName1;

				if (sectionName1 == null) {
					sectionName1 = sectionName12;
				} else {
					sectionName1 = sectionName1 + "~" + sectionName12;
				}

				sectionName[i] = sectionName1;

				if (actName1 == null) {
					actName1 = actName2;
				} else {
					actName1 = actName1 + "~" + actName2;
				}

				actName[i] = actName1;

				if (disc == null) {
					disc = disc1;
				} else {
					disc = disc + "~" + disc1;
				}

				clause[i] = claus;

				i++;
			}

		}
		criminalTaskDto.setAaccusedPunishment(punishmentID);
		criminalTaskDto.setAaccusedPunishmentName(punishmentName);
		criminalTaskDto.setAccusedCompoundability(compoundability);
		criminalTaskDto.setAccusedName(accusedDetails.getAccusedName());
		criminalTaskDto.setAccusedType(accusedDetails.getAccusedType());
		criminalTaskDto.setAccPan(accusedDetails.getPanNumber());
		criminalTaskDto.setCinRespondent(accusedDetails.getCinNumber());
		criminalTaskDto.setAddAccusedAddress(accusedDetails.getAddress());
		criminalTaskDto.setAccDesination2(accusedDetails.getDesignation());
		criminalTaskDto.setIndividualRelateTo(accusedDetails.getIndividualRelateTo());
		criminalTaskDto.setActName(actName1);
		criminalTaskDto.setSubsectionName(subsectionName1);
		criminalTaskDto.setSectionName(sectionName1);
		criminalTaskDto.setAccusedIdEdit(id);
		criminalTaskDto.setArrestDuringInvestigation(accusedDetails.isArrestDuringInvestigation());
		criminalTaskDto.setLocText(accusedDetails.isLocText());
		criminalTaskDto.setDateofOpen(accusedDetails.getDateofOpen());
		criminalTaskDto.setDateofDeletion(accusedDetails.getDateofDeletion());
		criminalTaskDto.setDateofArrest(accusedDetails.getDateofArrest());
		criminalTaskDto.setDateofGrantClosing(accusedDetails.getDateofGrantClosing());
		criminalTaskDto.setAccusedACT(accusedACT);
		criminalTaskDto.setAccusedSection(accusedSection);
		criminalTaskDto.setAccusedSubSection(accusedSubSection);
		criminalTaskDto.setAccusedDescription(disc);
		criminalTaskDto.setAccusedClause(clause);
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "saveHearingDetails1")
	public String saveHearingDetails1(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 266;

		Long id = criminalTaskDto.getAccusedStatusID();
		AccusedStatus accuse = new AccusedStatus();
		if (id != null) {
			accuse.setId(id);
		}
		Status status = criminalTaskDto.getCaseStatus();
		AddAccused accused = criminalTaskDto.getAddAccused();

		AssignedTaskPuhAfterCOurt assignTask = criminalTaskDto.getAssignedTask();

		List<AccusedStatus> accuseDetails = AccusedStatusRepo
				.findByAssignedTaskAndApproveStatusAndAddAccusedAndStatus(assignTask, 0, accused, true);

		if (!accuseDetails.isEmpty() && id == null) {
			bindResult.rejectValue("addAccused", "msg.accusedStatus");

			criminalTaskDto.setAccusedStatusID2((long) 2);
			modelAttributeObjectAfterCourt(assignTask, modelMap, tabId, criminalTaskDto);
			modelMap.addAttribute("message", " Please Select other Accused. : ");

			return "Task/CriminalTaskPage";

		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		accuse.setAssignedTask(assignTask);
		accuse.setCaseStatus(status);
		accuse.setCreatedDate(new Date());
		accuse.setCreatedBy(userdet);
		accuse.setUpdatedBy(userdet);
		accuse.setAddAccused(accused);
		accuse.setProcourtdtl(assignTask.getProCourtCaseDetails());
		if (id == null) {
			modelMap.addAttribute("message", " Accused Status Added Successfully : ");

		} else {
			modelMap.addAttribute("message", " Accused Status Updated Successfully : ");
		}

		AccusedStatusRepo.save(accuse);

		/*
		 * //criminalTaskDto.setAccusedStatusID2((long) 1);
		 * criminalTaskDto.setAccusedStatusID(null); modelAttributeObject(assignTask,
		 * modelMap, tabId,criminalTaskDto);
		 * 
		 * return "Task/CriminalTaskPage";
		 */

		redirect.addFlashAttribute("message", "Accused Status Added Successfully");
		redirect.addAttribute("tabId", tabId);
		redirect.addAttribute("assignedTaskPuh", assignTask);
		return "redirect:/proceedTask2";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "editAccusaedStatus")
	public String editAccusaedStatus(@RequestParam(value = "editAccusaedStatus", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto1) {
		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto1.getAssignedTask().getId()).get();

		AccusedStatus accused = AccusedStatusRepo.findById(id).get();
		criminalTaskDto.setCaseStatus(accused.getCaseStatus());
		// criminalTaskDto.setAddAccused(accused.getAddAccused());
		criminalTaskDto.setAccusedStatusID(accused.getId());
		criminalTaskDto.setAccusedStatusID2((long) 2);
		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "deleteAccusaedStatus")
	public String deleteAccusaedStatus(@RequestParam(value = "deleteAccusaedStatus", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) {

		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		AccusedStatus accused = AccusedStatusRepo.findById(id).get();
		accused.setStatus(false);
		AccusedStatusRepo.save(accused);

		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, new CriminalTaskDto());
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "saveHearingDetails")
	public String saveHearingDetails(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		// proCourtCaseDetails procasedetails =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();

		Long hearingID = criminalTaskDto.getHearingEditId();
		HearingDetails HearingDetails;
		HearingDetails hd = new HearingDetails();
		hd.setRemarks(criminalTaskDto.getFinalRemarks());
		hd.setDstatus(criminalTaskDto.getDstatus());		
		PairaviOfficer tt = criminalTaskDto.getOfficerH();

		if (criminalTaskDto.getOfficerH() == null) {

			bindResult.rejectValue("officerH", "errmsg.required");
		}
		if (criminalTaskDto.getCounselNameH() == null) {

			bindResult.rejectValue("counselNameH", "errmsg.required");
		}

		if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
			MultipartFile additionalDoc = criminalTaskDto.getAdditionalDoc();
			ProMISValidator promisValid = new ProMISValidator();
			promisValid.isValidadditionalFile("additionalDoc", additionalDoc, bindResult);

		}

		Long id = (hearingdtlRepo.findMaxid() != null) ? (hearingdtlRepo.findMaxid() + 1) : 1;
		String id1 = String.valueOf(id);

		if (hearingID != null) {
			hd.setId(hearingID);
			id1 = String.valueOf(hearingID);
		}

		hd.setProcourtdtl(procasedetails);
		hd.setLastHearingDate(criminalTaskDto.getLastHearingDate());
		hd.setNextHearingDate(criminalTaskDto.getNextHearingDate());
		hd.setStatus(criminalTaskDto.getStatus());
		hd.setDateofCaseStatusUpdate(criminalTaskDto.getDateofCaseStatusUpdate());
		hd.setCounselName(criminalTaskDto.getCounselNameH());
		hd.setOfficer(criminalTaskDto.getOfficerH());

		/*
		 * hd.setCounselDesignation(criminalTaskDto.getCounselDesignation());
		 * hd.setCounselDesignation1(criminalTaskDto.getCounselDesignation1());
		 * hd.setCounselEmail(criminalTaskDto.getCounselEmail());
		 * hd.setCounselEmail1(criminalTaskDto.getCounselEmail1());
		 * hd.setCounselMobileNo(criminalTaskDto.getCounselMobileNo());
		 * hd.setCounselMobileNo1(criminalTaskDto.getCounselMobileNo1());
		 * hd.setCounselName(criminalTaskDto.getCounselName());
		 * hd.setCounselName1(criminalTaskDto.getCounselName1());
		 */
		hd.setBriefHD(criminalTaskDto.getBriefHD());
		hd.setAssignedTask(assignedTaskPuh);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		hd.setUser(userdet);
		hd.setCreatedDate(new Date());

		// for court details

		hd.setCourtType(courtTypeRepo.findById((long) 0).get());
		hd.setState(stateRepo.findById((long) 0).get());
		hd.setCity(districtRepo.findById((long) 0).get());

		// Long id = hearingdtlRepo.findMaxid() + 1;

		Status status = criminalTaskDto.getStatus();
		Long statusID = status.getId();
		// For additional Doc Start
		MultipartFile additionalDoc;
		String orignalfilenameAdditionalDoc;
		String fileExtorderadditionalDoc;
		String additionalDocFileName;
		// For additional Doc Start End
		CriminalTaskValidation crimnalValidation = new CriminalTaskValidation();
		crimnalValidation.hearingDescription(criminalTaskDto, bindResult);

		if (criminalTaskDto.getStatus().getId() == 0) {

			crimnalValidation.HearingDetailsValidation(criminalTaskDto, bindResult);
			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}

		}

		if (statusID == 8) {

			crimnalValidation.HearingDetailsTransferValidation(criminalTaskDto, bindResult);
			if (criminalTaskDto.getCourtType().getId() == 0l) {
				bindResult.rejectValue("courtType", "errmsg.required");
			}
			if (criminalTaskDto.getState().getId() == 0L) {
				bindResult.rejectValue("state", "errmsg.required");
			}
			if (criminalTaskDto.getCity().getId() == 0L) {
				bindResult.rejectValue("city", "errmsg.required");
			}

			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}

			hd.setCourtType(criminalTaskDto.getCourtType());
			hd.setState(criminalTaskDto.getState());
			hd.setCity(criminalTaskDto.getCity());
			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}

			hd.setDateOfOrder(criminalTaskDto.getTransferDateOfOrder());
			hd.setDurationofStayTransfer(criminalTaskDto.getDetailsOfOfficeToWhichCaseisTransfered());
			hd.setDateOfTransferWithdrawClosing(criminalTaskDto.getTransferDateOfTransfer());

			MultipartFile ordercopyoftransfer = criminalTaskDto.getOrderCopyOfTransfer();

			String orignalfilenameOrderCopyOfTransfer = criminalTaskDto.getOrderCopyOfTransfer().getOriginalFilename();

			String fileExtordercopyoftransfer = orignalfilenameOrderCopyOfTransfer
					.substring(orignalfilenameOrderCopyOfTransfer.lastIndexOf("."));

			String filenameorderCopyOfTransfer = "OrderCopyofTransfer" + id1 + fileExtordercopyoftransfer;

			hd.setOrderCopyFN(filenameorderCopyOfTransfer);

			caseFileUpload(ordercopyoftransfer, filenameorderCopyOfTransfer);

			if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
				additionalDoc = criminalTaskDto.getAdditionalDoc();

				orignalfilenameAdditionalDoc = criminalTaskDto.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}
			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}

			HearingDetails = hearingdtlRepo.save(hd);
		} else if (statusID == 9) {

			crimnalValidation.HearingDetailsWithdrawValidation(criminalTaskDto, bindResult);
			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}

			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}

			hd.setDateOfOrder(criminalTaskDto.getWithdrawDateOfOrder());

			hd.setDateOfTransferWithdrawClosing(criminalTaskDto.getWithdrawDateOfWithdraw());
			MultipartFile orderCopyForWithdraw = criminalTaskDto.getOrderCopyForWithdraw();

			String orignalfilenameOrderCopyForWithdraw = criminalTaskDto.getOrderCopyForWithdraw()
					.getOriginalFilename();

			String fileExtOrderCopyForWithdraw = orignalfilenameOrderCopyForWithdraw
					.substring(orignalfilenameOrderCopyForWithdraw.lastIndexOf("."));

			String filenameorderCopyForWithdraw = "orderCopyForWithdraw" + id1 + fileExtOrderCopyForWithdraw;

			hd.setOrderCopyFN(filenameorderCopyForWithdraw);

			caseFileUpload(orderCopyForWithdraw, filenameorderCopyForWithdraw);

			if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
				additionalDoc = criminalTaskDto.getAdditionalDoc();

				orignalfilenameAdditionalDoc = criminalTaskDto.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}
			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}
			HearingDetails = hearingdtlRepo.save(hd);
		} else if (statusID == 10) {

			crimnalValidation.HearingDetailsStayValidation(criminalTaskDto, bindResult);
			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}
			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}

			hd.setDateOfOrder(criminalTaskDto.getStayDateOfOrder());
			hd.setDurationofStayTransfer(criminalTaskDto.getDurattionOfStay());
			hd.setReasonofStay(criminalTaskDto.getReasonnOfStay());
			hd.setRemarks(criminalTaskDto.getStayRemark());

			MultipartFile stayOrderCopy = criminalTaskDto.getStayOrderCopy();

			String orignalfilenamestayOrderCopy = criminalTaskDto.getStayOrderCopy().getOriginalFilename();

			String fileExtstayOrderCopy = orignalfilenamestayOrderCopy
					.substring(orignalfilenamestayOrderCopy.lastIndexOf("."));

			String filenamestayOrderCopy = "stayOrderCopy" + id1 + fileExtstayOrderCopy;

			hd.setOrderCopyFN(filenamestayOrderCopy);

			caseFileUpload(stayOrderCopy, filenamestayOrderCopy);
			if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
				additionalDoc = criminalTaskDto.getAdditionalDoc();

				orignalfilenameAdditionalDoc = criminalTaskDto.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}

			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}
			HearingDetails = hearingdtlRepo.save(hd);
		}

		else if (statusID == 12) {

			crimnalValidation.HearingDetailsWindUpValidation(criminalTaskDto, bindResult);
			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}

			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}
			hd.setDateOfTransferWithdrawClosing(criminalTaskDto.getWindingUpClosingDate());

			MultipartFile detailsOfOrderForWindingUp = criminalTaskDto.getDetailsOfOrderForWindingUp();

			String orignalfilenameDetailsOfOrderForWindingUp = criminalTaskDto.getDetailsOfOrderForWindingUp()
					.getOriginalFilename();

			String fileExtDetailsOfOrderForWindingUp = orignalfilenameDetailsOfOrderForWindingUp
					.substring(orignalfilenameDetailsOfOrderForWindingUp.lastIndexOf("."));

			String filenameOfOrderForWindingUp = "OfOrderForWindingUp" + id1 + fileExtDetailsOfOrderForWindingUp;

			hd.setOrderCopyFN(filenameOfOrderForWindingUp);

			caseFileUpload(detailsOfOrderForWindingUp, filenameOfOrderForWindingUp);
			if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
				additionalDoc = criminalTaskDto.getAdditionalDoc();

				orignalfilenameAdditionalDoc = criminalTaskDto.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}

			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}
			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}
			HearingDetails = hearingdtlRepo.save(hd);
		} else if (statusID == 04) {

			crimnalValidation.HearingDetailsDisposedOffValidation(criminalTaskDto, bindResult);

			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}

			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}
			hd.setDateOfOrder(criminalTaskDto.getDateOfDisposed());

			MultipartFile OrderCopyOfDisposedOff = criminalTaskDto.getOrderCopyOfDisposedOff();

			String orignalfilenameOrderCopyOfDisposedOff = criminalTaskDto.getOrderCopyOfDisposedOff()
					.getOriginalFilename();

			String fileExtOrderCopyOfDisposedOff = orignalfilenameOrderCopyOfDisposedOff
					.substring(orignalfilenameOrderCopyOfDisposedOff.lastIndexOf("."));

			String filenameCopyOfDisposedOff = "CopyOfDisposedOff" + id1 + fileExtOrderCopyOfDisposedOff;

			hd.setOrderCopyFN(filenameCopyOfDisposedOff);

			caseFileUpload(OrderCopyOfDisposedOff, filenameCopyOfDisposedOff);
			if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
				additionalDoc = criminalTaskDto.getAdditionalDoc();

				orignalfilenameAdditionalDoc = criminalTaskDto.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}

			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}
			HearingDetails = hearingdtlRepo.save(hd);
		}

		else {
			crimnalValidation.HearingDetailsother(criminalTaskDto, bindResult);
			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

				return "Task/CriminalTaskPage";
			}

			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}
			if (!criminalTaskDto.getAdditionalDoc().isEmpty()) {
				additionalDoc = criminalTaskDto.getAdditionalDoc();

				orignalfilenameAdditionalDoc = criminalTaskDto.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}
			
			
			
			
			if (hearingID == null) {
				hearingdtlRepo.unsetLatestHearing(procasedetails.getId());
		}

			HearingDetails = hearingdtlRepo.save(hd);
		}

		CriminalTaskDto criminalTaskDto2 = new CriminalTaskDto();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto2);

		redirect.addAttribute("tabId", tabId);
		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		List<AccusedStatus> accusedLst = AccusedStatusRepo.findByAssignedTaskAndApproveStatusAndStatus(assignedTaskPuh,
				0, true, Sort.by(Sort.Direction.ASC, "id"));
//Sort.by(Sort.Direction.ASC, "id"));
		if (!accusedLst.isEmpty()) {
			for (AccusedStatus accusedStatus : accusedLst) {

				accusedStatus.setHearingDetails(HearingDetails.getId());
				accusedStatus.setApproveStatus(1);
				AccusedStatusRepo.save(accusedStatus);

			}
		}
		if (hearingID == null) {
			redirect.addFlashAttribute("message", "Save Hearing Details Successfully  :");
			/* anjali */
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.hearingdetailsave") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		} else {
			redirect.addFlashAttribute("message", " Hearing Details Updated Successfully  : ");

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.hearingdetailupdate") + " " + " and Investigation Number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

		}

		return "redirect:/proceedTask2";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "editHearingDetails")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String editHearingDetails(@RequestParam(value = "editHearingDetails", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) {

		int tabId = 266;
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		criminalTaskDto.setHearingEditId(id);

		criminalTaskDto.setHearingEditId(hearingDtl.getId());
		criminalTaskDto.setRejectRemarkforHearing("");

		criminalTaskDto.setOfficerH(hearingDtl.getOfficer());
		criminalTaskDto.setCounselNameH(hearingDtl.getCounselName());

		criminalTaskDto.setCourtType(hearingDtl.getCourtType());
		criminalTaskDto.setState(hearingDtl.getState());
		criminalTaskDto.setCity(hearingDtl.getCity());
		criminalTaskDto.setCaseStatusDate(hearingDtl.getDateofCaseStatusUpdate());
		criminalTaskDto.setStatus(hearingDtl.getStatus());
		criminalTaskDto.setLastHearingDate(hearingDtl.getLastHearingDate());
		criminalTaskDto.setNextHearingDate(hearingDtl.getNextHearingDate());
		criminalTaskDto.setBriefHD(hearingDtl.getBriefHD());
		criminalTaskDto.setEditHearing(1);

		Long statusID = hearingDtl.getStatus().getId();
		if (statusID == 8) {

			criminalTaskDto.setTransferDateOfOrder(hearingDtl.getDateOfOrder());
			criminalTaskDto.setDetailsOfOfficeToWhichCaseisTransfered(hearingDtl.getDurationofStayTransfer());
			criminalTaskDto.setTransferDateOfTransfer(hearingDtl.getDateOfTransferWithdrawClosing());

		}
		if (statusID == 9) {

			criminalTaskDto.setWithdrawDateOfWithdraw(hearingDtl.getDateOfTransferWithdrawClosing());

			criminalTaskDto.setWithdrawDateOfOrder(hearingDtl.getDateOfOrder());

		}
		if (statusID == 10) {

			criminalTaskDto.setStayDateOfOrder(hearingDtl.getDateOfOrder());
			criminalTaskDto.setDurattionOfStay(hearingDtl.getDurationofStayTransfer());
			criminalTaskDto.setReasonnOfStay(hearingDtl.getReasonofStay());
			criminalTaskDto.setStayRemark(hearingDtl.getRemarks());

		}

		if (statusID == 12) {

			criminalTaskDto.setWindingUpClosingDate(hearingDtl.getDateOfTransferWithdrawClosing());

		}
		if (statusID == 04) {

			criminalTaskDto.setDateOfDisposed(hearingDtl.getDateOfOrder());

		}

		criminalTaskDto.setDateofCaseStatusUpdate(hearingDtl.getDateofCaseStatusUpdate());

		// modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardtoapprovalHearingDetails")
	// @RequestMapping(value ="/editPofficer", params = "editPairaviOfficer")
	public String forwardtoapprovalHearingDetails(

			@RequestParam(value = "forwardtoapprovalHearingDetails", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) throws Exception {
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();
		hearingDtl.setApproveStatus(2);
		hearingdtlRepo.save(hearingDtl);
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		model.addAttribute("message", "Hearing Details Confirmed Successfully  :");
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.hearingddetailforward") + " " + assignedTaskPuh.getUser().getSalutation()
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

		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "complainDtlSave1")
	public String ComplainDtlSave1(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 21;
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAddCase(assignedTaskPuh.getAddCase());
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		Complaintdetl complaintDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(proCourtCaseDtl,
				assignedTaskPuh);

		complaintDtl.setApprove_status(2);
		complaintdetlRepo.save(complaintDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.complDtlsForward") + " " + assignedTaskPuh.getUser().getSalutation() + " "
						+ assignedTaskPuh.getUser().getFirstName() + " "
						+ (assignedTaskPuh.getUser().getMiddleName().equals("") ? ""
								: assignedTaskPuh.getUser().getMiddleName() + "")
						+ assignedTaskPuh.getUser().getLastName() + " " + " and investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		modelMap.addAttribute("message", "Complainant Details Confirmed Successfully  :");

		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "addCriminalDtl", params = "forwardToApprovalCompany")
	public String forwardToApprovalCompany(@RequestParam(value = "forwardToApprovalCompany", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) {

		CaseCompany caseCompany = caseCompanyRepo.findById(id).get();
		caseCompany.setApproveStatus(1);
		caseCompanyRepo.save(caseCompany);

		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardtoapprovalPairaviOfficer")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String forwardtoapprovalPairaviOfficer(
			@RequestParam(value = "forwardtoapprovalPairaviOfficer", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) throws Exception {
		PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid"));
		int tabId = 23;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		pofficeredit.setApproveStatus(2);
		pairaviDetailRepo.save(pofficeredit);
		model.addAttribute("message", " pairavi officer details has confirmed. ");
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.addcasesPairaviforwarded") + " " + assignedTaskPuh.getUser().getSalutation()
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

		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "financialYear")
	public String SaveFinancialYear(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.financeyear(criminalTaskDto, bindResult);

		if (bindResult.hasErrors()) {

			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/CriminalTaskPage";
		}

		else {

			ProCourtCaseDetails courtdtl = proCourtCaseDetailsRepo
					.findALLById(criminalTaskDto.getProCourtDtl().getId());

			courtdtl.setFinancialYear(criminalTaskDto.getInstructionFy());
			proCourtCaseDetailsRepo.save(courtdtl);
			modelMap.addAttribute("courtdtl", courtdtl.getId());
			modelMap.addAttribute("desilst", designationRepo.findAll());
			modelMap.addAttribute("ptypelst", pairaviTypeRepo.findAll());

			CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
			return "Task/CriminalTaskPage";
		}
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardCaseProseingDates")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String forwardCaseProseingDates(@RequestParam(value = "forwardCaseProseingDates", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(id).get();
		caseproseingDate.setApproveStatus(2);
		caseProcessingRepo.save(caseproseingDate);

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.caseProcessingDateF") + " " + assignedTaskPuh.getUser().getSalutation()
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
		model.addAttribute("message", "Case Processing Date Confirmed Successfully  :");
		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "editCaseProseingDates")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String editCaseProseingDates(@RequestParam(value = "editCaseProseingDates", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(id).get();
		criminalTaskDto.setInvOrder(caseproseingDate.getOrderOfInv());
		criminalTaskDto.setSuppInv(caseproseingDate.getOrderOfSupplyInv());
		criminalTaskDto.setInvReport(caseproseingDate.getSubmissionOfInvReport());
		criminalTaskDto.setSuppInvReport(caseproseingDate.getSubmissionOfSupplyInvReport());
		criminalTaskDto.setDateFilling(caseproseingDate.getInsuranceOFInvByMCA());
		criminalTaskDto.setCaseproseingID(caseproseingDate.getId());
		criminalTaskDto.setForeEditStatus(2);

		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "caseProcessingDate")
	public String caseProcessingDate(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {

		int tabId = 23;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		InvCaseDetails invcaseDtl = InvCaseDtlRepo.findAllById(proCourtCaseDtl.getInvCaseDetail().getId());

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.caseProcessingAllDate(criminalTaskDto, bindResult);

		if (bindResult.hasErrors()) {

			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			return "Task/CriminalTaskPage";

		} else {
			CaseProcessingDates caseProcessDate = new CaseProcessingDates();
			redirect.addFlashAttribute("message", " Case Processing Dates Added Successfully  : ");

			if (criminalTaskDto.getCaseproseingID() != null) {
				redirect.addFlashAttribute("message", " Case Processing Dates Updated Successfully  : ");
				caseProcessDate.setId(criminalTaskDto.getCaseproseingID());

				UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Update"),
						utils.getMessage("log.login.Caseprocessingupdate") + " " + " and Investigation Number is "
								+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();
			} else {
				UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Save"),
						utils.getMessage("log.login.Caseprocessingsaved") + " " + " and Investigation number is "
								+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();
			}
			caseProcessDate.setOrderOfInv(criminalTaskDto.getInvOrder());
			caseProcessDate.setOrderOfSupplyInv(criminalTaskDto.getSuppInv());
			caseProcessDate.setSubmissionOfInvReport(criminalTaskDto.getInvReport());
			caseProcessDate.setSubmissionOfSupplyInvReport(criminalTaskDto.getSuppInvReport());
			caseProcessDate.setInsuranceOFInvByMCA(criminalTaskDto.getDateFilling());
			caseProcessDate.setProcourtdtl(proCourtCaseDtl);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			caseProcessDate.setCreatedBy(userdet);
			caseProcessDate.setCreatedDate(new Date());
			caseProcessDate.setAssignedTask(assignedTaskPuh);
			caseProcessDate.setApproveStatus(0);
			/*
			 * if (assignedTaskPuh.getForwardedStatus() == 0) {
			 * caseProcessDate.setApproveStatus(5); }
			 */

			caseProcessingRepo.save(caseProcessDate);

		}
		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

		modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		return "redirect:/proceedTask2";
		// return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "updateCourtCaseDetails")
	public String saveCourtCaseDetails(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		ProCourtCaseDetails proCourtCaseDetails =assignedTaskPuh.getProCourtCaseDetails();
		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findById(assignedTaskPuhRepo.findById(criminalTaskDto.getAssignedTask().getId()).get().getProCourtCase().getId()).get();
		String courtCaseNo = criminalTaskDto.getCourtCaseName();

		CriminalTaskValidation criminalTaskVelidation = new CriminalTaskValidation();
		criminalTaskVelidation.courtCaseNoValidation(criminalTaskDto, bindResult);

		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/CriminalTaskPage";

		}

		Long id = proCourtCaseDetails.getId();

		proCourtCaseDetails.setCourtCaseNo(criminalTaskDto.getCourtCaseName());
		proCourtCaseDetails.setApproveStatus(3);
		// String courtCaseName = criminalTaskDto.getCourtCaseName();
		MultipartFile courtCaseDtlFile = criminalTaskDto.getCourtCaseDtlFile();

		String orignalfilenameOrderCopyOfTransfer = courtCaseDtlFile.getOriginalFilename();

		String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");
		String filename = id + "_" + result;
		// String filename=proCourtCaseDetails.getCourtCaseNo()+".pdf";
		caseFileUpload(courtCaseDtlFile, filename);
		proCourtCaseDetails.setCourtCaseDtlFile(filename);
		// proCourtCaseDetails.setCourtCaseName(courtCaseName);
		proCourtCaseDetailsRepo.save(proCourtCaseDetails);
		modelMap.addAttribute("message", " Courtcase detail saved Successfully  : ");

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Save"),
				utils.getMessage("log.login.updatecourtcasenumbersave") + " " + " and Investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "updateCourtCaseDetails1")
	public String updateCourtCaseDetails1(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		ProCourtCaseDetails proCourtCaseDetails = assignedTaskPuh.getProCourtCaseDetails();
		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findById(assignedTaskPuhRepo.findById(criminalTaskDto.getAssignedTask().getId()).get().getProCourtCase().getId()).get();
		String courtCaseNo = criminalTaskDto.getCourtCaseName();

		CriminalTaskValidation criminalTaskVelidation = new CriminalTaskValidation();
		criminalTaskVelidation.courtCaseNoValidation(criminalTaskDto, bindResult);
		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/CriminalTaskPage";

		}

		Long id = proCourtCaseDetails.getId();

		proCourtCaseDetails.setCourtCaseNo(criminalTaskDto.getCourtCaseName());
		proCourtCaseDetails.setApproveStatus(3);
		// String courtCaseName = criminalTaskDto.getCourtCaseName();
		MultipartFile courtCaseDtlFile = criminalTaskDto.getCourtCaseDtlFile();

		String orignalfilenameOrderCopyOfTransfer = courtCaseDtlFile.getOriginalFilename();

		String result = orignalfilenameOrderCopyOfTransfer.replaceAll("\\s", "_");
		String filename = id + "_" + result;
		// String filename=proCourtCaseDetails.getCourtCaseNo()+".pdf";
		caseFileUpload(courtCaseDtlFile, filename);
		proCourtCaseDetails.setCourtCaseDtlFile(filename);
		// proCourtCaseDetails.setCourtCaseName(courtCaseName);
		proCourtCaseDetailsRepo.save(proCourtCaseDetails);
		modelMap.addAttribute("message", " Courtcase detail Updated Successfully  : ");
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Update"),
				utils.getMessage("log.login.updatecourtcasenumberupdate") + " " + " and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		return "Task/CriminalTaskPage";
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/SaveAccusedDetails1")
	public String SaveAccusedDetails1(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		ProMISValidator promisValid = new ProMISValidator();
		promisValid.isvalidPersonName("accusedName", criminalTaskDto.getAccusedName(), bindResult, "errmsg.name", true);
		promisValid.isValidpanNumber("accPan", criminalTaskDto.getAccPan(), bindResult, "errmsg.pan", true);

		if (criminalTaskDto.getAccDesc().split(" ").length > 100) {

			bindResult.rejectValue("accDesc", "msg.brief1");

		}
		if (criminalTaskDto.getAccDesination() == null) {

			bindResult.rejectValue("accDesination", "errmsg.required");

		}

		else {

			if (criminalTaskDto.getAccDesination().equals("Choose")) {
				bindResult.rejectValue("accDesination", "errmsg.required");
			}
		}

		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/CriminalTaskPage";
		}
		AddAccused accusedDetails = addAccusedRepo.findById(criminalTaskDto.getAccusedIdEdit()).get();
		AccusedMaster accusemaster = accusedMasterRepo.findAllByPanNumber(criminalTaskDto.getAccPan());
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		if (accusemaster != null) {
			// AddAccused accuseddetails1 =
			// addAccusedRepo.findAllByAccusedMasterAndProcourtdtl(accusemaster,proCourtCaseDtl);

			// String p = accusedDetails.getAccusedMaster().getPanNumber();
			String p2 = criminalTaskDto.getAccPan();

		}

		CaseCompany caseCompanyID = criminalTaskDto.getCaseCompanyForaccusedStatus();

		if (caseCompanyID == null) {

			try {
				AddCompany companylst = addCompanyRepo.findById((long) 0).get();
				caseCompanyID = caseCompanyRepo.findAllByCompanyAndAssignedTask(companylst, assignedTaskPuh);

			} catch (Exception e) {
				AddCompany companylst = addCompanyRepo.findById((long) 0).get();
				int approveStatus1 = 0;
				if (assignedTaskPuh.getForwardedStatus() == 0) {
					approveStatus1 = 5;
				}

				// caseCompanyID = caseCompanyRepo.save(caseCompany);

			}

		}

		// accusedDetails.setAccusedMaster(accusedMaster);
		accusedDetails.setCompany(caseCompanyID);

		accusedDetails.setCreatedDate(new Date());
		accusedDetails.setRejectRemark("");
		accusedDetails.setApproveStatus(0);

		if (assignedTaskPuh.getForwardedStatus() == 0) {
			accusedDetails.setApproveStatus(5);
		}

		addAccusedRepo.save(accusedDetails);
		modelMap.addAttribute("message", "Accsued have been updated successfully.");

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, new CriminalTaskDto());
		return "Task/CriminalTaskPage";
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/SaveAccusedDetails")
	public String SaveAccusedDetails(ModelMap modelMap,
			@ModelAttribute("criminalTaskDto") @Valid CriminalTaskDto criminalTaskDto, BindingResult bindResult,
			RedirectAttributes redirect) throws Exception {
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		int approveStatus1 = 0;
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		AddCompany companylst = addCompanyRepo.findById((long) 0).get();
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		CaseCompany caseCompany = new CaseCompany(companylst, proCourtCaseDtl, criminalTaskDto.getCompId(), userdet,
				new Date(), assignedTaskPuh, approveStatus1);

		if (criminalTaskDto.getAccusedType().equalsIgnoreCase("Individual")
				&& criminalTaskDto.getAccDesination2().getId() == 0) {
			bindResult.rejectValue("accDesination2", "errmsg.required");
		}
		if (criminalTaskDto.getAccusedType().equals("Choose")) {
			bindResult.rejectValue("accusedType", "errmsg.required");
		}
		if (criminalTaskDto.getAccusedName().isEmpty()) {
			bindResult.rejectValue("accusedName", "errmsg.required");
		}
		if (criminalTaskDto.isLocText()==true) {
			if(criminalTaskDto.getDateofOpen()==null) {
				bindResult.rejectValue("dateofOpen", "errmsg.required");
			}
			/*
			 * if(criminalTaskDto.getDateofDeletion()==null) {
			 * bindResult.rejectValue("dateofDeletion", "errmsg.required"); }
			 */
			
		}
		if (criminalTaskDto.isArrestDuringInvestigation()==true) {
			if(criminalTaskDto.getDateofArrest()==null) {
				bindResult.rejectValue("dateofArrest", "errmsg.required");
			}
			/*
			 * if(criminalTaskDto.getDateofGrantClosing()==null) {
			 * bindResult.rejectValue("dateofGrantClosing", "errmsg.required"); }
			 */
			
		}
		if (bindResult.hasErrors()) {
			modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/CriminalTaskPage";
		}

		CaseCompany SavedCaseComp = caseCompanyRepo.save(caseCompany);

		// List<AccusedActAndSection> actSections = new ArrayList<>();
		Set<AccusedActAndSection> actSections = new HashSet<>();

		AddAccused addAccuse = new AddAccused();
		if (criminalTaskDto.getAccusedIdEdit() != null) {
			Set<AccusedActAndSection> list = addAccusedRepo.findById(criminalTaskDto.getAccusedIdEdit()).get()
					.getActSection();
			accusedActSectionRepo.deleteInBatch(list);
			addAccuse.setId(criminalTaskDto.getAccusedIdEdit());

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.accuseddetailupdate") + " " + " and Investigation Number is "
							+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			redirect.addFlashAttribute("message", "Accused Details Updated Successfully  :");
		}

		else {
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.accuseddetailsaved") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			redirect.addFlashAttribute("message", "Accused Details Saved Successfully  :");
		}

		addAccuse.setCompany(SavedCaseComp);
		addAccuse.setAssignedTask(assignedTaskPuh);
		addAccuse.setCompany(caseCompanyRepo.findById((long) 1).get());

		addAccuse.setAccusedName(criminalTaskDto.getAccusedName());
		addAccuse.setPanNumber(criminalTaskDto.getAccPan());
		addAccuse.setAddress(criminalTaskDto.getAddAccusedAddress());
		addAccuse.setAccusedType(criminalTaskDto.getAccusedType());
		addAccuse.setIndividualRelateTo(criminalTaskDto.getIndividualRelateTo());
		addAccuse.setCinNumber(criminalTaskDto.getCinRespondent());
		addAccuse.setSsId(criminalTaskDto.getGDFSBCId());
		addAccuse.setCreatedDate(new Date());

		String desss = criminalTaskDto.getAccusedDescription();
		String[] arrayDiscription = desss.split("~");

		int lenth = criminalTaskDto.getAccusedClause().length;
		for (int i = 1; i <= lenth; i++) {
			AccusedActAndSection actandSection = new AccusedActAndSection();
			Long act = criminalTaskDto.getAccusedACT()[i - 1];
			Long section = criminalTaskDto.getAccusedSection()[i - 1];
			String compatability = criminalTaskDto.getAccusedCompoundability()[i - 1];
			String description = arrayDiscription[i - 1];
			String clouse = criminalTaskDto.getAccusedClause()[i - 1];
			Long addsubsec = criminalTaskDto.getAccusedSubSection()[i - 1];
			Integer punishmentID = criminalTaskDto.getAaccusedPunishment()[i - 1];
			Punishment1 punishment = punishmentRepo.findById(punishmentID).get();
			actandSection.setPunishment(punishment);
			actandSection.setDescription(description);
			actandSection.setCompatability(compatability);
			actandSection.setClause(clouse);
			AddSubSec subSection = addsubsecRepo.findById(addsubsec).get();
			actandSection.setSubSection(subSection);
			actandSection.setAct(addActRepo.findById(act).get());
			actandSection.setSection(addactsecRepo.findById(section).get());
			actandSection.setAddAccused(addAccuse);
			actSections.add(actandSection);

		}

		addAccuse.setActSection(actSections);
		addAccuse.setCreatedBy(userdet);
		addAccuse.setDesignation(criminalTaskDto.getAccDesination2());
		if (criminalTaskDto.isLocText()==true) {
			addAccuse.setLocText(criminalTaskDto.isLocText());
			addAccuse.setDateofOpen(criminalTaskDto.getDateofOpen());
			addAccuse.setDateofDeletion(criminalTaskDto.getDateofDeletion());
		}else {
			addAccuse.setLocText(false);
			addAccuse.setDateofOpen(null);
			addAccuse.setDateofDeletion(null);
		}
		if (criminalTaskDto.isArrestDuringInvestigation()==true) {
			addAccuse.setArrestDuringInvestigation(criminalTaskDto.isArrestDuringInvestigation());
			addAccuse.setDateofArrest(criminalTaskDto.getDateofArrest());
			addAccuse.setDateofGrantClosing(criminalTaskDto.getDateofGrantClosing());
		}else {
			addAccuse.setArrestDuringInvestigation(false);
			addAccuse.setDateofArrest(null);
			addAccuse.setDateofGrantClosing(null);
		}
		
		addAccuse.setProcourtdtl(proCourtCaseDtl);

		addAccusedRepo.save(addAccuse);

		// CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();

		// modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		// return "Task/CriminalTaskPage";

		redirect.addAttribute("tabId", tabId);
		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		return "redirect:/proceedTask2";
	}

	@RequestMapping(value = "addCriminalDtl", params = "forwardToApprovalAccused")
	public String forwardToApprovalAccused(@RequestParam(value = "forwardToApprovalAccused", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		accusedDetails.setApproveStatus(2);
		addAccusedRepo.save(accusedDetails);

		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		model.addAttribute("message", "Accused Details Confirmed Successfully  :");

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.accuseddetailforward") + " " + assignedTaskPuh.getUser().getSalutation()
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

		modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";

	}

	@RequestMapping(value = "addCriminalDtl", params = "addAccusedResponse")
	public String addAccusedResponse(@RequestParam(value = "addAccusedResponse", required = true) Long id,
			ModelMap model, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto) {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();

		model.addAttribute("accusedDetails", accusedDetails);
		AssignedTaskPuhAfterCOurt assignTask = criminalTaskDto.getAssignedTask();
		ProCourtCaseDetails proCourtDtls = assignTask.getProCourtCaseDetails();
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("proCourtDtls", proCourtDtls);
		List<TypeofResponse> typeofResponse = typeofResponseRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("typeofResponse", typeofResponse);
		model.addAttribute("courtType", courtType);

		List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("statelist", statelist);
		model.addAttribute("districtlist", districtlist);
		AccusedResponse accusedResponse = new AccusedResponse();
		accusedResponse.setAccusedDetailsID(accusedDetails.getId());
		String typeOfCase = criminalTaskDto.getTypeOfCase();
		accusedResponse.setTypeOfCase(typeOfCase);

		accusedResponse.setAccusedDetails(accusedDetails);
		accusedResponse.setAssignedTask(assignTask);
		accusedResponse.setAddCase(assignTask.getProCourtCaseDetails().getAddCase());
		model.addAttribute("accusedResponse", accusedResponse);
		int tabId = 266;
		AssignedTaskPuh assignedTaskPuh = assignedTaskPuhRepo.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
				proCourtDtls.getAddCase(), accusedDetails, Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("accusedResponseList", accusedResponseList);

		return "Task/AccusedRespond";

	}

	@RequestMapping(value = "backToAccused")
	public String backToAccused1(ModelMap model, @ModelAttribute AccusedResponse accusedResponse,
			BindingResult errors) {

		int tabId = 266;
		modelAttributeObjectAfterCourt(accusedResponse.getAssignedTask(), model, tabId, new CriminalTaskDto());

		return "Task/CriminalTaskPage";

	}

	@PostMapping("/confirmDetails")
	public String approveAction(@RequestParam Long id, Model model) {
		try {
			AccusedResponse accusedDetails = accusedResponseRepo.findById(id).get();
			accusedDetails.setApprovalStatus(2);
			accusedResponseRepo.save(accusedDetails);

			List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(accusedDetails.getAddCase());
			model.addAttribute("lst", lst);
			// AddAccused accusedDetails =
			// addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();
			AddAccused accusedDetails2 = accusedDetails.getAccusedDetails();

			ProCourtCaseDetails proCourtDtls = proCourtCaseDetailsRepo.findByAddCase(accusedDetails.getAddCase());
			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("proCourtDtls", proCourtDtls);
			List<TypeofResponse> typeofResponse = typeofResponseRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("typeofResponse", typeofResponse);
			model.addAttribute("courtType", courtType);

			AccusedResponse accusedResponse = new AccusedResponse();

			accusedResponse.setAccusedDetailsID(accusedDetails2.getId());
			String typeOfCase = accusedDetails.getAddCase().getTypeOfCaseT();
			accusedResponse.setTypeOfCase(typeOfCase);

			accusedResponse.setAccusedDetails(accusedDetails2);
			accusedResponse.setAssignedTask(accusedDetails.getAssignedTask());
			accusedResponse.setAddCase(accusedDetails.getAddCase());
			model.addAttribute("accusedResponse", accusedResponse);

			model.addAttribute("message", "Accused response Confirmed successfully");

			List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("statelist", statelist);
			model.addAttribute("districtlist", districtlist);
			List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
					proCourtDtls.getAddCase(), accusedDetails2, Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("accusedResponseList", accusedResponseList);

			int tabId = 266;
			// modelAttributeObject(assignTask, model, tabId, new CriminalTaskDto());

			return "Task/AccusedRespond";

		} catch (Exception e) {
			return "redirect:/accusedResponses";
		}
	}

	@PostMapping("/editDetailsCriminal")
	public String editDetails(@RequestParam Long id, Model model) {
		try {
			AccusedResponse accusedDetails = accusedResponseRepo.findById(id).get();
			Long id2 = accusedDetails.getAssignedTask().getId();

			AssignedTaskPuhAfterCOurt assignedTask = assignedTaskPuhAfterCOurtRepository.findById(id2).get();

			accusedDetails.setApprovalStatus(2);
			accusedResponseRepo.save(accusedDetails);
			List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(accusedDetails.getAddCase());
			model.addAttribute("lst", lst);
			AddAccused accusedDetails2 = accusedDetails.getAccusedDetails();
			ProCourtCaseDetails proCourtDtls = proCourtCaseDetailsRepo.findByAddCase(accusedDetails.getAddCase());
			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("proCourtDtls", proCourtDtls);
			List<TypeofResponse> typeofResponse = typeofResponseRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("typeofResponse", typeofResponse);
			model.addAttribute("courtType", courtType);
			accusedDetails.setAccusedDetailsID(accusedDetails2.getId());
			String typeOfCase = accusedDetails.getAddCase().getTypeOfCaseT();
			accusedDetails.setTypeOfCase(typeOfCase);
			accusedDetails.setAccusedDetails(accusedDetails2);
			accusedDetails.setAddCase(accusedDetails.getAddCase());
			model.addAttribute("accusedResponse", accusedDetails);
			List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("statelist", statelist);
			model.addAttribute("districtlist", districtlist);
			List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
					proCourtDtls.getAddCase(), accusedDetails2, Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("accusedResponseList", accusedResponseList);
			int tabId = 266;
			// modelAttributeObject(assignTask, model, tabId, new CriminalTaskDto());

			return "Task/AccusedRespond";

		} catch (Exception e) {
			return "redirect:/accusedResponses";
		}
	}

	@RequestMapping(value = "saveAccusedResponse")
	public String saveAccusedResponse(ModelMap model, @ModelAttribute AccusedResponse accusedResponse,
			BindingResult errors) throws Exception {
		AddCase addcase = accusedResponse.getAddCase();
		AccusedCompValidation accusedValidation = new AccusedCompValidation();
		accusedValidation.accusedResponse(accusedResponse, errors);
		AssignedTaskPuhAfterCOurt assignTask = accusedResponse.getAssignedTask();
		ProCourtCaseDetails proCourtDtls = assignTask.getProCourtCaseDetails();
		if (errors.hasErrors()) {

			List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(addcase);
			model.addAttribute("lst", lst);
			AddAccused accusedDetails = addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();
			// AddAccused accusedDetails = accusedResponse.getAccusedDetails();
			// model.addAttribute("accusedDetails", accusedDetails);

			List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("proCourtDtls", proCourtDtls);
			List<TypeofResponse> typeofResponse = typeofResponseRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("typeofResponse", typeofResponse);
			model.addAttribute("courtType", courtType);
			model.addAttribute("accusedResponse", accusedResponse);
			List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("statelist", statelist);
			model.addAttribute("districtlist", districtlist);
			List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
					proCourtDtls.getAddCase(), accusedDetails, Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("accusedResponseList", accusedResponseList);

			int tabId = 266;
			// modelAttributeObject(assignTask, model, tabId, new CriminalTaskDto());

			return "Task/AccusedRespond";
		}
		// =======================

		Long id;
		if (accusedResponse.getId() != null) {
			id = accusedResponse.getId();
		} else {
			id = (accusedResponseRepo.findMaxid() != null) ? (accusedResponseRepo.findMaxid() + 1) : 1;
		}
		if (!accusedResponse.getApplicationOrderFile().isEmpty()) {
			MultipartFile file = accusedResponse.getApplicationOrderFile();
			String orignalfilename = file.getOriginalFilename();
			String result = orignalfilename.replaceAll("\\s", "_");
			String filename = id + "_ApplicationOrderFile_" + result;
			caseFileUpload(file, filename);
			accusedResponse.setApplicationOrderFileName(filename);
		}
		// =======================
		if (!accusedResponse.getOrderFile().isEmpty()) {
			MultipartFile fileOrderFile = accusedResponse.getOrderFile();
			String orignalfilenamefileOrderFile = fileOrderFile.getOriginalFilename();
			String resultOrderFile = orignalfilenamefileOrderFile.replaceAll("\\s", "_");
			String OrderFilename = id + "_OrderFile_" + resultOrderFile;
			caseFileUpload(fileOrderFile, OrderFilename);
			accusedResponse.setOrderFileName(OrderFilename);
		}

		// =======================
		if (!accusedResponse.getReplyFiledOrder().isEmpty()) {
			MultipartFile replyFiledOrder = accusedResponse.getReplyFiledOrder();
			String orignalfilereplyFiledOrder = replyFiledOrder.getOriginalFilename();
			String resultReplyFiledOrder = orignalfilereplyFiledOrder.replaceAll("\\s", "_");
			String resultReplyFiledOrderName = id + "_ReplyFiledOrder_" + resultReplyFiledOrder;
			caseFileUpload(replyFiledOrder, resultReplyFiledOrderName);
			accusedResponse.setReplyOrderFiledName(resultReplyFiledOrderName);
		}

		// =======================
		AddAccused accusedDetails = addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();

		accusedResponse.setAccusedDetails(accusedDetails);
		UserDetails user = userDetailsService.getUserDetailssss();
		accusedResponse.setCreatedBy(user);
		accusedResponse.setUpdatedBy(user);
		accusedResponse.setApprovedBy(user);
		accusedResponse.setCreatedDate(new Date());

		accusedResponseRepo.save(accusedResponse);
		model.addAttribute("message", "Accused response added successfully");
		List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(addcase);
		model.addAttribute("lst", lst);
		// AddAccused accusedDetails = accusedResponse.getAccusedDetails();
		model.addAttribute("accusedDetails", accusedDetails);

		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("proCourtDtls", proCourtDtls);
		List<TypeofResponse> typeofResponse = typeofResponseRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("typeofResponse", typeofResponse);
		model.addAttribute("courtType", courtType);
		AccusedResponse accusedResponse11 = new AccusedResponse();
		accusedResponse11.setAccusedDetailsID(accusedResponse.getAccusedDetailsID());
		accusedResponse11.setAccusedDetails(accusedDetails);
		accusedResponse11.setAssignedTask(assignTask);
		accusedResponse11.setAddCase(proCourtDtls.getAddCase());
		accusedResponse11.setTypeOfCase(accusedResponse.getTypeOfCase());

		model.addAttribute("accusedResponse", accusedResponse11);
		List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("statelist", statelist);
		model.addAttribute("districtlist", districtlist);

		List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
				proCourtDtls.getAddCase(), accusedDetails, Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("accusedResponseList", accusedResponseList);

		int tabId = 266;
		// modelAttributeObject(assignTask, model, tabId, new CriminalTaskDto());

		return "Task/AccusedRespond";

	}

	@RequestMapping(value = "/proceedTask2")
	public String approveTask2(ModelMap modelMap,
			@RequestParam(value = "assignedTaskPuh") AssignedTaskPuhAfterCOurt assignedTaskPuh,
			@RequestParam(value = "tabId") int tabId) throws Exception {

		// int tabId = 23;
		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();

		modelAttributeObjectAfterCourt(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";
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
}