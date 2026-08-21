package com.pams.controllers;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.ReportDao;
import com.pams.dto.CriminalTaskDto;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.ComplaintReport;
import com.pams.entity.Complaintdetl;
import com.pams.entity.HearingDetails;
import com.pams.entity.PairaviDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.CaseCompanyRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.DetailsTypeRespository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
import com.pams.validation.ProMISValidator;

import jakarta.validation.Valid;

@Controller
public class GenerateComplaintController {

	private static final Logger logger = LoggerFactory.getLogger(GenerateComplaintController.class);
	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	OfficerController officectrl;
	@Autowired
	private DetailsTypeRespository detailsTypeRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;

	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;

	@Autowired
	private AccusedCompDAO accusedComdao;
	@Autowired

	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private CaseCompanyRepository caseCompanyRepos;

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepo1;
	@Autowired
	AddStatusRepository addStatusRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepo;
	@Autowired
	private ActSecDetailsRepository actSecDetailsRepo;

	@Autowired
	private ComplaintdetlRepository complaintdetlRepo;

	@Autowired
	private ComplaintReportRepository complaintReportRepo;
	@Autowired
	private HearingDetailsRepository hearingDetailsRepo;
	@Autowired
	private PairaviDetailsRepository pairaviDetailsRepo;

	@Autowired
	private OfficerController offtctrl;

	@Value("${file.upload}")
	public String filePath1;

	@Value("${file.proMis}")
	public String snmsapi;

	@Value("${pdf.exe}")
	public String pdfExe;

