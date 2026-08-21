package com.pams.controllers;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.AppUserDAO;
import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTTaskDTO;
import com.pams.entity.AccusedActAndSection;
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
import com.pams.entity.DetailsType;
import com.pams.entity.District;
import com.pams.entity.HearingDetails;
import com.pams.entity.InvCaseDetails;
import com.pams.entity.MiscellaneousFile;
import com.pams.entity.PairaviDetails;
import com.pams.entity.PerformaParty;
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
import com.pams.service.CaseStatusRepository;
import com.pams.service.ClauseRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.CouncilDetailsRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.DetailsTypeRespository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.InvCaseDetailsRepository;
import com.pams.service.MiscellaneousFileRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.PairaviTypeRepository;
import com.pams.service.PerformaPartyRepo;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.PunishmentRepository;
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
public class OfficeControllerNCLT {
	private static final Logger logger = LoggerFactory.getLogger(OfficeControllerNCLT.class);

	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private TypeofResponseRepository typeofResponseRepo;

	@Autowired
	private ClauseRepository clauseRepo;
	@Autowired
	private AccusedActSectionRepository accusedActSectionRepo;

	@Autowired
	private PunishmentRepository punishmentRepo;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;

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
	private AccusedStatusRepository AccusedStatusRepo;

	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private districtRepository districtRepo;

	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private MiscellaneousFileRepository misRepo;

