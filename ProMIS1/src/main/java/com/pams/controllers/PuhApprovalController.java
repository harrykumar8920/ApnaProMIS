package com.pams.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.AppUserDAO;
import com.pams.dao.PendingTaskForApprovalDAO;
import com.pams.dto.CinSearchDTO;
import com.pams.dto.CriminalTaskDto;
import com.pams.dto.PageNoDTO;
import com.pams.dto.PendingTaskForApprovalDTO;
import com.pams.dto.ReportByCINDTO;
import com.pams.entity.AccusedStatus;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.ComplaintReport;
import com.pams.entity.Complaintdetl;
import com.pams.entity.CouncilDetails;
import com.pams.entity.HearingDetails;
import com.pams.entity.Inspector;
import com.pams.entity.PairaviDetails;
import com.pams.entity.UploadAdditionalFilesDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AccusedCompCaseDtlRepository;
import com.pams.service.AccusedMasterRepository;
import com.pams.service.AccusedStatusRepository;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.AddSubSectionRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.CaseCompanyRepository;
import com.pams.service.CaseProcessingDatesRepository;
import com.pams.service.CaseStatusRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.CouncilDetailsRepository;
import com.pams.service.DetailsTypeRespository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.InspectorRepository;
import com.pams.service.InvCaseDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PairaviTypeRepository;
import com.pams.service.PetRespDetailRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UploadAdditionalFilesDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.service.addDisposalRepository;
import com.pams.utils.Utils;

import jakarta.validation.Valid;

@Controller
public class PuhApprovalController {
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhAfterCOurtRepository;
	@Autowired
	private PetRespDetailRepository petRespDetailRepo;
	@Autowired
	private AccusedStatusRepository AccusedStatusRepo;

	@Autowired
	private ComplaintReportRepository comprepo;

	@Autowired
	private PendingTaskForApprovalDAO pendingTaskForApprovalDAO;

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
	private CaseStatusRepository caseStatusRepo;
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
	private InspectorRepository inspectorRepository;

	@RequestMapping(value = "/puhApproveproTaskList")
	public String puhApproveproTaskList(Model model) throws Exception {

		UserDetails user = userDetailsService.getUserDetailssss();
		// List<AssignedTaskPuh> ApprovedTask =
		// assignedTaskPuhRepo.findAllByUserAndIsApproved(user, true);

		List<PendingTaskForApprovalDTO> approvalPendinglist = pendingTaskForApprovalDAO.pendingApproval();

		// List<PendingTaskForApprovalDTO> approvalPendinglist =
		// pendingTaskForApprovalDAO.approveAndReject();
		model.addAttribute("listAssinedTask", approvalPendinglist);

		/*
		 * . List<AssignedTaskPuh> ApprovedTask =
		 * assignedTaskPuhRepo.findAllIfByApproveStatus();
		 * 
		 * model.addAttribute("listAssinedTask", ApprovedTask);
		 */

		return "caseDetails/totalPendingForApproval";

	}