	@RequestMapping(value = "generateComplaintReport")
	public String generateComplaintReport(ModelMap modelMap, @Valid @ModelAttribute CriminalTaskDto criminalTaskDto,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception {

		ComplaintReport compReport = complaintReportRepo.findByAssignedTaskPuh(criminalTaskDto.getAssignedTask());

		if (compReport != null) {
			if (compReport.getTypeOfReport() == 1) {
				int tabId = 28;
				officectrl.modelAttributeObject(criminalTaskDto.getAssignedTask(), modelMap, tabId, criminalTaskDto);

				redirect.addAttribute("tabId", tabId);

				redirect.addAttribute("assignedTaskPuh", criminalTaskDto.getAssignedTask());

				modelMap.addAttribute("message", " File already Uploaded Manualy: ");

				return "Task/CriminalTaskPage";
			}
		}

		ComplaintReport complaintReport = new ComplaintReport();

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		ProCourtCaseDetails courtDtl = assignedTaskPuh.getProCourtCaseDetails();
		
		//proCourtCaseDetails courtDtl = proCourtCaseDetailsRepo.findALLById(assignedTaskPuh.getProCourtCase().getId());

		Complaintdetl compdtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(courtDtl, assignedTaskPuh);
		if (compdtl == null) {
			int tabId = 21;
			offtctrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			modelMap.addAttribute("message", " Please add complainant details: ");

			return "Task/CriminalTaskPage";
		}

		List<ActSecDetailsInfo> actsecDtl = actSecDetailsRepo
				.findAllByProcourtdtlID(courtDtl.getId());

		modelMap.addAttribute("actsecDtl", actsecDtl);

		modelMap.addAttribute("courtDtl", courtDtl);

		modelMap.addAttribute("compdtl", compdtl);

		// AccusedCompCaseDtl savedAccusedCompCaseDtl =
		// accusedComdao.findByProCourtId(assignedTaskPuh.getProCourtCase().getId());

		List<CaseCompany> coyList = caseCompanyRepos.findByProcourtdtl(courtDtl);
		if (coyList == null || coyList.isEmpty()) {

			int tabId = 22;
			offtctrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			modelMap.addAttribute("message", " Please add Company details: ");

			return "Task/CriminalTaskPage";
		}

		PairaviDetails pairaviofficer = pairaviDetailsRepo
				.findAllByProcourtdtlAndIsActive(courtDtl, true);

		if (pairaviofficer != null) {
			//complaintReport.setPairaviofficer(pairaviofficer.getName());

		} else {
			int tabId = 23;
			offtctrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			modelMap.addAttribute("message", " Please add Pairavi details: ");

			return "Task/CriminalTaskPage";

		}

		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtl(courtDtl);

		if (accusedList == null || accusedList.isEmpty()) {

			int tabId = 26;
			offtctrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			modelMap.addAttribute("message", " Please add Accused details: ");

			return "Task/CriminalTaskPage";
		}

		HearingDetails heringDtl = hearingDetailsRepo
				.findByProcourtdtlAndCurrentStatus(courtDtl, true);

		if (heringDtl != null) {
			// complaintReport.setCounsel(heringDtl.getCounselName());
			// complaintReport.setCounsel2(heringDtl.getCounselName1());
		} else {

			int tabId = 25;
			offtctrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);
			modelMap.addAttribute("message", " Please add Hearing details: ");

			return "Task/CriminalTaskPage";

		}

		try {

			ComplaintReport compDtlReport = complaintReportRepo.findByAssignedTaskPuh(assignedTaskPuh);
			if (compDtlReport != null)

			{

				complaintReport.setId(compDtlReport.getId());
				complaintReport.setMpara1(compDtlReport.getMpara1());
				complaintReport.setDescPara(compDtlReport.getDescPara());
				complaintReport.setBackPara(compDtlReport.getBackPara());
				complaintReport.setPrayerPara(compDtlReport.getPrayerPara());
				complaintReport.setPlace(compDtlReport.getPlace());
				complaintReport.setDate(compDtlReport.getDate());

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		complaintReport.setAssignedTaskPuh(assignedTaskPuh);
		complaintReport.setAddAccused(accusedList);
		complaintReport.setActsecDtl(actsecDtl);
		modelMap.addAttribute("CompanyList", coyList);
		modelMap.addAttribute("personList", accusedList);
		modelMap.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelMap.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelMap.addAttribute("complaintReport", complaintReport);

		// return "report/GenerateReport" ;

		return "report/generateReport1";
	}

	@RequestMapping(value = "generateReport1")
	public String forwardPdf(ModelMap modelMap, @Valid @ModelAttribute ComplaintReport complaintReport,
			RedirectAttributes redirect) throws Exception {

		int tabId = 28;
		complaintReport.setCreatedDate(new Date());
		complaintReport.setTypeOfReport(2);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		complaintReport.setUser(userdet);

		AssignedTaskPuhAfterCOurt asignntask = complaintReport.getAssignedTaskPuh();
		ProCourtCaseDetails proCourtCaseDtl = asignntask.getProCourtCaseDetails();

		ComplaintReport compReport = complaintReportRepo.findByAssignedTaskPuh(complaintReport.getAssignedTaskPuh());

		if (compReport == null) {

			complaintReportRepo.save(complaintReport);
		}

		

		else {
			complaintReport.setId(compReport.getId());
			complaintReport.setApproveStatus(0);
			complaintReportRepo.save(complaintReport);

		}

		//AssignedTaskPuh assignedTaskPuh = complaintReport.getAssignedTaskPuh();

		ProCourtCaseDetails courtDtl = proCourtCaseDetailsRepo.findALLById(proCourtCaseDtl.getId());

		Complaintdetl compdtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(courtDtl, asignntask);

		List<ActSecDetailsInfo> actsecDtl = actSecDetailsRepo
				.findAllByProcourtdtlID(proCourtCaseDtl.getId());

		modelMap.addAttribute("actsecDtl", actsecDtl);

		modelMap.addAttribute("courtDtl", courtDtl);

		modelMap.addAttribute("compdtl", compdtl);

		// AccusedCompCaseDtl savedAccusedCompCaseDtl =
		// accusedComdao.findByProCourtId(assignedTaskPuh.getProCourtCase().getId());

		List<CaseCompany> coyList = caseCompanyRepos.findByProcourtdtl(proCourtCaseDtl);

		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtl(proCourtCaseDtl);

		List<HearingDetails> heringDtl = hearingDetailsRepo.findByProcourtdtl(proCourtCaseDtl);
		HearingDetails hd = heringDtl.get(0);

		if (heringDtl != null) {
			// complaintReport.setCounsel(hd.getCounselName());
			// complaintReport.setCounsel2(hd.getCounselName1());
		}

		PairaviDetails pairaviofficer = pairaviDetailsRepo
				.findAllByProcourtdtlAndIsActive(proCourtCaseDtl, true);
		/*
		 * if (pairaviofficer != null) {
		 * complaintReport.setPairaviofficer(pairaviofficer.getName());
		 * 
		 * }
		 */

		try {

			ComplaintReport compDtlReport = complaintReportRepo.findByAssignedTaskPuh(asignntask);
			if (compDtlReport != null)

			{
				complaintReport.setId(compDtlReport.getId());
				complaintReport.setMpara1(compDtlReport.getMpara1());
				complaintReport.setDescPara(compDtlReport.getDescPara());
				complaintReport.setBackPara(compDtlReport.getBackPara());
				complaintReport.setPrayerPara(compDtlReport.getPrayerPara());
				complaintReport.setPlace(compDtlReport.getPlace());
				complaintReport.setDate(compDtlReport.getDate());
				compDtlReport.setId(compDtlReport.getId());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		complaintReport.setAssignedTaskPuh(asignntask);
		complaintReport.setAddAccused(accusedList);
		complaintReport.setActsecDtl(actsecDtl);
		modelMap.addAttribute("CompanyList", coyList);
		modelMap.addAttribute("personList", accusedList);
		modelMap.addAttribute("assignedTaskPuh", asignntask);

		modelMap.addAttribute("assignedTaskPuh", asignntask);

		modelMap.addAttribute("complaintReport", complaintReport);

		// return "report/GenerateReport" ;

		return "report/generateReport1";
	}

	@RequestMapping(value = "genComplaintPdfPreviewnew")
	public ResponseEntity<Resource> preViewPdf1(ModelMap modelMap,
			@Valid @ModelAttribute CriminalTaskDto criminalTaskDto, BindingResult bindResult,
			RedirectAttributes redirect) {

		try {

			File file = File.createTempFile("ComplaintReport", ".pdf");

			String s = "";

			s = " " + snmsapi.trim() + "/genComplaintPreview?assigneTaskID="
					+ criminalTaskDto.getAssignedTask().getId();

			System.out.println("FilePath=====================" + s);
			String Command = s.trim() + " ";
			createpdf(file, Command);
			// String filePath = "E:\\SNMS\\file_upload\\Preview";

			String filePath = filePath1 + File.separator + "Preview";

			File parent = new File(filePath).getParentFile().getCanonicalFile();
			//ESAPI.validator().getValidDirectoryPath("DirectoryName", filePath, parent, false);
			Path path = Paths.get(filePath + File.separator + file.getName());

			Resource resource = null;
			try {
				resource = new UrlResource(path.toUri());

			} catch (MalformedURLException e) {
				logger.info(e.getMessage());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=" + file.getName());

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(resource);

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "genComplaintPdfPreview")
	public ResponseEntity<Resource> preViewPdf(ModelMap modelMap,
			@Valid @ModelAttribute ComplaintReport complaintReport) {

		try {

			File file = File.createTempFile("ComplaintReport", ".pdf");

			String s = "";

			s = " " + snmsapi.trim() + "/genComplaintPreview?assigneTaskID="
					+ complaintReport.getAssignedTaskPuh().getId();

			System.out.println("FilePath=====================" + s);
			String Command = s.trim() + " ";
			createpdf(file, Command);
			// String filePath = "E:\\SNMS\\file_upload\\Preview";

			String filePath = filePath1 + File.separator + "Preview";

			File parent = new File(filePath).getParentFile().getCanonicalFile();
			//ESAPI.validator().getValidDirectoryPath("DirectoryName", filePath, parent, false);
			Path path = Paths.get(filePath + File.separator + file.getName());

			Resource resource = null;
			try {
				resource = new UrlResource(path.toUri());

			} catch (MalformedURLException e) {
				logger.info(e.getMessage());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=" + file.getName());

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(resource);

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void createpdf(File file, String s) throws InterruptedException {
		// String output= file.getPath();

		String output = filePath1 + File.separator + "Preview" + File.separator + file.getName();

		System.out.println("output===========================" + output);
		String command = pdfExe.trim() + " " + " " + s + output.trim();
		System.out.println("command==========================" + command);
		try {

			// Running the above command
			Runtime run = Runtime.getRuntime();
			Process proc = run.exec(command);
			proc.waitFor(45, TimeUnit.SECONDS);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "genComplaintPreview")
	public String generateComplaintReport(@RequestParam("assigneTaskID") Long assigneTaskID, ModelMap modelMap)
			throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(assigneTaskID).get();
		ProCourtCaseDetails courtDtl = assignedTaskPuh.getProCourtCaseDetails();

		//proCourtCaseDetails courtDtl = proCourtCaseDetailsRepo.findALLById(assignedTaskPuh.getProCourtCase().getId());

		Complaintdetl compdtl = complaintdetlRepo.findAllByProcourtdtlAndAssignedTask(courtDtl, assignedTaskPuh);

		modelMap.addAttribute("courtDtl", courtDtl);

		modelMap.addAttribute("compdtl", compdtl);

		// AccusedCompCaseDtl savedAccusedCompCaseDtl =
		// accusedComdao.findByProCourtId(assignedTaskPuh.getProCourtCase().getId());

		List<ActSecDetailsInfo> actsecDtl = actSecDetailsRepo.findAllByProcourtdtlID(courtDtl.getId());

		modelMap.addAttribute("actsecDtl", actsecDtl);

		List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtl(courtDtl);
		List<CaseCompany> coyList = caseCompanyRepos.findByProcourtdtl(courtDtl);

		ComplaintReport complaintReport = new ComplaintReport();

		List<HearingDetails> heringDtl = hearingDetailsRepo.findByProcourtdtl(courtDtl);
		HearingDetails hd = heringDtl.get(0);

		if (heringDtl != null) {
			// complaintReport.setCounsel(hd.getCounselName());
			// complaintReport.setCounsel2(hd.getCounselName1());
		}

		PairaviDetails pairaviofficer = pairaviDetailsRepo
				.findAllByProcourtdtlAndIsActive(courtDtl, true);

		if (pairaviofficer != null) {
		//	complaintReport.setPairaviofficer(pairaviofficer.getName());

		}

		complaintReport.setActsecDtl(actsecDtl);
		try {

			ComplaintReport compDtlReport = complaintReportRepo.findByAssignedTaskPuh(assignedTaskPuh);

			if (compDtlReport != null)

			{
				complaintReport.setId(compDtlReport.getId());
				complaintReport.setMpara1(compDtlReport.getMpara1());
				complaintReport.setDescPara(compDtlReport.getDescPara());
				complaintReport.setBackPara(compDtlReport.getBackPara());
				complaintReport.setPrayerPara(compDtlReport.getPrayerPara());
				complaintReport.setPlace(compDtlReport.getPlace());
				complaintReport.setDate(compDtlReport.getDate());
				// compDtlReport.setId(compDtlReport.getId());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		complaintReport.setAssignedTaskPuh(assignedTaskPuh);
		complaintReport.setAddAccused(accusedList);
		modelMap.addAttribute("CompanyList", coyList);
		modelMap.addAttribute("personList", accusedList);
		modelMap.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelMap.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelMap.addAttribute("complaintReport", complaintReport);

		// return "report/GenerateReport" ;

		return "report/generateReportPreview";
	}

	@RequestMapping(value = "complaintReportUpload1")

	public String generateComplaintReport2(ModelMap modelMap, @Valid @ModelAttribute CriminalTaskDto criminalTaskDto,
			BindingResult error, RedirectAttributes redirect) throws Exception {

		int tabId =27;
		MultipartFile compfile = criminalTaskDto.getComplaintReportUpload1();
		ProMISValidator promisValid = new ProMISValidator();

		if (compfile != null) {
			if (compfile.getSize() > 0 || !compfile.isEmpty()) {
				promisValid.isValidFile(compfile, error, true, "complaintReportUpload1");
				if (!promisValid.isValidFileName(compfile.getOriginalFilename()))
					error.rejectValue("complaintReportUpload1", "errmsg.filename");
				if (!promisValid.isValidFileTikka(compfile.getOriginalFilename(), compfile))
					error.rejectValue("complaintReportUpload1", "errmsg.maliciousdata");
			} else {
				error.rejectValue("complaintReportUpload1", "errmsg.required");
			}
		} else {
			error.rejectValue("complaintReportUpload1", "errmsg.required");
		}

		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		//proCourtCaseDetails proCourtCaseDtl = proCourtCaseDetailsRepo.findByAssignedTask(assignedTaskPuh);
		
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		if (error.hasErrors()) {

			officectrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			redirect.addAttribute("tabId", tabId);

			redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelMap.addAttribute("message", " Please Chose Correct file: ");

			return "Task/CriminalTaskPage";

		}

		CriminalTaskDto crimnaltaskDTO = new CriminalTaskDto();

		ComplaintReport compReport = complaintReportRepo.findByAssignedTaskPuh(assignedTaskPuh);

		if (compReport == null) {
			String courtcaseID = proCourtCaseDetailsRepo.findALLById(proCourtCaseDtl.getId()).getId().toString();
			String filename = "Complaint" + courtcaseID + ".pdf";
			caseFileUpload(compfile, filename);

			ComplaintReport compreport = new ComplaintReport();

			compreport.setComplaintReportUpload(filename);
			compreport.setCreatedDate(new Date());
			compreport.setTypeOfReport(1);
			compreport.setAssignedTaskPuh(assignedTaskPuh);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			compreport.setUser(userdet);

			complaintReportRepo.save(compreport);
		} else if (compReport.getTypeOfReport() == 2) {

			officectrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, crimnaltaskDTO);

			redirect.addAttribute("tabId", tabId);

			redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelMap.addAttribute("message", " File already generated: ");

			return "Task/CriminalTaskPage";

		} else {

			String courtcaseID = proCourtCaseDetailsRepo.findALLById(proCourtCaseDtl.getId()).getId()
					.toString();
			String filename = "Complaint" + courtcaseID + ".pdf";
			caseFileUpload(compfile, filename);

			compReport.setComplaintReportUpload(filename);
			compReport.setCreatedDate(new Date());
			compReport.setTypeOfReport(1);
			compReport.setApproveStatus(0);
			compReport.setAssignedTaskPuh(assignedTaskPuh);

			complaintReportRepo.save(compReport);
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
					+userdet.getFirstName() + " "
					+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
					+ userdet.getLastName(), "ProMIS",
					Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.Update"),
					utils.getMessage("log.login.fileuploadReplace") + " " + " and Investigation Number is "
							+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
									userdet.getFirstName() + " "
									+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
									+ userdet.getLastName(), "true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();

			officectrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, crimnaltaskDTO);

			redirect.addAttribute("tabId", tabId);

			redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelMap.addAttribute("message", " File replaced successfully: ");

			return "Task/CriminalTaskPage";

		}
		
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
				+userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Save"),
				utils.getMessage("log.login.fileuploadsaved") + " " + " and Investigation number is "
						+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
				userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();

		
		
		

		officectrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, crimnaltaskDTO);

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelMap.addAttribute("message", " File Upload Successfully: ");

		return "Task/CriminalTaskPage";
	}

	@RequestMapping(value = "complaintReportUpload2")

	public String generateComplaintReportedit(ModelMap modelMap, @Valid @ModelAttribute CriminalTaskDto criminalTaskDto,
			BindingResult error, RedirectAttributes redirect) throws Exception {

		int tabId = 27;
		CriminalTaskDto crimnaltaskDTO = new CriminalTaskDto();
		MultipartFile compfile = criminalTaskDto.getComplaintReportUpload1();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();
		ProCourtCaseDetails proCourtCaseDtl = assignedTaskPuh.getProCourtCaseDetails();
		ProMISValidator promisValid = new ProMISValidator();

		if (compfile != null) {
			if (compfile.getSize() > 0 || !compfile.isEmpty()) {
				promisValid.isValidFile(compfile, error, true, "complaintReportUpload1");
				if (!promisValid.isValidFileName(compfile.getOriginalFilename()))
					error.rejectValue("complaintReportUpload1", "errmsg.filename");
				if (!promisValid.isValidFileTikka(compfile.getOriginalFilename(), compfile))
					error.rejectValue("complaintReportUpload1", "errmsg.maliciousdata");
			} else {
				error.rejectValue("complaintReportUpload1", "errmsg.required");
			}
		} else {
			error.rejectValue("complaintReportUpload1", "errmsg.required");
		}

		if (error.hasErrors()) {

			officectrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, criminalTaskDto);

			redirect.addAttribute("tabId", tabId);

			redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

			modelMap.addAttribute("message", " Please Chose Correct file: ");

			return "Task/CriminalTaskPage";

		}

		ComplaintReport compReport = complaintReportRepo.findByAssignedTaskPuh(assignedTaskPuh);

		String courtcaseID = proCourtCaseDetailsRepo.findALLById(proCourtCaseDtl.getId()).getId()
				.toString();
		String filename = "Complaint" + courtcaseID + ".pdf";
		caseFileUpload(compfile, filename);

		compReport.setComplaintReportUpload(filename);
		compReport.setCreatedDate(new Date());
		compReport.setTypeOfReport(1);
		compReport.setAssignedTaskPuh(assignedTaskPuh);		
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
				+userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Save"),
				utils.getMessage("log.login.fileuploadsaved") + " " + " and Investigation number is "
						+ proCourtCaseDtl.getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
				userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true", proCourtCaseDtl.getAddCase().getId());
		auditBeanBo.save();


		complaintReportRepo.save(compReport);

		officectrl.modelAttributeObject(assignedTaskPuh, modelMap, tabId, crimnaltaskDTO);

		redirect.addAttribute("tabId", tabId);

		redirect.addAttribute("assignedTaskPuh", assignedTaskPuh);

		modelMap.addAttribute("message", " File Upload Successfully: ");

		return "Task/CriminalTaskPage";
	}

	public void caseFileUpload(@RequestParam("file") MultipartFile file, String name) {
	    BufferedOutputStream stream = null;

	    try {
	        // Get the parent directory of the filePath (use whatever base path you're working with)
	        File parent = new File(filePath).getParentFile().getCanonicalFile();

	        // 1. Validate directory path - Ensure the parent directory exists and is valid
	        String directory = validateDirectoryPath(filePath, parent);
	        
	        // 2. Validate the file name - Ensure the name is valid (no special characters or invalid characters for filenames)
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
	    Path path = parent.toPath();  // Convert File to Path
	    
	    // Ensure the parent directory exists
	    if (!Files.exists(path)) {
	        throw new IllegalArgumentException("Parent directory does not exist: " + parent.getAbsolutePath());
	    }
	    
	    // If the path exists and is a directory, return it; otherwise, throw an error
	    if (Files.isDirectory(path)) {
	        return path.toString();  // Return the valid directory
	    } else {
	        throw new IllegalArgumentException("Invalid directory path: " + filePath);
	    }
	}

	// Validate the file name using a regular expression (to check if it contains invalid characters)
	private boolean isValidFileName(String name) {
	    if (name == null || name.trim().isEmpty()) {
	        return false;
	    }

	    // Regex to check if the file name contains invalid characters like / \ : * ? " < > | 
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
	@RequestMapping(value = "addCriminalDtl", params = "forwardUploadFile1")
	public String forwardUploadFile111(@RequestParam(value = "forwardUploadFile1", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		
		
		int tabId = 27;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		ComplaintReport compreport = complaintReportRepo.findById(id).get();
		compreport.setApproveStatus(2);
		complaintReportRepo.save(compreport);
		
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
				+userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.forward"),
				utils.getMessage("log.login.fileuploadforward") + " "
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
		model.addAttribute("message", " Criminal complaint report Confirmed successfully ");
		
		officectrl.modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPage";

	}
	@RequestMapping(value = "addCriminalDtl", params = "approveUploadFile1")
	public String approveUploadFile1(@RequestParam(value = "approveUploadFile1", required = true) Long id,
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		
		
		int tabId = 27;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		ComplaintReport compreport = complaintReportRepo.findById(id).get();
		compreport.setApproveStatus(2);
		complaintReportRepo.save(compreport);
		
		
		 model.addAttribute("message", "Criminal complaint report approved Successfully  :");

			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
					+userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.approved"),
					utils.getMessage("log.login.fileuploadapproved") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
					userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		
		
		officectrl.modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPageforApproval";

	}
	
	@RequestMapping(value = "addCriminalDtl", params = "rejectUploadFile1")
	public String rejectUploadFile1(
			ModelMap model, CriminalTaskDto criminalTaskDto) throws Exception {

		
		
		int tabId = 27;
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1.findById(criminalTaskDto.getAssignedTask().getId()).get();

		ComplaintReport compreport = complaintReportRepo.findById(criminalTaskDto.getGenreportID()).get();
		compreport.setApproveStatus(3);
		compreport.setRejectRemark(criminalTaskDto.getRejectRemarkGenReport());
		complaintReportRepo.save(compreport);
		 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),userdet.getSalutation()+" "
					+userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"ProMIS", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.login.sendback"),
					utils.getMessage("log.login.fileuploadsendback") + " " + " and Investigation number is "
							+ assignedTaskPuh.getProCourtCaseDetails().getAddCase().getInvestigationOrderNo(),userdet.getSalutation()+" "+
					userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true", assignedTaskPuh.getProCourtCaseDetails().getAddCase().getId());
			auditBeanBo.save();
		
		 model.addAttribute("message", "Criminal complaint report rejected Successfully  :");
		
		officectrl.modelAttributeObject(assignedTaskPuh, model, tabId, criminalTaskDto);

		return "Task/CriminalTaskPageforApproval";

	}
	
	
	
	

}
