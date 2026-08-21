package com.pams.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.AppUserDAO;
import com.pams.dao.PendingTaskForApprovalDAO;
import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.AccusedStatus;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.ComplaintReport;
import com.pams.entity.Complaintdetl;
import com.pams.entity.FreezerAssetOrder;
import com.pams.entity.HearingDetails;
import com.pams.entity.PairaviDetails;
import com.pams.entity.PerformaParty;
import com.pams.entity.ResponseOfRespondent;
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
import com.pams.service.ClauseRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.CouncilDetailsRepository;
import com.pams.service.DetailsTypeRespository;
import com.pams.service.FreezerAssetOrderRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.InvCaseDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.PairaviTypeRepository;
import com.pams.service.PerformaPartyRepo;
import com.pams.service.PetRespDetailRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.PunishmentRepository;
import com.pams.service.ResponseOfRespondentRepository;
import com.pams.service.StateRepository;
import com.pams.service.UploadAdditionalFilesDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.service.addDisposalRepository;
import com.pams.service.districtRepository;
import com.pams.utils.Utils;

import jakarta.validation.Valid;
@Controller
public class PuhApprovalNCLTController {
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private PerformaPartyRepo performaPartyRepo;
	@Autowired
	private ClauseRepository clauseRepo;
	@Autowired
	private ResponseOfRespondentRepository responseOfRespondentRepository;
	@Autowired
	private FreezerAssetOrderRepository freezerAssetOrderRepository;
	@Autowired
	private PunishmentRepository punishmentRepo;
	@Autowired
	private districtRepository districtRepo;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;
	@Autowired
	private PetRespDetailRepository petRespDetailRepo;
	@Autowired
	private AccusedStatusRepository AccusedStatusRepo;
	@Autowired
	private StateRepository stateRepo;
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
	
