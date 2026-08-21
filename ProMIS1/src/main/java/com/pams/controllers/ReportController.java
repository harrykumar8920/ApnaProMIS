package com.pams.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pams.dao.ActReportDao;
import com.pams.dao.AppRoleDAO;
import com.pams.dao.ReportDao;
import com.pams.dto.FillingReportRequest;
import com.pams.dto.PriorityCaseDTO;
import com.pams.dto.ReportDTO;
import com.pams.dto.ReportPriorityInput;
import com.pams.dto.WeeklyListOfCasesWhereMCAIsPartyDTO;
import com.pams.entity.AccusedStatus;
import com.pams.entity.ActCompundRelevantSection;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddAccused;
import com.pams.entity.AddActSec;
import com.pams.entity.AddState;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.HearingDetails;
import com.pams.entity.PairaviDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.ReportWeeklyInput;
import com.pams.entity.Status;
import com.pams.entity.UserDetails;
import com.pams.service.AccusedStatusRepository;
import com.pams.service.ActCompundRelevantSectionRepo;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.CaseCompanyRepository;
import com.pams.service.ChanrgeInstanceRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.NCLTActofRespondantRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PriorityService;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.StateRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class ReportController {
	@Autowired
	private StateRepository addStateRepo;
	@Autowired
	private UserDetailsRepository userDetailsRepo;
	@Autowired
	private AccusedStatusRepository AccusedStatusRepo;
	@Autowired
	private ActCompundRelevantSectionRepo actCompundRelevan;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepo;
	@Autowired
	private ReportDao reportDTO;
	@Autowired
	private ActReportDao actReportDao;
	@Autowired
	private ChanrgeInstanceRepository chanrgeInstanceRepository;
	@Autowired
	private HearingDetailsRepository hearingDetailsRepos;

	@Autowired
	private PairaviDetailsRepository pairaviDetailsRepo;
	@Autowired
	private CaseCompanyRepository caseCompanyRepo;
	@Autowired
	private AddStatusRepository addStatusRepo;

	@Autowired
	private ActSecDetailsRepository actSecDetailsRepo;
	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private NCLTActofRespondantRepository ncltActofRespondantRepository;
	@Autowired
	private PriorityService priorityService;
	
	@GetMapping("pendingNextHearingDate")
	public String pendingNextHearingDate(ModelMap modelMap) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date oldcurrent = c.getTime();
		List<HearingDetails> byUserAndLessThanNextHearingDate = 
				hearingDetailsRepos.findLatestHearingPerCase(oldcurrent);

		
		modelMap.addAttribute("byUserAndLessThanNextHearingDate", byUserAndLessThanNextHearingDate);
		
		return "report/pendingNextHearingDte";
	}
	

	@RequestMapping(value = "fillingReportfetch")
	public String fillingReportfetch(@ModelAttribute("reportWeeklyInput") FillingReportRequest reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		Long caseStatus = reportWeeklyInput.getCaseStatus();
		int years = reportWeeklyInput.getYears();
		List<HearingDetails> hearingdata = null;
		if(years==21 && caseStatus==0) {
			 hearingdata = hearingDetailsRepos.findAllByFillingDateNotNull();
		}
		else if(years!=21 && caseStatus!=0)
		{
			hearingdata = hearingDetailsRepos.findAllByFillingDateNotNull(caseStatus,years);
		}
		
		else if(years==21 && caseStatus!=0)
		{
			hearingdata = hearingDetailsRepos.findAllByFillingDateNotNull(caseStatus);
		}
		else if(years!=21 && caseStatus==0)
		{
			hearingdata = hearingDetailsRepos.findAllByFillingDateNotNull(years);
		}
		
		
		
		
		
		
		
		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActiveOrderByStatusNameAsc("C",true);
		modelMap.addAttribute("courtCasedtl", hearingdata);
		modelMap.addAttribute("statusLst1", StatusList1);
		modelMap.addAttribute("reportWeeklyInput", new FillingReportRequest());
		modelMap.addAttribute("message", "Filing Report ");
		return "report/fillingReportInput";
	}

	@RequestMapping(value = "fillingReport")

	public String fillingReport(ModelMap modelMap) throws Exception {

		
		
		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActiveOrderByStatusNameAsc("C",true);

		modelMap.addAttribute("statusLst1", StatusList1);
		modelMap.addAttribute("reportWeeklyInput", new FillingReportRequest());
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		return "report/fillingReportInput";

	}

	@RequestMapping(value = "reportOfCourtCase")

	public String reportOfCourtCase(ModelMap modelMap) throws Exception {

		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		return "report/WeaklyReportInput";

	}

	@RequestMapping(value = "todayHearingDetailsLastProsecution")
	public String dailylistofcasesProsecutionLast(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		UserDetails user = userDetailsService.getUserDetailssss();

		modelMap.addAttribute("userRole", userrole);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		userDetailsService.getUserDetails();
		Date nextdate = last7days();

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

		List<HearingDetails> hearingdata = hearingDetailsRepos.findByNextHearingDateBetweenAndUser(nextdate, fromDate,
				user);

		modelMap.addAttribute("hearingdata", hearingdata);
		modelMap.addAttribute("message", "Due for hearing ");
		return "report/lastDailyReportInput";

	}

	@RequestMapping(value = "todayHearingDetailsProsecution")
	public String dailylistofcasesProsecution(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		Date nextdate = next7days();
		UserDetails user = userDetailsService.getUserDetailssss();
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

		// List<HearingDetails> hearingdata =
		// hearingDetailsRepos.findByNextHearingDateBetween(fromDate, nextdate);
		List<HearingDetails> hearingdata = hearingDetailsRepos.findByNextHearingDateBetweenAndUser(fromDate, nextdate,
				user);

		modelMap.addAttribute("hearingdata", hearingdata);
		modelMap.addAttribute("message", "Due for hearing ");
		return "report/DailyReportInput";

	}

	@GetMapping("lastHearingDetails")
	public String dailyLastlistofcases(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		
		Date currentdate = new Date();
		currentdate.setDate(currentdate.getDate()-1);
		
		String date = simpleDateFormat.format(currentdate);

		Date nextdate = last7days();

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

		List<HearingDetails> hearingdata = hearingDetailsRepos.findByNextHearingDateBetweenOrderByNextHearingDateDesc(nextdate, fromDate);

		modelMap.addAttribute("hearingdata", hearingdata);
		modelMap.addAttribute("message", "Due for hearing ");
		return "report/lastDailyReportInput";

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

	@RequestMapping(value = "findCaseOnPriority")
	public String findCaseOnPrioritydffs(@ModelAttribute("reportWeeklyInput") ReportPriorityInput input, ModelMap model)
			throws Exception {

		int casePriority = input.getCasePriority();
		List<AddState> all2 = addStateRepo.findAll();

		List<AddState> list = all2.stream().sorted(Comparator.comparing(d -> d.getState())).toList();

		List<UserDetails> all = userDetailsRepo.findAll();
		model.addAttribute("all", all);
		model.addAttribute("all2", list);

		List<PriorityCaseDTO> list3 = priorityService.getPriorityCases3(casePriority, input.getStateId(),
				input.getUserId());
		// List<PriorityCaseDTO> list = priorityService.getPriorityCases(casePriority);
		model.addAttribute("caseList", list3);
		return "report/priorityCase";
	}

	@RequestMapping(value = "priorityCaseReport")

	public String reportOfpriorityCase(ModelMap modelMap) throws Exception {
		modelMap.addAttribute("reportWeeklyInput", new ReportPriorityInput());
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		List<AddState> all2 = addStateRepo.findAll();
		List<UserDetails> all = userDetailsRepo.findAll();
		List<AddState> list = all2.stream().sorted(Comparator.comparing(d -> d.getState())).toList();
		modelMap.addAttribute("all", all);
		modelMap.addAttribute("all2", list);
		return "report/priorityCase";

	}

	@RequestMapping(value = "todayHearingDetails")
	public String dailylistofcases(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		Date nextdate = next7days();

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

		List<HearingDetails> hearingdata = hearingDetailsRepos.findByNextHearingDateBetweenOrderByNextHearingDateAsc(fromDate, nextdate);

		modelMap.addAttribute("hearingdata", hearingdata);
		modelMap.addAttribute("message", "Due for hearing ");
		return "report/DailyReportInput";

	}

	@SuppressWarnings("null")
	@RequestMapping(value = "weeklycauseList")
	public String reportOfCourtCase4(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();
		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<ReportDTO> caselist = new ArrayList<ReportDTO>();
		List<HearingDetails> hearingdata = hearingDetailsRepos.findLatestHearingBetweenDates(fromDate,
				toDate);
		for (HearingDetails hearingData : hearingdata) {
			List<HearingDetails> lastHearingData = hearingDetailsRepos
					.findByProcourtdtlAndNextHearingDateAndCurrentStatus(hearingData.getProcourtdtl(),
							hearingData.getLastHearingDate(), false);
			ReportDTO rdto = new ReportDTO();
			if (!lastHearingData.isEmpty()) {
				rdto.setLastcaseStatus(lastHearingData.get(0).getStatus().getStatusName());
				rdto.setCounselnameLast(lastHearingData.get(0).getCounselName().getName());
			}

			rdto.setProcourtdtl(hearingData.getProcourtdtl());
			rdto.setCaseStatus(hearingData.getStatus().getStatusName());
			rdto.setCounselmob(hearingData.getCounselName().getMobile());
			rdto.setCounselmob1(hearingData.getOfficer().getMobile());
			rdto.setCounselname(hearingData.getCounselName().getName());
			rdto.setCounselname1(hearingData.getOfficer().getName());
			rdto.setLastHearingDetails(hearingData.getLastHearingDate());
			rdto.setNextHearingDate(hearingData.getNextHearingDate());
			rdto.setCauseTitle(hearingData.getProcourtdtl().getAddCase().getCaseTitle());
			rdto.setStrategy(hearingData.getBriefHD());
			PairaviDetails temp2 = pairaviDetailsRepo
					.findAllByProcourtdtlAndIsActiveAndApproveStatus(hearingData.getProcourtdtl(), true, 2);

			rdto.setProsecutorname(
				    hearingData.getAssignedTask() != null && hearingData.getAssignedTask().getUser() != null
				        ? (hearingData.getAssignedTask().getUser().getFirstName() != null 
				               ? hearingData.getAssignedTask().getUser().getFirstName() : "")
				          + " " +
				          (hearingData.getAssignedTask().getUser().getMiddleName() != null 
				               ? hearingData.getAssignedTask().getUser().getMiddleName() : "")
				          + " " +
				          (hearingData.getAssignedTask().getUser().getLastName() != null 
				               ? hearingData.getAssignedTask().getUser().getLastName() : "")
				        : ""
				);
			if (temp2 != null) {

				rdto.setPairaviName(temp2.getPairaviOfficer().getName());
			}

			List<ActSecDetailsInfo> sec4 = new ArrayList<ActSecDetailsInfo>();
			List<ActSecDetailsInfo> sec3 = new ArrayList<ActSecDetailsInfo>();
			List<ActSecDetailsInfo> sec2 = new ArrayList<ActSecDetailsInfo>();
			List<ActSecDetailsInfo> checkData = actSecDetailsRepo
					.findAllByProcourtdtlIDAndIsActive(hearingData.getProcourtdtl().getId(), 1);
			for (ActSecDetailsInfo Data : checkData) {
				if (Data.getAct().getId() == 1) {
					sec2.add(Data);
				} else if (Data.getAct().getId() == 6) {
					sec3.add(Data);
				} else {
					sec4.add(Data);
				}
			}
			rdto.setSec2(sec2);
			rdto.setSec3(sec3);
			rdto.setSec4(sec4);
			rdto.setSec(checkData);
			caselist.add(rdto);
		}
		modelMap.addAttribute("courtCasedtl", caselist);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		modelMap.addAttribute("message", "Weekly cause List from " + datestr + " to " + todatestr);
		return "report/WeaklyReportInput";
	}

	@RequestMapping(value = "weeklyListOfCasesWhereMCAIsParty")
	public String weeklyListOfCasesWhereMCAIsParty1(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/weeklyListOfCasesWhereMCAIsParty";
	}

	@RequestMapping(value = "weeklyListOfCasesWhereMCAIsParty1")
	public String weeklyListOfCasesWhereMCAIsParty(
			@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput, ModelMap modelMap)
			throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<WeeklyListOfCasesWhereMCAIsPartyDTO> courtCasedtl = new ArrayList<WeeklyListOfCasesWhereMCAIsPartyDTO>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {
			ProCourtCaseDetails pccd = proCourtCaseDetailsRepo
					.findALLByIdAndIsMCAParty(hearingDetails.getProcourtdtl().getId(), true);
			List<ActCompundRelevantSection> sec = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec1 = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec2 = new ArrayList<ActCompundRelevantSection>();
			if (pccd != null) {
				List<ChargeInstaceMain> chargeInstaceMain = chanrgeInstanceRepository.findByProcourtdtl(pccd);

				// List<ActSecDetailsInfo> findAll =
				// actSecDetailsRepo.findAllByProcourtdtlID(pccd.getId());
				List<ActCompundRelevantSection> allActSections = chargeInstaceMain.stream()
						.filter(cim -> cim.getActCompundRelevantSection() != null)
						.flatMap(cim -> cim.getActCompundRelevantSection().stream()).collect(Collectors.toList());

				if (!allActSections.isEmpty())

				{

					for (ActCompundRelevantSection Data : allActSections) {
						if (Data.getAct().getId() == 1) {
							sec.add(Data);

						} else if (Data.getAct().getId() == 6) {
							sec1.add(Data);

						} else {
							sec2.add(Data);
						}
					}
				}
			}
			if (pccd != null) {
				WeeklyListOfCasesWhereMCAIsPartyDTO weeklylistMca = new WeeklyListOfCasesWhereMCAIsPartyDTO();
				weeklylistMca.setHd1(hearingDetails);
				weeklylistMca.setCaseTitle(pccd.getAddCase().getCaseTitle());
				weeklylistMca.setCauseTitle(pccd.getCauseTitle());
				weeklylistMca.setBrief(pccd.getBrief());
				weeklylistMca.setIsWhetherreplyfiled(pccd.getIsWhetherreplyfiled());
				weeklylistMca.setSecq(sec);
				weeklylistMca.setSec1q(sec1);
				weeklylistMca.setSec2q(sec2);

				weeklylistMca.setCourtType(pccd.getCourtType().getCourtName());

				// PairaviDetails pairaviDetails =
				// pairaviDetailsRepo.findAllByProcourtdtlAndIsActive(pccd, true);
				List<PairaviDetails> findAllByProcourtdtl = pairaviDetailsRepo
						.findAllByProcourtdtlAndApproveStatus(pccd, 2);
				/*
				 * if (pairaviDetails != null) {
				 * weeklylistMca.setPairaviofficer(pairaviDetails.getName()+" 'C'"); }
				 */
				if (findAllByProcourtdtl != null) {
					for (PairaviDetails pairaviDetail : findAllByProcourtdtl) {
						if (pairaviDetail.getApproveStatus() == 2 && pairaviDetail.getIsActive() == false) {
							weeklylistMca.setEPairaviofficer(pairaviDetail.getPairaviOfficer().getName() + " 'E'");
						} else {
							weeklylistMca.setPairaviofficer(pairaviDetail.getPairaviOfficer().getName() + " 'C'");
						}
					}

				}

				courtCasedtl.add(weeklylistMca);
			}

		}

		modelMap.addAttribute("message", "Weekly List Of Cases Where MCA is Party " + datestr + " to " + todatestr);
		modelMap.addAttribute("courtCasedtl", courtCasedtl);

		return "report/weeklyListOfCasesWhereMCAIsParty";
	}
	/*
	 * 
	 * @RequestMapping(value = "weeklyListOfCasesWhereMCAIsParty1") public String
	 * weeklyListOfCasesWhereMCAIsParty(@ModelAttribute("reportWeeklyInput")
	 * ReportWeeklyInput reportWeeklyInput,ModelMap modelMap) throws ParseException
	 * {
	 * 
	 * String datestr = reportWeeklyInput.getDate(); String todatestr =
	 * reportWeeklyInput.getToDate();
	 * 
	 * Date fromDate = new SimpleDateFormat("dd-MM-yyyy",
	 * Locale.ENGLISH).parse(datestr); Date toDate = new
	 * SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);
	 * 
	 * 
	 * List<WeeklyListOfCasesWhereMCAIsPartyDTO> courtCasedtl = new
	 * ArrayList<WeeklyListOfCasesWhereMCAIsPartyDTO>();
	 * 
	 * 
	 * List<HearingDetails> hearingDeatils =
	 * hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
	 * toDate,2);
	 * 
	 * for (HearingDetails hearingDetails : hearingDeatils) {
	 * 
	 * 
	 * proCourtCaseDetails pccd =
	 * proCourtCaseDetailsRepo.findALLByIdAndIsMCAParty(hearingDetails.
	 * getProcourtdtl().getId(), true);
	 * 
	 * if (pccd!=null) { WeeklyListOfCasesWhereMCAIsPartyDTO weeklylistMca = new
	 * WeeklyListOfCasesWhereMCAIsPartyDTO(); weeklylistMca.setHd1(hearingDetails);
	 * weeklylistMca.setCaseTitle(pccd.getAddCase().getCaseTitle());
	 * weeklylistMca.setCauseTitle(pccd.getCauseTitle());
	 * weeklylistMca.setBrief(pccd.getBrief());
	 * weeklylistMca.setIsWhetherreplyfiled(pccd.getIsWhetherreplyfiled());
	 * 
	 * weeklylistMca.setCourtType(pccd.getCourtType().getCourtName());
	 * 
	 * PairaviDetails pairaviDetails =
	 * pairaviDetailsRepo.findAllByProcourtdtlAndIsActive(pccd, true); if
	 * (pairaviDetails != null) {
	 * weeklylistMca.setPairaviofficer(pairaviDetails.getName());
	 * 
	 * }
	 * 
	 * 
	 * courtCasedtl.add(weeklylistMca); }
	 * 
	 * }
	 * 
	 * modelMap.addAttribute("message",
	 * "Weekly List Of Cases Where MCA is Party "+datestr+ " to "+todatestr);
	 * modelMap.addAttribute("courtCasedtl", courtCasedtl);
	 * 
	 * return "report/weeklyListOfCasesWhereMCAIsParty"; }
	 */

	@RequestMapping(value = "monthlyProgressiiveReport")
	public String monthlyProgressiiveReport(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport1";
	}

	@RequestMapping(value = "monthlyProgresiveReport")

	public String monthlyProgresiveReport(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);
		List<WeeklyListOfCasesWhereMCAIsPartyDTO> courtCasedtl = new ArrayList<WeeklyListOfCasesWhereMCAIsPartyDTO>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			ProCourtCaseDetails pccd = proCourtCaseDetailsRepo
					.findALLByIdAndIsMCAParty(hearingDetails.getProcourtdtl().getId(), true);

			if (pccd != null) {
				WeeklyListOfCasesWhereMCAIsPartyDTO weeklylistMca = new WeeklyListOfCasesWhereMCAIsPartyDTO();

				weeklylistMca.setCaseTitle(pccd.getAddCase().getCaseTitle());
				// weeklylistMca.setCaseTitle(pccd.getAddCase().getCaseTitle());
				weeklylistMca.setCaseNo(pccd.getCourtCaseNo());
				weeklylistMca.setCauseTitle(pccd.getCauseTitle());
				weeklylistMca.setBrief(pccd.getBrief());
				weeklylistMca.setIsWhetherreplyfiled(pccd.getIsWhetherreplyfiled());
				weeklylistMca.setFilingDate(pccd.getFillingDate());
				weeklylistMca.setFy(pccd.getFinancialYear());
				weeklylistMca.setCounselOfficer(hearingDetails.getCounselName().getName());
				weeklylistMca.setCounselOfficerPhone(hearingDetails.getCounselName().getMobile());
				String dd111d = pccd.getFinancialYear();
				weeklylistMca.setCourtType(pccd.getCourtType().getCourtName());

				List<AccusedStatus> lst = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());

				if (!lst.isEmpty()) {
					hearingDetails.setLst(lst);
				}
				weeklylistMca.setHd1(hearingDetails);
				courtCasedtl.add(weeklylistMca);

			}
		}

		modelMap.addAttribute("message", "Monthly Progressive Report from " + datestr + " to " + todatestr);

		modelMap.addAttribute("courtCasedtl", courtCasedtl);
		return "report/MonthlyProgresiveReport1";
	}

	@RequestMapping(value = "monthlyProgressiiveReport8")
	public String monthlyProgressiiveReport8(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport8";
	}

	@RequestMapping(value = "monthlyProgresiveReport88")

	public String monthlyProgresiveReport88(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {
			List<Long> sectionIds = Arrays.asList(247L, 248L);
			List<AddActSec> sections = ncltActofRespondantRepository
					.findDistinctSectionsByAssignedTask1(hearingDetails.getAssignedTask().getId(), sectionIds);

			Date fillingDate = hearingDetails.getProcourtdtl().getFillingDate();
			if (!sections.isEmpty() && fillingDate == null) {

				List<AddAccused> companyAccused = addAccusedRepo
						.findAllByProcourtdtlAndAccusedType(hearingDetails.getProcourtdtl(), "Company");
				if (!companyAccused.isEmpty()) {
					hearingDetails.setCompanyAccused(companyAccused);
				}
				hearingDetails.setSections(sections);
				monthlyProgressivelist.add(hearingDetails);
			}
		}

		modelMap.addAttribute("message", "Monthly Progressive Report Point 8 from " + datestr + " to " + todatestr
				+ " Cases wherein section U/s 241/242 have been issued by Ministry but are pending in NCLT");

		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);
		return "report/MonthlyProgresiveReport8";

	}

	@RequestMapping(value = "monthlyProgressiiveReport9")
	public String monthlyProgressiiveReport9(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport9";
	}

	@RequestMapping(value = "monthlyProgresiveReport99")

	public String monthlyProgresiveReport99(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			if (hearingDetails.getStatus().getId() == 12) {
				// List<CaseCompany> company =
				// caseCompanyRepo.findByProcourtdtl(hearingDetails.getProcourtdtl());
				List<AddAccused> companyAccused = addAccusedRepo
						.findAllByProcourtdtlAndAccusedType(hearingDetails.getProcourtdtl(), "Company");

				hearingDetails.setCompanyAccused(companyAccused);

				monthlyProgressivelist.add(hearingDetails);
			}
		}

		modelMap.addAttribute("message", "Monthly Progressive Report -Point 9 from " + datestr + " to " + todatestr
				+ " Cases wherein sanction related to winding up have been issued by Ministry");

		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);
		return "report/MonthlyProgresiveReport9";

	}

	@RequestMapping(value = "monthlyProgressiiveReport7")
	public String monthlyProgressiiveReport7(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport7";
	}

	@RequestMapping(value = "monthlyProgresiveReport77")

	public String monthlyProgresiveReport77(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();
		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);
		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();
		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {
			if (hearingDetails.getProcourtdtl().getCourtType().getId() == 1) {
				monthlyProgressivelist.add(hearingDetails);
			}
		}
		modelMap.addAttribute("message", "Monthly Progressive Report -Point 7 from " + datestr + " to " + todatestr
				+ " Details of cases pending before High Courts");
		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);
		return "report/MonthlyProgresiveReport7";
	}

	@RequestMapping(value = "monthlyProgressiiveReport6")
	public String monthlyProgressiiveReport6(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport6";
	}

	@RequestMapping(value = "monthlyProgresiveReport66")

	public String monthlyProgresiveReport66(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			if (hearingDetails.getProcourtdtl().getCourtType().getId() == 2) {

				monthlyProgressivelist.add(hearingDetails);
			}
		}

		modelMap.addAttribute("message", "Monthly Progressive Report -Point 6 from " + datestr + " to " + todatestr
				+ " Details of cases pending before Supreme Court");

		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);
		return "report/MonthlyProgresiveReport6";

	}

	@RequestMapping(value = "monthlyProgressiiveReport5")
	public String monthlyProgressiiveReport5(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport5";
	}

	@RequestMapping(value = "monthlyProgresiveReport55")

	public String monthlyProgresiveReport55(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			if (hearingDetails.getStatus().getId() == 10) {

				monthlyProgressivelist.add(hearingDetails);
			}
		}

		modelMap.addAttribute("message", "Monthly Progressive Report -Point 5 from " + datestr + " to " + todatestr
				+ " DETAILS OF PENDING INVESTIGATIONS ON ACCOUNT OF STAY ORDERED BY THE HON’BLE COURTS");

		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);
		return "report/MonthlyProgresiveReport5";

	}

	@RequestMapping(value = "monthlyProgressiiveReport4")
	public String monthlyProgressiiveReport4(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		// List<proCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findAll();
		// modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		// return "report/MonthlyProgresiveReport4";
		return "report/MonthlyProgresiveReport4";
	}

	@RequestMapping(value = "monthlyProgresiveReport44")
	// Date 12.06.2023 Change Logic

	public String monthlyProgresiveReport441(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			List<Long> sectionIds = Arrays.asList(226L, 247L, 248L);
			List<AddActSec> sections = ncltActofRespondantRepository
					.findDistinctSectionsByAssignedTask1(hearingDetails.getAssignedTask().getId(), sectionIds);

			Date fillingDate = hearingDetails.getProcourtdtl().getFillingDate();
			if (!sections.isEmpty() && fillingDate == null) {

				List<AddAccused> companyAccused = addAccusedRepo
						.findAllByProcourtdtlAndAccusedType(hearingDetails.getProcourtdtl(), "Company");
				if (!companyAccused.isEmpty()) {
					hearingDetails.setCompanyAccused(companyAccused);
				}

				hearingDetails.setSections(sections);
				monthlyProgressivelist.add(hearingDetails);

			}

		}

		modelMap.addAttribute("message", "Monthly Progressive Report -Point 4 from " + datestr + " to " + todatestr
				+ " Sanction issued under section 241/242/ 212(14A) of the Companies Act, 2013 but cases are yet to be filed");

		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);

		return "report/MonthlyProgresiveReport4";

	}

	@RequestMapping(value = "monthlyProgressiiveReport16")
	public String monthlyProgressiiveReport16(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		List<Status> statusList = addStatusRepo.findAllByTypeAndIsActive("A", true);

		modelMap.addAttribute("statusLst", statusList);
		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		return "report/MonthlyProgresiveReport16";
	}

	@RequestMapping(value = "monthlyProgresiveReport160")
	public String monthlyProgresiveReport160(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);

		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		List<HearingDetails> filteredList = hearingDeatils.stream()
				.filter(d -> d.getProcourtdtl().getTypeOfCase().getId() == 35).collect(Collectors.toList());

		for (HearingDetails hearingDetails : hearingDeatils) {
			List<AccusedStatus> lst = AccusedStatusRepo.findByHearingDetails(hearingDetails.getId());
			hearingDetails.setLst(lst);
		}

		modelMap.addAttribute("message", "Monthly Progressive Report -Point 16 from " + datestr + " to " + todatestr
				+ " Details of cases before ICAI/ICSI/NFRA");

		modelMap.addAttribute("courtCasedtl", filteredList);

		return "report/MonthlyProgresiveReport16";

	}

	@RequestMapping(value = "monthlyProgressiiveReport3")
	public String monthlyProgressiiveReport3(ModelMap modelMap) throws Exception {
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("userRole", userrole);
		Date m3 = ReportController.old3month();
		Date m6 = ReportController.old6month();
		Date m12 = ReportController.old12month();
		// proCourtCaseDetails CourtCaseDetails=new proCourtCaseDetails();
		List<ProCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findByApproveStatus(2);
		for (ProCourtCaseDetails proCourtCaseDetails : courtCasedtl) {

			List<ActCompundRelevantSection> sec = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec1 = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec2 = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec3 = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec4 = new ArrayList<ActCompundRelevantSection>();
			List<ActCompundRelevantSection> sec5 = new ArrayList<ActCompundRelevantSection>();
			HearingDetails hd = hearingDetailsRepos
					.findByProcourtdtlAndCurrentStatusAndApproveStatus(proCourtCaseDetails, true, 2);
			// List<ActSecDetailsInfo> findAll =
			// actSecDetailsRepo.findAllByProcourtdtlID(proCourtCaseDetails.getId());

			Long courtId = proCourtCaseDetails.getId();
			List<ActCompundRelevantSection> findAll = actReportDao.getActAndSectionByCourtCaseId(courtId.intValue());

			for (ActCompundRelevantSection Data : findAll) {
				if (Data.getActId() == 1) {
					sec.add(Data);
					modelMap.addAttribute("sec", sec);
				} else if (Data.getActId() == 6) {
					sec1.add(Data);
					modelMap.addAttribute("sec1", sec1);
				} else if (Data.getActId() == 7) {
					sec2.add(Data);
					modelMap.addAttribute("sec2", sec2);
				} else if (Data.getActId() == 8) {
					sec3.add(Data);
					modelMap.addAttribute("sec3", sec3);
				} else if (Data.getActId() == 2) {
					sec4.add(Data);
					modelMap.addAttribute("sec4", sec4);
				} else {
					sec5.add(Data);
					modelMap.addAttribute("sec5", sec5);
				}

				proCourtCaseDetails.setSec(sec);
				proCourtCaseDetails.setSec1(sec1);
				proCourtCaseDetails.setSec2(sec2);
				proCourtCaseDetails.setSec3(sec3);
				proCourtCaseDetails.setSec4(sec4);
				proCourtCaseDetails.setSec5(sec5);
			}
			if (hd != null) {
				proCourtCaseDetails.setHearingDetailsstatus(hd.getStatus().getStatusName());
				proCourtCaseDetails.setCounselOfficer(hd.getCounselName().getName());
				proCourtCaseDetails.setCounselOfficerPhone(hd.getCounselName().getMobile());
			}
			// Date d1 = proCourtCaseDetails.getProDate();
			Date d1 = proCourtCaseDetails.getAddCase().getProSanctionDate();
			Date corrigendumDate = proCourtCaseDetails.getCorrigendumDate();
			if (corrigendumDate != null) {
				if (corrigendumDate.compareTo(m3) > 0) {
					System.out.println("Date 1 occurs after Date 2");
					proCourtCaseDetails.setCorrigendumDateView("three");
				} else if (corrigendumDate.compareTo(m3) < 0 && corrigendumDate.compareTo(m6) > 0) {
					proCourtCaseDetails.setCorrigendumDateView("six");
					System.out.println("Date 1 occurs before Date 2");
				} else if (corrigendumDate.compareTo(m6) < 0 && corrigendumDate.compareTo(m12) > 0) {
					proCourtCaseDetails.setCorrigendumDateView("nine");
					System.out.println("Both dates are equal");
				} else if (corrigendumDate.compareTo(m12) < 0) {
					proCourtCaseDetails.setCorrigendumDateView("oneyear");
					System.out.println("Both dates are equal");
				}
			}
			if (d1 != null) {
				if (d1.compareTo(m3) > 0) {
					System.out.println("Date 1 occurs after Date 2");
					proCourtCaseDetails.setThreeMonth("three");
				} else if (d1.compareTo(m3) < 0 && d1.compareTo(m6) > 0) {
					proCourtCaseDetails.setThreeMonth("six");
					System.out.println("Date 1 occurs before Date 2");
				} else if (d1.compareTo(m6) < 0 && d1.compareTo(m12) > 0) {
					proCourtCaseDetails.setThreeMonth("nine");
					System.out.println("Both dates are equal");
				} else if (d1.compareTo(m12) < 0) {
					proCourtCaseDetails.setThreeMonth("oneyear");
					System.out.println("Both dates are equal");
				}
			}
		}

		modelMap.addAttribute("reportWeeklyInput", new ReportWeeklyInput());
		modelMap.addAttribute("courtCasedtl", courtCasedtl);
		modelMap.addAttribute("message", "Monthly Progressive Report-Point 3");
		return "report/MonthlyProgresiveReport3";
	}

	public static Date next7days() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.DAY_OF_MONTH, +6);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		Date todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(currentdate2);
		return todate;

	}

	public static Date old3month() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.MONTH, -3);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		Date todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(currentdate2);
		return todate;

	}

	public static Date old6month() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.MONTH, -6);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		Date todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(currentdate2);
		return todate;

	}

	public static Date old12month() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date currentdate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);
		c.add(Calendar.MONTH, -12);
		Date oldcurrent = c.getTime();

		String currentdate2 = dateFormat.format(oldcurrent);
		Date todate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(currentdate2);
		return todate;

	}

}
