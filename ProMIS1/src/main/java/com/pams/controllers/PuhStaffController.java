package com.pams.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.dao.AppRoleDAO;
import com.pams.dto.PageNoDTO;
import com.pams.entity.AddCase;
import com.pams.entity.HearingDetails;
import com.pams.entity.Type;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddCaseRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.TypeRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class PuhStaffController {
	@Autowired
	private AppRoleDAO appRoleDao;

	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired

	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private AddCaseRepository addCaseRepo;
	@Autowired
	private TypeRepository typeRepo;

	@Autowired
	HearingDetailsRepository hearingDetailsRepository;

	@RequestMapping(value = "/puhStaffhome")
	public String directorHome(ModelMap modelMap) throws Exception {
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		ReportController rc = new ReportController();
		Date nextdate = rc.next7days();

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

		List<AddCase> allByCreatedBy = addCaseRepo.findALLByCreatedBy(userdet, Sort.by(Sort.Direction.DESC, "id"));

		List<HearingDetails> totaltodaycase = hearingDetailsRepository.findByNextHearingDateBetween(fromDate, nextdate);

		modelMap.addAttribute("totaltodaycase", totaltodaycase.size());

		modelMap.addAttribute("TotalCaseN", allByCreatedBy.size());
		modelMap.addAttribute("totalforwardcaseN", allByCreatedBy.stream().filter(o -> o.getFinalisationStatus() == 1)
				.collect(Collectors.toList()).size());

		modelMap.addAttribute("totalsendbackcaseN", allByCreatedBy.stream().filter(o -> o.getFinalisationStatus() == 3)
				.collect(Collectors.toList()).size());

		return "puhStaffHome";
	}

	@RequestMapping(value = "listOfCourtCasesSN")
	public String listOfCourtCasesSN(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Long userId = userDetailsService.getUserDetails().getUserId();
		// System.out.println("userId "+userId);
		List<ProCourtCaseDetails> proCourtDtl = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
				Sort.by(Sort.Direction.DESC, "id"));

		// List<AddCase> caseList = addCaseRepo.findALLByCreatedBy(userdet,
		// Sort.by(Sort.Direction.DESC, "id"));
		// List<AddCase> findAll = (List<AddCase>)
		// addCaseRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

		/*
		 * Page<AddCase> findAll = addCaseRepo.findByType(typeRepo.findById((long)
		 * 1).get(),pagable);
		 */
		Page<AddCase> findAll = addCaseRepo.findAll(pagable);
		long totalRow = findAll.getTotalElements();
		int currentRow = 1;
		int lastRow = findAll.getNumberOfElements();
		int pageNo1 = findAll.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", findAll.getNumberOfElements());
		PageNoDTO pagedto = new PageNoDTO();
		pagedto.setCaseType(1);

		modelMap.addAttribute("pageNoDTO", pagedto);
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		// List<AddCase> findAll1 = (List<AddCase>) addCaseRepo.findAll();
		modelMap.addAttribute("userId", userId);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", findAll);
		return "caseDetails/listOfCourtCasesS";
	}

	@RequestMapping(value = "listOfCourtCasesSC")
	public String listOfCourtCasesSC(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Long userId = userDetailsService.getUserDetails().getUserId();
		// System.out.println("userId "+userId);
		List<ProCourtCaseDetails> proCourtDtl = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
				Sort.by(Sort.Direction.DESC, "id"));

		// List<AddCase> caseList = addCaseRepo.findALLByCreatedBy(userdet,
		// Sort.by(Sort.Direction.DESC, "id"));
		// List<AddCase> findAll = (List<AddCase>)
		// addCaseRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

		Page<AddCase> findAll = addCaseRepo.findAll(pagable);
		/*
		 * Page<AddCase> findAll = addCaseRepo.findByType(typeRepo.findById((long)
		 * 2).get(),pagable);
		 */
		long totalRow = findAll.getTotalElements();
		int currentRow = 1;
		int lastRow = findAll.getNumberOfElements();
		int pageNo1 = findAll.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", findAll.getNumberOfElements());

		PageNoDTO pagedto = new PageNoDTO();
		pagedto.setCaseType(2);
		modelMap.addAttribute("pageNoDTO", pagedto);
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		// List<AddCase> findAll1 = (List<AddCase>) addCaseRepo.findAll();
		modelMap.addAttribute("userId", userId);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", findAll);
		return "caseDetails/listOfCourtCasesS";
	}

	@RequestMapping(value = "listOfCourtCasesS")
	public String listOfCourtCasesS(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		// System.out.println(casetype);
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Long userId = userDetailsService.getUserDetails().getUserId();
		// System.out.println("userId "+userId);
		List<ProCourtCaseDetails> proCourtDtl = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
				Sort.by(Sort.Direction.DESC, "id"));

		// List<AddCase> caseList = addCaseRepo.findALLByCreatedBy(userdet,
		// Sort.by(Sort.Direction.DESC, "id"));
		// List<AddCase> findAll = (List<AddCase>)
		// addCaseRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		Page<AddCase> findAll;
		/*
		 * if(casetype==1||casetype==2) { findAll =
		 * addCaseRepo.findByType(typeRepo.findById(casetype).get(),pagable); } else {
		 */
		findAll = addCaseRepo.findAll(pagable);
		/* } */
		// Page<AddCase> findAll = addCaseRepo.findAll(pagable);
		long totalRow = findAll.getTotalElements();
		int currentRow = 1;
		int lastRow = findAll.getNumberOfElements();
		int pageNo1 = findAll.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", findAll.getNumberOfElements());
		PageNoDTO pd = new PageNoDTO();
		// pd.setCaseType((int) casetype);
		modelMap.addAttribute("pageNoDTO", pd);

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		// List<AddCase> findAll1 = (List<AddCase>) addCaseRepo.findAll();
		modelMap.addAttribute("userId", userId);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", findAll);
		return "caseDetails/listOfCourtCasesS";
	}
	
	@RequestMapping(value = "listOfCourtCasesnew")
	public String listOfCourtCasesSnew(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		// System.out.println(casetype);
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Long userId = userDetailsService.getUserDetails().getUserId();
		// System.out.println("userId "+userId);
	

		// List<AddCase> caseList = addCaseRepo.findALLByCreatedBy(userdet,
		// Sort.by(Sort.Direction.DESC, "id"));
		// List<AddCase> findAll = (List<AddCase>)
		// addCaseRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		Page<AddCase> findAll;
		/*
		 * if(casetype==1||casetype==2) { findAll =
		 * addCaseRepo.findByType(typeRepo.findById(casetype).get(),pagable); } else {
		 */
		findAll = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 2);
		/* } */
		// Page<AddCase> findAll = addCaseRepo.findAll(pagable);
		long totalRow = findAll.getTotalElements();
		int currentRow = 1;
		int lastRow = findAll.getNumberOfElements();
		int pageNo1 = findAll.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", findAll.getNumberOfElements());
		PageNoDTO pd = new PageNoDTO();
		// pd.setCaseType((int) casetype);
		modelMap.addAttribute("pageNoDTO", pd);

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		// List<AddCase> findAll1 = (List<AddCase>) addCaseRepo.findAll();
		modelMap.addAttribute("userId", userId);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", findAll);
		return "caseDetails/listOfCourtCasesNew";
	}
	
	
	@PostMapping("/serchSectionorder")
	public String searchSectionOrder(
	        @ModelAttribute("pageNoDTO") PageNoDTO pageNoDTO,
	        Model model) throws Exception {

		int pageNo = 0;
		int noOfrecord = 50;
		
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		model.addAttribute("rollID", userrole);

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "pro_sanction_order_id"));

	
		Long userId = userDetailsService.getUserDetails().getUserId();
	
	

		Page<AddCase> findAll;
		
		findAll = addCaseRepo.search(pageNoDTO.getStr(),pagable);
		
		long totalRow = findAll.getTotalElements();
		int currentRow = 1;
		int lastRow = findAll.getNumberOfElements();
		int pageNo1 = findAll.getTotalPages();
		model.addAttribute("currentPage", pageNo + 1);
		model.addAttribute("totalPages", pageNo1);
		model.addAttribute("totalItems", findAll.getNumberOfElements());
		PageNoDTO pd = new PageNoDTO();
		
		model.addAttribute("pageNoDTO", pd);

		model.addAttribute("totalRow", totalRow);
		model.addAttribute("currentRow", currentRow);
		model.addAttribute("lastRow", lastRow);

		model.addAttribute("userId", userId);
		model.addAttribute("userRole", userrole);

		model.addAttribute("lstCourtCase", findAll);
		return "caseDetails/listOfCourtCasesS";
	}

	@PostMapping("listOfCourtCasesS1")
	public String listOfCourtCasesS1(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {
		int pageNo;

		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Long userId = userDetailsService.getUserDetails().getUserId();
		// System.out.println("userId "+userId);

		List<ProCourtCaseDetails> proCourtDtl = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,
				Sort.by(Sort.Direction.DESC, "id"));

		// List<AddCase> caseList = addCaseRepo.findALLByCreatedBy(userdet,
		// Sort.by(Sort.Direction.DESC, "id"));
		// List<AddCase> findAll = (List<AddCase>)
		// addCaseRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

		//int casetype = pageDTO.getCaseType();

		Page<AddCase> findAll;
		/*
		 * if(casetype==1||casetype==2) { findAll =
		 * addCaseRepo.findByType(typeRepo.findById((long) casetype).get(),pagable); }
		 * else { findAll=addCaseRepo.findAll(pagable); }
		 */

		findAll = addCaseRepo.findAll(pagable);

		// Page<AddCase> findAll =
		// addCaseRepo.findByType(typeRepo.findById((long)casetype).get(),pagable);

		// Page<AddCase> findAll = addCaseRepo.findAll(pagable);
		long totalRow = findAll.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		;
		int lastRow = (noOfrecord * pageNo) + findAll.getNumberOfElements();
		int pageNo1 = findAll.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", findAll.getNumberOfElements());

		PageNoDTO pd = new PageNoDTO();
	//	pd.setCaseType((int) casetype);
		modelMap.addAttribute("pageNoDTO", pd);
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		// List<AddCase> findAll1 = (List<AddCase>) addCaseRepo.findAll();
		modelMap.addAttribute("userId", userId);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", findAll);
		return "caseDetails/listOfCourtCasesS";
	}

	/*
	 * @RequestMapping(value = "listOfCourtCasesS") public String
	 * listOfCourtCasesS(ModelMap modelMap) throws Exception {
	 * 
	 * 
	 * String userrole =
	 * appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
	 * 
	 * UserDetails userdet =
	 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
	 * ));
	 * 
	 * List<proCourtCaseDetails> proCourtDtl =
	 * proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,Sort.by(Sort.Direction.
	 * DESC, "id"));
	 * 
	 * // List<AddCase> caseList = addCaseRepo.findALLByCreatedBy(userdet,
	 * Sort.by(Sort.Direction.DESC, "id")); List<AddCase> caseList = (List<AddCase>)
	 * addCaseRepo.findAll();
	 * 
	 * AppUser userID = userdet.getUserId(); modelMap.addAttribute("userID",
	 * userID);
	 * 
	 * modelMap.addAttribute("userRole", userrole);
	 * 
	 * modelMap.addAttribute("lstCourtCase", caseList); return
	 * "caseDetails/listOfCourtCasesS"; }
	 */
	@RequestMapping(value = "listOfForwardCourtCases1")
	public String listOfForwardCourtCases1(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		List<ProCourtCaseDetails> proCourtDtl = proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1, 2,
				userdet);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", proCourtDtl);
		return "caseDetails/listOfForwardCourtCases";
	}

	@RequestMapping(value = "listOfForwardCourtCases")
	public String listOfForwardCourtCases(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		modelMap.addAttribute("rollID", userrole);
		// List<proCourtCaseDetails> proCourtDtl =
		// proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1,2,
		// userdet);
		modelMap.addAttribute("userRole", userrole);

		// modelMap.addAttribute("lstCourtCase", proCourtDtl);
		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 1);
		long totalRow = caseList.getTotalElements();
		int currentRow = 1;
		int lastRow = caseList.getNumberOfElements();
		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfForwardCourtCases";
	}

	@RequestMapping(value = "listOfForwardCourtCasesN")
	public String listOfForwardCourtCasesN(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		modelMap.addAttribute("rollID", userrole);
		// List<proCourtCaseDetails> proCourtDtl =
		// proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1,2,
		// userdet);
		modelMap.addAttribute("userRole", userrole);

		// modelMap.addAttribute("lstCourtCase", proCourtDtl);
		/*
		 * Page<AddCase> caseList =
		 * addCaseRepo.findALLByCreatedByAndFinalisationStatusAndType(pagable,userdet,1,
		 * typeRepo.findById((long) 1).get());
		 */
		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 1);
		long totalRow = caseList.getTotalElements();
		int currentRow = 1;
		int lastRow = caseList.getNumberOfElements();
		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());
		PageNoDTO pagedto = new PageNoDTO();
		pagedto.setCaseType(1);
		modelMap.addAttribute("pageNoDTO", pagedto);
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfForwardCourtCases";
	}

	@RequestMapping(value = "listOfForwardCourtCasesC")
	public String listOfForwardCourtCasesC(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;
		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		modelMap.addAttribute("rollID", userrole);
		// List<proCourtCaseDetails> proCourtDtl =
		// proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1,2,
		// userdet);
		modelMap.addAttribute("userRole", userrole);

		// modelMap.addAttribute("lstCourtCase", proCourtDtl);
		// Page<AddCase> caseList =
		// addCaseRepo.findALLByCreatedByAndFinalisationStatusAndType(pagable,userdet,1,typeRepo.findById((long)
		// 2).get());
		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 1);
		long totalRow = caseList.getTotalElements();
		int currentRow = 1;
		int lastRow = caseList.getNumberOfElements();
		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());

		PageNoDTO pagedto = new PageNoDTO();
		pagedto.setCaseType(2);
		modelMap.addAttribute("pageNoDTO", pagedto);
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfForwardCourtCases";
	}

	@RequestMapping(value = "listOfForwardCourtCases2")
	public String listOfForwardCourtCases2(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {
		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		// List<proCourtCaseDetails> proCourtDtl =
		// proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1,2,
		// userdet);
		modelMap.addAttribute("userRole", userrole);

		// modelMap.addAttribute("lstCourtCase", proCourtDtl);
		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 1);
		/*
		 * long totalRow = caseList.getTotalElements(); int
		 * currentRow=(noOfrecord*pageNo)+1; int
		 * lastRow=(noOfrecord*pageNo)+caseList.getSize();
		 */

		long totalRow = caseList.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + caseList.getNumberOfElements();
		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfForwardCourtCases";
	}

	@RequestMapping(value = "listOfSendBackCourtCases1")
	public String listOfSendBackCourtCases1(ModelMap modelMap) throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		List<ProCourtCaseDetails> proCourtDtl = proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(3, userdet);

		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", proCourtDtl);
		return "caseDetails/listOfSendBackCourtCases";
	}

	@RequestMapping(value = "listOfSendBackCourtCasesN")
	public String listOfSendBackCourtCasesN(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("rollID", userrole);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 3);
		
		long totalRow = caseList.getTotalElements();
		int currentRow = 1;
		int lastRow = caseList.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());
		PageNoDTO pagedto = new PageNoDTO();
		pagedto.setCaseType(1);
		modelMap.addAttribute("pageNoDTO", pagedto);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfSendBackCourtCases";
	}

	@RequestMapping(value = "listOfSendBackCourtCasesC")
	public String listOfSendBackCourtCasesC(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("rollID", userrole);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 3);

		long totalRow = caseList.getTotalElements();
		int currentRow = 1;
		int lastRow = caseList.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());

		PageNoDTO pagedto = new PageNoDTO();
		pagedto.setCaseType(2);
		modelMap.addAttribute("pageNoDTO", pagedto);
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfSendBackCourtCases";
	}

	@RequestMapping(value = "listOfSendBackCourtCases")
	public String listOfSendBackCourtCases(ModelMap modelMap) throws Exception {
		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("rollID", userrole);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		// List<proCourtCaseDetails> proCourtDtl =
		// proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(3, userdet);
		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 3);
		// List<AddCase> caseList =
		// addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet,3,
		// Sort.by(Sort.Direction.DESC, "id"));
		long totalRow = caseList.getTotalElements();
		int currentRow = 1;
		int lastRow = caseList.getNumberOfElements();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfSendBackCourtCases";
	}

	@RequestMapping(value = "listOfSendBackCourtCases2")
	public String listOfSendBackCourtCases2(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {
		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		// List<proCourtCaseDetails> proCourtDtl =
		// proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(3, userdet);
		Page<AddCase> caseList = addCaseRepo.findALLByCreatedByAndFinalisationStatus(pagable, userdet, 3);
		// List<AddCase> caseList =
		// addCaseRepo.findALLByCreatedByAndFinalisationStatus(userdet,3,
		// Sort.by(Sort.Direction.DESC, "id"));
		long totalRow = caseList.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + caseList.getSize();
		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		int pageNo1 = caseList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", caseList.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("userRole", userrole);

		modelMap.addAttribute("lstCourtCase", caseList);
		return "caseDetails/listOfSendBackCourtCases";
	}
}