	@RequestMapping(value = "/addCriminalDtl", params = "complainDtlSaveNCLT1")
	public String ComplainDtlSave1(ModelMap modelMap, @ModelAttribute @Valid CriminalTaskDto criminalTaskDto,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		int tabId = 21;
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
				utils.getMessage("log.login.complDtlsForwardNCLT") + " " + assignedTaskPuh.getUser().getSalutation()
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
		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		modelMap.addAttribute("message", "Petitioner Details Confirmed Successfully  :");

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "addCriminalDtl", params = "forwardToApprovalAccusedNCLT")
	public String forwardToApprovalAccusedNCLT(
			@RequestParam(value = "forwardToApprovalAccusedNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) throws Exception {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		accusedDetails.setApproveStatus(2);
		addAccusedRepo.save(accusedDetails);
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		model.addAttribute("message", "Respondent Details Confirmed Successfully  :");
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.respondentdetailforward") + " " + assignedTaskPuh.getUser().getSalutation()
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
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "addCriminalDtl", params = "editAccusedNCLT")
	public String editAccusedNCLT(@RequestParam(value = "editAccusedNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) {

		AddAccused accusedDetails = addAccusedRepo.findById(id).get();
		Set<AccusedActAndSection> actSection = accusedDetails.getActSection();
		Long[] accusedACT = new Long[actSection.size()];
		Long[] accusedSection = new Long[actSection.size()];
		Long[] accusedSubSection = new Long[actSection.size()];
		String[] clause = new String[actSection.size()];
		String[] actName = new String[actSection.size()];
		String[] sectionName = new String[actSection.size()];
		String[] subsectionName = new String[actSection.size()];

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
				accusedACT[i] = accusedACT132;
				accusedSection[i] = section;
				accusedSubSection[i] = subsection;

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

		criminalTaskDto.setAccusedACT(accusedACT);
		criminalTaskDto.setAccusedSection(accusedSection);
		criminalTaskDto.setAccusedSubSection(accusedSubSection);
		criminalTaskDto.setAccusedDescription(disc);
		criminalTaskDto.setAccusedClause(clause);
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "editHearingDetailsNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String editHearingDetails(@RequestParam(value = "editHearingDetailsNCLT", required = true) Long id,
			ModelMap model, NCLTTaskDTO criminalTaskDto) {

		int tabId = 266;
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		criminalTaskDto.setHearingEditId(id);

		criminalTaskDto.setHearingEditId(hearingDtl.getId());
		criminalTaskDto.setRejectRemarkforHearing("");
		/*
		 * criminalTaskDto.setCounselName(hearingDtl.getCounselName());
		 * criminalTaskDto.setCounselName1(hearingDtl.getCounselName1());
		 * criminalTaskDto.setCounselEmail(hearingDtl.getCounselEmail());
		 * criminalTaskDto.setCounselEmail1(hearingDtl.getCounselEmail1());
		 * criminalTaskDto.setCounselDesignation(hearingDtl.getCounselDesignation());
		 * criminalTaskDto.setCounselDesignation1(hearingDtl.getCounselDesignation1());
		 * criminalTaskDto.setCounselMobileNo(hearingDtl.getCounselMobileNo());
		 * criminalTaskDto.setCounselMobileNo1(hearingDtl.getCounselMobileNo1());
		 */
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

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardUpdateCourtcaseDetailsNCLT")
	public String forwardUpdateCourtcaseDetails(ModelMap modelMap, @ModelAttribute @Valid NCLTTaskDTO criminalTaskDto,
			BindingResult bindResult, ModelMap model) throws Exception {
		criminalTaskDto.setAddAccused(null);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		int tabId = 23;
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();

		proCourtCaseDtl.setApproveStatus(4);
		proCourtCaseDetailsRepo.save(proCourtCaseDtl);

		model.addAttribute("message", " Court Case No. send for approval : ");
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.ncltcourtcasenumberforward") + " "
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

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardtoapprovalPairaviOfficerNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String forwardtoapprovalPairaviOfficer(
			@RequestParam(value = "forwardtoapprovalPairaviOfficerNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) throws Exception {
		PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid"));
		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		pofficeredit.setApproveStatus(2);
		pairaviDetailRepo.save(pofficeredit);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.pairaviforward") + " " + assignedTaskPuh.getUser().getSalutation() + " "
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
		model.addAttribute("message", " pairavi officer Confirmed details.");

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "addCriminalDtl", params = "addAccusedResponseNCLT")
	public String addAccusedResponse(@RequestParam(value = "addAccusedResponseNCLT", required = true) Long id,
			ModelMap model, @ModelAttribute @Valid NCLTTaskDTO criminalTaskDto) {

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
		// AssignedTaskPuh assignedTaskPuh =
		// assignedTaskPuhRepo.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
				proCourtDtls.getAddCase(), accusedDetails, Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("accusedResponseList", accusedResponseList);

		return "Task/AccusedRespondNCLT";

	}

	@PostMapping("/confirmDetailsNCLT")
	public String confirmDetailsNCLT(@RequestParam Long id, Model model) {
		try {
			AccusedResponse accusedDetails = accusedResponseRepo.findById(id).get();
			accusedDetails.setApprovalStatus(2);
			accusedResponseRepo.save(accusedDetails);

			List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(accusedDetails.getAddCase());
			model.addAttribute("lst", lst);
			// AddAccused accusedDetails =
			// addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();
			AddAccused accusedDetails2 = accusedDetails.getAccusedDetails();

			AssignedTaskPuhAfterCOurt assignedTaskPuhAfterCOurt = assignedTaskPuhRepo1
					.findById(accusedDetails.getAssignedTask().getId()).get();
			ProCourtCaseDetails proCourtDtls = assignedTaskPuhAfterCOurt.getProCourtCaseDetails();
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

			return "Task/AccusedRespondNCLT";

		} catch (Exception e) {
			return "redirect:/accusedResponses";
		}
	}

	@PostMapping("/editDetailsNCLT")
	public String editDetailsNCLT(@RequestParam Long id, Model model) {
		try {
			AccusedResponse accusedDetails = accusedResponseRepo.findById(id).get();
			// accusedDetails.setApprovalStatus(2);
			accusedResponseRepo.save(accusedDetails);

			List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(accusedDetails.getAddCase());
			model.addAttribute("lst", lst);
			// AddAccused accusedDetails =
			// addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();
			AddAccused accusedDetails2 = accusedDetails.getAccusedDetails();

			AssignedTaskPuhAfterCOurt assignedTaskPuhAfterCOurt = assignedTaskPuhRepo1
					.findById(accusedDetails.getAssignedTask().getId()).get();
			ProCourtCaseDetails proCourtDtls = assignedTaskPuhAfterCOurt.getProCourtCaseDetails();
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

			// model.addAttribute("message", "Accused response Confirmed successfully");

			List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("statelist", statelist);
			model.addAttribute("districtlist", districtlist);
			List<AccusedResponse> accusedResponseList = accusedResponseRepo.findByAddCaseAndAccusedDetails(
					proCourtDtls.getAddCase(), accusedDetails2, Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("accusedResponseList", accusedResponseList);

			int tabId = 266;
			// modelAttributeObject(assignTask, model, tabId, new CriminalTaskDto());

			return "Task/AccusedRespondNCLT";

		} catch (Exception e) {
			return "redirect:/accusedResponses";
		}
	}

	@RequestMapping(value = "saveAccusedResponseNCLT")
	public String saveAccusedResponse(ModelMap model, @ModelAttribute AccusedResponse accusedResponse,
			BindingResult errors) throws Exception {
		AddCase addcase = accusedResponse.getAddCase();
		AccusedCompValidation accusedValidation = new AccusedCompValidation();
		accusedValidation.accusedResponse(accusedResponse, errors);
		if (errors.hasErrors()) {

			List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(addcase);
			model.addAttribute("lst", lst);
			AddAccused accusedDetails = addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();
			// AddAccused accusedDetails = accusedResponse.getAccusedDetails();
			// model.addAttribute("accusedDetails", accusedDetails);
			AssignedTaskPuhAfterCOurt assignTask = accusedResponse.getAssignedTask();
			ProCourtCaseDetails proCourtDtls = assignTask.getProCourtCaseDetails();
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

			return "Task/AccusedRespondNCLT";
		}
		AddAccused accusedDetails = addAccusedRepo.findById(accusedResponse.getAccusedDetailsID()).get();

		accusedResponse.setAccusedDetails(accusedDetails);
		UserDetails user = userDetailsService.getUserDetailssss();
		accusedResponse.setCreatedBy(user);
		accusedResponse.setUpdatedBy(user);
		accusedResponse.setApprovedBy(user);
		accusedResponse.setCreatedDate(new Date());
		accusedResponseRepo.save(accusedResponse);

		model.addAttribute("message", "Responsedant added successfully");
		List<AccusedResponse> lst = accusedResponseRepo.findByAddCase(addcase);
		model.addAttribute("lst", lst);
		// AddAccused accusedDetails = accusedResponse.getAccusedDetails();
		model.addAttribute("accusedDetails", accusedDetails);
		AssignedTaskPuhAfterCOurt assignTask = accusedResponse.getAssignedTask();
		ProCourtCaseDetails proCourtDtls = assignTask.getProCourtCaseDetails();
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("proCourtDtls", proCourtDtls);
		List<TypeofResponse> typeofResponse = typeofResponseRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("typeofResponse", typeofResponse);
		model.addAttribute("courtType", courtType);
		AccusedResponse accusedResponse11 = new AccusedResponse();
		accusedResponse11.setAccusedDetailsID(accusedResponse.getAccusedDetailsID());
		accusedResponse11.setAccusedDetails(accusedDetails);
		accusedResponse11.setAssignedTask(assignTask);
		accusedResponse11.setAddCase(assignTask.getProCourtCaseDetails().getAddCase());
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

		return "Task/AccusedRespondNCLT";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "updateCourtCaseDetailsNCLT")
	public String saveCourtCaseDetails(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") NCLTTaskDTO criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 23;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		ProCourtCaseDetails proCourtCaseDetails = assignedTaskPuh.getProCourtCaseDetails();
		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findById(assignedTaskPuhRepo.findById(criminalTaskDto.getAssignedTask().getId()).get().getProCourtCase().getId()).get();
		String courtCaseNo = criminalTaskDto.getCourtCaseName();

		CriminalTaskValidation criminalTaskVelidation = new CriminalTaskValidation();
		criminalTaskVelidation.courtCaseNoValidation1(criminalTaskDto, bindResult);

		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/NCLTtaskPage";

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
				utils.getMessage("log.login.ncltcourtcasenumbersave") + " " + " and Investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "updateCourtCaseDetails1NCLT")
	public String updateCourtCaseDetails1(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") NCLTTaskDTO criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 23;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		ProCourtCaseDetails proCourtCaseDetails = assignedTaskPuh.getProCourtCaseDetails();
		// proCourtCaseDetails proCourtCaseDetails =
		// proCourtCaseDetailsRepo.findById(assignedTaskPuhRepo.findById(criminalTaskDto.getAssignedTask().getId()).get().getProCourtCase().getId()).get();
		String courtCaseNo = criminalTaskDto.getCourtCaseName();

		CriminalTaskValidation criminalTaskVelidation = new CriminalTaskValidation();
		criminalTaskVelidation.courtCaseNoValidation1(criminalTaskDto, bindResult);
		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			return "Task/NCLTtaskPage";

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
				utils.getMessage("log.login.Update"),
				utils.getMessage("log.login.ncltcourtcasenumberupdated") + " " + " and Investigation Number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		NCLTTaskDTO criminalTaskDto1 = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto1);
		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "editPairaviOfficerNCLT")

	public String editTask(@RequestParam(value = "editPairaviOfficerNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) {
		PairaviDetails pofficeredit = pairaviDetailRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid"));
		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();

		// criminalTaskDto.setPairaviemail(pofficeredit.getEmail());
		// criminalTaskDto.setPairavidesignation(pofficeredit.getDesignation());
		// criminalTaskDto.setPairaviName(pofficeredit.getName());
		criminalTaskDto.setPairaviId(pofficeredit.getId());
		criminalTaskDto.setPairaviType(pofficeredit.getPairaviType());
		criminalTaskDto.setPairaviOfficer(pofficeredit.getPairaviOfficer());
		// criminalTaskDto.setPairaviMobile(pofficeredit.getMobile());
		criminalTaskDto.setPairavifromDate(pofficeredit.getFromDate());
		criminalTaskDto.setPairavitoDate(pofficeredit.getToDate());

		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/proceedTask3")
	public String proceedTask3(ModelMap modelMap,
			@RequestParam(value = "assignedTaskPuh") AssignedTaskPuhAfterCOurt assignedTaskPuh,
			@RequestParam(value = "tabId") int tabId) throws Exception {

		// int tabId = 23;
		NCLTTaskDTO criminalTaskDto = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "backToAccusedNCLT")
	public String backToAccused(ModelMap model, @ModelAttribute AccusedResponse accusedResponse, BindingResult errors) {

		int tabId = 266;
		modelAttributeObject(accusedResponse.getAssignedTask(), model, tabId, new NCLTTaskDTO());

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/proceedTaskNCLT")
	public String approveTask2(ModelMap modelMap,
			@RequestParam(value = "assignedTaskPuh") AssignedTaskPuhAfterCOurt assignedTaskPuh,
			@RequestParam(value = "tabId") int tabId) throws Exception {

		// int tabId = 23;
		NCLTTaskDTO nCLTTaskDTO = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/additionalDetails", params = "assignTaskIDNCLT")
	public String ApproveproTaskList(ModelMap modelMap,
			@RequestParam(value = "assignTaskIDNCLT", required = true) Long id) throws Exception {

		int tabId = 21;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();

		NCLTTaskDTO NCLTTaskDTO = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO);

		return "Task/NCLTtaskPage";
	}
	@RequestMapping(value = "/additionalDetails", params = "assignTaskIDSuprime")
	public String ApproveproTaskListSuprime(ModelMap modelMap,
			@RequestParam(value = "assignTaskIDSuprime", required = true) Long id) throws Exception {

		int tabId = 21;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();

		NCLTTaskDTO NCLTTaskDTO = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO);

		return "Task/SuprimetaskPage";
	}
	@RequestMapping(value = "/additionalDetails", params = "assignTaskIDHigh")
	public String ApproveproTaskListHigh(ModelMap modelMap,
			@RequestParam(value = "assignTaskIDHigh", required = true) Long id) throws Exception {

		int tabId = 21;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(id).get();

		NCLTTaskDTO NCLTTaskDTO = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO);

		return "Task/HighCourttaskPage";
	}
	

	@RequestMapping(value = "NCLTAddCompanyDetails")
	public String addCompany(ModelMap modelMap, @Valid @ModelAttribute NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		int tabId = 22;

		AddCompany companylst = new AddCompany();
		companylst.setId(nCLTTaskDTO.getCompanyId());

		String compName = nCLTTaskDTO.getCompName();
		String compCin = nCLTTaskDTO.getCin();
		String coyadd = nCLTTaskDTO.getCompAddess();
		Long compId = nCLTTaskDTO.getCompanyId();

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.companyValidation1(nCLTTaskDTO, bindResult);

		if (bindResult.hasErrors()) {

			NCLTTaskDTO crim = new NCLTTaskDTO();
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, crim);
			return "Task/NCLTtaskPage";
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
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);
				return "Task/NCLTtaskPage";

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

		NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO1);

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "addCriminalDtl", params = "editcompanyNCLT")
	public String editcompany(@RequestParam(value = "editcompanyNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO NCLTTaskDTO) {
		AddCompany AddCompanyDetails = addCompanyRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		int tabId = 22;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(NCLTTaskDTO.getAssignedTask().getId())
				.get();

		NCLTTaskDTO.setCompanyId(AddCompanyDetails.getId());
		NCLTTaskDTO.setCompName(AddCompanyDetails.getCompanyName());
		NCLTTaskDTO.setCompAddess(AddCompanyDetails.getAddress());
		NCLTTaskDTO.setCin(AddCompanyDetails.getCin());

		modelAttributeObject(assignedTaskPuh, model, tabId, NCLTTaskDTO);

		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "complainDtlSaveNCLT")
	public String ComplainDtlSave(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") @Valid NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		int tabId = 21;

		/*
		 * if (nCLTTaskDTO.getComplanitdesignation() == null ||
		 * nCLTTaskDTO.getComplanitdesignation().getId() == 0) {
		 * bindResult.rejectValue("complanitdesignation", "msg.wrongId"); }
		 */

		if (nCLTTaskDTO.getIOName() == null || nCLTTaskDTO.getIOName().equalsIgnoreCase("0")) {
			bindResult.rejectValue("IOName", "msg.wrongId");
		}

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.complaintValidation1(nCLTTaskDTO, bindResult);
		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);
			return "Task/NCLTtaskPage";
		}

		Complaintdetl complaintDtl1 = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(proCourtCaseDtl,
				assignedTaskPuh);
		if (complaintDtl1 == null) {

			Complaintdetl complaintDtl = new Complaintdetl();
			complaintDtl.setProcourtdtl(nCLTTaskDTO.getProCourtDtl());
			complaintDtl.setComplaintPetinoner(nCLTTaskDTO.getComplaintPetinoner());
			complaintDtl.setComplanitdesignation(nCLTTaskDTO.getComplanitdesignation());
			complaintDtl.setDesigInvesOffi(nCLTTaskDTO.getDesigInvesOffi());
			complaintDtl.setComplanitEmail(nCLTTaskDTO.getComplanitEmail());
			complaintDtl.setComplanitName(nCLTTaskDTO.getComplanitName());
			complaintDtl.setComplaintMobile(nCLTTaskDTO.getComplaintMobile());
			complaintDtl.setIOName(nCLTTaskDTO.getIOName());
			complaintDtl.setAssignedTask(assignedTaskPuh);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			complaintDtl.setCreatedBy(userdet);
			complaintDtl.setUpdatedBy(userdet);
			complaintDtl.setApproveBy(userdet);
			complaintDtl.setCreatedDate(new Date());
			complaintDtl.setComplaintPetinonerDate(nCLTTaskDTO.getComplaintPetinonerDate());

			if (assignedTaskPuh.getForwardedStatus() == 0) {
				complaintDtl.setApprove_status(0);
			}

			complaintdetlRepo.save(complaintDtl);
			modelMap.addAttribute("message", " Petitioner officer Added Successfully  : ");
			NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO1);
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.petitionersave") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

			return "Task/NCLTtaskPage";
		} else {
			complaintDtl1.setProcourtdtl(nCLTTaskDTO.getProCourtDtl());
			complaintDtl1.setComplaintPetinoner(nCLTTaskDTO.getComplaintPetinoner());
			complaintDtl1.setComplanitdesignation(nCLTTaskDTO.getComplanitdesignation());
			complaintDtl1.setDesigInvesOffi(nCLTTaskDTO.getDesigInvesOffi());
			complaintDtl1.setComplanitEmail(nCLTTaskDTO.getComplanitEmail());
			complaintDtl1.setComplanitName(nCLTTaskDTO.getComplanitName());
			complaintDtl1.setComplaintMobile(nCLTTaskDTO.getComplaintMobile());
			complaintDtl1.setIOName(nCLTTaskDTO.getIOName());
			complaintDtl1.setAssignedTask(assignedTaskPuh);
			complaintDtl1.setApprove_status(0);
			if (assignedTaskPuh.getForwardedStatus() == 0) {
				complaintDtl1.setApprove_status(0);
			}

			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

			complaintDtl1.setUpdatedBy(userdet);

			complaintDtl1.setUpdatedDate(new Date());

			complaintdetlRepo.save(complaintDtl1);
			NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO1);
			modelMap.addAttribute("message", " Petitioner officer updated Successfully  : ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.petitionerupdate") + " " + " and Investigation Number is "
							+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

			return "Task/NCLTtaskPage";
		}
	}

	@RequestMapping(value = "/addCriminalDtl", params = "savePairaviNCLT")
	public String savePairavi(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") @Valid NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();

		Long paraviid = nCLTTaskDTO.getPairaviId();

		int tabId = 22;
		PairaviDetails pairaviDetails = new PairaviDetails();

		pairaviDetails.setId(paraviid);
		PairaviDetails pairavidtl = pairaviDetailRepo.findAllByProcourtdtlAndIsActive(proCourtCaseDtl, true);

		if (pairavidtl != null && nCLTTaskDTO.getPairavifromDate() != null) {
			Date fromDate = nCLTTaskDTO.getPairavifromDate();

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

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		if (nCLTTaskDTO.getPairaviType() == null || nCLTTaskDTO.getPairaviType().getId() == 0) {

			criminalTaskVal.pairaviOfficerEarliar1(nCLTTaskDTO, bindResult);
		}

		else {

			if (nCLTTaskDTO.getPairaviType().getPairaviType().equals("Earlier")) {

				criminalTaskVal.pairaviOfficerEarliar1(nCLTTaskDTO, bindResult);
			}

			if (nCLTTaskDTO.getPairaviType().getPairaviType().equals("Current")) {

				criminalTaskVal.pairaviOfficercurrent1(nCLTTaskDTO, bindResult);
			}

		}
		if (bindResult.hasErrors()) {

			// NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();

			modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);

			return "Task/NCLTtaskPage";

		} else {

			if (pairavidtl != null) {
				pairavidtl.setIsActive(false);

				pairavidtl.setToDate(nCLTTaskDTO.getPairavifromDate());
				pairaviDetailRepo.save(pairavidtl);
			}

			DetailsType dt = detailsTypeRepo.findAllById(4L);
			pairaviDetails.setId(paraviid);
			pairaviDetails.setDetailsType(dt);
			pairaviDetails.setPairaviType(nCLTTaskDTO.getPairaviType());
			// pairaviDetails.setName(nCLTTaskDTO.getPairaviName());
			// pairaviDetails.setEmail(nCLTTaskDTO.getPairaviemail());
			// pairaviDetails.setMobile(nCLTTaskDTO.getPairaviMobile());
			pairaviDetails.setPairaviOfficer(nCLTTaskDTO.getPairaviOfficer());
			pairaviDetails.setFromDate(nCLTTaskDTO.getPairavifromDate());
			pairaviDetails.setToDate(nCLTTaskDTO.getPairavitoDate());
			pairaviDetails.setProcourtdtl(proCourtCaseDtl);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			pairaviDetails.setCreatedBy(userdet);
			pairaviDetails.setCreatedDate(new Date());
			pairaviDetails.setAssignedTask(assignedTaskPuh);

			pairaviDetailRepo.save(pairaviDetails);

			if (paraviid == null) {
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Save"),
						utils.getMessage("log.login.pairavisave") + " " + " and Investigation number is "
								+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();

				modelMap.addAttribute("message", " pairavi officer Added Successfully  : ");
			} else {

				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Update"),
						utils.getMessage("log.login.pairaviupdate") + " " + " and Investigation Number is "
								+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();

				modelMap.addAttribute("message", " pairavi officer Updated Successfully  : ");
			}

		}

		NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO1);

		// return "Task/NCLTtaskPage";

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		// return "redirect:/proceedTask";
		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardCaseProseingDatesNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String forwardCaseProseingDates(
			@RequestParam(value = "forwardCaseProseingDatesNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO nCLTTaskDTO) throws Exception {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
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
				utils.getMessage("log.login.addcasesCaseprocessingforward") + " "
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

		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "editCaseProseingDatesNCLT")
	// @RequestMapping(value = "/editPofficer", params = "editPairaviOfficer")
	public String editCaseProseingDates(@RequestParam(value = "editCaseProseingDatesNCLT", required = true) Long id,
			ModelMap model, NCLTTaskDTO nCLTTaskDTO) {

		int tabId = 24;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		CaseProcessingDates caseproseingDate = caseProcessingRepo.findById(id).get();
		nCLTTaskDTO.setInvOrder(caseproseingDate.getOrderOfInv());
		nCLTTaskDTO.setSuppInv(caseproseingDate.getOrderOfSupplyInv());
		nCLTTaskDTO.setInvReport(caseproseingDate.getSubmissionOfInvReport());
		nCLTTaskDTO.setSuppInvReport(caseproseingDate.getSubmissionOfSupplyInvReport());
		nCLTTaskDTO.setDateFilling(caseproseingDate.getInsuranceOFInvByMCA());
		nCLTTaskDTO.setCaseproseingID(caseproseingDate.getId());
		nCLTTaskDTO.setForeEditStatus(2);

		modelAttributeObject(assignedTaskPuh, model, tabId, nCLTTaskDTO);
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "caseProcessingDateNCLT")
	public String caseProcessingDate(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") @Valid NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {

		int tabId = 24;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		// proCourtCaseDetails proCourtCaseDtl =
		// proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);

		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		InvCaseDetails invcaseDtl = InvCaseDtlRepo.findAllById(proCourtCaseDtl.getInvCaseDetail().getId());

		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();

		criminalTaskVal.caseProcessingDate1(nCLTTaskDTO, bindResult);

		if (bindResult.hasErrors()) {

			modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);

			return "Task/NCLTtaskPage";

		} else {
			CaseProcessingDates caseProcessDate = new CaseProcessingDates();
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

			if (nCLTTaskDTO.getCaseproseingID() != null) {
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Update"),
						utils.getMessage("log.login.caseprocessingdateupdate") + " " + " and Investigation Number is "
								+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();

				redirect.addFlashAttribute("message", " Case Processing Dates Updated Successfully  : ");
				caseProcessDate.setId(nCLTTaskDTO.getCaseproseingID());
			} else {
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.login.Save"),
						utils.getMessage("log.login.caseprocessingdatesave") + " " + " and Investigation number is "
								+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
						userdet.getSalutation() + " " + userdet.getFirstName() + " "
								+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
								+ userdet.getLastName(),
						"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
				auditBeanBo.save();
				redirect.addFlashAttribute("message", " Case Processing Dates Added Successfully  : ");

			}
			caseProcessDate.setOrderOfInv(nCLTTaskDTO.getInvOrder());
			caseProcessDate.setOrderOfSupplyInv(nCLTTaskDTO.getSuppInv());
			caseProcessDate.setSubmissionOfInvReport(nCLTTaskDTO.getInvReport());
			caseProcessDate.setSubmissionOfSupplyInvReport(nCLTTaskDTO.getSuppInvReport());
			caseProcessDate.setInsuranceOFInvByMCA(nCLTTaskDTO.getDateFilling());
			caseProcessDate.setProcourtdtl(proCourtCaseDtl);
			// UserDetails userdet =
			// useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			caseProcessDate.setCreatedBy(userdet);
			caseProcessDate.setCreatedDate(new Date());
			caseProcessDate.setAssignedTask(assignedTaskPuh);

			if (assignedTaskPuh.getForwardedStatus() == 0) {
				caseProcessDate.setApproveStatus(0);
			}

			caseProcessingRepo.save(caseProcessDate);

		}
		NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO1);

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		return "redirect:/proceedTaskNCLT";
		// return "Task/NCLTtaskPage";
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/SaveAccusedDetailsNCLT")
	public String SaveAccusedDetails(ModelMap modelMap, @ModelAttribute @Valid NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult) throws Exception {
		int tabId = 25;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();

		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);
			return "Task/NCLTtaskPage";
		}

		String desss = nCLTTaskDTO.getAccusedDescription();
		String[] arrayDiscription = desss.split("~");
		int approveStatus1 = 0;
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		AddCompany companylst = addCompanyRepo.findById((long) 0).get();
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		CaseCompany caseCompany = new CaseCompany(companylst, proCourtCaseDtl, nCLTTaskDTO.getCompId(), userdet,
				new Date(), assignedTaskPuh, approveStatus1);

		CaseCompany SavedCaseComp = caseCompanyRepo.save(caseCompany);

		// List<AccusedActAndSection> actSections = new ArrayList<>();
		Set<AccusedActAndSection> actSections = new HashSet<>();

		AddAccused addAccuse = new AddAccused();
		if (nCLTTaskDTO.getAccusedIdEdit() != null) {
			Set<AccusedActAndSection> list = addAccusedRepo.findById(nCLTTaskDTO.getAccusedIdEdit()).get()
					.getActSection();
			accusedActSectionRepo.deleteInBatch(list);
			addAccuse.setId(nCLTTaskDTO.getAccusedIdEdit());
		}

		addAccuse.setCompany(SavedCaseComp);
		addAccuse.setAssignedTask(assignedTaskPuh);
		addAccuse.setCompany(caseCompanyRepo.findById((long) 1).get());
		addAccuse.setPerformaPartyRespondent(nCLTTaskDTO.getPerformaPartyRespondent());
		addAccuse.setRespondentNumber(nCLTTaskDTO.getRespondentNumber());
		addAccuse.setAccusedName(nCLTTaskDTO.getAccusedName());
		addAccuse.setPanNumber(nCLTTaskDTO.getAccPan());
		addAccuse.setAddress(nCLTTaskDTO.getAddAccusedAddress());
		addAccuse.setAccusedType(nCLTTaskDTO.getAccusedType());
		addAccuse.setCreatedDate(new Date());

		int lenth = nCLTTaskDTO.getAccusedACT().length;
		for (int i = 1; i <= lenth; i++) {
			AccusedActAndSection actandSection = new AccusedActAndSection();
			Long act = nCLTTaskDTO.getAccusedACT()[i - 1];
			Long section = nCLTTaskDTO.getAccusedSection()[i - 1];
			// String compatability = nCLTTaskDTO.getAccusedCompoundability()[i-1];
			String description = arrayDiscription[i - 1];
			String clouse = nCLTTaskDTO.getAccusedClause()[i - 1];
			Long addsubsec = nCLTTaskDTO.getAccusedSubSection()[i - 1];
			// Integer punishmentID = nCLTTaskDTO.getAaccusedPunishment()[i-1];
			// Punishment1 punishment = punishmentRepo.findById(punishmentID).get();
			// actandSection.setPunishment(punishment);
			actandSection.setDescription(description);
			// actandSection.setCompatability(compatability);
			actandSection.setClause(clouse);
			AddSubSec subSection = addsubsecRepo.findById(addsubsec).get();
			// getById(addsubsec);
			actandSection.setSubSection(subSection);
			actandSection.setAct(addActRepo.findById(act).get());
			actandSection.setSection(addactsecRepo.findById(section).get());
			actandSection.setAddAccused(addAccuse);
			actSections.add(actandSection);

		}

		addAccuse.setActSection(actSections);
		addAccuse.setIndividualRelateTo(nCLTTaskDTO.getIndividualRelateTo());
		addAccuse.setCinNumber(nCLTTaskDTO.getCinRespondent());
		addAccuse.setCreatedBy(userdet);
		addAccuse.setDesignation(nCLTTaskDTO.getAccDesination2());
		addAccuse.setProcourtdtl(proCourtCaseDtl);

		addAccusedRepo.save(addAccuse);

		if (nCLTTaskDTO.getAccusedIdEdit() == null) {
			modelMap.addAttribute("message", " Respondent has been saved : ");

			/* anjali */
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.respondentdetailsaved") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

			/* anjali */
		} else {
			modelMap.addAttribute("message", " Respondent has been updated : ");

			/* anjali */

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.respondentdetailupdate") + " " + " and Investigation Number is "
							+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

			/* anjali */
		}

		NCLTTaskDTO NCLTTaskDTO1 = new NCLTTaskDTO();

		// modelMap.addAttribute("message", " Respondent has been saved. : ");
		modelAttributeObject(assignedTaskPuh, modelMap, tabId, NCLTTaskDTO1);
		return "Task/NCLTtaskPage";
	}

	public void modelAttributeObject(@Valid AssignedTaskPuhAfterCOurt assignedTaskPuh, ModelMap modelMap, int tabId,
			NCLTTaskDTO nCLTTaskDTO) {
		modelMap.addAttribute("performaPartyList", performaPartyRepo.findByAssignedTask(assignedTaskPuh));
		nCLTTaskDTO.setAddAccused(null);
		List<AddState> statelist = stateRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		AddCase addCase = assignedTaskPuh.getProCourtCaseDetails().getAddCase();

		List<ProCourtCaseDetails> byAddCase = proCourtCaseDetailsRepo.findByAddCaseId(addCase.getId());
		List<AddAccused> allByProcourtdtl = null;

		if (!byAddCase.isEmpty()) {
			ProCourtCaseDetails proCourtCaseDetails = byAddCase.stream().filter(n -> n.getType().getId() == 2)
					.findFirst().orElse(null);

			if (proCourtCaseDetails != null) {
				allByProcourtdtl = addAccusedRepo.findAllByProcourtdtl(proCourtCaseDetails);
				// allByProcourtdtl.forEach(h -> System.out.println(h.getAccusedName()));
			}
		}
		modelMap.addAttribute("allAccusedFromCriminal", allByProcourtdtl);
		List<District> districtlist = districtRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));

