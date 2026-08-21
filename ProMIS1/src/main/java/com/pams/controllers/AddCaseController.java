package com.pams.controllers;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AppRoleDAO;
import com.pams.entity.AddCase;
import com.pams.entity.AddCourt;
import com.pams.entity.AddInvestigaOrderDateSub;
import com.pams.entity.AddState;
import com.pams.entity.District;
import com.pams.entity.Type;
import com.pams.entity.TypeofCase;
import com.pams.entity.UserDetails;
import com.pams.service.AddCaseRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.BasisofIORepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.StateRepository;
import com.pams.service.TypeRepository;
import com.pams.service.TypeofBenchRepository;
import com.pams.service.TypeofCaseRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.districtRepository;
import com.pams.utils.Utils;
import com.pams.validation.CourtCaseValidator;

@Controller
public class AddCaseController {
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 1GB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size
	private static final Logger logger = LoggerFactory.getLogger(AddCase.class);
	@Autowired
	private AddCaseRepository addCaseRepos;
	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private TypeRepository typeRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private CourtTypeRepository courtTypeRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private districtRepository districtRepo;
	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private TypeofCaseRepository typeofCaseRepo;
	@Autowired
	private TypeofBenchRepository typeofBenchRepo;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	BasisofIORepository BasisofIORepo;

