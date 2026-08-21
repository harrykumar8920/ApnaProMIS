
package com.pams.controllers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dto.GetDataFromSNMSDTO;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AddCase;
import com.pams.entity.AddCourt;
import com.pams.entity.AddState;
import com.pams.entity.AddSubSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseForwardHistory;
import com.pams.entity.CaseStatus;
import com.pams.entity.CourtCaseName;
import com.pams.entity.District;
import com.pams.entity.HearingDetails;
import com.pams.entity.HighCourtCaseDetails;
import com.pams.entity.InvCaseDetails;
import com.pams.entity.PairaviDetails;
import com.pams.entity.PetRespDetail;
import com.pams.entity.SfioAs;
import com.pams.entity.Type;
import com.pams.entity.TypeofBench;
import com.pams.entity.TypeofCase;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AccusedCompCaseDtlRepository;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AddCaseRepository;
import com.pams.service.AddSubSectionRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.CaseForwardHistoryRepository;
import com.pams.service.CaseStatusRepository;
import com.pams.service.ClauseRepository;
import com.pams.service.CourtCaseNameRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.InvCaseDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PetRespDetailRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.RestTemplateProvider;
import com.pams.service.ServiceLayerAPI;
import com.pams.service.SfioAsRepository;
import com.pams.service.StateRepository;
import com.pams.service.TypeRepository;
import com.pams.service.TypeofBenchRepository;
import com.pams.service.TypeofCaseRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.districtRepository;
import com.pams.utils.Utils;
import com.pams.validation.CourtCaseValidator;
import com.pams.validation.ProMISValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;

@Controller
public class courtCaseDtlController {
	private static final Logger logger = LoggerFactory.getLogger(courtCaseDtlController.class);
	@Value("${file.snmsapi}")
	public String snmsapi;
	@Value("${file.upload}")
	public String filePath;
	
	@Autowired
	private  CaseForwardHistoryRepository caseForwardHistoryRepository;

    
	@Autowired
	private CourtTypeRepository courtTypeRepo;
	@Autowired
	private ClauseRepository clauseRepo;

	@Autowired
	public CourtCaseNameRepository CourtCaseNameRepo;

	@Autowired
	private TypeofBenchRepository typeofBenchRepo;
	@Autowired
	private TypeofCaseRepository typeofCaseRepo;
	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private ActSecDetailsRepository actSecDetailsRepo;

	@Autowired
	private ServiceLayerAPI serviceLayerAPI;

	@Autowired
	RestTemplateProvider restTemplateProvider;
	@Autowired
	private AddCaseRepository addCaseRepo;

	@Autowired
	private SfioAsRepository sfioAsRepo;

	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	PetRespDetailRepository petRespDetailRepo;
	@Autowired
	private InvCaseDetailsRepository invCaseDtlRepo;
	@Autowired
	private districtRepository districtRepo;
	private InvCaseDetails invcaseDetails;
	@Autowired
	private AddAccusedRepository addAccusedRepo;

	@Autowired
	private AddActRepository addActRepo;

	@Autowired
	private AddActSecRepository addActSecRepo;

	@Autowired
	private PairaviDetailsRepository pairaviDetailRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private HearingDetailsRepository hearingdtlRepo;
	@Autowired
	private AccusedCompCaseDtlRepository accusedCompCaseDtlRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhAfterCOurtrepo;
	@Autowired
	private ActSecDetailsRepository actsecdtlsRepo;
	@Autowired
	private AddSubSectionRepository addSubSectionRepo;
	@Autowired
	private AuditBeanBo auditBeanBo;

	@Autowired
	private Utils utils;

	@Autowired
	private CaseStatusRepository caseStatusRepo;

	@RequestMapping(value = "/approvalAndRejectCourtCase", params = "initiateApprove")
	public String viewPendingTaskdtl(ModelMap modelMap,
			@RequestParam(value = "initiateApprove", required = true) Long id) throws Exception {
		/*
		 * AssignedTaskPuh assignedDtl = assignedTaskPuhRepo.findById(id).get(); if
		 * (assignedDtl.getIsApproved() == false) { assignedDtl.setIsApproved(true);
		 * assignedTaskPuhRepo.save(assignedDtl); }
		 */
		ProCourtCaseDetails pcrtdtls = proCourtCaseDetailsRepo.findById(id).get();
		modelMap.addAttribute("assignedDtl", new AssignedTaskPuh());
		modelMap.addAttribute("pcrtdtls", pcrtdtls);
		return "caseDetails/viewPendingTaskdtl";
	}

