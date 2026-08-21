package com.pams.Restcontrollers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pams.dao.ActReportDao;
import com.pams.dao.AppRoleDAO;
import com.pams.dto.HearingDetailsDto;
import com.pams.dto.HearingDetailsReport5Dto;
import com.pams.dto.MonthlyProgressiveReportDto16;
import com.pams.dto.MonthlyProgressiveReportDto3;
import com.pams.dto.MonthlyProgressiveReportDto4;
import com.pams.dto.MonthlyReportPoint8;
import com.pams.dto.MonthlyReportPoint9;
import com.pams.dto.ReportWeeklyMCAIsPartyDto;
import com.pams.dto.WeeklyCauseListReportDTO;
import com.pams.entity.AccusedStatus;
import com.pams.entity.ActCompundRelevantSection;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddAccused;
import com.pams.entity.AddActSec;
import com.pams.entity.HearingDetails;
import com.pams.entity.PairaviDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AccusedStatusRepository;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.NCLTActofRespondantRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/reports")
public class ReportControllerApi {
	@Autowired
	private HearingDetailsRepository hearingDetailsRepos;
	@Autowired
	private AddAccusedRepository addAccusedRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private NCLTActofRespondantRepository ncltActofRespondantRepository;
	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private PairaviDetailsRepository pairaviDetailsRepo;
	@Autowired
	private ActSecDetailsRepository actSecDetailsRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private ActReportDao actReportDao;
	@Autowired
	private AccusedStatusRepository accusedStatusRepo;

	@GetMapping("/weeklycause1")
	public ResponseEntity<?> getWeeklyCauseList() throws Exception {

		// 🔹 Static values
		String datestr = "01-09-2023";
		String todatestr = "07-09-2026";

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<WeeklyCauseListReportDTO> caselist = new ArrayList<>();

		List<HearingDetails> hearingdata = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2, Sort.by(Sort.Direction.ASC, "procourtdtl"));