	@GetMapping("/AddNewCase")
	public String AddNewCase1(ModelMap modelMap) throws Exception {

		AddCase addCase = new AddCase();
		addCase.setTypeofOrder(1);
		// UserDetails userdet =
		// useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);
		List<AddState> statelist = stateRepo.findAll();
		List<District> districtlist = districtRepo.findAll();
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		modelMap.addAttribute("typeOfBench", typeofBenchRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		List<TypeofCase> typeofcase = typeofCaseRepo.findAllByIdNot((long) 0);
		Collections.sort(typeofcase);
		modelMap.addAttribute("typeofcase", typeofcase);
		// modelMap.addAttribute("typeofcase",
		// typeofCaseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		// modelMap.addAttribute("typeofcase", typeofcase);
		List<Type> typeList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		//List<AddCourt> courtType = courtTypeRepo.findAllByIdGreaterThan((long) 0);
		List<AddCourt> courtType = courtTypeRepo.findAllByIdGreaterThanAndActiveTrue((long) 0);
		modelMap.addAttribute("courtType", courtType);
		modelMap.addAttribute("addCase", addCase);
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		modelMap.addAttribute("typeList", typeList);
		modelMap.addAttribute("basicInvorderlist", BasisofIORepo.findAll());
		return "Prosecutor/AddCourtDeatil1";
	}

	@RequestMapping(value = "/getTypeOfCase", method = RequestMethod.GET)
	@ResponseBody

	public List<TypeofCase> getTypeOfCase(@RequestParam("courtType") Long typeId) {
		Type type = typeRepository.findById(typeId).get();
		List<TypeofCase> findTypeOfCaseByType = typeofCaseRepo.findByTypeAndIdNot(type, (long) 0);
		return findTypeOfCaseByType;
	}

	/*
	 * @RequestMapping(value = "/getCourt321", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public List<TypeofCase> getTypeOfCase(@RequestParam("stateId")
	 * Long stateId) { Type type = typeRepository.findById(stateId).get(); String
	 * type2 = type.getType(); List<TypeofCase> findByType =
	 * typeofCaseRepo.findByType(type2); for(TypeofCase sd:findByType) {
	 * 
	 * System.out.println("ID: "+sd.getId()+"Type: "+sd.getType()); } //return
	 * districtService.findDistrictsByState(stateId); return
	 * typeofCaseRepo.findByType(type2); }
	 */
	@RequestMapping(value = "/viewPendingTaskdtl2", params = "forwardToPuh")
	public String forwardToPuh(ModelMap modelMap, @RequestParam(value = "forwardToPuh", required = true) Long id,
			RedirectAttributes redirect) throws Exception {

		AddCase addCase = addCaseRepos.findById(id).get();
		addCase.setFinalisationStatus(1);
		addCaseRepos.save(addCase);

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.addcaseforwarded") + " and investigation number is "
						+ addCase.getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", addCase.getId());

		auditBeanBo.save();

		redirect.addFlashAttribute("message", "Sanction Order forwarded Successfully !!");
		/* redirect.addAttribute("casetype", addCase.getType().getId()); */

		return "redirect:/listOfCourtCasesS";

		/*
		 * modelMap.addAttribute("addCase", addCase); return
		 * "Prosecutor/AddCourtDeatil1";
		 */

	}

	@RequestMapping(value = "/approveAndReject", params = "approve")
	public String approve(ModelMap modelMap, @RequestParam(value = "approve", required = true) Long id,
			RedirectAttributes redirect) throws Exception {

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		AddCase addCase = addCaseRepos.findById(id).get();
		addCase.setFinalisationStatus(2);
		addCase.setApprovedBy(userdet);

		addCase.setApprovedDate(new Date());

		addCaseRepos.save(addCase);

		redirect.addFlashAttribute("message", "Sanction Order Finalized Successfully !!");
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.approved"),
				utils.getMessage("log.login.addcaseapproved") + " and investigation number is "
						+ addCase.getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", addCase.getId());

		auditBeanBo.save();
		return "redirect:/pendingFinalizecourtCaseDtl";

	}

	@RequestMapping(value = "/approveAndReject", params = "sendBackCase")
	public String reject(ModelMap modelMap, @ModelAttribute("addCase") AddCase addCase, RedirectAttributes redirect)
			throws Exception {

		AddCase addCase1 = addCaseRepos.findById(addCase.getId()).get();
		addCase1.setFinalisationStatus(3);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		addCase1.setUpdatedBy(userdet);
		addCase1.setRemarksByPUH(addCase.getRemarksByPUH());
		addCase1.setUpdatedDate(new Date());
		addCaseRepos.save(addCase1);

		redirect.addFlashAttribute("message", "Sanction Order sent back for updation");
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.sendback"),
				utils.getMessage("log.login.addcasesendback") + " and investigation number is "
						+ addCase1.getInvestigationOrderNo(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", addCase1.getId());

		auditBeanBo.save();

		return "redirect:/pendingFinalizecourtCaseDtl";

	}

	@RequestMapping(value = "/viewPendingTaskdtl2", params = "editProCourtCase")
	public String editProCourtCase1(ModelMap modelMap,
			@RequestParam(value = "editProCourtCase", required = true) Long id, RedirectAttributes redirect)
			throws Exception {

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);
		AddCase addCase = addCaseRepos.findById(id).get();
		modelMap.addAttribute("typeOfBench", typeofBenchRepo.findAll());
		List<AddCourt> courtType;

		courtType = courtTypeRepo.findByCourtNameNotContaining("National", Sort.by(Sort.Direction.ASC, "id"));
		List<AddInvestigaOrderDateSub> addInvestigaOrderDateSub = addCase.getAddInvestigaOrderDateSub();

		String[] additionalInvestigationArray = new String[addInvestigaOrderDateSub.size()];
		String[] additionalInvestigationDateArray = new String[addInvestigaOrderDateSub.size()];

		for (int i = 0; i < addInvestigaOrderDateSub.size(); i++) {
			AddInvestigaOrderDateSub sub = addInvestigaOrderDateSub.get(i);
			additionalInvestigationArray[i] = sub.getAdditionalInvestigation();
			additionalInvestigationDateArray[i] = sub.getAdditionalInvestigationDate();
		}

		addCase.setAdditionalInvestigation(additionalInvestigationArray);
		addCase.setAdditionalInvestigationDate(additionalInvestigationDateArray);

		modelMap.addAttribute("courtType", courtType);
		List<AddState> statelist = stateRepo.findAll();
		List<District> districtlist = districtRepo.findAll();
		modelMap.addAttribute("statelist", statelist);
		modelMap.addAttribute("districtlist", districtlist);
		modelMap.addAttribute("typeofcase", typeofCaseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("typeList", typeRepository.findAll());
		modelMap.addAttribute("addCase", addCase);
		modelMap.addAttribute("basicInvorderlist", BasisofIORepo.findAll());
		return "Prosecutor/AddCourtDeatil1";

	}

	@RequestMapping(value = "/viewPendingTaskdtl2", params = "viewCaseDetails")
	public String viewCaseDetails(ModelMap modelMap, @RequestParam(value = "viewCaseDetails", required = true) Long id,
			RedirectAttributes redirect) throws Exception {

		AddCase addCase = addCaseRepos.findById(id).get();
		/*
		 * if (addCase.getTypeOfCase().getType().getType().equals("NCLT")) {
		 * addCase.setTypeOfCaseT("NCLT"); } else { addCase.setTypeOfCaseT("NCLT11"); }
		 */

		modelMap.addAttribute("addCase", addCase);
		return "caseDetails/viewCaseDetails";

	}

	@RequestMapping(value = "/viewPendingTaskdtl2", params = "viewCaseDetails1")
	public String viewCaseDetails1(ModelMap modelMap,
			@RequestParam(value = "viewCaseDetails1", required = true) Long id, RedirectAttributes redirect)
			throws Exception {

		AddCase addCase = addCaseRepos.findById(id).get();
		modelMap.addAttribute("addCase", addCase);
		return "caseDetails/viewCaseDetails1";

	}

	@PostMapping("/saveaddCase")
	public String addCourtOrderdtlq(ModelMap modelMap,

			@ModelAttribute("addCase") AddCase addCase, BindingResult bindResult, RedirectAttributes redirect)
			throws Exception {
		/* Long casetype = addCase.getType().getId(); */
		String prosectionOrder = addCase.getProSectionOrderNumber().trim();
		
		Integer finalisationStatus = addCase.getFinalisationStatus();
		
		if (addCase.getId()==null && addCaseRepos.existsByCinNumber(addCase.getCinNumber().toUpperCase().trim())) {
		    bindResult.rejectValue(
		        "cinNumber",
		        "errmsg.cinNumberData"
		    );
		}

		
		addCase.setProSectionOrderNumber(prosectionOrder);

		CourtCaseValidator Casevalid = new CourtCaseValidator();

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);
		/*
		 * if (addCase.getType().getId() == 0) { bindResult.rejectValue("type",
		 * "errmsg.required"); }
		 */
		Casevalid.addCinNumber(addCase, bindResult);
		if (addCase.getTypeofOrder() == 4) {
			Casevalid.checkCourtCaseNumber(addCase, bindResult);
			Casevalid.checkAdvanceNotice(addCase, bindResult);
			Casevalid.checkCompanyName(addCase, bindResult);
			
			
		} else {
			if (addCase.getId() == null && (!prosectionOrder.isEmpty())) {
				List<AddCase> addCaseDtl = addCaseRepos.findByProSectionOrderNumber(prosectionOrder);

				if (!addCaseDtl.isEmpty()) {
					bindResult.rejectValue("proSectionOrderNumber", "errmsg.ProSectionOrder");

				} else {
					Casevalid.addCasefile(addCase, bindResult);
				}

			}
			if (addCase.getId() != null && (!addCase.getProsectionSanctionOrderFile().isEmpty())) {
				Casevalid.addCasefile(addCase, bindResult);

			}
			if (!addCase.getSupplimentoryOrderNo().equalsIgnoreCase("NIL")) {
				if (addCase.getSupplimentoryOrderNo().equals("")) {
					bindResult.rejectValue("supplimentoryOrderNo", "errmsg.supplimentoryOrderNo");
				}
				if (addCase.getSupplimentoryOrderDate() == null) {
					bindResult.rejectValue("supplimentoryOrderDate", "errmsg.required");
				}

			}

			if (addCase.getInvestigationOrderDate() == null || addCase.getInvestigationOrderDate().equals("")) {
				bindResult.rejectValue("investigationOrderDate", "errmsg.required");
			}
			/*
			 * if (addCase.getType().getId() == 0) { bindResult.rejectValue("type",
			 * "errmsg.required"); }
			 */

			if (addCase.getTypeofOrder() == 1) {

				addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
				Casevalid.addCase(addCase, bindResult);
				addCase.setCourtType(courtTypeRepo.findById((long) 0).get());
				addCase.setState(stateRepo.findById((long) 0).get());
				addCase.setCity(districtRepo.findById((long) 0).get());
			} else if (addCase.getTypeofOrder() == 2) {
				/*
				 * if (addCase.getTypeOfCase().getType().getType().equals("NCLT")) {
				 * 
				 * Casevalid.addCaseNCLT(addCase, bindResult); //
				 * addCase.setCourtType(courtTypeRepo.findById((long) 0).get());
				 * addCase.setState(stateRepo.findById((long) 0).get());
				 * addCase.setCity(districtRepo.findById((long) 0).get());
				 * 
				 * } else { addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
				 * Casevalid.addCase1(addCase, bindResult); }
				 */
				addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
				Casevalid.addCase1(addCase, bindResult);
			} else {
				addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
				Casevalid.addCase3(addCase, bindResult);

			}
		}
		for (int it1 = 0; it1 < addCase.getAdditionalInvestigation().length; it1++) {

			String addiOrder = addCase.getAdditionalInvestigation()[it1];

			CourtCaseValidator.isvalidName("testError", addiOrder, bindResult, "errMsg");
		}

		// Casevalid.addCasefile(addCase, bindResult);

		AddCase invDetails = addCaseRepos.findALLByInvestigationOrderNoAndInvestigationOrderDateAndCaseTitle(
				addCase.getInvestigationOrderNo(), addCase.getInvestigationOrderDate(), addCase.getCaseTitle());

		if (invDetails != null && addCase.getId() == null) {
			bindResult.rejectValue("investigationOrderNo", "msg.InvestigationOrderNoDuplicate");
		}

		if (bindResult.hasErrors()) {
			modelMap.addAttribute("basicInvorderlist", BasisofIORepo.findAll());
			modelMap.addAttribute("typeOfBench", typeofBenchRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			//List<AddCourt> courtType = courtTypeRepo.findAllByIdGreaterThan((long) 0);
			List<AddCourt> courtType = courtTypeRepo.findAllByIdGreaterThanAndActiveTrue((long) 0);
			modelMap.addAttribute("courtType", courtType);
			List<AddState> statelist = stateRepo.findAll();
			List<District> districtlist = districtRepo.findAll();
			modelMap.addAttribute("statelist", statelist);
			modelMap.addAttribute("typeList", typeRepository.findAll());
			modelMap.addAttribute("districtlist", districtlist);
			modelMap.addAttribute("typeofcase", typeofCaseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));

			modelMap.addAttribute("addCase", addCase);
			return "Prosecutor/AddCourtDeatil1";
		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		addCase.setCinNumber(addCase.getCinNumber().trim().toUpperCase());
		addCase.setCreatedBy(userdet);
		addCase.setUpdatedBy(userdet);
		addCase.setApprovedBy(userdet);
		addCase.setCreatedDate(new Date());
		addCase.setApprovedDate(new Date());
		addCase.setUpdatedDate(new Date());

		Long id = (addCaseRepos.findMaxid() != null) ? (addCaseRepos.findMaxid() + 1) : 1;
		addCase.getProsectionSanctionOrderFile();
		if (addCase.getId() != null) {
			id = addCase.getId();
		}
		List<AddInvestigaOrderDateSub> addInvestigaOrderDateSub = new ArrayList<>();
		for (int it1 = 0; it1 < addCase.getAdditionalInvestigation().length; it1++) {
			AddInvestigaOrderDateSub addInvestDateSub = new AddInvestigaOrderDateSub();
			String addiOrder = addCase.getAdditionalInvestigation()[it1];
			String orderDate = addCase.getAdditionalInvestigationDate()[it1];
			addInvestDateSub.setAdditionalInvestigation(addiOrder);
			addInvestDateSub.setAdditionalInvestigationDate(orderDate);
			addInvestDateSub.setAddCase(addCase);
			addInvestigaOrderDateSub.add(addInvestDateSub);
		}
		addCase.setAddInvestigaOrderDateSub(addInvestigaOrderDateSub);
		if (!addCase.getProsectionSanctionOrderFile().isEmpty()) {

			String fileExt = addCase.getProsectionSanctionOrderFile().getOriginalFilename();
			fileExt = fileExt.substring(fileExt.lastIndexOf("."));

			addCase.setProSanctionFileName("ProSanctionFile_" + id + fileExt);
			caseFileUpload(addCase.getProsectionSanctionOrderFile(), addCase.getProSanctionFileName());
		} else {
			addCase.setProSanctionFileName(addCase.getProSanctionFileName());
		}

		if (addCase.getId() != null) {
			redirect.addFlashAttribute("message", "Sanction Order Details Updated Successfully");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.addcaseUpdate") + " and investigation number is "
							+ addCase.getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", addCase.getId());

			auditBeanBo.save();

		} else {

			redirect.addFlashAttribute("message", "Sanction Order Details Added Successfully");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.addcasesave") + " and investigation number is "
							+ addCase.getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", addCase.getId());

			auditBeanBo.save();
		}

		addCaseRepos.save(addCase);

		modelMap.addAttribute("addCase", new AddCase());
		// return "Prosecutor/AddCourtDeatil1";
		/* redirect.addAttribute("casetype", casetype); */
		
		if(finalisationStatus==2)
		return "redirect:/listOfCourtCasesnew";
		
		return "redirect:/listOfCourtCasesS";
	}

	@RequestMapping(value = "/saveaddCase")
	public String addCourtOrderdtl(ModelMap modelMap,

			@ModelAttribute("addCase") AddCase addCase, BindingResult bindResult, RedirectAttributes redirect)
			throws Exception {
		/* Long casetype = addCase.getType().getId(); */
		String prosectionOrder = addCase.getProSectionOrderNumber().trim();

		addCase.setProSectionOrderNumber(prosectionOrder);

		CourtCaseValidator Casevalid = new CourtCaseValidator();

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		modelMap.addAttribute("rollID", userrole);

		/*
		 * if (addCase.getType().getId() == 0) { bindResult.rejectValue("type",
		 * "errmsg.required"); }
		 */

		if (addCase.getId() == null && (!prosectionOrder.isEmpty())) {
			List<AddCase> addCaseDtl = addCaseRepos.findByProSectionOrderNumber(prosectionOrder);

			if (!addCaseDtl.isEmpty()) {
				bindResult.rejectValue("proSectionOrderNumber", "errmsg.ProSectionOrder");

			} else {
				Casevalid.addCasefile(addCase, bindResult);
			}

		}
		if (addCase.getId() != null && (!addCase.getProsectionSanctionOrderFile().isEmpty())) {
			isValidFile(addCase.getProsectionSanctionOrderFile(), bindResult, true, "prosectionSanctionOrderFile");
		}
		if (!addCase.getSupplimentoryOrderNo().equalsIgnoreCase("NIL")) {
			if (addCase.getSupplimentoryOrderNo().equals("")) {
				bindResult.rejectValue("supplimentoryOrderNo", "errmsg.supplimentoryOrderNo");
			}
			if (addCase.getSupplimentoryOrderDate() == null) {
				bindResult.rejectValue("supplimentoryOrderDate", "errmsg.required");
			}

		}

		if (addCase.getInvestigationOrderDate() == null || addCase.getInvestigationOrderDate().equals("")) {
			bindResult.rejectValue("investigationOrderDate", "errmsg.required");
		}
		/*
		 * if (addCase.getType().getId() == 0) { bindResult.rejectValue("type",
		 * "errmsg.required"); }
		 */

		if (addCase.getTypeofOrder() == 1) {

			addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
			Casevalid.addCase(addCase, bindResult);
			addCase.setCourtType(courtTypeRepo.findById((long) 0).get());
			addCase.setState(stateRepo.findById((long) 0).get());
			addCase.setCity(districtRepo.findById((long) 0).get());
		} else if (addCase.getTypeofOrder() == 2) {
			/*
			 * if (addCase.getTypeOfCase().getType().getType().equals("NCLT")) {
			 * 
			 * Casevalid.addCaseNCLT(addCase, bindResult); //
			 * addCase.setCourtType(courtTypeRepo.findById((long) 0).get());
			 * addCase.setState(stateRepo.findById((long) 0).get());
			 * addCase.setCity(districtRepo.findById((long) 0).get());
			 * 
			 * } else { addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
			 * Casevalid.addCase1(addCase, bindResult); }
			 */
			addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
			Casevalid.addCase1(addCase, bindResult);
		} else {
			addCase.setBenchName(typeofBenchRepo.findById((long) 0).get());
			Casevalid.addCase3(addCase, bindResult);

		}

		// Casevalid.addCasefile(addCase, bindResult);

		if (bindResult.hasErrors()) {
			modelMap.addAttribute("basicInvorderlist", BasisofIORepo.findAll());
			modelMap.addAttribute("typeOfBench", typeofBenchRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			//List<AddCourt> courtType = courtTypeRepo.findAllByIdGreaterThan((long) 0);
			List<AddCourt> courtType = courtTypeRepo.findAllByIdGreaterThanAndActiveTrue((long) 0);
			modelMap.addAttribute("courtType", courtType);
			List<AddState> statelist = stateRepo.findAll();
			List<District> districtlist = districtRepo.findAll();
			modelMap.addAttribute("statelist", statelist);
			modelMap.addAttribute("typeList", typeRepository.findAll());
			modelMap.addAttribute("districtlist", districtlist);
			modelMap.addAttribute("typeofcase", typeofCaseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));

			modelMap.addAttribute("addCase", addCase);
			return "Prosecutor/AddCourtDeatil1";
		}

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		addCase.setCreatedBy(userdet);
		addCase.setUpdatedBy(userdet);
		addCase.setApprovedBy(userdet);
		addCase.setCreatedDate(new Date());
		addCase.setApprovedDate(new Date());
		addCase.setUpdatedDate(new Date());

		Long id = (addCaseRepos.findMaxid() != null) ? (addCaseRepos.findMaxid() + 1) : 1;
		addCase.getProsectionSanctionOrderFile();
		if (addCase.getId() != null) {
			id = addCase.getId();
		}

		if (!addCase.getProsectionSanctionOrderFile().isEmpty()) {

			String fileExt = addCase.getProsectionSanctionOrderFile().getOriginalFilename();
			fileExt = fileExt.substring(fileExt.lastIndexOf("."));

			addCase.setProSanctionFileName("ProSanctionFile_" + id + fileExt);
			caseFileUpload(addCase.getProsectionSanctionOrderFile(), addCase.getProSanctionFileName());
		} else {
			addCase.setProSanctionFileName(addCase.getProSanctionFileName());
		}

		if (addCase.getId() != null) {
			redirect.addFlashAttribute("message", "Sanction Order Details Updated Successfully");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.addcaseUpdate") + " and investigation number is "
							+ addCase.getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", addCase.getId());

			auditBeanBo.save();

		} else {

			redirect.addFlashAttribute("message", "Sanction Order Details Added Successfully");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.addcasesave") + " and investigation number is "
							+ addCase.getInvestigationOrderNo(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", addCase.getId());

			auditBeanBo.save();
		}

		addCaseRepos.save(addCase);

		modelMap.addAttribute("addCase", new AddCase());
		// return "Prosecutor/AddCourtDeatil1";
		/* redirect.addAttribute("casetype", casetype); */
		return "redirect:/listOfCourtCasesS";
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

	public void isValidFile(MultipartFile file, BindingResult errors, boolean isRequired, String errFieldName) {

		if (isRequired && (file.isEmpty() || file == null)) {
			errors.rejectValue(errFieldName, "errmsg.required");
		} else if (PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
			if (file.getSize() > MB_IN_BYTES) {
				errors.rejectValue(errFieldName, "errmsg.exceeded.size");
			}
		} else {
			errors.rejectValue(errFieldName, "errmsg.invalid.file");
		}
	}

}
