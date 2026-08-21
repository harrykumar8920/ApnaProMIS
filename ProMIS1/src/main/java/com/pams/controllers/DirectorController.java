package com.pams.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.entity.AddCase;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.HearingDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddCaseRepository;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AppRoleRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;

@Controller
public class DirectorController {

	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;

	@Autowired
	private HearingDetailsRepository hearingDetailsRepository;

	@Autowired
	private AppRoleRepository appRoleRepository;
	@Autowired
	private AddDesignationRepository addDesignationRepository;
	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	AddCaseRepository addCaseRepository;
	
	
	
	@GetMapping("/getStatusFromChart")
	public String getSecondPage(@RequestParam(name = "status") String status, ModelMap model) {
	    model.addAttribute("selectedStatus", status);
	    List<HearingDetails> allByApproveStatus = hearingDetailsRepository.findAllByQuery();
	    List<HearingDetails> collect = allByApproveStatus.stream()
	    		.filter(s -> s.getStatus().getStatusName() != null && s.getStatus().getStatusName().equals(status))
	            .collect(Collectors.toList());
	    model.addAttribute("statusList", collect);
	    return "caseDetails/viewStatusList";
	}
	
	
	
	
	

	@RequestMapping(value = "/directorhome")
	public String directorHome(ModelMap modelMap) throws Exception {

		List<ProCourtCaseDetails> TotalCase = proCourtCaseDetailsRepo.findAll();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		ReportController rc = new ReportController();
		Date nextdate = rc.next7days();
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
	//	List<HearingDetails> allByApproveStatus = hearingDetailsRepository.findAll();
		
		List<HearingDetails> allByApproveStatus = hearingDetailsRepository.findAllByQuery();

		List<HearingDetails> totaltodaycase = hearingDetailsRepository.findByNextHearingDateBetween(fromDate, nextdate);

		LocalDate fromLocalDate = toLocalDate(fromDate);

		Map<String, Integer> dayWiseCount = IntStream.rangeClosed(1, 7).boxed()
				.collect(Collectors.toMap(i -> "DAY" + i, i -> {
					LocalDate currentDay = fromLocalDate.plusDays(i - 1);
					return (int) totaltodaycase.stream().map(HearingDetails::getNextHearingDate)
							.filter(Objects::nonNull).map(DirectorController::toLocalDate) // Use helper safely
							.filter(hearingDate -> hearingDate.equals(currentDay)).count();
				}, (a, b) -> b, LinkedHashMap::new));

		modelMap.addAttribute("dayWiseCountUpdate", dayWiseCount);
		// 1. Last 7 Days ka count nikalne ke liye logic (Chronological Order: DAY1 = 6 Days ago, DAY7 = Today)
		Map<String, Integer> Last7DaysCount = IntStream.rangeClosed(1, 7).boxed()
		        .collect(Collectors.toMap(i -> "DAY" + i, i -> {
		            // DAY1 -> 6 days ago, DAY2 -> 5 days ago ... DAY7 -> Today (0 days ago)
		            LocalDate currentDay = fromLocalDate.minusDays(7 - i);
		            
		            return (int) allByApproveStatus.stream()
		                    .map(HearingDetails::getNextHearingDate) // NOTE: Agar past hearing ki date koi doosra field hai to wo use karein
		                    .filter(Objects::nonNull)
		                    .map(DirectorController::toLocalDate) // Apka helper method
		                    .filter(hearingDate -> hearingDate.equals(currentDay))
		                    .count();
		        }, (a, b) -> b, LinkedHashMap::new));

		// 2. Aapka existing model mapping (Ise waise hi rehne dein)
		modelMap.addAttribute("Last7DaysCount", Last7DaysCount);
		modelMap.addAttribute("totaltodaycase", totaltodaycase.size());

		List<AddCase> Totalsanctionorder = addCaseRepository.findALLByFinalisationStatus(2,
				Sort.by(Sort.Direction.DESC, "id"));
		modelMap.addAttribute("Totalsanctionorder", Totalsanctionorder.size());

		List<AssignedTaskPuh> pendingAssignedTask = assignedTaskPuhRepo.findAllByIsApproved(false);

		Map<Integer, Long> YearWiseCase2 = pendingAssignedTask.stream().filter(kk -> kk.getCreatedDate() != null)
				.collect(Collectors.groupingBy(kk -> {
					Calendar cal = Calendar.getInstance();
					cal.setTime(kk.getCreatedDate());
					return cal.get(Calendar.YEAR);
				}, Collectors.counting()));
		Map<Integer, Long> collect33 = TotalCase.stream().filter(ff -> ff.getCreatedDate() != null)
				.collect(Collectors.groupingBy(gg -> {
					Calendar cal = Calendar.getInstance();
					cal.setTime(gg.getCreatedDate());
					return cal.get(Calendar.YEAR);
				}, Collectors.counting()));

		modelMap.addAttribute("collect33", collect33);
		modelMap.addAttribute("YearWiseCase2", YearWiseCase2);

		modelMap.addAttribute("totalAssignedTask", assignedTaskPuhRepo.count());
		modelMap.addAttribute("pendingAssignedTask", pendingAssignedTask.size());
		modelMap.addAttribute("TotalCase", TotalCase.size());

		List<AssignedTaskPuh> allDataChart = assignedTaskPuhRepo.findAll();
		List<AddCase> addCaseList = (List<AddCase>) addCaseRepository.findAll();

		Map<String, Integer> statusCountMap = allByApproveStatus.stream().filter(h -> h.getStatus() != null)
				.collect(Collectors.groupingBy(h -> h.getStatus().getStatusName(),
						Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
		Map<Integer, Long> YearWiseCase = addCaseList.stream().filter(kk -> kk.getCreatedDate() != null)
				.collect(Collectors.groupingBy(kk -> {
					Calendar cal = Calendar.getInstance();
					cal.setTime(kk.getCreatedDate());
					return cal.get(Calendar.YEAR);
				}, Collectors.counting()));

		Map<Integer, Long> yearWiseCount = allDataChart.stream().filter(task -> task.getCreatedDate() != null) // Ensure
																												// createdDate
																												// is
																												// not
																												// null
				.collect(Collectors.groupingBy(task -> {
					Calendar cal = Calendar.getInstance();
					cal.setTime(task.getCreatedDate());
					return cal.get(Calendar.YEAR); // Extract Year
				}, Collectors.counting() // Count occurrences per year
				));

		Map<Integer, Long> casePositionCount = TotalCase.stream()
				.collect(Collectors.groupingBy(ProCourtCaseDetails::getCasePosition, Collectors.counting()));
		// Sort years in ascending order
		Map<Integer, Long> sortedYearWiseCount = new TreeMap<>(yearWiseCount);
		// 1. Repository se data fetch kiya (Aapki existing line)
		List<ProCourtCaseDetails> byFillingDateIsNotNull = proCourtCaseDetailsRepo.findByFillingDateIsNotNull();

		// 2. Financial Year wise count ka Map taiyyar kiya
		Map<String, Integer> financialYearCountMap = byFillingDateIsNotNull.stream()
		        .filter(caseDtl -> caseDtl.getFinancialYear() != null) // Null safety check for financialYear
		        .collect(Collectors.groupingBy(
		                ProCourtCaseDetails::getFinancialYear, // Key: financialYear (String)
		                Collectors.summingInt(e -> 1)          // Value: Count (Integer)
		        ));

		// 3. UI (Thymeleaf) par bhejne ke liye model mein add karein
		modelMap.addAttribute("financialYearCountMap", financialYearCountMap);
		// Add attributes to model
		modelMap.addAttribute("YearWiseCase", YearWiseCase);
		modelMap.addAttribute("statusCountMap", statusCountMap);

		modelMap.addAttribute("casePositionCount", casePositionCount);
		modelMap.addAttribute("yearWiseCount", sortedYearWiseCount);
		modelMap.addAttribute("totleDesignation", addDesignationRepository.count());
		modelMap.addAttribute("totleRole", appRoleRepository.count());

		return "directorhome";
	}

	private static LocalDate toLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@RequestMapping(value = "/viewAllSanctionOrderDetails")
	public String viewAllSanctionOrderDetails(ModelMap modelMap) throws Exception {
		List<AddCase> viewAllSanctionOrderDetails = addCaseRepository.findALLByFinalisationStatus(2,
				Sort.by(Sort.Direction.DESC, "id"));
		modelMap.addAttribute("viewAllSanctionOrderDetails", viewAllSanctionOrderDetails);
		return "caseDetails/viewAllSanctionOrderDetails";
	}

	@RequestMapping(value = "/courtCaseSanctionOrderDetails")
	public String pendingAssignedTask(ModelMap modelMap) throws Exception {
		List<AssignedTaskPuh> pendingAssignedTask = assignedTaskPuhRepo.findAllByIsApproved(false);
		List<ProCourtCaseDetails> courtDetails = proCourtCaseDetailsRepo.findAll();
		Map<Integer, Long> casePositionCount = courtDetails.stream()
				.collect(Collectors.groupingBy(ProCourtCaseDetails::getCasePosition, Collectors.counting()));
		System.out.println(casePositionCount);
		modelMap.addAttribute("courtCaseSanctionOrderDetails", courtDetails);
		modelMap.addAttribute("casePositionCount", casePositionCount);
		return "caseDetails/courtCaseSanctionOrderDetails";
	}
}