		for (HearingDetails hearingData : hearingdata) {

			List<HearingDetails> lastHearingData = hearingDetailsRepos
					.findByProcourtdtlAndNextHearingDateAndCurrentStatus(hearingData.getProcourtdtl(),
							hearingData.getLastHearingDate(), false);

			WeeklyCauseListReportDTO rdto = new WeeklyCauseListReportDTO();

			if (!lastHearingData.isEmpty()) {
				rdto.setLastcaseStatus(lastHearingData.get(0).getStatus().getStatusName());
				rdto.setCounselnameLast(lastHearingData.get(0).getCounselName().getName());
			}

			rdto.setCaseTitle(hearingData.getProcourtdtl().getAddCase().getCaseTitle());
			rdto.setCourtName(hearingData.getProcourtdtl().getCourtType().getCourtName());
			rdto.setState(hearingData.getProcourtdtl().getState().getState());
			rdto.setBench(hearingData.getProcourtdtl().getBench_Name().getBench());
			rdto.setCauseTitle(hearingData.getProcourtdtl().getCauseTitle());
			rdto.setBrief(hearingData.getProcourtdtl().getBrief());

			rdto.setCaseStatus(hearingData.getStatus().getStatusName());
			rdto.setCourtName(hearingData.getProcourtdtl().getCourtCaseNo());
			rdto.setPairaviName(hearingData.getOfficer().getName());
			rdto.setCounselname(hearingData.getCounselName().getName());
			rdto.setCounselmob(hearingData.getCounselName().getMobile());
			rdto.setPairavimob(hearingData.getOfficer().getMobile());

			rdto.setCounselnameLast(hearingData.getOfficer().getName());

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			if (hearingData.getLastHearingDate() != null) {
				String lastHearingDateStr = sdf.format(hearingData.getLastHearingDate());
				rdto.setLastHearingDetails(lastHearingDateStr);
			}

			if (hearingData.getNextHearingDate() != null) {
				String nextHearingDateStr = sdf.format(hearingData.getNextHearingDate());
				rdto.setNextHearingDate(nextHearingDateStr);
			}

			PairaviDetails temp2 = pairaviDetailsRepo
					.findAllByProcourtdtlAndIsActiveAndApproveStatus(hearingData.getProcourtdtl(), true, 2);

			rdto.setCounselnameLast(hearingData.getAssignedTask().getUser().getFirstName());
			rdto.setLastcaseStatus(hearingData.getStatus().getStatusName());
			if (temp2 != null) {
				rdto.setPairaviName(temp2.getPairaviOfficer().getName());
			}

			List<ActSecDetailsInfo> sec2 = new ArrayList<>();
			List<ActSecDetailsInfo> sec3 = new ArrayList<>();
			List<ActSecDetailsInfo> sec4 = new ArrayList<>();

			List<ActSecDetailsInfo> checkData = actSecDetailsRepo
					.findAllByProcourtdtlIDAndIsActive(hearingData.getProcourtdtl().getId(), 1);

			for (ActSecDetailsInfo data : checkData) {
				if (data.getAct().getId() == 1) {
					sec2.add(data);
				} else if (data.getAct().getId() == 6) {
					sec3.add(data);
				} else {
					sec4.add(data);
				}
			}

			rdto.setSec2(sec2);
			rdto.setSec3(sec3);
			rdto.setSec4(sec4);
			rdto.setSec(checkData);

			caselist.add(rdto);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Weekly cause List from " + datestr + " to " + todatestr);
		response.put("data", caselist);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthlyProgressive3")
	public ResponseEntity<?> monthlyProgressiveReport3Public() {
		try {
			// Static date range (change as required)
			String datestr = "01-06-2025";
			String todatestr = "30-08-2025";
			Date m3 = old3month();
			Date m6 = old6month();
			Date m12 = old12month();

			List<ProCourtCaseDetails> courtCasedtl = proCourtCaseDetailsRepo.findByApproveStatus(2);
			List<MonthlyProgressiveReportDto3> dto3 = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			for (ProCourtCaseDetails proCourtCaseDetails : courtCasedtl) {
				// prepare act sections lists
				MonthlyProgressiveReportDto3 ffff = new MonthlyProgressiveReportDto3();
				List<ActCompundRelevantSection> sec = new ArrayList<>();
				List<ActCompundRelevantSection> sec1 = new ArrayList<>();
				List<ActCompundRelevantSection> sec2 = new ArrayList<>();
				List<ActCompundRelevantSection> sec3 = new ArrayList<>();
				List<ActCompundRelevantSection> sec4 = new ArrayList<>();
				List<ActCompundRelevantSection> sec5 = new ArrayList<>();

				HearingDetails hd = hearingDetailsRepos
						.findByProcourtdtlAndCurrentStatusAndApproveStatus(proCourtCaseDetails, true, 2);

				Long courtId = proCourtCaseDetails.getId();
				List<ActCompundRelevantSection> findAll = actReportDao
						.getActAndSectionByCourtCaseId(courtId.intValue());
				if (findAll != null) {
					for (ActCompundRelevantSection data : findAll) {
						int actId = data.getActId();
						if (actId == 1)
							sec.add(data);
						else if (actId == 6)
							sec1.add(data);
						else if (actId == 7)
							sec2.add(data);
						else if (actId == 8)
							sec3.add(data);
						else if (actId == 2)
							sec4.add(data);
						else
							sec5.add(data);
					}
				}

				ffff.setSec(sec);
				ffff.setSec1(sec1);
				ffff.setSec2(sec2);
				ffff.setSec3(sec3);
				ffff.setSec4(sec4);
				ffff.setSec5(sec5);

				if (hd != null && hd.getStatus() != null) {
					proCourtCaseDetails.setHearingDetailsstatus(hd.getStatus().getStatusName());
				}

				Date sanctionDate = proCourtCaseDetails.getAddCase() != null
						? proCourtCaseDetails.getAddCase().getProSanctionDate()
						: null;
				Date corrigendumDate = proCourtCaseDetails.getCorrigendumDate();

				if (corrigendumDate != null) {
					if (corrigendumDate.compareTo(m3) > 0)
						ffff.setCorrigendumDateView("three");
					else if (corrigendumDate.compareTo(m3) < 0 && corrigendumDate.compareTo(m6) > 0)
						ffff.setCorrigendumDateView("six");
					else if (corrigendumDate.compareTo(m6) < 0 && corrigendumDate.compareTo(m12) > 0)
						ffff.setCorrigendumDateView("nine");
					else if (corrigendumDate.compareTo(m12) < 0)
						ffff.setCorrigendumDateView("oneyear");
				}

				if (sanctionDate != null) {
					if (sanctionDate.compareTo(m3) > 0)
						ffff.setThreeMonth("three");
					else if (sanctionDate.compareTo(m3) < 0 && sanctionDate.compareTo(m6) > 0)
						ffff.setThreeMonth("six");
					else if (sanctionDate.compareTo(m6) < 0 && sanctionDate.compareTo(m12) > 0)
						ffff.setThreeMonth("nine");
					else if (sanctionDate.compareTo(m12) < 0)
						ffff.setThreeMonth("oneyear");
				}

				ffff.setCauseTitle(proCourtCaseDetails.getCauseTitle());
				if (proCourtCaseDetails.getAddCase().getProSanctionDate() != null) {
					ffff.setProSanctionDate(sdf.format(proCourtCaseDetails.getAddCase().getProSanctionDate()));
				}

				if (proCourtCaseDetails.getCorrigendumDate() != null) {
					ffff.setCorrigendumDate(sdf.format(proCourtCaseDetails.getCorrigendumDate()));
				}

				ffff.setHearingDetailsstatus(proCourtCaseDetails.getHearingDetailsstatus());
				dto3.add(ffff);

			}

			Map<String, Object> resp = new HashMap<>();
			resp.put("message", "Monthly Progressive Report-Point 3 (public)");
			resp.put("data", dto3);
			return ResponseEntity.ok(resp);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Failed to generate monthly progressive report");
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/weeklyListOfCasesWhereMCAIsParty")
	public ResponseEntity<?> getWeeklyListOfCasesWhereMCAIsParty() {
		try {

			String fromDate = "01-06-2021";
			String toDate = "30-08-2026";

			Date from = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(fromDate);
			Date to = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(toDate);

			List<ReportWeeklyMCAIsPartyDto> courtCasedtl = new ArrayList<>();

			List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(from,
					to, 2);

			for (HearingDetails hearingDetails : hearingDeatils) {

				ProCourtCaseDetails pccd = proCourtCaseDetailsRepo
						.findALLByIdAndIsMCAParty(hearingDetails.getProcourtdtl().getId(), true);

				if (pccd != null) {
					List<ActSecDetailsInfo> sec = new ArrayList<>();
					List<ActSecDetailsInfo> sec1 = new ArrayList<>();
					List<ActSecDetailsInfo> sec2 = new ArrayList<>();

					List<ActSecDetailsInfo> findAll = actSecDetailsRepo.findAllByProcourtdtlID(pccd.getId());
					if (!findAll.isEmpty()) {
						for (ActSecDetailsInfo Data : findAll) {
							if (Data.getAct().getId() == 1) {
								sec.add(Data);
							} else if (Data.getAct().getId() == 6) {
								sec1.add(Data);
							} else {
								sec2.add(Data);
							}
						}
					}

					ReportWeeklyMCAIsPartyDto weeklylistMca = new ReportWeeklyMCAIsPartyDto();
					// weeklylistMca.setHd1(hearingDetails);
					weeklylistMca.setCaseTitle(pccd.getAddCase().getCaseTitle());
					weeklylistMca.setCauseTitle(pccd.getCauseTitle());
					weeklylistMca.setBrief(pccd.getBrief());
				//	weeklylistMca.setIsWhetherreplyfiled(pccd.getIsWhetherreplyfiled());
					weeklylistMca.setSec(sec);
					weeklylistMca.setSec1(sec1);
					weeklylistMca.setSec2(sec2);
					weeklylistMca.setCourtType(pccd.getCourtType().getCourtName());

					List<PairaviDetails> findAllByProcourtdtl = pairaviDetailsRepo
							.findAllByProcourtdtlAndApproveStatus(pccd, 2);

					if (findAllByProcourtdtl != null) {
						for (PairaviDetails pairaviDetail : findAllByProcourtdtl) {
							if (pairaviDetail.getApproveStatus() == 2 && !pairaviDetail.getIsActive()) {
								weeklylistMca.setEPairaviofficer(pairaviDetail.getPairaviOfficer().getName() + " 'E'");
							} else {
								weeklylistMca.setPairaviofficer(pairaviDetail.getPairaviOfficer().getName() + " 'C'");
							}
						}
					}

					courtCasedtl.add(weeklylistMca);
				}
			}

			// JSON response
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Weekly List Of Cases Where MCA is Party " + fromDate + " to " + toDate);
			// response.put("userRole", userrole);
			response.put("data", courtCasedtl);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Failed to fetch Weekly List Of Cases Where MCA is Party");
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/monthlyprogressivereport")
	public ResponseEntity<Map<String, Object>> monthlyProgresiveReport() throws Exception {

		// String userrole =
		// appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		String datestr = "01-06-2021";
		String todatestr = "30-08-2026";

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<ReportWeeklyMCAIsPartyDto> courtCasedtl = new ArrayList<>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			ProCourtCaseDetails pccd = proCourtCaseDetailsRepo
					.findALLByIdAndIsMCAParty(hearingDetails.getProcourtdtl().getId(), true);

			if (pccd != null) {
				ReportWeeklyMCAIsPartyDto weeklylistMca = new ReportWeeklyMCAIsPartyDto();

				weeklylistMca.setCaseTitle(pccd.getAddCase().getCaseTitle());
				weeklylistMca.setCaseNo(pccd.getCourtCaseNo());
				weeklylistMca.setCauseTitle(pccd.getCauseTitle());
				weeklylistMca.setBrief(pccd.getBrief());
			//	weeklylistMca.setIsWhetherreplyfiled(pccd.getIsWhetherreplyfiled());
				weeklylistMca.setFilingDate(pccd.getFillingDate());
				weeklylistMca.setFy(pccd.getFinancialYear());
				weeklylistMca.setCourtType(pccd.getCourtType().getCourtName());

				List<AccusedStatus> lst = accusedStatusRepo.findByHearingDetails(hearingDetails.getId());
				if (!lst.isEmpty()) {
					hearingDetails.setLst(lst);
				}

				courtCasedtl.add(weeklylistMca);
			}
		}

		Map<String, Object> response = new HashMap<>();

		response.put("message", "Monthly Progressive Report from " + datestr + " to " + todatestr);
		response.put("courtCasedtl", courtCasedtl);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthlyprogressivereport66")
	public ResponseEntity<Map<String, Object>> monthlyProgresiveReport66() throws Exception {

		String datestr = "01-06-2021";
		String todatestr = "30-08-2026";
		// String userrole =
		// appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		// List<HearingDetails> monthlyProgressivelist = new ArrayList<>();
		List<HearingDetailsDto> monthlyProgressivelist = new ArrayList<>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			if (hearingDetails.getProcourtdtl().getCourtType().getId() == 2) {
				HearingDetailsDto dto = new HearingDetailsDto();
				dto.setCaseTitle(hearingDetails.getProcourtdtl().getAddCase().getCaseTitle());
				dto.setCourtCaseNo(hearingDetails.getProcourtdtl().getCourtCaseNo());
				dto.setCauseTitle(hearingDetails.getProcourtdtl().getCauseTitle());
				dto.setLastHearingDate(hearingDetails.getLastHearingDate());
				dto.setNextHearingDate(hearingDetails.getNextHearingDate());
				dto.setCounselOfficerName(
						hearingDetails.getCounselName().getName() + " " + hearingDetails.getOfficer().getName());
				dto.setStatusName(hearingDetails.getStatus().getStatusName());
				monthlyProgressivelist.add(dto);
			}
		}

		// Response Map बनाना
		Map<String, Object> response = new HashMap<>();
		// response.put("userRole", userrole);
		response.put("message", "Monthly Progressive Report - Point 6 from " + datestr + " to " + todatestr
				+ " | Details of cases pending before Supreme Court");
		response.put("courtCasedtl", monthlyProgressivelist);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthlyProgressiveReport77")
	public ResponseEntity<Map<String, Object>> getMonthlyProgressiveReport77() {

		Map<String, Object> response = new HashMap<>();
		try {

			String datestr = "01-06-2021";
			String todatestr = "30-08-2026";

			Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
			Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

			List<HearingDetails> hearingDetailsList = hearingDetailsRepos
					.findByNextHearingDateBetweenAndApproveStatus(fromDate, toDate, 2);

			List<HearingDetailsDto> monthlyProgressiveList = hearingDetailsList.stream()
					.filter(h -> h.getProcourtdtl() != null && h.getProcourtdtl().getCourtType() != null
							&& h.getProcourtdtl().getCourtType().getId() == 1)
					.map(s -> {
						HearingDetailsDto dto = new HearingDetailsDto();
						dto.setCaseTitle(s.getProcourtdtl().getAddCase().getCaseTitle());
						dto.setCourtCaseNo(s.getProcourtdtl().getCourtCaseNo());
						dto.setCauseTitle(s.getProcourtdtl().getCauseTitle());
						dto.setLastHearingDate(s.getLastHearingDate());
						dto.setNextHearingDate(s.getNextHearingDate());
						dto.setCounselOfficerName(s.getCounselName().getName() + " " + s.getOfficer().getName());
						dto.setStatusName(s.getStatus().getStatusName());
						return dto;
					}).collect(Collectors.toList());

			response.put("message", "Monthly Progressive Report - Point 7 from " + fromDate + " to " + toDate
					+ " | Details of cases pending before High Courts");
			response.put("courtCaseDetails", monthlyProgressiveList);
			response.put("totalCount", monthlyProgressiveList.size());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("error", "Error generating report: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/monthlyprogressivereport99")
	public ResponseEntity<?> monthlyProgressiveReport99() {
		Map<String, Object> resp = new HashMap<>();
		try {

			String datestr = "01-06-2021";
			String todatestr = "30-08-2026";

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
			Date fromDate = sdf.parse(datestr);
			Date toDate = sdf.parse(todatestr);

			List<HearingDetails> hearingDetailsList = hearingDetailsRepos
					.findByNextHearingDateBetweenAndApproveStatus(fromDate, toDate, 2);

			List<MonthlyReportPoint9> monthlyProgressivelist = new ArrayList<>();

			if (hearingDetailsList != null) {
				for (HearingDetails hearingDetails : hearingDetailsList) {
					MonthlyReportPoint9 dto = new MonthlyReportPoint9();
					if (hearingDetails == null)
						continue;

					if (hearingDetails.getStatus() != null && hearingDetails.getStatus().getId() == 12) {

						try {
							List<AddAccused> companyAccused = addAccusedRepo
									.findAllByProcourtdtlAndAccusedType(hearingDetails.getProcourtdtl(), "Company");
							dto.setCauseTitle(hearingDetails.getProcourtdtl().getCauseTitle());
							dto.setCompanyAccused(
									companyAccused.stream().map(l -> l.getAccusedName()).collect(Collectors.toList()));
							dto.setStatusFilled(!companyAccused.isEmpty() ? true : false);
							dto.setLastHearingDate(hearingDetails.getLastHearingDate());
							dto.setNextHearingDate(hearingDetails.getNextHearingDate());

						} catch (Exception e) {

							dto.setCompanyAccused(Collections.emptyList());
						}

						monthlyProgressivelist.add(dto);
					}
				}
			}

			resp.put("message", "Monthly Progressive Report - Point 9 from " + datestr + " to " + todatestr
					+ " | Cases wherein sanction related to winding up have been issued by Ministry");
			resp.put("totalCount", monthlyProgressivelist.size());
			resp.put("data", monthlyProgressivelist);

			return ResponseEntity.ok(resp);

		} catch (Exception ex) {
			Map<String, Object> err = new HashMap<>();
			err.put("message", "Failed to generate report");
			err.put("error", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
		}
	}

	@GetMapping("/monthlyprogressivereport55")
	public ResponseEntity<Map<String, Object>> monthlyProgresiveReport55() throws Exception {

		String datestr = "01-06-2021";
		String todatestr = "30-08-2026";

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetailsReport5Dto> monthlyProgressivelist = new ArrayList<>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {

			if (hearingDetails.getStatus().getId() == 10) {
				HearingDetailsReport5Dto dto = new HearingDetailsReport5Dto();
				dto.setCaseTitle(hearingDetails.getProcourtdtl().getAddCase().getCaseTitle());
				dto.setDateOfOrder(hearingDetails.getDateOfOrder());
				dto.setNextHearingDate(hearingDetails.getNextHearingDate());
				dto.setReasonofStay(hearingDetails.getReasonofStay());
				monthlyProgressivelist.add(dto);
			}
		}

		Map<String, Object> response = new HashMap<>();

		response.put("message", "Monthly Progressive Report - Point 5 from " + datestr + " to " + todatestr
				+ " | DETAILS OF PENDING INVESTIGATIONS ON ACCOUNT OF STAY ORDERED BY THE HON’BLE COURTS");
		response.put("courtCasedtl", monthlyProgressivelist);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthlyProgressiveReport4")
	public ResponseEntity<Map<String, Object>> monthlyProgressiveReport4() throws Exception {
		String datestr = "01-06-2021";
		String todatestr = "30-08-2026";
		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);
		List<MonthlyProgressiveReportDto4> dto = new ArrayList<>();
		List<HearingDetails> hearingDetailsList = hearingDetailsRepos
				.findByNextHearingDateBetweenAndApproveStatus(fromDate, toDate, 2);

		List<Long> sectionIds = Arrays.asList(226L, 247L, 248L);

		for (HearingDetails hearingDetails : hearingDetailsList) {
			List<AddActSec> sections = ncltActofRespondantRepository
					.findDistinctSectionsByAssignedTask1(hearingDetails.getAssignedTask().getId(), sectionIds);

			Date fillingDate = hearingDetails.getProcourtdtl().getFillingDate();
			if (!sections.isEmpty() && fillingDate == null) {
				List<AddAccused> companyAccused = addAccusedRepo
						.findAllByProcourtdtlAndAccusedType(hearingDetails.getProcourtdtl(), "Company");
				MonthlyProgressiveReportDto4 mmmm = new MonthlyProgressiveReportDto4();
				if (!companyAccused.isEmpty()) {
					List<String> collect = companyAccused.stream().map(o -> o.getAccusedName())
							.collect(Collectors.toList());
					mmmm.setAccusedName(collect);
				}
				mmmm.setDateOfInstruction(hearingDetails.getNextHearingDate());
				List<String> sectionss = sections.stream().map(j -> j.getSection()).collect(Collectors.toList());
				mmmm.setSections(sectionss);
				if (hearingDetails.getProcourtdtl().getBench_Name().getId() == 0) {
					mmmm.setNcltBenchName(hearingDetails.getProcourtdtl().getState().getState());
				} else {
					mmmm.setNcltBenchName(hearingDetails.getProcourtdtl().getBench_Name().getBench());
				}
				mmmm.setStatus(hearingDetails.getStatus().getStatusName());
				dto.add(mmmm);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Monthly Progressive Report - Point 4 from " + datestr + " to " + todatestr
				+ " Sanction issued under section 241/242/212(14A) of the Companies Act, 2013 but cases are yet to be filed");
		response.put("courtCaseDetails", dto);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthlyProgressiveReport8")
	public ResponseEntity<?> monthlyProgressiveReport8() {
		Map<String, Object> response = new HashMap<>();

		try {

			String datestr = "01-06-2021";
			String todatestr = "30-08-2026";
			Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
			Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

			List<HearingDetails> hearingDeatils = hearingDetailsRepos
					.findByNextHearingDateBetweenAndApproveStatus(fromDate, toDate, 2);

			List<HearingDetails> monthlyProgressivelist = new ArrayList<>();
			List<MonthlyReportPoint8> dto = new ArrayList<>();

			List<Long> sectionIds = Arrays.asList(247L, 248L);

			for (HearingDetails hearingDetails : hearingDeatils) {
				if (hearingDetails == null || hearingDetails.getAssignedTask() == null)
					continue;

				List<AddActSec> sections = ncltActofRespondantRepository
						.findDistinctSectionsByAssignedTask1(hearingDetails.getAssignedTask().getId(), sectionIds);

				Date fillingDate = hearingDetails.getProcourtdtl() != null
						? hearingDetails.getProcourtdtl().getFillingDate()
						: null;
				MonthlyReportPoint8 ll = new MonthlyReportPoint8();
				if (sections != null && !sections.isEmpty() && fillingDate == null) {

					List<AddAccused> companyAccused = addAccusedRepo
							.findAllByProcourtdtlAndAccusedType(hearingDetails.getProcourtdtl(), "Company");

					if (companyAccused != null && !companyAccused.isEmpty()) {
						List<String> collect = companyAccused.stream().map(o -> o.getAccusedName())
								.collect(Collectors.toList());
						ll.setAccusedName(collect);
						ll.setCausTitle(!hearingDetails.getProcourtdtl().getCauseTitle().isEmpty()?hearingDetails.getProcourtdtl().getCauseTitle():"NA");
						
						List<String> sectionss = sections.stream().map(j -> j.getSection())
								.collect(Collectors.toList());
						ll.setSections(sectionss);

					} else {
						ll.setAccusedName(Collections.emptyList());
					}
					ll.setLastDate(hearingDetails.getLastHearingDate());
					ll.setNextDate(hearingDetails.getNextHearingDate());
					ll.setStatus(hearingDetails.getStatus().getStatusName());
					dto.add(ll);

				}
			}

			response.put("message", "Monthly Progressive Report - Point 8 from " + datestr + " to " + todatestr
					+ " | Cases wherein section U/s 241/242 have been issued by Ministry but are pending in NCLT");
			response.put("totalCount", monthlyProgressivelist.size());
			response.put("data", dto);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("error", "Error generating report: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	@GetMapping("/monthlyProgressiveReport16")
    public ResponseEntity<?> monthlyProgressiveReport16() {
        Map<String, Object> resp = new HashMap<>();
        try {
        	String datestr = "01-06-2021";
			String todatestr = "30-08-2026";
			Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
			Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);
            List<HearingDetails> hearingDeatils = hearingDetailsRepos
                    .findByNextHearingDateBetweenAndApproveStatus(fromDate, toDate, 2);

            if (hearingDeatils == null) hearingDeatils = Collections.emptyList();

            List<HearingDetails> filteredList = hearingDeatils.stream()
                    .filter(d -> d != null
                            && d.getProcourtdtl() != null
                            && d.getProcourtdtl().getTypeOfCase() != null
                            && d.getProcourtdtl().getTypeOfCase().getId() == 35)
                    .collect(Collectors.toList());

            List<MonthlyProgressiveReportDto16> dto=new ArrayList<>();
            
            for (HearingDetails hd : filteredList) {
            	MonthlyProgressiveReportDto16 ff=new MonthlyProgressiveReportDto16();
                try {
                	
                	ff.setCaseTitle(hd.getProcourtdtl().getAddCase().getCaseTitle());
                    List<AccusedStatus> lst = accusedStatusRepo.findByHearingDetails(hd.getId());
                    List<String> collect = lst.stream().map(f->f.getAddAccused().getAccusedName()).collect(Collectors.toList());
                   ff.setCaseNumber(hd.getProcourtdtl().getCourtCaseNo());
                   ff.setCauseTitle(hd.getProcourtdtl().getCauseTitle());
                   ff.setFillingDate(hd.getProcourtdtl().getFillingDate());
                   ff.setFinancialYear(hd.getProcourtdtl().getFinancialYear());
                   ff.setCourtName(hd.getProcourtdtl().getCourtType().getCourtName());
                   ff.setNextdateHearing(hd.getNextHearingDate());
                   List<String> collect2 = lst.stream().map(f->f.getCaseStatus().getStatusName()).collect(Collectors.toList());
                   ff.setStatusName(collect2);
                    dto.add(ff);
                    ff.setLst(collect != null ? collect : Collections.emptyList());
                } catch (Exception e) {
                    ff.setLst(Collections.emptyList());
                }
            }

           
            resp.put("message", "Monthly Progressive Report - Point 16 from " +datestr
                    + " to " + todatestr + " | Details of cases before ICAI/ICSI/NFRA");
            resp.put("totalCount", dto.size());
            resp.put("data", dto);

            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Failed to generate report");
            err.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
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