	@RequestMapping(value = "/viewPendingTaskdtl1", params = "forwardToPuh")
	public String forwardToPuh(ModelMap modelMap, @RequestParam(value = "forwardToPuh", required = true) Long id,
			RedirectAttributes redirect) throws Exception {

		ProCourtCaseDetails procourtcaseDtls = proCourtCaseDetailsRepo.findById(id).get();

		procourtcaseDtls.setApproveStatus(1);
		procourtcaseDtls.setCasePosition(1);
		proCourtCaseDetailsRepo.save(procourtcaseDtls);

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.addcourtcasedetailforwarded") + " "
						+ procourtcaseDtls.getAssignedTask().getUser().getSalutation() + " "
						+ procourtcaseDtls.getAssignedTask().getUser().getFirstName() + " "
						+ (procourtcaseDtls.getAssignedTask().getUser().getMiddleName().equals("") ? ""
								: procourtcaseDtls.getAssignedTask().getUser().getMiddleName() + "")
						+ procourtcaseDtls.getAssignedTask().getUser().getLastName() + " "
						+ " and investigation number is "
						+ procourtcaseDtls.getAssignedTask().getAddCase().getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", procourtcaseDtls.getAssignedTask().getAddCase().getId());
		auditBeanBo.save();
		redirect.addFlashAttribute("message", "court Case Details had been forwarded");
		return "redirect:/totalNumberOfCourtCases";
	}


	
	@RequestMapping(value = "/viewPendingTaskdtl1", params = "editProCourtCase")
	public String editProCourtCase(ModelMap modelMap,
			@RequestParam(value = "editProCourtCase", required = true) Long id, RedirectAttributes redirect)
			throws Exception {
		ProCourtCaseDetails proCourtCaseDetails = proCourtCaseDetailsRepo.findById(id).get();

		proCourtCaseDetails.setViewFile(true);
		String courtcaseno = proCourtCaseDetails.getCourtCaseNo();
		if (proCourtCaseDetails.getId() != null) {

			if(courtcaseno!=null) {
			String[] courtCaseNo = courtcaseno.split("/");
			proCourtCaseDetails.setCourtCaseNo1(courtCaseNo[0]);
			proCourtCaseDetails.setCourtCaseNo2(courtCaseNo[1]);
			proCourtCaseDetails.setCourtCaseNo3(courtCaseNo[2]);
			}
		}
		List<TypeofBench> list = null;
		if (proCourtCaseDetails.getCourtType().getId() == 4L) {

			list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

		} else if (proCourtCaseDetails.getCourtType().getId() == 3L) {
			list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
		} else {
			list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
		}
		/*
		 * modelMap.addAttribute("subseclst",
		 * addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
		 * Sort.by(Sort.Direction.ASC, "id")));
		 * 
		 * modelMap.addAttribute("clauselist",
		 * clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		 */
		modelMap.addAttribute("typeOfBench", list);

		proCourtCaseDetails.setCaseId(proCourtCaseDetails.getAssignedTask().getAddCase().getInvestigationOrderNo());
		proCourtCaseDetails.setCaseTitle(proCourtCaseDetails.getAssignedTask().getAddCase().getCaseTitle());
		proCourtCaseDetails.setAssignedTaskID(proCourtCaseDetails.getAssignedTask().getId());
		String proSectionOrderNumber = proCourtCaseDetails.getAssignedTask().getAddCase().getProSectionOrderNumber();
		Date proSanctionDate = proCourtCaseDetails.getAssignedTask().getAddCase().getProSanctionDate();
		String petionerName = proCourtCaseDetails.getAssignedTask().getAddCase().getPetionerName();
		AddCourt courtType2 = proCourtCaseDetails.getCourtType();
		AddState state = proCourtCaseDetails.getState();
		District city = proCourtCaseDetails.getCity();
		int typeofOrder = proCourtCaseDetails.getAssignedTask().getAddCase().getTypeofOrder();
		proCourtCaseDetails.setTypeofOrder(typeofOrder);
		proCourtCaseDetails.setProSanctionDate(proSanctionDate);
		proCourtCaseDetails.setProSectionOrderNumber(proSectionOrderNumber);
		proCourtCaseDetails.setPetionerName(petionerName);
		proCourtCaseDetails.setCourtType(courtType2);
		proCourtCaseDetails.setState(state);
		proCourtCaseDetails.setCity(city);

		List<CourtCaseName> courtcaseName;

		if (proCourtCaseDetails.getAssignedTask().getCreateTask().getId() == 18) {
			proCourtCaseDetails.setCourtTypeC("NCLT");
		} else {
			proCourtCaseDetails.setCourtTypeC("other");
		}
		courtcaseName = CourtCaseNameRepo.findByTypeCase("Criminal");

		modelMap.addAttribute("courtCaseList", courtcaseName);
		List<Type> typeList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("typeList", typeList);
		List<TypeofCase> typeofcase = typeofCaseRepo.findAllByIdNot((long) 0);
		// Collections.sort(typeofcase);
		modelMap.addAttribute("typeofcase", typeofcase);
		List<AddCourt> courtType = courtTypeRepo.findByCourtNameNotContainingAndCourtType("National", 1,
				Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("subseclst", addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
				Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));

		// List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<AddState> statelist = stateRepo.findAll();
		List<SfioAs> sfiolst = sfioAsRepo.findAll();
		List<ActSecDetailsInfo> seclist = actSecDetailsRepo
				.findByAssignedTaskAndIsActive(proCourtCaseDetails.getAssignedTask(), 1);
		modelMap.addAttribute("seclist", seclist);
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("addActList", addactlist);
		List<District> districtlist = districtRepo.findAllByState(proCourtCaseDetails.getState());
		proCourtCaseDetails.setCity(proCourtCaseDetails.getCity());
		/* modelMap.addAttribute("courtType", courtType); */
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		modelMap.addAttribute("sfiolst", sfiolst);
		proCourtCaseDetails.setProgistFile(proCourtCaseDetails.getProgistFile());
		modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);
		return "Prosecutor/AddCourtDeatil";
	}
	

	@RequestMapping(value = "/addCourtCaseDtls", params = "addCourtDtls")
	public String AddNewCase(ModelMap modelMap, @RequestParam(value = "addCourtDtls", required = true) Long id)
			throws Exception {
		ProCourtCaseDetails proCourtCaseDetails = new ProCourtCaseDetails();
		proCourtCaseDetails.setAssignedTaskID(id);
		AssignedTaskPuh assigntask = assignedTaskPuhRepo.findById(id).get();
		
		List<CourtCaseName> courtcaseName = null;
		if (assigntask.getCreateTask().getId() == 18) {
			proCourtCaseDetails.setCourtTypeC("NCLT");
			courtcaseName = CourtCaseNameRepo.findByTypeCase("NCLT");
			proCourtCaseDetails.setType(typeRepository.findById(1l).get());
		} else if (assigntask.getCreateTask().getId() == 19) {
			courtcaseName = CourtCaseNameRepo.findByTypeCase("Criminal");
			proCourtCaseDetails.setType(typeRepository.findById(2l).get());
			proCourtCaseDetails.setCourtTypeC("other");
		} else {
			proCourtCaseDetails.setType(null);
			proCourtCaseDetails.setCourtTypeC("other");
		}

		
		List<AddCourt> courtType = courtTypeRepo.findByCourtNameNotContainingAndCourtType("National", 1,
				Sort.by(Sort.Direction.ASC, "id"));
		
		AddCourt courtType2 = assigntask.getAddCase().getCourtType();
		List<TypeofBench> list = null;
		if (courtType2.getId() == 4L) {

			list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

		} else if (courtType2.getId() == 3L) {
			list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
		} else {
			list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
		}
		modelMap.addAttribute("subseclst", addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
				Sort.by(Sort.Direction.ASC, "id")));

		modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("typeOfBench", list);

		List<AddState> statelist = stateRepo.findAll();
		List<District> districtlist = districtRepo.findAll();
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("districtlist", districtlist);
		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findByAssignedTaskAndIsActive(assigntask, 1);
		proCourtCaseDetails.setCaseTitle(assigntask.getAddCase().getCaseTitle());
		String proSectionOrderNumber = assigntask.getAddCase().getProSectionOrderNumber();
		Date proSanctionDate = assigntask.getAddCase().getProSanctionDate();
		String petionerName = assigntask.getAddCase().getPetionerName();

		AddState state = assigntask.getAddCase().getState();
		District city = assigntask.getAddCase().getCity();
		int typeofOrder = assigntask.getAddCase().getTypeofOrder();
		proCourtCaseDetails.setTypeofOrder(typeofOrder);
		proCourtCaseDetails.setProSanctionDate(proSanctionDate);
		proCourtCaseDetails.setProSectionOrderNumber(proSectionOrderNumber);
		proCourtCaseDetails.setPetionerName(petionerName);
		proCourtCaseDetails.setBench_Name(assigntask.getAddCase().getBenchName());
		proCourtCaseDetails.setCourtType(courtType2);
		proCourtCaseDetails.setCaseId(assigntask.getAddCase().getInvestigationOrderNo());
		proCourtCaseDetails.setState(state);
		proCourtCaseDetails.setCity(city);
		modelMap.addAttribute("courtCaseList", courtcaseName);
		List<TypeofCase> typeofcase = typeofCaseRepo.findAllByIdNot((long) 0);
		Collections.sort(typeofcase);
		List<Type> typeList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("seclist", seclist);
		modelMap.addAttribute("addActList", addactlist);
		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("typeofcase", typeofcase);
		modelMap.addAttribute("typeList", typeList);
		
		
		if(assigntask.getCreateTask().getId()==15) {
			return "Prosecutor/AddCourtDeatilSuprime";
		}
		if(assigntask.getCreateTask().getId()==16) {
			HighCourtCaseDetails hc=new HighCourtCaseDetails();
			hc.setAssignedTaskIda(id);
			
			hc.setProSanctionDate(proSanctionDate);
			hc.setProSectionOrderNumber(proSectionOrderNumber);
			hc.setPetionerName(petionerName);
			
			hc.setState(state);
			hc.setCaseTitle(assigntask.getAddCase().getCaseTitle());
			hc.setCity(city);
			hc.setAddCaseIda(assigntask.getAddCase().getId());
			modelMap.addAttribute("proCourtCaseDetails", hc);
			return "Prosecutor/AddCourtDeatilHigh";
		}
		
		modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);
		return "Prosecutor/AddCourtDeatil";
	}

	@RequestMapping(value = "/addNewLegacyCase")
	public String addNewLegacyCase(ModelMap modelMap) throws Exception {
		System.out.println("inside");
		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<AddState> statelist = stateRepo.findAll();
		List<District> districtlist = districtRepo.findAll();
		
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		Long id = (proCourtCaseDetailsRepo.findMaxid() != null) ? (proCourtCaseDetailsRepo.findMaxid() + 1) : 1;
		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive(
				(long) 0, useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName()), 0, 1);
		ProCourtCaseDetails proCourtCaseDetails = new ProCourtCaseDetails();
		proCourtCaseDetails.setCaseType(0);
		modelMap.addAttribute("seclist", seclist);
		modelMap.addAttribute("addActList", addactlist);
		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);
		return "Prosecutor/AddCourtDeatil";
	}

	public static Date last7days() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.DAY_OF_MONTH, -7);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		Date todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(currentdate2);
		return todate;

	}
	/*
	 * @RequestMapping(value = "/userHome") public String addDesignation(Model
	 * modelMap, HttpServletRequest req) throws Exception { UserDetails user =
	 * userDetailsService.getUserDetailssss(); List<AssignedTaskPuh> TotalTask =
	 * assignedTaskPuhRepo.findAllByUser(user); List<AssignedTaskPuh> PendingTask =
	 * assignedTaskPuhRepo.findAllByUserAndIsApprovedAndApprovalStatus(user, false,
	 * 0);
	 * 
	 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 * String date = simpleDateFormat.format(new Date()); String attribute =
	 * (String) req.getSession().getAttribute("userfname"); ReportController rc =
	 * new ReportController(); Date nextdate = rc.next7days();
	 * 
	 * Date fromDate = new SimpleDateFormat("yyyy-MM-dd",
	 * Locale.ENGLISH).parse(date);
	 * 
	 * List<HearingDetails> totaltodaycase =
	 * hearingdtlRepo.findByNextHearingDateBetweenAndUser(fromDate, nextdate,user);
	 * 
	 * modelMap.addAttribute("totaltodaycase", totaltodaycase.size());
	 * 
	 * Date nextdate1 = last7days();
	 * 
	 * List<HearingDetails> hearingdata =
	 * hearingdtlRepo.findByNextHearingDateBetweenAndUser( nextdate1,fromDate,user);
	 * 
	 * 
	 * modelMap.addAttribute("totaltodaycase1", hearingdata.size());
	 * 
	 * List<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtrepo
	 * .findAllIfByApproveStatusIsOne(user.getId());
	 * modelMap.addAttribute("TotalTask", TotalTask.size());
	 * modelMap.addAttribute("ApprovedTask", ApprovedTask.size());
	 * modelMap.addAttribute("PendingTask", PendingTask.size()); UserDetails userdet
	 * =
	 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
	 * )); List<ProCourtCaseDetails> TotalCase =
	 * proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
	 * Sort.by(Sort.Direction.DESC, "id")); List<ProCourtCaseDetails>
	 * totalforwardcase = proCourtCaseDetailsRepo
	 * .findALLByApproveStatusBetweenAndCreatedBy(1, 2, userdet);
	 * List<ProCourtCaseDetails> totalsendbackcase =
	 * proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(3, userdet);
	 * modelMap.addAttribute("TotalCase", TotalCase.size());
	 * modelMap.addAttribute("totalforwardcase", totalforwardcase.size());
	 * modelMap.addAttribute("totalsendbackcase", totalsendbackcase.size());
	 * List<AddCase> totalforwardcase1 =
	 * addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet, 1,
	 * Sort.by(Sort.Direction.DESC, "id")); List<AddCase> totalsendbackcase1 =
	 * addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet, 3,
	 * Sort.by(Sort.Direction.DESC, "id")); List<AddCase> caseList = (List<AddCase>)
	 * addCaseRepo.findAll(); modelMap.addAttribute("TotalCase1", caseList.size());
	 * modelMap.addAttribute("totalforwardcase1", totalforwardcase1.size());
	 * modelMap.addAttribute("totalsendbackcase1", totalsendbackcase1.size());
	 * 
	 * List<AddCase> totalapprovedsection =
	 * addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet, 2,
	 * Sort.by(Sort.Direction.ASC,"id"));
	 * modelMap.addAttribute("totalapprovedsection", totalapprovedsection.size());
	 * 
	 * return "IOOfficer/officerHome"; }
	 */

	@RequestMapping(value = "/userHome")
	public String addDesignation(Model modelMap, HttpServletRequest req) throws Exception {
	    Boolean showPopup = (Boolean) req.getSession().getAttribute("showFirstLoginPopup");
	    if (showPopup != null && showPopup) {
	        modelMap.addAttribute("showFirstLoginPopup", true);
	        req.getSession().removeAttribute("showFirstLoginPopup");  
	    } else {
	        modelMap.addAttribute("showFirstLoginPopup", false);
	    }
		UserDetails user = userDetailsService.getUserDetailssss();
		List<AssignedTaskPuh> TotalTask = assignedTaskPuhRepo.findAllByUser(user);
		List<AssignedTaskPuh> PendingTask = assignedTaskPuhRepo.findAllByUserAndIsApprovedAndApprovalStatus(user, false,
				0);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		String attribute = (String) req.getSession().getAttribute("userfname");
		ReportController rc = new ReportController();
		Date nextdate = rc.next7days();

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);	

		List<HearingDetails> totaltodaycase = hearingdtlRepo.findByNextHearingDateBetweenAndUser(fromDate, nextdate,user);	
		
		modelMap.addAttribute("totaltodaycase", totaltodaycase.size());
		
		Date nextdate1 = last7days();

		List<HearingDetails> hearingdata = hearingdtlRepo.findByNextHearingDateBetweenAndUser( nextdate1,fromDate,user);
		
		
		modelMap.addAttribute("totaltodaycase1", hearingdata.size());
		
		List<AssignedTaskPuhAfterCOurt> ApprovedTask = assignedTaskPuhAfterCOurtrepo
				.findAllIfByApproveStatusIsOne(user.getId());
		modelMap.addAttribute("TotalTask", TotalTask.size());
		modelMap.addAttribute("ApprovedTask", ApprovedTask.size());
		modelMap.addAttribute("PendingTask", PendingTask.size());
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		List<ProCourtCaseDetails> TotalCase = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
				Sort.by(Sort.Direction.DESC, "id"));
		List<ProCourtCaseDetails> totalforwardcase = proCourtCaseDetailsRepo
				.findALLByApproveStatusBetweenAndCreatedBy(1, 2, userdet);
		List<ProCourtCaseDetails> totalsendbackcase = proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(3,
				userdet);
		modelMap.addAttribute("TotalCase", TotalCase.size());
		modelMap.addAttribute("totalforwardcase", totalforwardcase.size());
		modelMap.addAttribute("totalsendbackcase", totalsendbackcase.size());
		List<AddCase> totalforwardcase1 = addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet, 1,
				Sort.by(Sort.Direction.DESC, "id"));
		List<AddCase> totalsendbackcase1 = addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet, 3,
				Sort.by(Sort.Direction.DESC, "id"));
		List<AddCase> caseList = (List<AddCase>) addCaseRepo.findAll();
		modelMap.addAttribute("TotalCase1", caseList.size());
		modelMap.addAttribute("totalforwardcase1", totalforwardcase1.size());
		modelMap.addAttribute("totalsendbackcase1", totalsendbackcase1.size());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);	
		
		//List<HearingDetails> byUserAndLessThanNextHearingDate = hearingdtlRepo.findByUserAndNextHearingDateLessThanOrderByNextHearingDateDesc( userdet,oldcurrent);
		
		List<HearingDetails> byUserAndLessThanNextHearingDate = 
		        hearingdtlRepo.findLatestHearingPerCase(userdet.getId(), oldcurrent);

		// Group by ProCourtCaseDetails
		Map<ProCourtCaseDetails, List<HearingDetails>> groupedByCourtCase = 
		        byUserAndLessThanNextHearingDate.stream()
		                .collect(Collectors.groupingBy(HearingDetails::getProcourtdtl));

		modelMap.addAttribute("reportList", byUserAndLessThanNextHearingDate);   // sorted flat list, agar chahiye
		modelMap.addAttribute("groupedReportList", groupedByCourtCase);          // grouped map
		modelMap.addAttribute("reportList", byUserAndLessThanNextHearingDate);
		List<AddCase> totalapprovedsection = addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet, 2, Sort.by(Sort.Direction.ASC,"id"));
		modelMap.addAttribute("totalapprovedsection", totalapprovedsection.size());
		
		return "IOOfficer/officerHome";
	}
	
	@RequestMapping(value = "/petitionerCases")
	public String petitionerCases(ModelMap modelMap) {
		List<ProCourtCaseDetails> petioner = proCourtCaseDetailsRepo.findALLBySfioAs("Petitioner");
		modelMap.addAttribute("caseType", "Petitioner Court Case Details ");
		modelMap.addAttribute("courtlist", petioner);
		return "IOOfficer/CourtCaselst";
	}

	@RequestMapping(value = "/RespondentCases")
	public String RespondentCases(ModelMap modelMap) {
		List<ProCourtCaseDetails> respondent = proCourtCaseDetailsRepo.findALLBySfioAs("Respondent");
		modelMap.addAttribute("caseType", "Respondent Court Case Details ");
		modelMap.addAttribute("courtlist", respondent);
		return "IOOfficer/CourtCaselst";
	}

	@RequestMapping(value = "/appellantCases")
	public String appellantCases(ModelMap modelMap) {
		List<ProCourtCaseDetails> Appellant = proCourtCaseDetailsRepo.findALLBySfioAs("Appellant");
		modelMap.addAttribute("caseType", "Appellant Court Case Details ");
		modelMap.addAttribute("courtlist", Appellant);
		return "IOOfficer/CourtCaselst";
	}

	@RequestMapping(value = "/UnionOfIndiaCases")
	public String UnionOfIndiaCases(ModelMap modelMap) {
		List<ProCourtCaseDetails> unionOfIndia = proCourtCaseDetailsRepo
				.findALLBySfioAs("On the Behalf of Union of India");
		modelMap.addAttribute("caseType", "On the Behalf of Union of India Court Case Details ");
		modelMap.addAttribute("courtlist", unionOfIndia);
		return "IOOfficer/CourtCaselst";
	}

	// When Click on "ADD ADDITIONAL INFORMATION" Button
	@RequestMapping(value = "AdditionalInfo", params = "addInfo")
	public String AdditionalInfoNew(ModelMap modelMap, @RequestParam(value = "addInfo", required = true) Long courtId) {

		ProCourtCaseDetails procourtdt = proCourtCaseDetailsRepo.findALLById(courtId);

		List<PetRespDetail> petdtl = petRespDetailRepo.findAllByProcourtdtl(procourtdt);
		List<PairaviDetails> pairavidtl = pairaviDetailRepo.findAllByProcourtdtl(procourtdt);
		List<AddAccused> accdtl = addAccusedRepo.findAllByProcourtdtl(procourtdt);
		List<CaseStatus> caseStatuslst = caseStatusRepo.findAllByProcourtdtl(procourtdt);
		List<HearingDetails> hearingdtls1 = hearingdtlRepo.findAll();
		// List<ActSecDetailsInfo> actsecdtlsList =
		// actsecdtlsRepo.findAllByProcourtdtl(procourtdt);

		// List<ActSecDetailsInfo> actsecdtlsList =
		// actsecdtlsRepo.findAllByProcourtdtl(procourtdt);

		// modelMap.addAttribute("actsecdtls", actsecdtlsList);

		modelMap.addAttribute("hearingDtls1", hearingdtls1);
		modelMap.addAttribute("petdtl", petdtl);
		modelMap.addAttribute("pairavidtl", pairavidtl);
		modelMap.addAttribute("procourtdt", procourtdt);
		modelMap.addAttribute("accdtl", accdtl);
		modelMap.addAttribute("caseStatuslst", caseStatuslst);
		return "IOOfficer/AdditionalInfoNew";
	}

	// For Back Button
	@RequestMapping(value = "AdditionalInfo")
	public String AdditionalInfo(ModelMap modelMap, @RequestParam(value = "courtId", required = true) Long courtId) {

		ProCourtCaseDetails procourtdt = proCourtCaseDetailsRepo.findALLById(courtId);
		List<PetRespDetail> petdtl = petRespDetailRepo.findAllByProcourtdtl(procourtdt);
		List<PairaviDetails> pairavidtl = pairaviDetailRepo.findAllByProcourtdtl(procourtdt);
		List<AddAccused> accdtl = addAccusedRepo.findAllByProcourtdtl(procourtdt);
		// List<HearingDetails> hearingdtls1 = hearingdtlRepo.findAll();
		// List<ActSecDetailsInfo> actsecdtls =
		// actsecdtlsRepo.findAllByProcourtdtl(procourtdt);

		// modelMap.addAttribute("actsecdtls", actsecdtls);

		// modelMap.addAttribute("hearingDtls1", hearingdtls1);
		modelMap.addAttribute("petdtl", petdtl);
		modelMap.addAttribute("pairavidtl", pairavidtl);
		modelMap.addAttribute("procourtdt", procourtdt);
		modelMap.addAttribute("accdtl", accdtl);

		return "IOOfficer/AdditionalInfoNew";
	}

	// When click on the "Added Cases" Sidebar button
	@GetMapping("getCaseList")
	public String getCaseList(ModelMap modelMap, Long courtId) throws Exception {

		ProCourtCaseDetails courtdtl = proCourtCaseDetailsRepo.findALLById(courtId);
		modelMap.addAttribute("courtdtl", courtdtl);

		HearingDetails hearingDetails = new HearingDetails();
		modelMap.addAttribute("hearingDetails", hearingDetails);

		List<HearingDetails> hearingdtls1 = hearingdtlRepo.findAll();
		modelMap.addAttribute("hearingDtls1", hearingdtls1);

		List<ProCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		modelMap.addAttribute("courtCasedtl", courtCasedtl);

		return "IOOfficer/assignedCase";
	}

	@RequestMapping(value = "/resetproCourtCaseDetails")
	public String resetproCourtCaseDetails(ModelMap modelMap,
			@ModelAttribute("proCourtCaseDetails") ProCourtCaseDetails proCourtCaseDetails, BindingResult bindResult)
			throws Exception {
		AssignedTaskPuh assigntask = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID()).get();
		String proSectionOrderNumber = assigntask.getAddCase().getProSectionOrderNumber();
		Date proSanctionDate = assigntask.getAddCase().getProSanctionDate();
		String petionerName = assigntask.getAddCase().getPetionerName();
		AddCourt courtType2 = assigntask.getAddCase().getCourtType();
		AddState state = assigntask.getAddCase().getState();
		District city = assigntask.getAddCase().getCity();
		int typeofOrder = assigntask.getAddCase().getTypeofOrder();
		List<ActSecDetailsInfo> actAndSection = actSecDetailsRepo
				.findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive((long) 0,
						useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName()),
						proCourtCaseDetails.getCaseType(), 1);
		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findByAssignedTaskAndIsActive(assigntask, 1);
		for (ActSecDetailsInfo actSecDetailsInfo : seclist) {
			ActSecDetailsInfo actAnsSectionDtls = actSecDetailsRepo.findById(actSecDetailsInfo.getId()).get();
			actAnsSectionDtls.setIsActive(0);
			actSecDetailsRepo.save(actAnsSectionDtls);

		}
		List<AddCourt> courtType;
		List<CourtCaseName> courtcaseName;

		if (proCourtCaseDetails.getTypeOfCase().getTypeOfCase().equals("NCLT/NCLAT")) {
			courtType = courtTypeRepo.findByCourtNameStartsWithOrCourtNameStartsWithAndCourtType("National", "Select",
					1, Sort.by(Sort.Direction.ASC, "id"));
			proCourtCaseDetails.setCourtTypeC("NCLT");
			courtcaseName = CourtCaseNameRepo.findByTypeCase("NCLT");
		} else {
			courtType = courtTypeRepo.findByCourtNameNotContainingAndCourtType("National", 1,
					Sort.by(Sort.Direction.ASC, "id"));
			proCourtCaseDetails.setCourtTypeC("other");
			courtcaseName = CourtCaseNameRepo.findByTypeCase("Criminal");
		}
		courtType = courtTypeRepo.findByCourtNameNotContainingAndCourtType("National", 1,
				Sort.by(Sort.Direction.ASC, "id"));
		proCourtCaseDetails.setCourtTypeC("other");
		courtcaseName = CourtCaseNameRepo.findByTypeCase("Criminal");
		modelMap.addAttribute("courtCaseList", courtcaseName);
		// List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<AddState> statelist = stateRepo.findAll();
		List<District> districtlist = districtRepo.findAll();
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<ActSecDetailsInfo> seclist1 = actSecDetailsRepo.findByAssignedTaskAndIsActive(assigntask, 1);
		modelMap.addAttribute("seclist", seclist1);
		modelMap.addAttribute("addActList", addactlist);
		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("statelist", statelist);
		com.pams.entity.ProCourtCaseDetails courtcaseDtls = new ProCourtCaseDetails();
		courtcaseDtls.setTypeofOrder(typeofOrder);
		courtcaseDtls.setProSanctionDate(proSanctionDate);
		courtcaseDtls.setProSectionOrderNumber(proSectionOrderNumber);
		courtcaseDtls.setPetionerName(petionerName);
		courtcaseDtls.setCourtType(courtType2);
		courtcaseDtls.setState(state);
		courtcaseDtls.setCity(city);
		courtcaseDtls.setAssignedTaskID(assigntask.getId());
		courtcaseDtls.setCaseType(proCourtCaseDetails.getCaseType());
		courtcaseDtls.setCaseTitle(proCourtCaseDetails.getCaseTitle());
		// courtcaseDtls.setProgistFile(proCourtCaseDetails.getProgistFile());
		courtcaseDtls.setProSectionOrderNumber(proCourtCaseDetails.getProSectionOrderNumber());
		courtcaseDtls.setProSanctionDate(proCourtCaseDetails.getProSanctionDate());
		modelMap.addAttribute("proCourtCaseDetails", courtcaseDtls);
		return "Prosecutor/AddCourtDeatil";
	}

	
	@RequestMapping(value = "/SaveHighCourtCaseDetails")
	public String addSaveHighCourtCaseDetails(ModelMap modelMap,
			@ModelAttribute("proCourtCaseDetails") HighCourtCaseDetails proCourtCaseDetails, BindingResult bindResult,
			RedirectAttributes redirect) throws Exception {
		
		
		System.out.println(proCourtCaseDetails);
		
		
		return "redirect:/totalNumberOfCourtCases";
	}
	
	
	
	@RequestMapping(value = "/SaveproCourtCaseDetails")
	public String addCourtOrderdtl(ModelMap modelMap,
			@ModelAttribute("proCourtCaseDetails") ProCourtCaseDetails proCourtCaseDetails, BindingResult bindResult,
			RedirectAttributes redirect) throws Exception {
		InvCaseDetails invCaseDetailBhakua = proCourtCaseDetails.getInvCaseDetail();
		Long tempID = proCourtCaseDetails.getId();
		courtCaseDtlController cortcont = new courtCaseDtlController();
		AssignedTaskPuh assigntask = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID()).get();
		Date fillingDate = proCourtCaseDetails.getFillingDate();
		if (fillingDate != null && !fillingDate.equals("")) {
			int year = cortcont.getYearFromDate(fillingDate);
			proCourtCaseDetails.setFinancialYear(+year + "-" + (year + 1));
		}
		if(proCourtCaseDetails.getCourtTypeC().equals("NCLT")) {
			AddCourt orElseThrow = courtTypeRepo.findById(0l).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:"));
			proCourtCaseDetails.setCourtType(orElseThrow);
		}else {
			if (proCourtCaseDetails.getState().getId() == 0L && (proCourtCaseDetails.getCourtType().getId() != 3L
					&& proCourtCaseDetails.getCourtType().getId() != 4l)) {
				bindResult.rejectValue("state", "msg.wrongId");
			}if (proCourtCaseDetails.getCourtType().getId() != 3L && proCourtCaseDetails.getCourtType().getId() != 4L) {

				proCourtCaseDetails.setBench_Name(typeofBenchRepo.findById((long) 0).get());
				if (proCourtCaseDetails.getCity().getId() == 0L) {
					bindResult.rejectValue("city", "msg.wrongId");
				}

			}if (proCourtCaseDetails.getCourtType() == null || proCourtCaseDetails.getCourtType().getId() == 0) {
				bindResult.rejectValue("courtType", "msg.wrongId");
			}if ((proCourtCaseDetails.getCourtType().getId() == 3 || proCourtCaseDetails.getCourtType().getId() == 4)
					&& proCourtCaseDetails.getCnrNumber().trim().equals("")) {
				String regex = "[A-Za-z0-9]{2,30}";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(proCourtCaseDetails.getCnrNumber());
				if (!m.matches()) {
					bindResult.rejectValue("cnrNumber", "msg.cnrNumber");
				}
			}
		}

		

		String cnrNumber = proCourtCaseDetails.getCnrNumber();

		if (!(proCourtCaseDetails.getCnrNumber().trim().equals("")) && (proCourtCaseDetails.getCourtType().getId() != 3
				|| proCourtCaseDetails.getCourtType().getId() != 4)) {
			String regex = "^[a-zA-Z0-9\\s-_/.]{0,30}$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(proCourtCaseDetails.getCnrNumber());
			if (!m.matches()) {
				bindResult.rejectValue("cnrNumber", "msg.cnrNumber");
			}
		}

		CourtCaseValidator courtCasevalid = new CourtCaseValidator();
		String courtcaseno = "";
		if (!proCourtCaseDetails.getCourtCaseNo1().equals("0")) {

			String regex = "[A-Z0-9a-z]{1,7}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(proCourtCaseDetails.getCourtCaseNo2());
			if (!m.matches()) {
				bindResult.rejectValue("courtCaseNo2", "msg.courtCaseNo2");
			}

			String regex1 = "[0-9]{4}";
			Pattern p1 = Pattern.compile(regex1);
			Matcher m1 = p1.matcher(proCourtCaseDetails.getCourtCaseNo3());
			if (!m1.matches()) {
				bindResult.rejectValue("courtCaseNo3", "msg.courtCaseNo3");
			}

		}
		if (proCourtCaseDetails.getType() != null || proCourtCaseDetails.getType().getId() != 0) {
			System.out.println("check heare of type : - " + proCourtCaseDetails.getType());
		} else {

		}
		if (proCourtCaseDetails.getTypeOfCase() != null || proCourtCaseDetails.getTypeOfCase().getId() != 0) {
			System.out.println("check heare of type : - " + proCourtCaseDetails.getTypeOfCase());
		} else {

		}

		if ((proCourtCaseDetails.getCourtCaseNo1() != null && !proCourtCaseDetails.getCourtCaseNo1().equals(""))
				&& (proCourtCaseDetails.getCourtCaseNo2() != null && !proCourtCaseDetails.getCourtCaseNo2().equals(""))
				&& (proCourtCaseDetails.getCourtCaseNo3() != null
						&& !proCourtCaseDetails.getCourtCaseNo3().equals(""))) {
			courtcaseno = proCourtCaseDetails.getCourtCaseNo1() + "/" + proCourtCaseDetails.getCourtCaseNo2() + "/"
					+ proCourtCaseDetails.getCourtCaseNo3();

			proCourtCaseDetails.setCourtCaseNo(courtcaseno);
		}
		if (proCourtCaseDetails.getType() == null || proCourtCaseDetails.getType().getId() == 0) {
			bindResult.rejectValue("type", "msg.type");
		}
		if (proCourtCaseDetails.getTypeOfCase() == null || proCourtCaseDetails.getTypeOfCase().getId() == 0) {
			bindResult.rejectValue("typeOfCase", "msg.typeOfCase");
		}
		if (proCourtCaseDetails.getBrief().split(" ").length > 401) {
			bindResult.rejectValue("brief", "msg.brief");
		}

		if (proCourtCaseDetails.getBackgroundofcase().split(" ").length > 401) {
			bindResult.rejectValue("backgroundofcase", "msg.brief");
		}

		

		if (proCourtCaseDetails.getIsMCAParty() == null) {
			bindResult.rejectValue("isMCAParty", "msg.wrongId");
		}

		if (proCourtCaseDetails.getIsWhetherreplyfiled() == null) {
			bindResult.rejectValue("whetherreplyfiled", "msg.wrongId");
		}
		if (proCourtCaseDetails.getSfioAs() == null) {
			bindResult.rejectValue("sfioAs", "msg.wrongId");
		}
		
		if (proCourtCaseDetails.getBench_Name() != null) {
			if (proCourtCaseDetails.getBench_Name().getBench().equals("Choose Bench")
					&& (proCourtCaseDetails.getCourtType().getId() == 3L
							|| proCourtCaseDetails.getCourtType().getId() == 4L)) {
				bindResult.rejectValue("bench_Name", "errmsg.required");
			}
		}
		

		// By keyraj Sharma

		/*
		 * if((proCourtCaseDetails.getProgistFile().isEmpty() ||
		 * proCourtCaseDetails.getProgistFile()==null)&&proCourtCaseDetails.getId()==
		 * null ) { bindResult.rejectValue("progistFile", "errmsg.required"); }
		 */
		if (proCourtCaseDetails.getSfioAs() != null) {
			if (proCourtCaseDetails.getSfioAs().getId() == 1) {
				courtCasevalid.validatecourtCase(proCourtCaseDetails, bindResult);
			}

			if (proCourtCaseDetails.getSfioAs().getId() != 1)

			{
				courtCasevalid.validatecourtCaseRespondent(proCourtCaseDetails, bindResult);
			}
		}
		if (bindResult.hasErrors()) {

			proCourtCaseDetails.setProSectionOrderNumber(assigntask.getAddCase().getProSectionOrderNumber());
			proCourtCaseDetails.setTypeofOrder(assigntask.getAddCase().getTypeofOrder());
			proCourtCaseDetails.setProSanctionDate(assigntask.getAddCase().getProSanctionDate());
			proCourtCaseDetails.setCourtType(proCourtCaseDetails.getCourtType());
			proCourtCaseDetails.setState(proCourtCaseDetails.getState());
			proCourtCaseDetails.setCity(proCourtCaseDetails.getCity());
			proCourtCaseDetails.setPetionerName(assigntask.getAddCase().getPetionerName());

			// List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			List<AddCourt> courtType;
			List<CourtCaseName> courtcaseName;

			if (proCourtCaseDetails.getType().getType().equals("NCLT")) {
				courtType = courtTypeRepo.findByCourtNameStartsWithOrCourtNameStartsWithAndCourtType("National",
						"Select", 1);
				proCourtCaseDetails.setCourtTypeC("NCLT");
				courtcaseName = CourtCaseNameRepo.findByTypeCase("NCLT");
				List<TypeofBench> list = null;
				if (proCourtCaseDetails.getCourtType().getId() == 4L) {

					list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

				}
				if (proCourtCaseDetails.getCourtType().getId() == 3L) {
					list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
				}else {
					list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
				}

				modelMap.addAttribute("typeOfBench", list);

			} else {
				courtType = courtTypeRepo.findByCourtNameNotContainingAndCourtType("National", 1);
				courtcaseName = CourtCaseNameRepo.findByTypeCase("Criminal");
				proCourtCaseDetails.setCourtTypeC("other");
			}

			modelMap.addAttribute("courtCaseList", courtcaseName);
			List<Type> typeList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
			modelMap.addAttribute("typeList", typeList);
			List<TypeofCase> typeofcase = typeofCaseRepo.findAllByIdNot((long) 0);
			Collections.sort(typeofcase);
			modelMap.addAttribute("typeofcase", typeofcase);
			courtType = courtTypeRepo.findByCourtNameNotContainingAndCourtType("National", 1,
					Sort.by(Sort.Direction.ASC, "id"));
			modelMap.addAttribute("courtType", courtType);
			modelMap.addAttribute("subseclst", addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
					Sort.by(Sort.Direction.ASC, "id")));

			modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			List<AddState> statelist = stateRepo.findAll();
			List<SfioAs> sfiolst = sfioAsRepo.findAll();
			AssignedTaskPuh assignedTaskPuh = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID())
					.get();
			List<ActSecDetailsInfo> actSectionDtl = actSecDetailsRepo.findByAssignedTaskAndIsActive(assignedTaskPuh, 1);
			modelMap.addAttribute("seclist", actSectionDtl);
			List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			modelMap.addAttribute("addActList", addactlist);
			List<District> districtlist = districtRepo.findAllByState(proCourtCaseDetails.getState());
			proCourtCaseDetails.setCity(proCourtCaseDetails.getCity());
			/* modelMap.addAttribute("courtType", courtType); */
			modelMap.addAttribute("statelist", statelist);
			modelMap.addAttribute("districtlist", districtlist);
			modelMap.addAttribute("sfiolst", sfiolst);

			proCourtCaseDetails.setProgistFile(proCourtCaseDetails.getProgistFile());

			modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);
			return "Prosecutor/AddCourtDeatil";
		}
		Long id;
		if (proCourtCaseDetails.getId() == null) {
			id = (proCourtCaseDetailsRepo.findMaxid() != null) ? (proCourtCaseDetailsRepo.findMaxid() + 1) : 1;

		} else

		{
			id = proCourtCaseDetails.getId();
		}
		// Long id1 = proCourtCaseDetailsRepo.findMaxid();

		InvCaseDetails caseDetails = new InvCaseDetails();

		if (proCourtCaseDetails.getInvCaseId() != null) {
			InvCaseDetails caseDetails1 = invCaseDtlRepo.findAllByInvcaseDetailsId(proCourtCaseDetails.getInvCaseId());
			if (caseDetails1 != null) {
				caseDetails1.setCaseId(proCourtCaseDetails.getCaseId());

				caseDetails1.setInvcaseDetailsId(proCourtCaseDetails.getInvCaseId());
				caseDetails1.setMcaOrder(proCourtCaseDetails.getMcaorderno());
				// caseDetails1.setCaseTitle(proCourtCaseDetails.getCaseTitle());
				caseDetails1.setInvcaseDetailsId(proCourtCaseDetails.getInvCaseId());
				caseDetails1.setFy(proCourtCaseDetails.getFy());

				caseDetails = invCaseDtlRepo.save(caseDetails1);
			} else {
				caseDetails.setCaseId(proCourtCaseDetails.getCaseId());
				caseDetails.setMcaOrder(proCourtCaseDetails.getMcaorderno());
				// caseDetails.setCaseTitle(proCourtCaseDetails.getCaseTitle());
				caseDetails.setInvcaseDetailsId(proCourtCaseDetails.getInvCaseId());
				caseDetails.setFy(proCourtCaseDetails.getFy());
				caseDetails = invCaseDtlRepo.save(caseDetails);
			}
		} else {
			// caseDetails.setCaseId(proCourtCaseDetails.getCaseId());
			caseDetails.setMcaOrder(proCourtCaseDetails.getMcaorderno());
			// caseDetails.setCaseTitle(proCourtCaseDetails.getCaseTitle());
			caseDetails.setInvcaseDetailsId(proCourtCaseDetails.getInvCaseId());
			caseDetails.setFy(proCourtCaseDetails.getFy());
			caseDetails = invCaseDtlRepo.save(caseDetails);
		}
		if (!proCourtCaseDetails.getProgistFile().isEmpty()) {
			proCourtCaseDetails.setGistFile(proCourtCaseDetails.getProgistFile().getOriginalFilename());
			String fileExt = proCourtCaseDetails.getProgistFile().getOriginalFilename();
			fileExt = fileExt.substring(fileExt.lastIndexOf("."));

			proCourtCaseDetails.setGistFile("ProGist_" + id + fileExt);
			caseFileUpload(proCourtCaseDetails.getProgistFile(), proCourtCaseDetails.getGistFile());
		}

		if (!proCourtCaseDetails.getBackgroundFile().isEmpty()) {
			String fileExt = proCourtCaseDetails.getBackgroundFile().getOriginalFilename();
			fileExt = fileExt.substring(fileExt.lastIndexOf("."));

			proCourtCaseDetails.setBackgroundFileName("Background_" + id + fileExt);
			caseFileUpload(proCourtCaseDetails.getBackgroundFile(), proCourtCaseDetails.getBackgroundFileName());
		}
		if (!proCourtCaseDetails.getBriefFile().isEmpty()) {

			String fileExt = proCourtCaseDetails.getBriefFile().getOriginalFilename();
			fileExt = fileExt.substring(fileExt.lastIndexOf("."));

			proCourtCaseDetails.setBriefFileName("Brief_" + id + fileExt);
			caseFileUpload(proCourtCaseDetails.getBriefFile(), proCourtCaseDetails.getBriefFileName());
		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		proCourtCaseDetails.setCreatedBy(userdet);
		proCourtCaseDetails.setUpdatedBy(userdet);
		proCourtCaseDetails.setApproveBy(userdet);
		proCourtCaseDetails.setInvCaseDetail(caseDetails);
		proCourtCaseDetails.setCreatedDate(new Date());

		Boolean checkH=false;
		if (proCourtCaseDetails.getId() == null || proCourtCaseDetails.getId() == 0) {
			// modelMap.addAttribute("message", "court Case Details had been added");
			checkH=true;
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.addcourtcasedetail") + " " + " and Investigation number is "
							+ assigntask.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assigntask.getAddCase().getId());
			auditBeanBo.save();
			
			

			redirect.addFlashAttribute("message", "Court Case Details had been added");
		} else {

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.addcourtcasedetailupdated") + " " + " and Investigation Number is "
							+ assigntask.getAddCase().getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assigntask.getAddCase().getId());
			auditBeanBo.save();
			// modelMap.addAttribute("message", "court Case Details had been Updated");
			redirect.addFlashAttribute("message", "Court Case Details had been Updated");
		}
		
		
		AssignedTaskPuh assignedTaskPuh = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID()).get();
		// assignedTaskPuh.setIsApproved(true);
		assignedTaskPuh.setApprovalStatus(1);
		assignedTaskPuhRepo.save(assignedTaskPuh);
		if (proCourtCaseDetails.getApproveStatus() == 2) {
			proCourtCaseDetails.setInvCaseDetail(invCaseDetailBhakua);
		}
		if (proCourtCaseDetails.getApproveStatus() == 3) {
			proCourtCaseDetails.setApproveStatus(0);
		}
		proCourtCaseDetails.setAssignedTask(assignedTaskPuh);
		proCourtCaseDetails.setAddCase(assignedTaskPuh.getAddCase());
		//proCourtCaseDetails.setCourtCaseNo(assignedTaskPuh.getAddCase().getCourtCaseNumber());
		proCourtCaseDetails = proCourtCaseDetailsRepo.save(proCourtCaseDetails);
		if(checkH)
		{
			CaseForwardHistory caseForwardHistory2 = new CaseForwardHistory();
	    	 caseForwardHistory2.setFromDate(LocalDate.now());
	    	 caseForwardHistory2.setUserName(userdet);
	    	 caseForwardHistory2.setToDate(LocalDate.now());
	    	 caseForwardHistory2.setProCourtCaseDetails(proCourtCaseDetails);
	 	   caseForwardHistoryRepository.save(caseForwardHistory2);
		}

		List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<AddState> statelist = stateRepo.findAll();
		List<District> districtlist = districtRepo.findAll();
		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("addActList", addactlist);

		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("statelist", statelist);

		Long proCourtcaseid;
		// List<ActSecDetailsInfo> actSectionDtl =
		// actSecDetailsRepo.findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive((long)
		// 0,useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName()),proCourtCaseDetails.getCaseType(),1);
		/*
		 * List<ActSecDetailsInfo> actSectionDtl; if (proCourtCaseDetails.getId() ==
		 * null || proCourtCaseDetails.getId() == 0) { actSectionDtl =
		 * actSecDetailsRepo.findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive((
		 * long) 0,
		 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
		 * )), proCourtCaseDetails.getCaseType(), 1); proCourtcaseid =
		 * proCourtCaseDetailsRepo.findMaxid();
		 * 
		 * }
		 * 
		 * else { actSectionDtl =
		 * actSecDetailsRepo.findAllByProcourtdtlIDOrProcourtdtlIDAndCreatedBy((long) 0,
		 * proCourtCaseDetails.getId(),
		 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
		 * ))); proCourtcaseid = proCourtCaseDetails.getId();
		 * 
		 * }
		 */

		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findByAssignedTaskAndIsActive(assignedTaskPuh, 1);
		for (ActSecDetailsInfo actSecDetailsInfo : seclist) {
			actSecDetailsInfo.setProcourtdtlID(proCourtCaseDetails.getId());
			actSecDetailsRepo.save(actSecDetailsInfo);

		}

		com.pams.entity.ProCourtCaseDetails procourtdtl = new ProCourtCaseDetails();
		procourtdtl.setCaseType(proCourtCaseDetails.getCaseType());

		modelMap.addAttribute("proCourtCaseDetails", procourtdtl);

		// return "Prosecutor/AddCourtDeatil";

		return "redirect:/totalNumberOfCourtCases";

	}

	@RequestMapping(value = "/deleteActSection")
	public String deleteActSection(ModelMap modelMap,
			@ModelAttribute("proCourtCaseDetails") ProCourtCaseDetails proCourtCaseDetails, BindingResult bindResult,
			RedirectAttributes redirect) throws Exception {

		Long id = proCourtCaseDetails.getActSectionID();
		AssignedTaskPuh assignTask = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID()).get();
		int typeofOrder = assignTask.getAddCase().getTypeofOrder();

		proCourtCaseDetails.setTypeofOrder(typeofOrder);
		if (id != null) {
			ActSecDetailsInfo actAnsSectionDtls = actSecDetailsRepo.findById(id).get();

			actAnsSectionDtls.setIsActive(0);
			actSecDetailsRepo.save(actAnsSectionDtls);

			modelMap.addAttribute("message", "Act & Section  is deleted successfully.");
		}
		// List<ActSecDetailsInfo> seclist =
		// actSecDetailsRepo.findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive((long)
		// 0,useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName()),proCourtCaseDetails.getCaseType(),1);
		/*
		 * List<ActSecDetailsInfo> seclist = null; if (proCourtCaseDetails.getId() ==
		 * null || proCourtCaseDetails.getId() == 0) { seclist =
		 * actSecDetailsRepo.findAllByProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive((
		 * long) 0,
		 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
		 * )), proCourtCaseDetails.getCaseType(), 1); } else { seclist =
		 * actSecDetailsRepo.
		 * findAllByProcourtdtlIDOrProcourtdtlIDAndCreatedByAndCaseTypeAndIsActive(
		 * (long) 0, proCourtCaseDetails.getId(),
		 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
		 * )), proCourtCaseDetails.getCaseType(), 1); }
		 */

		AssignedTaskPuh assigntask = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID()).get();
		List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findByAssignedTaskAndIsActive(assigntask, 1);
		modelMap.addAttribute("seclist", seclist);

		List<TypeofBench> list = null;
		if (proCourtCaseDetails.getCourtType().getId() == 4L) {

			list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

		}
		if (proCourtCaseDetails.getCourtType().getId() == 3L) {
			list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
		}
		modelMap.addAttribute("typeOfBench", list);

		// List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
		List<AddCourt> courtType;
		/*
		 * if
		 * (assigntask.getAddCase().getTypeOfCase().getTypeOfCase().equals("NCLT/NCLAT")
		 * ) { courtType =
		 * courtTypeRepo.findByCourtNameStartsWithOrCourtNameStartsWithAndCourtType(
		 * "National", "Select",1, Sort.by(Sort.Direction.ASC, "id"));
		 * proCourtCaseDetails.setCourtTypeC("NCLT");
		 * 
		 * } else { courtType =
		 * courtTypeRepo.findByCourtNameNotContainingAndCourtType("National",1,
		 * Sort.by(Sort.Direction.ASC, "id"));
		 * proCourtCaseDetails.setCourtTypeC("other"); }
		 */
		courtType = courtTypeRepo.findAll();

		proCourtCaseDetails.setCourtTypeC("other");

		List<AddState> statelist = stateRepo.findAll();

		List<SfioAs> sfiolst = sfioAsRepo.findAll();

		List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		modelMap.addAttribute("addActList", addactlist);
		modelMap.addAttribute("subseclst", addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
				Sort.by(Sort.Direction.ASC, "id")));

		modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));

		List<District> districtlist = districtRepo.findAllByState(proCourtCaseDetails.getState());
		proCourtCaseDetails.setCity(proCourtCaseDetails.getCity());
		proCourtCaseDetails.setPetionerName(assignTask.getAddCase().getPetionerName());
		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		modelMap.addAttribute("sfiolst", sfiolst);
		proCourtCaseDetails.setProgistFile(proCourtCaseDetails.getProgistFile());
		modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);

		// redirect.addAttribute("proCourtCaseDetails", proCourtCaseDetails);

		return "Prosecutor/AddCourtDeatil";

	}

	@RequestMapping(value = "/SaveActSection")
	public String saveact(ModelMap modelMap,
			@ModelAttribute("proCourtCaseDetails") ProCourtCaseDetails proCourtCaseDetails, BindingResult bindResult)
			throws Exception {

		AssignedTaskPuh assignTask = assignedTaskPuhRepo.findById(proCourtCaseDetails.getAssignedTaskID()).get();
		proCourtCaseDetails.setTypeofOrder(assignTask.getAddCase().getTypeofOrder());

		if (proCourtCaseDetails.getId() == null) {
			proCourtCaseDetails.setId((long) 0);
		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		proCourtCaseDetails.setCreatedBy(userdet);

		if (proCourtCaseDetails.getAct() == null) {
			bindResult.rejectValue("act", "msg.wrongId");
		}
		if (proCourtCaseDetails.getSection() == null || proCourtCaseDetails.getSection().getId() == 0) {
			bindResult.rejectValue("section", "msg.wrongId");
		}
		if (proCourtCaseDetails.getSubsection() == null) {
			bindResult.rejectValue("section", "msg.wrongId");
		}

		if (!bindResult.hasErrors()) {

			List<ActSecDetailsInfo> lst1 = actSecDetailsRepo
					.findByProcourtdtlIDAndActAndSectionAndSubSectionAndIsActive(proCourtCaseDetails.getId(),
							proCourtCaseDetails.getAct(), proCourtCaseDetails.getSection(),
							proCourtCaseDetails.getSubsection(), 1);

			List<ActSecDetailsInfo> lst = actSecDetailsRepo.findByAssignedTaskAndActAndSectionAndSubSectionAndIsActive(
					assignTask, proCourtCaseDetails.getAct(), proCourtCaseDetails.getSection(),
					proCourtCaseDetails.getSubsection(), 1);

			if (!lst.isEmpty()) {
				bindResult.rejectValue("section", "msg.subsection");

			}
		}

		if (bindResult.hasErrors()) {
			int acterr1 = 4;

			List<TypeofBench> list = null;
			if (proCourtCaseDetails.getCourtType().getId() == 4L) {

				list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

			}
			if (proCourtCaseDetails.getCourtType().getId() == 3L) {
				list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
			}
			modelMap.addAttribute("typeOfBench", list);

			List<ActSecDetailsInfo> actseclist = actSecDetailsRepo.findByAssignedTaskAndIsActive(assignTask, 1);

			modelMap.addAttribute("seclist", actseclist);

			// List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			List<AddCourt> courtType;
			List<CourtCaseName> courtcaseName;
			/*
			 * if (assignTask.getAddCase().getType().getType().equals("NCLT/NCLAT")) {
			 * courtType =
			 * courtTypeRepo.findByCourtNameStartsWithOrCourtNameStartsWithAndCourtType(
			 * "National", "Select",1, Sort.by(Sort.Direction.ASC, "id"));
			 * proCourtCaseDetails.setCourtTypeC("NCLT"); courtcaseName =
			 * CourtCaseNameRepo.findByTypeCase("NCLT");
			 * 
			 * } else { courtType =
			 * courtTypeRepo.findByCourtNameNotContainingAndCourtType("National",1,
			 * Sort.by(Sort.Direction.ASC, "id"));
			 * proCourtCaseDetails.setCourtTypeC("other"); courtcaseName =
			 * CourtCaseNameRepo.findByTypeCase("Criminal"); }
			 */

			List<AddState> statelist = stateRepo.findAll();

			List<SfioAs> sfiolst = sfioAsRepo.findAll();
			/* modelMap.addAttribute("courtCaseList", courtcaseName); */
			List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			modelMap.addAttribute("addActList", addactlist);
			List<District> districtlist = districtRepo.findAllByState(proCourtCaseDetails.getState());
			String state = proCourtCaseDetails.getState().getState();

			String districtName = proCourtCaseDetails.getCity().getDistrictName();
			proCourtCaseDetails.setState(proCourtCaseDetails.getState());
			proCourtCaseDetails.setCity(proCourtCaseDetails.getCity());
			proCourtCaseDetails.setPetionerName(assignTask.getAddCase().getPetionerName());

			/* modelMap.addAttribute("courtType", courtType); */
			modelMap.addAttribute("statelist", statelist);
			modelMap.addAttribute("districtlist", districtlist);
			modelMap.addAttribute("sfiolst", sfiolst);
			modelMap.addAttribute("subseclst", addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
					Sort.by(Sort.Direction.ASC, "id")));
			modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			proCourtCaseDetails.setProgistFile(proCourtCaseDetails.getProgistFile());

			proCourtCaseDetails.setActerror(acterr1);
			modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);
			return "Prosecutor/AddCourtDeatil";
		}

		else {

			int acterr1 = 1;
			proCourtCaseDetails.setActerror(acterr1);

			ActSecDetailsInfo actSecDetailsInfo = new ActSecDetailsInfo();

			actSecDetailsInfo.setAct(proCourtCaseDetails.getAct());
			actSecDetailsInfo.setSection(proCourtCaseDetails.getSection());
			proCourtCaseDetails.setState(proCourtCaseDetails.getState());

			if (proCourtCaseDetails.getSubsection() != null) {

				actSecDetailsInfo.setSubSection(proCourtCaseDetails.getSubsection());
			}
			actSecDetailsInfo.setDescription(proCourtCaseDetails.getDescription());
			proCourtCaseDetails.setDescription("");

			actSecDetailsInfo.setCreatedBy(userdet);
			actSecDetailsInfo.setUpdatedBy(userdet);

			if (proCourtCaseDetails.getId() == null || proCourtCaseDetails.getId() == 0) {

				actSecDetailsInfo.setProcourtdtlID((long) 0);
			} else {
				actSecDetailsInfo.setProcourtdtlID(proCourtCaseDetails.getId());
			}

			actSecDetailsInfo.setCreatedDate(new Date());

			actSecDetailsInfo.setCaseType(proCourtCaseDetails.getCaseType());
			proCourtCaseDetails.setState(proCourtCaseDetails.getState());
			proCourtCaseDetails.setPetionerName(assignTask.getAddCase().getPetionerName());

			actSecDetailsInfo.setAssignedTask(assignTask);
			actSecDetailsInfo.setClause(proCourtCaseDetails.getClause());
			actSecDetailsRepo.save(actSecDetailsInfo);
			modelMap.addAttribute("message", "Act & Section has been added");

			proCourtCaseDetails.setAct(addActRepo.findById((long) 0).get());
			proCourtCaseDetails.setClause("choose");
			proCourtCaseDetails.setSubsection(addSubSectionRepo.findById((long) 0).get());
			proCourtCaseDetails.setSection(addActSecRepo.findById((long) 0).get());

			List<ActSecDetailsInfo> seclist = actSecDetailsRepo.findByAssignedTaskAndIsActive(assignTask, 1);

			modelMap.addAttribute("seclist", seclist);
			List<TypeofBench> list = null;
			if (proCourtCaseDetails.getCourtType().getId() == 4L) {

				list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

			}
			if (proCourtCaseDetails.getCourtType().getId() == 3L) {
				list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
			}
			modelMap.addAttribute("typeOfBench", list);

			// List<AddCourt> courtType = courtTypeRepo.findByCourtType(1);
			List<AddCourt> courtType;
			List<CourtCaseName> courtcaseName;
			/*
			 * if (assignTask.getAddCase().getType().getType().equals("NCLT")) { courtType =
			 * courtTypeRepo.findByCourtNameStartsWithOrCourtNameStartsWith("National",
			 * "Select", Sort.by(Sort.Direction.ASC, "id"));
			 * proCourtCaseDetails.setCourtTypeC("NCLT"); courtcaseName =
			 * CourtCaseNameRepo.findByTypeCase("NCLT");
			 * 
			 * } else { courtType = courtTypeRepo.findByCourtNameNotContaining("National",
			 * Sort.by(Sort.Direction.ASC, "id"));
			 * proCourtCaseDetails.setCourtTypeC("other"); courtcaseName =
			 * CourtCaseNameRepo.findByTypeCase("Criminal"); }
			 */

			List<AddState> statelist = stateRepo.findAll();

			List<SfioAs> sfiolst = sfioAsRepo.findAll();
			/* modelMap.addAttribute("courtCaseList", courtcaseName); */
			List<AddAct> addactlist = addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
			modelMap.addAttribute("addActList", addactlist);
			List<District> districtlist = districtRepo.findAllByState(proCourtCaseDetails.getState());
			proCourtCaseDetails.setState(proCourtCaseDetails.getState());

			proCourtCaseDetails.setCity(proCourtCaseDetails.getCity());
			/* modelMap.addAttribute("courtType", courtType); */
			modelMap.addAttribute("statelist", statelist);
			modelMap.addAttribute("districtlist", districtlist);
			modelMap.addAttribute("sfiolst", sfiolst);
			proCourtCaseDetails.setProgistFile(proCourtCaseDetails.getProgistFile());
			modelMap.addAttribute("subseclst", addSubSectionRepo.findAllBySection(addActSecRepo.findById((long) 0),
					Sort.by(Sort.Direction.ASC, "id")));

			modelMap.addAttribute("clauselist", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			modelMap.addAttribute("proCourtCaseDetails", proCourtCaseDetails);
			return "Prosecutor/AddCourtDeatil";

		}
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

	/*
	 * @GetMapping(value = "/showmcaOrderDetails") public @ResponseBody String
	 * showmcaOrderDetails(ModelMap model, @RequestParam("mcaorder") String mcaNo,
	 * 
	 * @RequestParam("compname") String compName) throws Exception { mcaNo =
	 * mcaNo.replace(" ", "?"); compName = compName.replace(" ", "?");
	 * GetDataFromSNMSDTO getDataFromSNMSDTO = new GetDataFromSNMSDTO();
	 * getDataFromSNMSDTO.setCompNameo(compName);
	 * getDataFromSNMSDTO.setMcaOrderNo(mcaNo); String strCall =
	 * restTemplateProvider.ApiCallForList(getDataFromSNMSDTO, snmsapi +
	 * "/showmcaOrderDetails", "GET", "application/json");
	 * System.out.println(strCall); return strCall; }
	 */

	@RequestMapping(value = "/getActiveCompany", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String showInvcomplist(ModelMap model, @RequestParam("invCaseId") Long invCaseId)
			throws Exception {
		// String strCall = ApiCallForList(snmsapi + "/mcaOrderDtl/" +
		// "showInvcomplist?invCaseId=" + invCaseId, "GET","application/json");

		GetDataFromSNMSDTO getDataFromSNMSDTO = new GetDataFromSNMSDTO();
		getDataFromSNMSDTO.setInvCaseId(invCaseId);

		String strCall = restTemplateProvider.ApiCallForList(getDataFromSNMSDTO, snmsapi + "/showInvcomplist", "GET",
				"application/json");

		return strCall;
	}

	@RequestMapping(value = "/getActiveIo", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getActiveIo(ModelMap model, @RequestParam("invCaseId") Long invCaseId)
			throws Exception {
		GetDataFromSNMSDTO getDataFromSNMSDTO = new GetDataFromSNMSDTO();
		getDataFromSNMSDTO.setInvCaseId(invCaseId);

		String strCall = restTemplateProvider.ApiCallForList(getDataFromSNMSDTO, snmsapi + "/showInvIOlist", "GET",
				"application/json");

		// String strCall = ApiCallForList(snmsapi + "/mcaOrderDtl/" +
		// "showInvIOlist?invCaseId=" + invCaseId, "GET","application/json");
		System.out.println(strCall);
		return strCall;
	}

	@RequestMapping(value = "/getActiveKMP", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String showInvKmplist(ModelMap model, @RequestParam("invCaseId") Long invCaseId)
			throws Exception {
		// String strCall = ApiCallForList(snmsapi + "/mcaOrderDtl/" +
		// "showInvKmplist?invCaseId=" + invCaseId, "GET", "application/json");

		GetDataFromSNMSDTO getDataFromSNMSDTO = new GetDataFromSNMSDTO();
		getDataFromSNMSDTO.setInvCaseId(invCaseId);

		String strCall = restTemplateProvider.ApiCallForList(getDataFromSNMSDTO, snmsapi + "/showInvKmplist", "GET",
				"application/json");

		return strCall;
	}

	@GetMapping("/showsDistrictList")
	public @ResponseBody List<District> districtList(ModelMap model, @RequestParam long stateid) {

		AddState state = stateRepo.findById(stateid).get();
		List<District> districtlt = districtRepo.findAllByState(state);
		return districtlt;
	}
	@GetMapping("/showsSectionList")
	public @ResponseBody List<AddActSec> districtSectionList(@RequestParam long stateid) {
		AddAct addAct = addActRepo.findById(stateid).orElse(null);
		return addAct != null ? addActSecRepo.findAllByAct(addAct) : Collections.emptyList();
	}

	@GetMapping("/getState")
	@ResponseBody
	List<AddState> getState(@RequestParam("courtType") Long courtType) {
		List<AddState> statelst;
		if (courtType == 2L || courtType == 4L) {
			statelst = stateRepo.findAllById(7L);

			return statelst;
		} else {
			statelst = stateRepo.findAll();
			return statelst;
		}
	}

	@GetMapping("/getBench")
	@ResponseBody
	List<TypeofBench> getBench(@RequestParam("courtType") Long courtType) {
		List<TypeofBench> list = null;
		if (courtType == 4L) {

			list = typeofBenchRepo.findByBenchContainingOrBenchStartsWith("NCLAT", "Choose");

			return list;
		}
		if (courtType == 3L) {
			list = typeofBenchRepo.findByBenchNotContaining("NCLAT");
			return list;
		}
		return list;
	}

	@GetMapping("/getcity")
	@ResponseBody
	List<District> getcity(@RequestParam("courtType") Long courtType) {
		List<District> districtlt;
		if (courtType == 2L || courtType == 4L) {
			AddState state = stateRepo.findById(7L).get();

			districtlt = districtRepo.findAllById(79L);

			return districtlt;
		} else {
			districtlt = districtRepo.findAll();
			return districtlt;
		}
	}

	@RequestMapping(value = "/searchmcaOrder", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String searchmcaOrder(ModelMap model, @RequestParam("q") String mcaNo)
			throws MalformedURLException, IOException {

		System.out.println("methodiasdhjkshadkj");

		String strCall = ApiCallForList(snmsapi + "/mcaOrderDtl/" + "searchmcaOrder?mcaNo=" + mcaNo, "GET",
				"application/json");

		System.out.println(strCall);
		return strCall;
	}

	private String ApiCallForList(String url, String methodType, String consumeType)
			throws MalformedURLException, IOException {
		// String encodedURL=java.net.URLEncoder.encode(url,"UTF-8");
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
		httpcon.setDoOutput(true);
		httpcon.setRequestMethod(methodType);
		httpcon.setRequestProperty("Accept", consumeType);
		httpcon.connect();
		BufferedReader inreader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
		StringBuffer decodedString = new StringBuffer();
		String s;
		while ((s = inreader.readLine()) != null) {
			decodedString.append(s);
		}
		inreader.close();
		httpcon.disconnect();

		String rrr = serviceLayerAPI.cunsumeAPI(url);
		return decodedString.toString();
		// return rrr;

	}

	@RequestMapping(value = "/downloadFiles", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadFileFromLocal(@Param(value = "fileName") String fileName)
			throws IOException, ValidationException {

		ProMISValidator proMis = new ProMISValidator();
		if (!proMis.isValidFileName(fileName)) {
			return null;
		} else {
			File parent = new File(filePath).getParentFile().getCanonicalFile();
			// ESAPI.validator().getValidDirectoryPath("DirectoryName", filePath, parent,
			// false);
			Path path = Paths.get(filePath + File.separator + fileName);
			Resource resource = null;
			try {
				resource = new UrlResource(path.toUri());
			} catch (MalformedURLException e) {
				logger.info(e.getMessage());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=" + fileName);

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(resource);

		}

	}

	@RequestMapping(value = "AdditionalInfo", params = "ReturnHearingPage")
	public String HearingPage(ModelMap modelMap,
			@RequestParam(value = "ReturnHearingPage", required = true) Long courtId) {

		List<HearingDetails> hearingdtls1 = hearingdtlRepo.findAll();
		modelMap.addAttribute("hearingDtls1", hearingdtls1);

		ProCourtCaseDetails procourtdtl = proCourtCaseDetailsRepo.findALLById(courtId);
		System.out.println("++++++++++++++++++++++++++++" + procourtdtl);
		HearingDetails hearingDetails = new HearingDetails();
		hearingDetails.setProcourtdtl(procourtdtl);
		modelMap.addAttribute("hearingDetails", hearingDetails);
		modelMap.addAttribute("procourtdt", procourtdtl);
		modelMap.addAttribute("courtdtl", procourtdtl.getId());

		return "IOOfficer/AddHearingDetail";
	}

	@RequestMapping(value = "/getsection", method = RequestMethod.GET)
	@ResponseBody
	List<AddActSec> getsection(@RequestParam("stateid") Long actid) {

		AddAct acttype = addActRepo.findById(actid).get();

		List<AddActSec> seclst1 = addActSecRepo.findAllByAct(acttype);

		return seclst1;
	}

	@RequestMapping(value = "/getsection2", method = RequestMethod.GET)
	@ResponseBody
	List<AddActSec> getsection2(@RequestParam("stateid") Long actid, @RequestParam("typeOfCase") String typeOfCase) {

		System.out.println(typeOfCase);

		AddAct acttype = addActRepo.findById(actid).get();
		List<AddActSec> seclst1 = null;
		if (actid == 6 && typeOfCase.equals("NCLT")) {
			seclst1 = addActSecRepo.findAllNCLTCaseAndAct2013();
		} else {
			seclst1 = addActSecRepo.findAllByAct(acttype);
		}
		return seclst1;
	}

	@RequestMapping(value = "/getsection1", method = RequestMethod.GET)
	@ResponseBody
	List<AddSubSec> getsection1(@RequestParam("stateid") Long actid) {

		// AddAct acttype = addActRepo.findById(actid).get();

		AddActSec secType = addActSecRepo.findById(actid).get();

		// List<AddActSec> seclst1 = addActSecRepo.findAllByAct(acttype);
		List<AddSubSec> seclst1 = addSubSectionRepo.findAllBySection(secType);

		return seclst1;
	}

	private static int getMonthFromDate(Date date) {
		int result = -1;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = cal.get(Calendar.MONTH) + 1;
		}
		return result;
	}

	public int getYearFromDate(Date date) {

		final int FIRST_FISCAL_MONTH = Calendar.MARCH;

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		return (month >= FIRST_FISCAL_MONTH) ? year : year - 1;
	}

	public static int getYearFromDate1(Date date) {

		int result = -1;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = cal.get(Calendar.YEAR);
		}
		return result;
	}

	@RequestMapping(value = "/getCourt", method = RequestMethod.GET)
	@ResponseBody
	public List<AddCourt> getCourt(@RequestParam("selectedText") String typeOfCase) {
		List<AddCourt> lst = null;
		if (typeOfCase.equals("NCLT")) {
			List<AddCourt> lst1 = courtTypeRepo.findByCourtType(1);
			lst = courtTypeRepo.findByCourtNameStartsWithOrCourtNameStartsWith("National", "Select",
					Sort.by(Sort.Direction.ASC, "id"));
		}

		else {
			lst = courtTypeRepo.findByCourtNameNotContaining("National", Sort.by(Sort.Direction.ASC, "id"));
		}
		return lst;

	}

}