		modelMap.addAttribute("pairaviOfficerList",
				pairaviofficerRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("counselOfficerList",
				pairaviofficerRepo.findByType(2, Sort.by(Sort.Direction.ASC, "name")));
		modelMap.addAttribute("punishmentlist", punishmentRepo.findAll());
		modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		nCLTTaskDTO.setInvCaseNo(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo());
		nCLTTaskDTO.setInvOrder(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderDate());
		nCLTTaskDTO.setSuppInv(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getSupplimentoryOrderDate());
		nCLTTaskDTO.setDateFilling(assignedTaskPuh.getProCourtCaseDetails().getAddCase().getProSanctionDate());

		String tt = assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo();
		// NCLTTaskDTO.setInvCaseNo(InvCaseNo);
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1, Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("courtType", courtType);
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("addActList", addactlist);

		modelMap.addAttribute("seclst", addactsecRepo.findAll());
		modelMap.addAttribute("subseclst",
				addsubsecRepo.findAllBySection(addactsecRepo.findById((long) 0), Sort.by(Sort.Direction.ASC, "id")));

		nCLTTaskDTO.setTabId(tabId);
		// List<AddAccused> accuseDtlForStatus =
		// addAccusedRepo.findAllByAssignedTask(assignedTaskPuh);
		// modelMap.addAttribute("accuseDtlForStatus", accuseDtlForStatus);
		modelMap.addAttribute("accuseDtlForStatus1", new AccusedStatus());

		List<AccusedStatus> lstAccusedStatus = null;
		if (nCLTTaskDTO.getEditHearing() == null) {
			lstAccusedStatus = AccusedStatusRepo.findByAssignedTaskAndApproveStatusAndStatus(assignedTaskPuh, 0, true,
					Sort.by(Sort.Direction.DESC, "id"));

		} else {
			Long hearingID = nCLTTaskDTO.getHearingEditId();
			lstAccusedStatus = AccusedStatusRepo.findByHearingDetailsAndStatus(nCLTTaskDTO.getHearingEditId(), true);
		}
		modelMap.addAttribute("lstAccusedStatus", lstAccusedStatus);

		assignedTaskPuh.getId();

		// proCourtCaseDetails procasedetails =
		// proCourtCaseDetailsRepo.findByAddCase(assignedTaskPuh.getAddCase());
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		if (procasedetails.getApproveStatus() == 5) {
			nCLTTaskDTO.setCourtCaseName(procasedetails.getCourtCaseNo());
		}
		nCLTTaskDTO.setProCourtDtl(procasedetails);

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
		nCLTTaskDTO.setAssignedTask(assignedTaskPuh);
		/*
		 * if (assignedTaskPuh.getAddCase().getTypeOfCase().getTypeOfCase().equals(
		 * "NCLT/NCLAT")) { nCLTTaskDTO.setTypeOfCase("NCLT"); } else {
		 * nCLTTaskDTO.setTypeOfCase("NCLT11"); }
		 */

		List<AddDesignation> sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Officer");
		List<AddDesignation> sfioCounslorDesignation = designationRepo.findByDeginationtype("SFIO Counsel");
		List<AddDesignation> CompanyEmployeeDesignation = designationRepo.findByDeginationtype("Company Employee");
		modelMap.addAttribute("desilst", sfioOfficerDesignation);
		modelMap.addAttribute("desilstC", sfioCounslorDesignation);
		modelMap.addAttribute("desilstE", CompanyEmployeeDesignation);

		modelMap.addAttribute("assignedDtl", procasedetails);
		modelMap.addAttribute("ptypelst", pairaviTypeRepo.findAll());
		modelMap.addAttribute("nCLTTaskDTO", nCLTTaskDTO);
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
		 * nCLTTaskDTO.setLastHearingDate(currenthearingDtl.getNextHearingDate()); }
		 */