	@GetMapping("/puhApprovedAndRejectedTaskList")
	public String puhApprovedAndRejectedTaskList(Model model) throws Exception {
		
		
		
		int pageNo = 0;
		int noOfrecord = 2000;
		
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		
		Page<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtRepository.findAll(pagable);
		
		
		
		
		List<AssignedTaskPuhAfterCOurt> result = ApprovedTask.stream()
			    .sorted(Comparator.comparing(
			        a -> a.getProCourtCaseDetails().getCaseStatusCheck ()
			    ))
			    .collect(Collectors.toList());
		
		List<AssignedTaskPuhAfterCOurt> resul1t = ApprovedTask.stream()
			    .sorted(Comparator.comparing(
			        (AssignedTaskPuhAfterCOurt a) -> a.getProCourtCaseDetails().getCaseStatusCheck()
			    ).reversed())
			    .collect(Collectors.toList());
		
		model.addAttribute("cinSearch", new CinSearchDTO());
		long totalRow = ApprovedTask.getTotalElements();
		int currentRow = 1;
		int lastRow = ApprovedTask.getNumberOfElements();
		int pageNo1 = ApprovedTask.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", ApprovedTask.getNumberOfElements());
		model.addAttribute("pageNoDTO", new PageNoDTO());
		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);
		model.addAttribute("listAssinedTask", resul1t);
		return "caseDetails/puhApprovedAndRejectedTaskList";
	}

		
		
		
		
		
		
		
		
		
		
		/*
		 * UserDetails user = userDetailsService.getUserDetailssss();
		 * List<PendingTaskForApprovalDTO> approvalPendinglist =
		 * pendingTaskForApprovalDAO.approveAndReject();
		 * model.addAttribute("listAssinedTask", approvalPendinglist); return
		 * "caseDetails/puhApprovedAndRejectedTaskList";
		 */

	
	
	@RequestMapping(value = "/puhApproval", params = "assignTaskID1")
	public String ApproveproTaskList1111(ModelMap modelMap,
			@RequestParam(value = "assignTaskID1", required = true) Long id) throws Exception {
		int tabId = 21;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();
		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
		return "Task/CriminalTaskPageforApprovalView";
	}
	
	
	

	
	
	

	@RequestMapping(value = "/puhApproval", params = "assignTaskIDNew")
	public String ApproveproTaskListNew(ModelMap model,
			@RequestParam(value = "assignTaskIDNew", required = true) Long id) throws Exception {

    	ReportByCINDTO reportByCINDTO=new ReportByCINDTO();
		ProCourtCaseDetails proCourtCaseDetails =  proCourtCaseDetailsRepo.findById(id).get();
		List<CouncilDetails> allouncilDetails = councilDetailsRepo.findAllByProcourtdtl(proCourtCaseDetails);
		List<HearingDetails> allHearingDetails1 = hearingdtlRepo.findByProcourtdtl(proCourtCaseDetails);
		
		List<HearingDetails> allHearingDetails = allHearingDetails1.stream()
			    .sorted(Comparator.comparing(HearingDetails::getNextHearingDate).reversed())
			    .collect(Collectors.toList());
		
		
		Complaintdetl complaintdetl = complaintdetlRepo.findByProcourtdtl(proCourtCaseDetails);
		List<PairaviDetails> allPairaviDetails = pairaviDetailRepo.findAllByProcourtdtl(proCourtCaseDetails);
		CaseProcessingDates caseProcessingDates = caseProcessingRepo.findByProcourtdtl(proCourtCaseDetails);
		List<Inspector> byProCourtCaseDetails = inspectorRepository.findByProcourtdtl(proCourtCaseDetails);
		List<AddAccused> allByProcourtdtlAndApproveStatus = addAccusedRepo.findAllByProcourtdtlAndApproveStatus(proCourtCaseDetails, 2);
		
		reportByCINDTO.setCaseProcessingDates(caseProcessingDates);
		reportByCINDTO.setComplaintdetl(complaintdetl);
		reportByCINDTO.setHearingDetails(allHearingDetails);
		reportByCINDTO.setPairaviDetails(allPairaviDetails);
		reportByCINDTO.setCouncilDetails(allouncilDetails);
		reportByCINDTO.setProCourtCaseDetails(proCourtCaseDetails);
		reportByCINDTO.setInspector(byProCourtCaseDetails);
		reportByCINDTO.setAddAccused(allByProcourtdtlAndApproveStatus);
		
		
		 model.addAttribute("reportByCINDTO", reportByCINDTO);
		 model.addAttribute("cinSearch", new CinSearchDTO());
  	  model.addAttribute("caseListt",new ArrayList<ProCourtCaseDetails>());
  	
  	return "report/viewDetailsByCIN_new";
  }


	@RequestMapping(value = "/puhApproval", params = "assignTaskID")
	public String ApproveproTaskList111(ModelMap modelMap,
			@RequestParam(value = "assignTaskID", required = true) Long id) throws Exception {
		int tabId = 21;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();
		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
		return "Task/CriminalTaskPageforApproval";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "approveCriminalDtl")
	public String approveCriminalDtl(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 0;

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
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedCriminalComplaint") + " " + "and Investigation Number is "
						+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();
		modelMap.addAttribute("message", " Complaintant details has been approved : ");
		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPageforApproval";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "rejectCriminalDtl")
	public String rejectCriminalDtl(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 0;
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		Complaintdetl complaintDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(proCourtCaseDtl,
				assignedTaskPuh);

		complaintDtl.setApprove_status(3);
		complaintDtl.setRejectRemark(criminalTaskDto.getRejectRemark());
		complaintdetlRepo.save(complaintDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedCriminalComplaint") + " " + "and Investigation Number is "
						+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();
		modelMap.addAttribute("message", " Criminal details has been rejected : ");
		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPageforApproval";
	}

	@RequestMapping(value = "addCriminalDtl", params = "approveCompany")
	public String approveCompany(@RequestParam(value = "approveCompany", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) {

		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		CaseCompany casecompDtl = caseCompanyRepo.findById(id).get();
		casecompDtl.setApproveStatus(2);
		caseCompanyRepo.save(casecompDtl);
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPageforApproval";
	}

	@RequestMapping(value = "addCriminalDtl", params = "rejectCompanybyPUH")
	public String rejectCompany(ModelMap model, CriminalTaskDto criminalTaskDto) {

		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseCompany casecompDtl = caseCompanyRepo.findById(criminalTaskDto.getCompanyId()).get();
		casecompDtl.setApproveStatus(3);
		casecompDtl.setRejectRemark(criminalTaskDto.getRejectRemarkforCompany());
		caseCompanyRepo.save(casecompDtl);
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPageforApproval";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "approvedbyPUH")
	public String approvedbyPUH(@RequestParam(value = "approvedbyPUH", required = true) Long id, ModelMap model,
			CriminalTaskDto criminalTaskDto) throws Exception {
		try {
			
			PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid"));
			pofficeredit.setApproveStatus(2);
			pairaviDetailRepo.save(pofficeredit);
			model.addAttribute("message", " pairavi officer has been approved : ");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", " Please select correct ID : ");
			// TODO: handle exception
		}
		
		
		
		int tabId = 22;
		
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedpairaviOfficer") + " " + "and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		/*
		/*
		 * pofficeredit.setApproveStatus(2); pairaviDetailRepo.save(pofficeredit);
		 * model.addAttribute("message", " pairavi officer has been approved : ");
		 */

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPageforApproval";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "rejectBYPUH")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectBYPUH(ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {
		Long id = criminalTaskDto.getPairaviId();
		PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid"));
		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		pofficeredit.setApproveStatus(3);
		pofficeredit.setRejectRemark(criminalTaskDto.getRejectRemarkforPairavi());
		pairaviDetailRepo.save(pofficeredit);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedpairaviOfficer") + " " + "and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " pairavi officer rejected : ");

		modelAttributeObject(assignedTaskPuh, model, tabId, new CriminalTaskDto());
		return "Task/CriminalTaskPageforApproval";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "approveCaseProseingDates")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String approveCaseProseingDates(@RequestParam(value = "approveCaseProseingDates", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(id).get();
		caseproseingDate.setApproveStatus(2);
		caseProcessingRepo.save(caseproseingDate);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedCaseProcessing") + " " + "and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		 model.addAttribute("message", "Case Prosessing Details Approved  :");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPageforApproval";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "rejectCaseProcessingDate")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectCaseProcessingDate(ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(criminalTaskDto.getCaseproseingID()).get();
		caseproseingDate.setApproveStatus(3);
		caseproseingDate.setRejectRemark(criminalTaskDto.getRejectRemarkforcaseProseing());
		caseProcessingRepo.save(caseproseingDate);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedCaseProcessing") + " " + "and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		  model.addAttribute("message", "Case Prosessing Details Rejected  :");
		modelAttributeObject(assignedTaskPuh, model, tabId, new CriminalTaskDto());
		return "Task/CriminalTaskPageforApproval";

	}

	

	@RequestMapping(value = "addCriminalDtl", params = "rejectBYPUH1")
	public String rejectAccused(ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		Long id = criminalTaskDto.getAccusedIDforDelete();
		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		accusedDetails.setApproveStatus(3);
		accusedDetails.setRejectRemark(criminalTaskDto.getRejectRemarkforAccused());
		
		addAccusedRepo.save(accusedDetails);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedAccused") + " " + "and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Accused details has been rejected : ");
		int tabId = 266;
		

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPageforApproval";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "approveUploadFile")
	
	public String approveUploadFile(@RequestParam(value = "approveUploadFile", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {
		 UploadAdditionalFilesDetails hearingDtl = uploadAdditionalFilesDetailsRepo.findById(id).get();
		hearingDtl.setApproveStatus(2);
		uploadAdditionalFilesDetailsRepo.save(hearingDtl);
		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedFilingDetails")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Upload file details has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPageforApproval";

	}
	@RequestMapping(value = "/addCriminalDtl", params = "rejectUploadFile")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectUploadFile(ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {
		Long id = criminalTaskDto.getRejectuploadID();
		 UploadAdditionalFilesDetails hearingDtl = uploadAdditionalFilesDetailsRepo.findById(id).get();
		hearingDtl.setApproveStatus(3);
		hearingDtl.setRejectRemark(criminalTaskDto.getRejectRemarkforAddUploadFile());
		uploadAdditionalFilesDetailsRepo.save(hearingDtl);
		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedFilingDetails")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Upload file details has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, new CriminalTaskDto());
		return "Task/CriminalTaskPageforApproval";

	}
	
	
	
	@RequestMapping(value = "/addCriminalDtl", params = "approveHearingDetails")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String approveHearingDetails(@RequestParam(value = "approveHearingDetails", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();
		hearingDtl.setApproveStatus(2);
		hearingdtlRepo.save(hearingDtl);
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedHearingDetails")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Hearing details has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/CriminalTaskPageforApproval";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "rejectHearingDetails")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectHearingDetails(ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {
		Long id = criminalTaskDto.getHearingID();
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();
		hearingDtl.setApproveStatus(3);
		hearingDtl.setRejectRemark(criminalTaskDto.getRejectRemarkforHearing());

		hearingdtlRepo.save(hearingDtl);
		model.addAttribute("message", " Hearing details has been rejected : ");
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedHearingDetails")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		modelAttributeObject(assignedTaskPuh, model, tabId, new CriminalTaskDto());
		return "Task/CriminalTaskPageforApproval";

	}
	@RequestMapping(value = "/addCriminalDtl", params = "approveUpdateCourtcaseDetails")
	public String approveUpdateCourtcaseDetails(@RequestParam(value = "approveUpdateCourtcaseDetails") Long id,
			ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto, BindingResult bindResult)
			throws Exception {
		criminalTaskDto.setAddAccused(null);
		int tabId = 24;
		ProCourtCaseDetails proCourtCaseDtl = proCourtCaseDetailsRepo.findById(id).get();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findByProCourtCaseDetails(proCourtCaseDtl);
		proCourtCaseDtl.setApproveStatus(2);
		proCourtCaseDetailsRepo.save(proCourtCaseDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedUpdateCourtcaseDetailsC")+" "+"and Investigation Number is "+proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();
		modelMap.addAttribute("message", " Court case details has been approved : ");
		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPageforApproval";
	}
	
	@RequestMapping(value = "/addCriminalDtl", params = "rejectdUpdateCourtcaseDetails")
	public String rejectdUpdateCourtcaseDetails(ModelMap modelMap,
			@ModelAttribute @Valid CriminalTaskDto criminalTaskDto, BindingResult bindResult) throws Exception {
		criminalTaskDto.setAddAccused(null);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		int tabId = 24;
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		modelMap.addAttribute("message", " Court case details has been rejectd : ");
		proCourtCaseDtl.setApproveStatus(5);
		proCourtCaseDtl.setSendBackRemarks(criminalTaskDto.getRejectRemarkforCourtcase());
		proCourtCaseDetailsRepo.save(proCourtCaseDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedUpdateCourtcaseDetailsC")+" "+"and Investigation Number is "+proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();

		CriminalTaskDto criminalTaskDto1 = new CriminalTaskDto();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/CriminalTaskPageforApproval";
	}
	
	  @RequestMapping(value = "addCriminalDtl", params = "ApprovedAccused") public
	  String ApprovedAccused(@RequestParam(value = "ApprovedAccused", required =
	  true) Long id, ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {
	  
	  AddAccused accusedDetails = addAccusedRepo.findById(id).get();
	  accusedDetails.setApproveStatus(2); addAccusedRepo.save(accusedDetails);
	  model.addAttribute("message", " Accused details has been approved : ");
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh =assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedAccused")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
	  
	  modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
	  
	  return "Task/CriminalTaskPageforApproval";
	  
	  }
	 
	  public void  modelAttributeObjectCourt(@Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, ModelMap modelMap, int tabId,
				CriminalTaskDto criminalTaskDto) {
		  criminalTaskDto.setTabId(tabId);

			assignedTaskPuh.getId();
			ProCourtCaseDetails procasedetails =assignedTaskPuh.getProCourtCaseDetails();

			int approveStatus = 1;
			int approveStatus1 = 3;
			criminalTaskDto.setProCourtDtl(procasedetails);
			List<CaseProcessingDates> caseproseingdates = caseProcessingRepo
					.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(procasedetails, assignedTaskPuh,
							approveStatus, approveStatus1);
			modelMap.addAttribute("caseproseingdates", caseproseingdates);
			
			List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
					assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
			
			modelMap.addAttribute("personList", accusedList);

			//InvCaseDetails invcaseDtl = InvCaseDtlRepo.findAllById(procasedetails.getInvCaseDetail().getId());
			criminalTaskDto.setAssignedTask(assignedTaskPuh);

			modelMap.addAttribute("assignedDtl", procasedetails);

			modelMap.addAttribute("criminalTaskDto", criminalTaskDto);
			//modelMap.addAttribute("invCasedtl", invcaseDtl);

			/*
			 * if (assignedTaskPuh.getAddCase().getType().getType().equals("NCLT")) {
			 * criminalTaskDto.setTypeOfCase("NCLT"); } else {
			 * criminalTaskDto.setTypeOfCase("NCLT11"); }
			 */

			List<HearingDetails> hearinglist = hearingdtlRepo.findByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
					procasedetails, assignedTaskPuh, approveStatus, approveStatus1);

			if (!hearinglist.isEmpty()) {
				for (HearingDetails hearingDetails : hearinglist) {

					List<AccusedStatus> accusedwithStatus = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());
					hearingDetails.setAccusedwithStatus(accusedwithStatus);
				}
			}

			

			ComplaintReport genreport = comprepo.findByAssignedTaskPuh(assignedTaskPuh);

			if (genreport != null) {
				criminalTaskDto.setTypeofreport(genreport.getTypeOfReport());
				criminalTaskDto.setGenreportID(genreport.getId());
				criminalTaskDto.setApproveStatusGenReport(genreport.getApproveStatus());
				criminalTaskDto.setRejectRemarkGenReport(genreport.getRejectRemark());
			}

			List<UploadAdditionalFilesDetails> uploadadditionalfile = uploadAdditionalFilesDetailsRepo
					.findByAssignedTaskPuhdtlAndApproveStatusBetween(assignedTaskPuh, approveStatus, approveStatus1);

			modelMap.addAttribute("uploadadditionalfile", uploadadditionalfile);
			modelMap.addAttribute("genreport", genreport);

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
				criminalTaskDto.setInvCaseNo(compDtl.getProcourtdtl().getAddCase().getInvestigationOrderNo());
			}
			List<PairaviDetails> pairvidtl = pairaviDetailRepo.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
					procasedetails, assignedTaskPuh, approveStatus, approveStatus1);

			ProCourtCaseDetails t = procasedetails;

			List<CaseCompany> companyList = caseCompanyRepo.findByAssignedTaskAndApproveStatusBetween(assignedTaskPuh,
					approveStatus, approveStatus1);

			modelMap.addAttribute("companyList", companyList);

			modelMap.addAttribute("pairvidtl", pairvidtl);

		
	  }
	public void modelAttributeObject(@Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, ModelMap modelMap, int tabId,
			CriminalTaskDto criminalTaskDto) {

		criminalTaskDto.setTabId(tabId);

		assignedTaskPuh.getId();
		ProCourtCaseDetails procasedetails =assignedTaskPuh.getProCourtCaseDetails();

		int approveStatus = 1;
		int approveStatus1 = 3;
		criminalTaskDto.setProCourtDtl(procasedetails);
		List<CaseProcessingDates> caseproseingdates = caseProcessingRepo
				.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(procasedetails, assignedTaskPuh,
						approveStatus, approveStatus1);
		modelMap.addAttribute("caseproseingdates", caseproseingdates);
		
		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		
		modelMap.addAttribute("personList", accusedList);

		//InvCaseDetails invcaseDtl = InvCaseDtlRepo.findAllById(procasedetails.getInvCaseDetail().getId());
		criminalTaskDto.setAssignedTask(assignedTaskPuh);

		modelMap.addAttribute("assignedDtl", procasedetails);

		modelMap.addAttribute("criminalTaskDto", criminalTaskDto);
		//modelMap.addAttribute("invCasedtl", invcaseDtl);

		/*
		 * if (assignedTaskPuh.getAddCase().getType().getType().equals("NCLT")) {
		 * criminalTaskDto.setTypeOfCase("NCLT"); } else {
		 * criminalTaskDto.setTypeOfCase("NCLT11"); }
		 */

		List<HearingDetails> hearinglist = hearingdtlRepo.findByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
				procasedetails, assignedTaskPuh, approveStatus, approveStatus1);

		if (!hearinglist.isEmpty()) {
			for (HearingDetails hearingDetails : hearinglist) {

				List<AccusedStatus> accusedwithStatus = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());
				hearingDetails.setAccusedwithStatus(accusedwithStatus);
			}
		}

		

		ComplaintReport genreport = comprepo.findByAssignedTaskPuh(assignedTaskPuh);

		if (genreport != null) {
			criminalTaskDto.setTypeofreport(genreport.getTypeOfReport());
			criminalTaskDto.setGenreportID(genreport.getId());
			criminalTaskDto.setApproveStatusGenReport(genreport.getApproveStatus());
			criminalTaskDto.setRejectRemarkGenReport(genreport.getRejectRemark());
		}

		List<UploadAdditionalFilesDetails> uploadadditionalfile = uploadAdditionalFilesDetailsRepo
				.findByAssignedTaskPuhdtlAndApproveStatusBetween(assignedTaskPuh, approveStatus, approveStatus1);

		modelMap.addAttribute("uploadadditionalfile", uploadadditionalfile);
		modelMap.addAttribute("genreport", genreport);

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
			criminalTaskDto.setInvCaseNo(compDtl.getProcourtdtl().getAddCase().getInvestigationOrderNo());
		}
		List<PairaviDetails> pairvidtl = pairaviDetailRepo.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
				procasedetails, assignedTaskPuh, approveStatus, approveStatus1);

		ProCourtCaseDetails t = procasedetails;

		List<CaseCompany> companyList = caseCompanyRepo.findByAssignedTaskAndApproveStatusBetween(assignedTaskPuh,
				approveStatus, approveStatus1);

		modelMap.addAttribute("companyList", companyList);

		modelMap.addAttribute("pairvidtl", pairvidtl);

	}

}