	@RequestMapping(value = "/addCriminalDtl", params = "approvedByPerformaParty")
	public String approvedByPerformaParty(@RequestParam(value = "approvedByPerformaParty", required = true) Long id,
			ModelMap model, NCLTTaskDTO nCLTTaskDTO) throws Exception {
		int tabId = 27;
		
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		PerformaParty performaParty = performaPartyRepo.findById(id).get();
		//ResponseOfRespondent responseOfRespondent = responseOfRespondentRepository.findById(id).get();
		performaParty.setApproveStatus(2);
		performaPartyRepo.save(performaParty);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedPerformaParty")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Performa Party  has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";
	}
	
	
	@RequestMapping(value = "/addCriminalDtl", params = "rejectBYPUHPerformaParty")
	public String rejectPerformaParty(ModelMap model,
			@ModelAttribute @Valid NCLTTaskDTO nCLTTaskDTO, BindingResult bindResult) throws Exception {
		int tabId = 27;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		PerformaParty performaParty = performaPartyRepo.findById(nCLTTaskDTO.getPerformaPartyId()).get();
		performaParty.setApproveStatus(3);
		performaParty.setRejectRemark(nCLTTaskDTO.getRejectRemarkForPerformaParty());
		performaPartyRepo.save(performaParty);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedPerformaParty")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		
		nCLTTaskDTO.setRejectRemarkForPerformaParty("");
		model.addAttribute("message", " Performa Party  has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";
	}
	
	
	@RequestMapping(value = "addCriminalDtl", params = "rejectBYPUH1NCLT")
	public String rejectAccused(ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception {

		Long id = criminalTaskDto.getAccusedIDforDelete();
		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		accusedDetails.setApproveStatus(3);
		accusedDetails.setRejectRemark(criminalTaskDto.getRejectRemarkforAccused());
		
		addAccusedRepo.save(accusedDetails);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedRespondent")+" "+"and Investigation Number is "+accusedDetails.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",accusedDetails.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Respondent details has been rejected : ");
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/NCLTTaskPageForApproval";

	}
	
	@RequestMapping(value = "/addCriminalDtl", params = "rejectUploadFileNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectUploadFile(ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception, Exception {
		Long id = criminalTaskDto.getRejectuploadID();
		 UploadAdditionalFilesDetails hearingDtl = uploadAdditionalFilesDetailsRepo.findById(id).get();
		hearingDtl.setApproveStatus(3);
		hearingDtl.setRejectRemark(criminalTaskDto.getRejectRemarkforAddUploadFile());
		uploadAdditionalFilesDetailsRepo.save(hearingDtl);
		
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedFilingDetails")+" "+"and Investigation Number is "+hearingDtl.getAssignedTaskPuhdtl().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",hearingDtl.getAssignedTaskPuhdtl().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		model.addAttribute("message", " Filing details has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, new NCLTTaskDTO());
		return "Task/NCLTTaskPageForApproval";

	}
	
@RequestMapping(value = "/addCriminalDtl", params = "approveUploadFileNCLT")
	
	public String approveUploadFileNCLT(@RequestParam(value = "approveUploadFileNCLT", required = true) Long id,
			ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception, Exception {
		 UploadAdditionalFilesDetails hearingDtl = uploadAdditionalFilesDetailsRepo.findById(id).get();
		hearingDtl.setApproveStatus(2);
		uploadAdditionalFilesDetailsRepo.save(hearingDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedFilingDetails")+" "+"and Investigation Number is "+hearingDtl.getAssignedTaskPuhdtl().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",hearingDtl.getAssignedTaskPuhdtl().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		model.addAttribute("message", " Filing details has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTTaskPageForApproval";

	}
	
	
	@RequestMapping(value = "/addCriminalDtl", params = "approvedFreeze")
	
	public String approvedFreeze(@RequestParam(value = "approvedFreeze", required = true) Long id,
			ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception {

		int tabId = 28;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		 FreezerAssetOrder frezeOrderAssets = freezerAssetOrderRepository.findById(id).get();
		 frezeOrderAssets.setApprovalStatus(2);
		 freezerAssetOrderRepository.save(frezeOrderAssets);
		 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
					Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.approved"),
					utils.getMessage("log.login.approvedFreezerAssets")+" "+"and Investigation Number is "+frezeOrderAssets.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",frezeOrderAssets.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		 model.addAttribute("message", " Assets Freeze has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTTaskPageForApproval";

	}
	
	
	
	@RequestMapping(value = "/addCriminalDtl", params = "approveCaseProseingDatesNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String approveCaseProseingDatesNCLT(@RequestParam(value = "approveCaseProseingDatesNCLT", required = true) Long id,
			ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception, Exception {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(id).get();
		caseproseingDate.setApproveStatus(2);
		caseProcessingRepo.save(caseproseingDate);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedCaseProcessing")+" "+"and Investigation Number is "+caseproseingDate.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",caseproseingDate.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Case Processing date  has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTTaskPageForApproval";

	}
	
	@RequestMapping(value = "/addCriminalDtl", params = "rejectFreeze")
	
	public String rejectFreeze(ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception, Exception {

		int tabId = 28;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		 FreezerAssetOrder frezeOrderAssets = freezerAssetOrderRepository.findById(criminalTaskDto.getRejectfreezeID()).get();
		 frezeOrderAssets.setApprovalStatus(3);
		 
		 frezeOrderAssets.setRejectRemarkFrezeAssets(criminalTaskDto.getRejectRemarkFreez());
		 freezerAssetOrderRepository.save(frezeOrderAssets);
		 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
					Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.rejected"),
					utils.getMessage("log.login.rejectedFreezerAssets")+" "+"and Investigation Number is "+frezeOrderAssets.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",frezeOrderAssets.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		 model.addAttribute("message", " Upload File has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, new NCLTTaskDTO());
		return "Task/NCLTTaskPageForApproval";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "rejectCaseProcessingDateNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectCaseProcessingDate(ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception, Exception {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(criminalTaskDto.getCaseproseingID()).get();
		caseproseingDate.setApproveStatus(3);
		caseproseingDate.setRejectRemark(criminalTaskDto.getRejectRemarkforcaseProseing());
		caseProcessingRepo.save(caseproseingDate);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedCaseProcessing")+" "+"and Investigation Number is "+caseproseingDate.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",caseproseingDate.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Case Processing date  has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, new NCLTTaskDTO());
		return "Task/NCLTTaskPageForApproval";

	}

	
	
	
	
	@RequestMapping(value = "/addCriminalDtl", params = "approvedbyPUHNCLT")
	public String approvedbyPUH(@RequestParam(value = "approvedbyPUHNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) throws Exception, Exception {
		PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid"));
		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		pofficeredit.setApproveStatus(2);
		pairaviDetailRepo.save(pofficeredit);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedpairaviOfficer")+" "+"and Investigation Number is "+pofficeredit.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",pofficeredit.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " pairavi officer has been approved : ");

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTTaskPageForApproval";


	}

	@RequestMapping(value = "/addCriminalDtl", params = "rejectBYPUHNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String rejectBYPUHNCLT(ModelMap model, NCLTTaskDTO criminalTaskDto) throws Exception, Exception {
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
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedpairaviOfficer")+" "+"and Investigation Number is "+pofficeredit.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",pofficeredit.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " pairavi officer rejected : ");

		modelAttributeObject(assignedTaskPuh, model, tabId, new NCLTTaskDTO());
		return "Task/NCLTTaskPageForApproval";

	}
	
	
	
	
	
	@RequestMapping(value = "/addCriminalDtl", params = "approveUpdateCourtcaseDetailsNCLT")
	public String approveUpdateCourtcaseDetailsNCLT(@RequestParam(value = "approveUpdateCourtcaseDetailsNCLT") Long id,
			ModelMap modelMap, @ModelAttribute @Valid NCLTTaskDTO criminalTaskDto, BindingResult bindResult)
			throws Exception {
		criminalTaskDto.setAddAccused(null);
		int tabId = 23;
		ProCourtCaseDetails proCourtCaseDtl = proCourtCaseDetailsRepo.findById(id).get();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findByProCourtCaseDetails(proCourtCaseDtl);
		proCourtCaseDtl.setApproveStatus(2);
		proCourtCaseDetailsRepo.save(proCourtCaseDtl);
		modelMap.addAttribute("message", " Court Case No. approved : ");
		
		
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedUpdateCourtCaseNo")+" "+"and Investigation Number is "+proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();
		
		
		
		
		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/NCLTTaskPageForApproval";
	}
	@RequestMapping(value = "/addCriminalDtl", params = "rejectdUpdateCourtcaseDetailsNCLT")
	public String rejectdUpdateCourtcaseDetailsNCLT(ModelMap modelMap,
			@ModelAttribute @Valid NCLTTaskDTO criminalTaskDto, BindingResult bindResult) throws Exception {
		criminalTaskDto.setAddAccused(null);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		int tabId = 23;
		ProCourtCaseDetails proCourtCaseDtl =assignedTaskPuh.getProCourtCaseDetails();

		proCourtCaseDtl.setApproveStatus(5);
		proCourtCaseDtl.setSendBackRemarks(criminalTaskDto.getRejectRemarkforCourtcase());
		proCourtCaseDetailsRepo.save(proCourtCaseDtl);
		modelMap.addAttribute("message", " Court Case No. rejected : ");
		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/NCLTTaskPageForApproval";
	}
	@RequestMapping(value = "/addCriminalDtl", params = "rejectCriminalDtlNCLT")
	public String rejectCriminalDtl(ModelMap modelMap, @ModelAttribute @Valid NCLTTaskDTO criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 21;
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		Complaintdetl complaintDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(proCourtCaseDtl,
				assignedTaskPuh);

		complaintDtl.setApprove_status(3);
		complaintDtl.setRejectRemark(criminalTaskDto.getRejectRemark());
		complaintdetlRepo.save(complaintDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.sendback"),
				utils.getMessage("log.login.sendbackPetitioner")+" "+"and Investigation Number is "+proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();
		modelMap.addAttribute("message", " Petitioner has been rejected : ");
		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/NCLTTaskPageForApproval";
	}
	@RequestMapping(value = "/addCriminalDtl", params = "approvalCriminalDtlNCLT")
	public String approvalCriminalDtlNCLT(ModelMap modelMap, @ModelAttribute @Valid NCLTTaskDTO criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 21;
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		Complaintdetl complaintDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(proCourtCaseDtl,
				assignedTaskPuh);

		complaintDtl.setApprove_status(2);
		
		complaintdetlRepo.save(complaintDtl);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedPetitioner")+" "+"and Investigation Number is "+proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();
		modelMap.addAttribute("message", " Petitioner has been approved : ");
		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);

		return "Task/NCLTTaskPageForApproval";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/puhApproval", params = "assignTaskIDNCLT")
	public String ApproveproTaskList111(ModelMap modelMap,
			@RequestParam(value = "assignTaskIDNCLT", required = true) Long id) throws Exception {
		int tabId = 21;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();
		/*
		 * CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
		 * modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
		 */
		NCLTTaskDTO nCLTTaskDTO = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);

		
		return "Task/NCLTTaskPageForApproval";
	}
	
	@RequestMapping(value = "/addCriminalDtl", params = "approveforResponseOfRespondent")
	public String approveforResponseOfRespondent(@RequestParam(value = "approveforResponseOfRespondent", required = true) Long id,
			ModelMap model, NCLTTaskDTO nCLTTaskDTO) throws Exception, Exception {
		int tabId = 27;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		ResponseOfRespondent responseOfRespondent = responseOfRespondentRepository.findById(id).get();
		responseOfRespondent.setApprovalStatus(2);
		responseOfRespondentRepository.save(responseOfRespondent);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedResponseOfRespondent")+" "+"and Investigation Number is "+responseOfRespondent.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",responseOfRespondent.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Response Of Respondent  has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";
	}
	@RequestMapping(value = "/addCriminalDtl", params = "rejectResponseOfResapomdent")
	public String rejectResponseOfResapomdent(ModelMap model,
			@ModelAttribute @Valid NCLTTaskDTO nCLTTaskDTO, BindingResult bindResult) throws Exception {
		int tabId = 27;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		ResponseOfRespondent responseOfRespondent = responseOfRespondentRepository.findById(nCLTTaskDTO.getResponseOfRespondentId()).get();
		responseOfRespondent.setApprovalStatus(3);
		responseOfRespondent.setRejectRemark(nCLTTaskDTO.getRejectRemarkforResponseOfRespondent());
		responseOfRespondentRepository.save(responseOfRespondent);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedResponseOfRespondent")+" "+"and Investigation Number is "+responseOfRespondent.getAssignedTask().getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",responseOfRespondent.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", " Response Of Respondent  has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";
	}
	@RequestMapping(value = "addCriminalDtl", params = "ApprovedRespondent")
	public String ApprovedRespondent(@RequestParam(value = "ApprovedRespondent", required = true) Long id,
			ModelMap model, NCLTTaskDTO nCLTTaskDTO) throws Exception, Exception {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		accusedDetails.setApproveStatus(2);
		addAccusedRepo.save(accusedDetails);
		model.addAttribute("message", " Respondent  has been approved : ");

		int tabId = 25;
	
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedRespondent")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";

	}
	
	@RequestMapping(value = "addCriminalDtl", params = "RejectRespondent")
	public String RejectRespondent(@RequestParam(value = "RejectRespondent", required = true) Long id,
			ModelMap model, NCLTTaskDTO nCLTTaskDTO) {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		accusedDetails.setApproveStatus(3);
		addAccusedRepo.save(accusedDetails);

		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		model.addAttribute("message", "Respondent  has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";

	}
	@RequestMapping(value = "/addCriminalDtl", params = "approveHearingDetailsNclt")
	public String approveHearingDetailsNclt(@RequestParam(value = "approveHearingDetailsNclt", required = true) Long id,
			ModelMap model, NCLTTaskDTO nCLTTaskDTO) throws Exception {
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();
		hearingDtl.setApproveStatus(2);
		hearingdtlRepo.save(hearingDtl);
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.approvedHearingDetails")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		model.addAttribute("message", "Hearing Details  has been approved : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";

	}
	
	@RequestMapping(value = "addCriminalDtl", params = "rejectHearingDetails321")
	public String rejectHearingDetails321(@ModelAttribute  NCLTTaskDTO nCLTTaskDTO,ModelMap model) throws Exception {
		
		HearingDetails hearingDetails = hearingdtlRepo.findById(nCLTTaskDTO.getHearingID()).get();
		hearingDetails.setApproveStatus(3);
		hearingDetails.setRejectRemark(nCLTTaskDTO.getRejectRemarkforHearing());
		hearingdtlRepo.save(hearingDetails);

		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId()).get();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.rejected"),
				utils.getMessage("log.login.rejectedHearingDetails")+" "+"and Investigation Number is "+assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true",assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		
		model.addAttribute("message", "Hearing Details  has been rejected : ");
		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTTaskPageForApproval";

	}
	
	
	
	public void modelAttributeObject(@Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, ModelMap modelMap, int tabId,
			NCLTTaskDTO nCLTTaskDTO) {
		modelMap.addAttribute("performaPartyList", performaPartyRepo.findByAssignedTask(assignedTaskPuh));


		nCLTTaskDTO.setTabId(tabId);
		int approveStatus = 1;
		int approveStatus1 = 3;
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();

		nCLTTaskDTO.setProCourtDtl(procasedetails);
		List<CaseProcessingDates> caseproseingdates = caseProcessingRepo
				.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(procasedetails, assignedTaskPuh,
						approveStatus, approveStatus1);
		modelMap.addAttribute("caseproseingdates", caseproseingdates);
	
		nCLTTaskDTO.setAssignedTask(assignedTaskPuh);

			nCLTTaskDTO.setTypeOfCase("NCLT");
		
		modelMap.addAttribute("assignedDtl", procasedetails);
		
		modelMap.addAttribute("nCLTTaskDTO", nCLTTaskDTO);
	
		
		ComplaintReport genreport = comprepo.findByAssignedTaskPuh(assignedTaskPuh);

		if (genreport != null) {
			nCLTTaskDTO.setTypeofreport(genreport.getTypeOfReport());
			nCLTTaskDTO.setGenreportID(genreport.getId());
			nCLTTaskDTO.setApproveStatusGenReport(genreport.getApproveStatus());
			nCLTTaskDTO.setRejectRemarkGenReport(genreport.getRejectRemark());
		}

		List<UploadAdditionalFilesDetails> uploadadditionalfile = uploadAdditionalFilesDetailsRepo
				.findByAssignedTaskPuhdtlAndApproveStatusBetween( assignedTaskPuh, approveStatus, approveStatus1);
		modelMap.addAttribute("uploadadditionalfile", uploadadditionalfile);
		modelMap.addAttribute("genreport", genreport);
		List<HearingDetails> hearinglist = hearingdtlRepo.findByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
				procasedetails, assignedTaskPuh, approveStatus, approveStatus1);

		if (!hearinglist.isEmpty()) {
			for (HearingDetails hearingDetails : hearinglist) {

				List<AccusedStatus> accusedwithStatus = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());
				hearingDetails.setAccusedwithStatus(accusedwithStatus);
			}
		}
		modelMap.addAttribute("hearinglist", hearinglist);

		Complaintdetl compDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(procasedetails, assignedTaskPuh);

		if (compDtl != null) {
			nCLTTaskDTO.setRejectRemark(compDtl.getRejectRemark());
			nCLTTaskDTO.setApprove_status(compDtl.getApprove_status());
			nCLTTaskDTO.setInvCaseNo(procasedetails.getAddCase().getInvestigationOrderNo());
			nCLTTaskDTO.setComplanitId(compDtl.getComplanitId());
			nCLTTaskDTO.setComplanitEmail(compDtl.getComplanitEmail());
			nCLTTaskDTO.setComplanitName(compDtl.getComplanitName());
			nCLTTaskDTO.setComplaintMobile(compDtl.getComplaintMobile());
			nCLTTaskDTO.setComplanitdesignation(compDtl.getComplanitdesignation());
			nCLTTaskDTO.setComplaintPetinoner(compDtl.getComplaintPetinoner());
			nCLTTaskDTO.setIOName(compDtl.getIOName());
			nCLTTaskDTO.setComplaintPetinonerDate(compDtl.getComplaintPetinonerDate());

		}
		List<PairaviDetails> pairvidtl = pairaviDetailRepo.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(
				procasedetails, assignedTaskPuh, approveStatus, approveStatus1);
		modelMap.addAttribute("pairvidtl", pairvidtl);
		
		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtlAndAssignedTaskAndApproveStatusBetween(procasedetails,
				assignedTaskPuh,approveStatus, approveStatus1);
		
		List<ResponseOfRespondent> listOfResponseOfRespondent = responseOfRespondentRepository.findAllByAssignedTaskAndApprovalStatusBetween(assignedTaskPuh, approveStatus, approveStatus1);
		
		List<FreezerAssetOrder> listfreezerAssetList = freezerAssetOrderRepository.findAllByAssignedTaskAndApprovalStatusBetween(assignedTaskPuh, approveStatus, approveStatus1);
		modelMap.addAttribute("personList", accusedList);
		modelMap.addAttribute("responselist", listOfResponseOfRespondent);
		modelMap.addAttribute("freezerAssetList", listfreezerAssetList);
		List<AddAccused> addaccusedList = addAccusedRepo.findAllByAssignedTaskAndAccusedTypeNot(assignedTaskPuh, "Individual");
		
		modelMap.addAttribute("addaccusedList", addaccusedList);
		List<CaseCompany> companyList = caseCompanyRepo.findAllIbyAssignTask(assignedTaskPuh.getId());

		modelMap.addAttribute("companyList", companyList);

		
		
	}
	

}