		List<Status> StatusList = addStatusRepo.findAllByTypeAndIsActive("A",true);
		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActive("nclt",true);
		ComplaintReport genreport = comprepo.findByAssignedTaskPuh(assignedTaskPuh);

		if (genreport != null) {
			nCLTTaskDTO.setTypeofreport(genreport.getTypeOfReport());
			nCLTTaskDTO.setGenreportID(genreport.getId());
			nCLTTaskDTO.setApproveStatusGenReport(genreport.getApproveStatus());
			nCLTTaskDTO.setRejectRemarkGenReport(genreport.getRejectRemark());
		}

		List<UploadAdditionalFilesDetails> uploadadditionalfile = uploadAdditionalFilesDetailsRepo
				.findByAssignedTaskPuhdtl(assignedTaskPuh);

		modelMap.addAttribute("uploadadditionalfile", uploadadditionalfile);
		List<MiscellaneousFile> all = misRepo.findByAssignedTask(assignedTaskPuh);
		modelMap.addAttribute("misAllDataByAssignTask", all);
		modelMap.addAttribute("genreport", genreport);

		modelMap.addAttribute("statusLst", StatusList);
		modelMap.addAttribute("statusLst1", StatusList1);
		modelMap.addAttribute("hearinglist", hearinglist);

		Complaintdetl compDtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(procasedetails, assignedTaskPuh);

