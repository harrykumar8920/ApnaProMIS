package com.pams.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dto.CriminalTaskDto;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.FreezerAssetOrder;
import com.pams.entity.FreezerAssetsItem;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.FreezerAssetOrderRepository;
import com.pams.service.FreezerAssetsItemRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.ResponseOfRespondentRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;

@Controller
public class FreezerAssetOrderController {
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 200048576; // 200 MB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private FreezerAssetsItemRepository freezerAssetsItemRepo;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private AddAccusedRepository addAccusedRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	ResponseOfRespondentRepository responseOfRespondentRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepo1;
	@Autowired
	private OfficerController officerControl;
	@Autowired
	private FreezerAssetOrderRepository freezerAssetOrderRepo;
	@Autowired
	private ResponceOfRespondentController ResponceOfResp;

	@RequestMapping(value = "deleteFreezerAssetOrder")
	public String deleteFreezerAssetOrder(@ModelAttribute("freezerAsset") FreezerAssetOrder fa, ModelMap model) {
		Long id = fa.getDeletedFreezerItemId();
		Integer idtemp = fa.getTempid();
		FreezerAssetsItem freezeitems = freezerAssetsItemRepo.findById(id).get();

		Date orderdate = fa.getdeleteOrderDate();
		MultipartFile file = fa.getOrderFile();

		if (!fa.getOrderFile().isEmpty()) {

			String fileExt = fa.getOrderFile().getOriginalFilename();
			fileExt = fileExt.substring(fileExt.lastIndexOf("."));

			freezeitems.setuploadOrderFileNamee("OrderUploadFile_" + id + fileExt);

			officerControl.caseFileUpload(file, freezeitems.getuploadOrderFileNamee());
		}
		if (idtemp == 0) {
			freezeitems.setAllStatus(2);
			model.addAttribute("message", " Freeze Assets Items Deleted Successfully : ");
		} else {
			freezeitems.setAllStatus(1);
			model.addAttribute("message", " Freeze Assets Items Unfreeze Successfully : ");
		}
		freezeitems.setDeletedDate(new Date());
		freezeitems.setRegionRemarks(fa.getDeleteRemarks());
		freezeitems.setOrderDate(orderdate);

		model.addAttribute("message", " Freeze Assets Items Deleted Successfully : ");
		

		freezerAssetsItemRepo.save(freezeitems);
		List<FreezerAssetOrder> findAllByProcourtdtlAndAssignedTaskId = freezerAssetOrderRepo
				.findAllByProcourtdtlAndAssignedTask(freezeitems.getFreezerAssetOrder().getProcourtdtl(),
						freezeitems.getFreezerAssetOrder().getAssignedTask(), Sort.by(Sort.Direction.DESC, "id"));
		List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(
				freezeitems.getFreezerAssetOrder().getProcourtdtl(),
				freezeitems.getFreezerAssetOrder().getAssignedTask(), Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("freezerAssetList", findAllByProcourtdtlAndAssignedTaskId);
		FreezerAssetOrder freezerAsset = new FreezerAssetOrder();
		freezerAsset.setAssignedTask(freezeitems.getFreezerAssetOrder().getAssignedTask());
		freezerAsset.setProcourtdtl(freezeitems.getFreezerAssetOrder().getProcourtdtl());
		freezerAsset.setTempID(freezeitems.getFreezerAssetOrder().getId());
		model.addAttribute("freezerAsset", freezerAsset);
		return "Prosecutor/freezerAsset";
	}

	@RequestMapping(value = "editForward")

	public String editForward(@RequestParam(value = "editForward", required = true) Long id, ModelMap model) throws Exception {
		FreezerAssetOrder freezerAssetOrder = freezerAssetOrderRepo.findById(id).get();
		freezerAssetOrder.setApprovalStatus(2);
		freezerAssetOrderRepo.save(freezerAssetOrder);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = freezerAssetOrder.getAssignedTask();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
				+userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.freezerassetorderforward") + " "
						+ assignedTaskPuh.getUser().getSalutation() + " " + assignedTaskPuh.getUser().getFirstName()
						+ " "
						+ (assignedTaskPuh.getUser().getMiddleName().equals("") ? ""
								: assignedTaskPuh.getUser().getMiddleName() + "")
						+ assignedTaskPuh.getUser().getLastName() + " " + " and investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
				userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		FreezerAssetOrder freezerAsset = new FreezerAssetOrder();

	

		List<FreezerAssetOrder> freezerAssetList = freezerAssetOrderRepo.findAllByProcourtdtlAndAssignedTask(
				freezerAssetOrder.getProcourtdtl(), assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));

		model.addAttribute("freezerAssetList", freezerAssetList);
		freezerAsset.setAssignedTask(assignedTaskPuh);
		freezerAsset.setProcourtdtl(freezerAssetOrder.getProcourtdtl());
		List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(
				freezerAssetOrder.getProcourtdtl(), assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("freezerAsset", freezerAsset);
		model.addAttribute("message", "Forward successfully !");

		return "Prosecutor/freezerAsset";

	}

	@RequestMapping(value = "editFreeze")

	public String editFreeze(@RequestParam(value = "editFreeze", required = true) Long id, ModelMap model) {
		FreezerAssetOrder freezerAssetOrder = freezerAssetOrderRepo.findById(id).get();
		Set<FreezerAssetsItem> itemsList = freezerAssetOrder.getFreezerAssetsItem();

		String[] test = new String[itemsList.size()];
		String[] test1 = new String[itemsList.size()];
		String assetsName = null;
		String assetsType = null;

		int i = 0;

		for (FreezerAssetsItem freezerAssetsItem2 : itemsList) {
			assetsName = freezerAssetsItem2.getFreezerAssetsName();
			assetsType = freezerAssetsItem2.getFreezerAssetsType();
			test1[i] = assetsType;
			test[i] = assetsName;
			i++;
		}

		freezerAssetOrder.setFreezerAssetsNameT(test);
		freezerAssetOrder.setFreezerAssetsTypeT(test1);
		List<FreezerAssetOrder> freezerAssetList = freezerAssetOrderRepo.findAllByProcourtdtlAndAssignedTask(
				freezerAssetOrder.getProcourtdtl(), freezerAssetOrder.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		List<AddAccused> allRespondentList = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(
				freezerAssetOrder.getProcourtdtl(), freezerAssetOrder.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", allRespondentList);
		model.addAttribute("freezerAssetList", freezerAssetList);

		model.addAttribute("freezerAsset", freezerAssetOrder);
		return "Prosecutor/freezerAsset";
	}

	/*
	 * @GetMapping("/freezerAssetOrder") public String getCourtCase(Model
	 * model, @RequestParam(value = "inputField") Long assignedTaskID) {
	 */
		
		
		
		
		@RequestMapping(value = "freezerAssetsOrder")
		public String preViewPdf1(ModelMap model, @ModelAttribute CriminalTaskDto criminalTaskDto, BindingResult bindResult,
				RedirectAttributes redirect) {

		FreezerAssetOrder freezerAsset = new FreezerAssetOrder();
		freezerAsset.setAddResponse(freezerAsset.getAddResponse());

		/* freezerAsset.setFreezResponseType(freezerAsset.getFreezResponseType()); */
		AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();

		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();

		List<FreezerAssetOrder> freezerAssetList = freezerAssetOrderRepo.findAllByProcourtdtlAndAssignedTask(
				procasedetails, assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));

		model.addAttribute("freezerAssetList", freezerAssetList);
		freezerAsset.setAssignedTask(assignedTaskPuh);
		freezerAsset.setProcourtdtl(procasedetails);
		List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(procasedetails,
				assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("freezerAsset", freezerAsset);

		return "Prosecutor/freezerAsset";
	}

	@RequestMapping(value = "unFreezerAssetOrder")
	public String unFreezerAssetOrder(@RequestParam(value = "unFreezerAssetOrder", required = true) Long id,
			ModelMap model) {

		FreezerAssetsItem freezeitems = freezerAssetsItemRepo.findById(id).get();
		freezeitems.setAllStatus(1);
		model.addAttribute("message", " Unfreeze Asset Successfully : ");

		freezerAssetsItemRepo.save(freezeitems);

		// FreezerAssetOrder freezerAssetOrder =
		// freezerAssetsItemRepo.findById(freezeitems.getFreezerAssetOrder().getId()).get();
		List<FreezerAssetOrder> findAllByProcourtdtlAndAssignedTaskId = freezerAssetOrderRepo
				.findAllByProcourtdtlAndAssignedTask(freezeitems.getFreezerAssetOrder().getProcourtdtl(),
						freezeitems.getFreezerAssetOrder().getAssignedTask(), Sort.by(Sort.Direction.DESC, "id"));
		List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(
				freezeitems.getFreezerAssetOrder().getProcourtdtl(),
				freezeitems.getFreezerAssetOrder().getAssignedTask(), Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("freezerAssetList", findAllByProcourtdtlAndAssignedTaskId);

		FreezerAssetOrder freezerAsset = new FreezerAssetOrder();
		freezerAsset.setAssignedTask(freezeitems.getFreezerAssetOrder().getAssignedTask());
		freezerAsset.setProcourtdtl(freezeitems.getFreezerAssetOrder().getProcourtdtl());

		freezerAsset.setTempID(freezeitems.getFreezerAssetOrder().getId());
		model.addAttribute("freezerAsset", freezerAsset);
		return "Prosecutor/freezerAsset";
	}

	// forwardFreezerAssets
	@RequestMapping(value = "forwardFreezerAssets")
	public String forwardFreezerAssets(@RequestParam(value = "forwardFreezerAssets", required = true) Long id,
			ModelMap model) {
		FreezerAssetOrder freezerAssetOrder = freezerAssetOrderRepo.findById(id).get();
		freezerAssetOrder.setApprovalStatus(1);
		freezerAssetOrderRepo.save(freezerAssetOrder);
		List<FreezerAssetOrder> freezerAssetlist1 = freezerAssetOrderRepo.findAllByProcourtdtlAndAssignedTask(
				freezerAssetOrder.getProcourtdtl(), freezerAssetOrder.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		FreezerAssetOrder freezerAssetOrder1 = new FreezerAssetOrder();
		freezerAssetOrder1.setAssignedTask(freezerAssetOrder.getAssignedTask());
		freezerAssetOrder1.setProcourtdtl(freezerAssetOrder.getProcourtdtl());
		List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(
				freezerAssetOrder.getProcourtdtl(), freezerAssetOrder.getAssignedTask(),
				Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("freezerAssetList", freezerAssetlist1);
		model.addAttribute("freezerAsset", freezerAssetOrder1);
		return "Prosecutor/freezerAsset";
	}

	@RequestMapping(value = "saveFreezerAssetOrder")
	public String saveFreezerAssetOrder(@ModelAttribute(value = "freezerAsset") FreezerAssetOrder freezerAsset,
			ModelMap model, BindingResult bindResult) throws Exception {
		Long id1 = freezerAsset.getId();

		if (id1 != null) {
			List<FreezerAssetsItem> lstcheck = freezerAssetsItemRepo.findByFreezerAssetOrder(freezerAsset);
			freezerAssetsItemRepo.deleteInBatch(lstcheck);
		}
		if (freezerAsset.getAddResponse() == null || freezerAsset.getAddResponse().getId() == 0) {
			bindResult.rejectValue("addResponse", "errmsg.required");
		}
		/*
		 * if (freezerAsset.getFreezResponseType() == null ||
		 * freezerAsset.getFreezResponseType().equals("Select")) {
		 * bindResult.rejectValue("freezResponseType", "errmsg.required"); }
		 */

		if (freezerAsset.getFreezingOrderDate() == null) {
			bindResult.rejectValue("freezingOrderDate", "errmsg.required");
		}
		
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(freezerAsset.getAssignedTask().getId()).get();
		ProCourtCaseDetails proCourtCaseDetails = assignedTaskPuh.getProCourtCaseDetails();
		if (bindResult.hasErrors()) {
			List<FreezerAssetOrder> freezerAssetList = freezerAssetOrderRepo.findAllByProcourtdtlAndAssignedTask(
					proCourtCaseDetails, assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));

			List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(proCourtCaseDetails,
					assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("allRespondentList", findAll);

			model.addAttribute("freezerAssetList", freezerAssetList);
			model.addAttribute("freezerAsset", freezerAsset);
			return "Prosecutor/freezerAsset";
		}
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		Long id2 = (freezerAssetOrderRepo.findMaxid() != null) ? (freezerAssetOrderRepo.findMaxid() + 1) : 1;
		if (!freezerAsset.getMovableAssetsFile().isEmpty()) {
			MultipartFile movableFile = freezerAsset.getMovableAssetsFile();
			String orignalfilenameOfmovable = movableFile.getOriginalFilename();
			String result = orignalfilenameOfmovable.replaceAll("\\s", "_");

			String movableFilename = "";
			if (id1 != null) {
				freezerAsset.setId(id1);
				movableFilename = id1 + "_movableFile_" + result;
			} else {
				movableFilename = id2 + "_movableFile_" + result;
			}
			officerControl.caseFileUpload(movableFile, movableFilename);
			freezerAsset.setMovableAssetsFileName(movableFilename);
		} else {
			freezerAsset.setMovableAssetsFileName(freezerAsset.getMovableAssetsFileName());
		}
		if ((!freezerAsset.getFreezerOrderFile().isEmpty())) {
			MultipartFile freezingFile = freezerAsset.getFreezerOrderFile();
			String orignalfilenameOfFreezingFile = freezingFile.getOriginalFilename();
			String result1 = orignalfilenameOfFreezingFile.replaceAll("\\s", "_");
			String freezingFileName = "";
			if (id1 != null) {
				freezerAsset.setId(id1);
				freezingFileName = id1 + "_freezing_" + result1;
			} else {
				freezingFileName = id2 + "_freezing_" + result1;
			}
			officerControl.caseFileUpload(freezingFile, freezingFileName);
			freezerAsset.setFreezerOrderFileName(freezingFileName);
		}
		if ((!freezerAsset.getComplianceOrderFile().isEmpty())) {
			MultipartFile complianceOrderFile = freezerAsset.getComplianceOrderFile();
			String orignalfilenameOfcomplianceOrderFile = complianceOrderFile.getOriginalFilename();
			String result2 = orignalfilenameOfcomplianceOrderFile.replaceAll("\\s", "_");
			String complianceOrderFileName = "";
			if (id1 != null) {
				freezerAsset.setId(id1);
				complianceOrderFileName = id1 + "_compliance1_" + result2;
			} else {
				complianceOrderFileName = id2 + "_compliance1_" + result2;
			}
			officerControl.caseFileUpload(complianceOrderFile, complianceOrderFileName);
			freezerAsset.setComplianceOrderFileName(complianceOrderFileName);
		}
		if ((!freezerAsset.getIssueNoticeFile().isEmpty())) {
			MultipartFile issueNoticeFile = freezerAsset.getIssueNoticeFile();
			String orignalfilenameOfissueNoticeFile = issueNoticeFile.getOriginalFilename();
			String result3 = orignalfilenameOfissueNoticeFile.replaceAll("\\s", "_");
			String issueNoticeFileName = "";
			if (id1 != null) {
				freezerAsset.setId(id1);
				issueNoticeFileName = id1 + "_issueNotice1_" + result3;
			} else {
				issueNoticeFileName = id2 + "_issueNotice1_" + result3;
			}
			officerControl.caseFileUpload(issueNoticeFile, issueNoticeFileName);
			freezerAsset.setIssueNoticeFileName(issueNoticeFileName);
		}
		if ((!freezerAsset.getFilingAffidavitFile().isEmpty())) {
			MultipartFile FilingAffidavitFile = freezerAsset.getFilingAffidavitFile();
			String orignalfilenameOfFilingAffidavitFile = FilingAffidavitFile.getOriginalFilename();
			String result4 = orignalfilenameOfFilingAffidavitFile.replaceAll("\\s", "_");
			String FilingAffidavitFileName = "";
			if (id1 != null) {
				freezerAsset.setId(id1);
				FilingAffidavitFileName = id1 + "_FilingAffidavit1_" + result4;
			} else {
				FilingAffidavitFileName = id2 + "_FilingAffidavit1_" + result4;
			}
			officerControl.caseFileUpload(FilingAffidavitFile, FilingAffidavitFileName);
			freezerAsset.setFilingAffidavitFileName(FilingAffidavitFileName);
		}
		if ((!freezerAsset.getImmovableAssetsFile().isEmpty())) {
			MultipartFile immovableAssetsFile = freezerAsset.getImmovableAssetsFile();
			String orignalfilenameOfimmovableAssetsFile = immovableAssetsFile.getOriginalFilename();
			String result5 = orignalfilenameOfimmovableAssetsFile.replaceAll("\\s", "_");
			String immovableAssetsFileName = "";
			if (id1 != null) {
				freezerAsset.setId(id1);
				immovableAssetsFileName = id1 + "_immovableAssets_" + result5;
			} else {
				immovableAssetsFileName = id2 + "_immovableAssets_" + result5;
			}
			officerControl.caseFileUpload(immovableAssetsFile, immovableAssetsFileName);
			freezerAsset.setImmovableAssetsFileName(immovableAssetsFileName);
		} else {
			freezerAsset.setImmovableAssetsFileName(freezerAsset.getImmovableAssetsFileName());
		}
		freezerAsset.setCreatedBy(userdet);
		freezerAsset.setCreatedDate(new Date());
		freezerAsset.setUpdatedBy(userdet);
		freezerAsset.setUpdatedDate(new Date());
		Set<FreezerAssetsItem> freezerAssetsItems = new HashSet<>();
		int size = freezerAsset.getFreezerAssetsNameT().length;
		for (int i = 1; i <= size; i++) {
			FreezerAssetsItem freezerAssetsItem = new FreezerAssetsItem();
			String assetsName = freezerAsset.getFreezerAssetsNameT()[i - 1];
			String assetsType = freezerAsset.getFreezerAssetsTypeT()[i - 1];
			freezerAssetsItem.setFreezerAssetsType(assetsType);
			freezerAssetsItem.setFreezerAssetsName(assetsName);
			freezerAssetsItem.setFreezerAssetOrder(freezerAsset);
			freezerAssetsItems.add(freezerAssetsItem);

		}
		freezerAsset.setFreezerAssetsItem(freezerAssetsItems);
		
		if (id1 == null) {
			model.addAttribute("message", " freezerAssetOrder Added Successfully : ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
					+userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Save"),
					utils.getMessage("log.login.freezerassetordersave") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
					userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

		} else {
			model.addAttribute("message", " freezerAssetOrder Updated Successfully : ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
					+userdet.getFirstName() + " "
					+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
					+ userdet.getLastName(), "ProMIS",
					Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.freezerassetorderupdate") + " " + " and Investigation Number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
									userdet.getFirstName() + " "
									+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
									+ userdet.getLastName(), "true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

			/* anjali */
			
		}
		freezerAssetOrderRepo.save(freezerAsset);
		List<FreezerAssetOrder> freezerAssetList = freezerAssetOrderRepo.findAllByProcourtdtlAndAssignedTask(
				freezerAsset.getProcourtdtl(), assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		List<AddAccused> findAll = addAccusedRepository.findAllByProcourtdtlAndAssignedTask(
				freezerAsset.getProcourtdtl(), assignedTaskPuh, Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("freezerAssetList", freezerAssetList);

		FreezerAssetOrder freezerAsset1 = new FreezerAssetOrder();

		freezerAsset1.setAssignedTask(assignedTaskPuh);
		freezerAsset1.setProcourtdtl(freezerAsset.getProcourtdtl());
		model.addAttribute("allRespondentList", findAll);
		model.addAttribute("freezerAsset", freezerAsset1);
		return "Prosecutor/freezerAsset";
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