		if (compDtl != null) {
			nCLTTaskDTO.setRejectRemark(compDtl.getRejectRemark());
			nCLTTaskDTO.setApprove_status(compDtl.getApprove_status());
			nCLTTaskDTO.setComplanitId(compDtl.getComplanitId());
			nCLTTaskDTO.setComplanitEmail(compDtl.getComplanitEmail());
			nCLTTaskDTO.setComplanitName(compDtl.getComplanitName());
			nCLTTaskDTO.setComplaintMobile(compDtl.getComplaintMobile());
			nCLTTaskDTO.setComplanitdesignation(compDtl.getComplanitdesignation());
			nCLTTaskDTO.setComplaintPetinoner(compDtl.getComplaintPetinoner());
			nCLTTaskDTO.setIOName(compDtl.getIOName());
			nCLTTaskDTO.setComplaintPetinonerDate(compDtl.getComplaintPetinonerDate());
			nCLTTaskDTO.setDesigInvesOffi(compDtl.getDesigInvesOffi());

		} else {
			nCLTTaskDTO.setComplaintPetinoner(procasedetails.getCnrNumber());
			nCLTTaskDTO.setComplaintPetinonerDate(procasedetails.getFillingDate());

		}
		List<PairaviDetails> pairvidtl = pairaviDetailRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh);
		List<CouncilDetails> councildtl = councilDetailsRepo.findAllByProcourtdtl(procasedetails);
		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		ProCourtCaseDetails t = procasedetails;
		modelMap.addAttribute("personList", accusedList);
		List<AddAccused> addaccusedList = addAccusedRepo.findAllByAssignedTaskAndAccusedTypeNot(assignedTaskPuh,
				"Individual");

		modelMap.addAttribute("addaccusedList", addaccusedList);
		List<CaseCompany> companyList = caseCompanyRepo.findAllIbyAssignTask(assignedTaskPuh.getId());

		modelMap.addAttribute("companyList", companyList);

		modelMap.addAttribute("pairvidtl", pairvidtl);
		modelMap.addAttribute("councildtl", councildtl);
	}

	@PostMapping(value = "/addCriminalDtl", params = "saveHearingDetailsNCLT")
	public String saveHearingDetails(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") @Valid NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();

		Long hearingID = nCLTTaskDTO.getHearingEditId();
		HearingDetails HearingDetails;
		HearingDetails hd = new HearingDetails();
		hd.setBriefHD(nCLTTaskDTO.getBriefHD());
	
			ProMISValidator promisValid = new ProMISValidator();
			promisValid.isvalidBrifeHD("briefHD", nCLTTaskDTO.getBriefHD(), bindResult, "errmsg.briefHD", true);
			
		
		if (!nCLTTaskDTO.getAdditionalDoc().isEmpty()) {
			MultipartFile additionalDoc = nCLTTaskDTO.getAdditionalDoc();
			
			promisValid.isValidadditionalFile("additionalDoc", additionalDoc, bindResult);

		} 
		if (nCLTTaskDTO.getOfficerH() == null) {
			bindResult.rejectValue("officerH", "errmsg.required");
		}
		if (nCLTTaskDTO.getCounselNameH() == null) {
			bindResult.rejectValue("counselNameH", "errmsg.required");
		}
		Long id = (hearingdtlRepo.findMaxid() != null) ? (hearingdtlRepo.findMaxid() + 1) : 1;
		String id1 = String.valueOf(id);
		if (hearingID != null) {
			hd.setId(hearingID);
			id1 = String.valueOf(hearingID);
		}
		hd.setProcourtdtl(procasedetails);
		hd.setLastHearingDate(nCLTTaskDTO.getLastHearingDate());
		hd.setNextHearingDate(nCLTTaskDTO.getNextHearingDate());
		hd.setStatus(nCLTTaskDTO.getStatus());
		
	
		hd.setCounselName(nCLTTaskDTO.getCounselNameH());
		hd.setOfficer(nCLTTaskDTO.getOfficerH());
		
		hd.setAssignedTask(assignedTaskPuh);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		hd.setUser(userdet);
		hd.setCreatedDate(new Date());
		hd.setCourtType(courtTypeRepo.findById((long) 0).get());
		hd.setState(stateRepo.findById((long) 0).get());
		hd.setCity(districtRepo.findById((long) 0).get());
		Status status = nCLTTaskDTO.getStatus();
		Long statusID = status.getId();
		MultipartFile additionalDoc;
		String orignalfilenameAdditionalDoc;
		String fileExtorderadditionalDoc;
		String additionalDocFileName;
		CriminalTaskValidation crimnalValidation = new CriminalTaskValidation();
		
			crimnalValidation.HearingDetailsValidation1(nCLTTaskDTO, bindResult);
			
		
			if (bindResult.hasErrors()) {
				modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);

				return "Task/NCLTtaskPage";
			}

			HearingDetails currenthearingDtl = hearingdtlRepo.findByProcourtdtlAndCurrentStatus(procasedetails, true);

			if (currenthearingDtl != null) {
				currenthearingDtl.setCurrentStatus(false);

				hearingdtlRepo.save(currenthearingDtl);
			}
			if (!nCLTTaskDTO.getAdditionalDoc().isEmpty()) {
				additionalDoc = nCLTTaskDTO.getAdditionalDoc();

				orignalfilenameAdditionalDoc = nCLTTaskDTO.getAdditionalDoc().getOriginalFilename();

				fileExtorderadditionalDoc = orignalfilenameAdditionalDoc
						.substring(orignalfilenameAdditionalDoc.lastIndexOf("."));

				additionalDocFileName = "HearingAdditionalDoc" + id1 + fileExtorderadditionalDoc;

				hd.setAdditionalDocFileName(additionalDocFileName);

				caseFileUpload(additionalDoc, additionalDocFileName);

			}

			HearingDetails = hearingdtlRepo.save(hd);
		

		NCLTTaskDTO nCLTTaskDTO2 = new NCLTTaskDTO();

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO2);
		if (hearingID == null) {
			redirect.addFlashAttribute("message", "Save Hearing Details Successfully  :");

		
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
					utils.getMessage("log.login.hearingdetailsupdate") + " " + " and Investigation Number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

		}


		redirect.addAttribute("tabId", tabId);
		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);
		
		return "redirect:/proceedTaskNCLT";

		

	}
	@RequestMapping(value = "/addCriminalDtl", params = "editAccusaedStatusNCLT")
	public String editAccusaedStatus(@RequestParam(value = "editAccusaedStatusNCLT", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto1) {
		NCLTTaskDTO criminalTaskDto = new NCLTTaskDTO();
		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto1.getAssignedTask().getId()).get();

		AccusedStatus accused = AccusedStatusRepo.findById(id).get();
		criminalTaskDto.setCaseStatus(accused.getCaseStatus());
		// criminalTaskDto.setAddAccused(accused.getAddAccused());
		criminalTaskDto.setAccusedStatusID(accused.getId());
		criminalTaskDto.setAccusedStatusID2((long) 2);
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "deleteAccusaedStatusNCLT")
	public String deleteAccusaedStatus(@RequestParam(value = "deleteAccusaedStatusNCLT", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) {

		int tabId = 26;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		AccusedStatus accused = AccusedStatusRepo.findById(id).get();
		accused.setStatus(false);
		AccusedStatusRepo.save(accused);

		modelAttributeObject(assignedTaskPuh, model, tabId, new NCLTTaskDTO());
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "saveHearingDetailsNCLT1")
	public String saveHearingDetails1(ModelMap modelMap, @ModelAttribute @Valid NCLTTaskDTO criminalTaskDto,
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
			modelAttributeObject(assignTask, modelMap, tabId, new NCLTTaskDTO());
			modelMap.addAttribute("message", " Please Select other Respondent. : ");

			return "Task/NCLTtaskPage";

		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		accuse.setAssignedTask(assignTask);
		accuse.setCaseStatus(status);
		accuse.setCreatedDate(new Date());
		accuse.setCreatedBy(userdet);
		accuse.setUpdatedBy(userdet);
		accuse.setProcourtdtl(assignTask.getProCourtCaseDetails());
		accuse.setAddAccused(accused);
		if (id == null) {
			modelMap.addAttribute("message", " Respondent Status Added Successfully : ");

		} else {
			modelMap.addAttribute("message", " Respondent Status Updated Successfully : ");
		}

		AccusedStatusRepo.save(accuse);

		// criminalTaskDto.setAccusedStatusID2((long) 1);
		criminalTaskDto.setAccusedStatusID(null);
		modelAttributeObject(assignTask, modelMap, tabId, criminalTaskDto);

		return "Task/NCLTtaskPage";
	}

	@RequestMapping(value = "/savePerformaParty")
	public String savePerformaParty(ModelMap modelMap, @ModelAttribute("nCLTTaskDTO") @Valid NCLTTaskDTO nCLTTaskDTO,
			BindingResult bindResult) throws Exception {
		int tabId = 27;

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(nCLTTaskDTO.getAssignedTask().getId())
				.get();
		PerformaParty performaParty = new PerformaParty();
		ProMISValidator promisValid = new ProMISValidator();
		CriminalTaskValidation criminalTaskVal = new CriminalTaskValidation();
		if (nCLTTaskDTO.getTypeofOrder() == 1) {
			criminalTaskVal.companyPerformaPartyValidation1(nCLTTaskDTO, bindResult);
		} else {
			promisValid.isvalidPersonName("pPRespondentName", nCLTTaskDTO.getPPRespondentName(), bindResult,
					"errmsg.name", true);
			if (nCLTTaskDTO.getPPRespondentDesgination() == null) {

				bindResult.rejectValue("pPRespondentDesgination", "errmsg.required");

			}

		}
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		promisValid.isvalidAddress("pPAddress", nCLTTaskDTO.getPPAddress(), bindResult, "errmsg.name", true);
		if (bindResult.hasErrors()) {
			modelAttributeObject(assignedTaskPuh, modelMap, tabId, nCLTTaskDTO);
			return "Task/NCLTtaskPage";
		}
		if (nCLTTaskDTO.getTypeofOrder() == 1) {
			performaParty.setTypeofOrder(1);
			performaParty.setPPRespondentDesgination("");
		} else {
			performaParty.setTypeofOrder(2);
			performaParty.setPPRespondentDesgination(nCLTTaskDTO.getPPRespondentDesgination());
		}
		if (nCLTTaskDTO.getPerformaID() != null) {
			performaParty.setId(nCLTTaskDTO.getPerformaID());
		}
		performaParty.setAssignedTask(nCLTTaskDTO.getAssignedTask());
		performaParty.setProcourtdtl(nCLTTaskDTO.getProCourtDtl());
		performaParty.setCreatedBy(userdet);
		performaParty.setCreatedDate(new Date());
		performaParty.setPPCompany(nCLTTaskDTO.getPPCompany());
		performaParty.setPPCompCin(nCLTTaskDTO.getPPCompCin());
		performaParty.setPPRespondentName(nCLTTaskDTO.getPPRespondentName());
		performaParty.setPPAddress(nCLTTaskDTO.getPPAddress());
		performaPartyRepo.save(performaParty);

		if (nCLTTaskDTO.getPerformaID() == null) {

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.proformapartysave") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
			modelMap.addAttribute("message", " Performa Party Added Successfully  : ");
		} else {
			modelMap.addAttribute("message", " Performa Party updated Successfully  : ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.proformapartyupdate") + " " + " and Investigation Number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

		}

		modelMap.addAttribute("message", " Performa Party Added Successfully  : ");

		modelAttributeObject(assignedTaskPuh, modelMap, tabId, new NCLTTaskDTO());
		return "Task/NCLTtaskPage";

	}

	@RequestMapping(value = "/addCriminalDtl", params = "forwardtoapprovalHearingDetailsNCLT")
	// @RequestMapping(value ="/editPofficer", params = "editPairaviOfficer")
	public String forwardtoapprovalHearingDetails(

			@RequestParam(value = "forwardtoapprovalHearingDetailsNCLT", required = true) Long id, ModelMap model,
			NCLTTaskDTO criminalTaskDto) throws Exception {
		HearingDetails hearingDtl = hearingdtlRepo.findById(id).get();
		hearingDtl.setApproveStatus(2);
		hearingdtlRepo.save(hearingDtl);
		int tabId = 266;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
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

		model.addAttribute("message", "Hearing Details Confirmed Successfully  :");
		modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);
		return "Task/NCLTtaskPage";

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
