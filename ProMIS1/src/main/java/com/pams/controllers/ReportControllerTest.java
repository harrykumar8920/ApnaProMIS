package com.pams.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.dao.ReportDao;
import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.HearingDetails;
import com.pams.entity.ReportWeeklyInput;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.ActSecDetailsRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.CaseCompanyRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;

@Controller
public class ReportControllerTest {
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepo;
	@Autowired
	private ReportDao reportDTO;

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
	private AssignedTasksPuhRepository assignedTasksPuhRepo;
	@Value("${file.upload}")
	public String filePath1;

	@Value("${file.proMis}")
	public String snmsapi;

	@Value("${pdf.exe}")
	public String pdfExe;

	
	@RequestMapping(value = "genComplaintPreview1")
	public String generateComplaintReport(@RequestParam("assigneTaskID") String assigneTaskID, ModelMap modelMap)
			throws Exception {

		String datestr = assigneTaskID;
		String todatestr = assigneTaskID;

		Date fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(datestr);
		Date toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todatestr);

		List<HearingDetails> monthlyProgressivelist = new ArrayList<HearingDetails>();

		List<HearingDetails> hearingDeatils = hearingDetailsRepos.findByNextHearingDateBetweenAndApproveStatus(fromDate,
				toDate, 2);

		for (HearingDetails hearingDetails : hearingDeatils) {
			/*
			 * List<ActSecDetailsInfo> actSectiondetails = actSecDetailsRepo
			 * .findAllByProcourtdtlID(hearingDetails.getProcourtdtl().getId());
			 */
			
			List<ActSecDetailsInfo> seclist =actSecDetailsRepo.findByAssignedTaskAndIsActive(hearingDetails.getProcourtdtl().getAssignedTask(), 1);

			List<ActSecDetailsInfo> actSectiondetails123 = new ArrayList<ActSecDetailsInfo>();
			for (ActSecDetailsInfo actSectiondetails1 : seclist) {

				ProCourtCaseDetails casedetails = hearingDetails.getProcourtdtl();

				if (((actSectiondetails1.getAct().getId() == 6 && actSectiondetails1.getSection().getId() == 219)||(actSectiondetails1.getAct().getId() == 6 && actSectiondetails1.getSection().getId() == 248)
						|| (actSectiondetails1.getAct().getId() == 6 && actSectiondetails1.getSection().getId() == 249))
						&& casedetails.getFillingDate() == null)

				{
					actSectiondetails123.add(actSectiondetails1);

				}

			}
			if (!actSectiondetails123.isEmpty()) {

				hearingDetails.setActSecDetailsInfo(actSectiondetails123);
				monthlyProgressivelist.add(hearingDetails);
			}

		}

		modelMap.addAttribute("message", "Monthly Progressive Report-Point 4 from " + datestr + " to " + todatestr);

		modelMap.addAttribute("courtCasedtl", monthlyProgressivelist);

		return "report/reportTest4";

	}
	
	@SuppressWarnings("null")
	@RequestMapping(value = "monthlyProgresiveReport441111")
	//Date 12.06.2023 Change LogicmonthlyProgresiveReport44

	public ResponseEntity<Resource> monthlyProgresiveReport4448(@ModelAttribute("reportWeeklyInput") ReportWeeklyInput reportWeeklyInput,
			ModelMap modelMap) throws ParseException {

		String datestr = reportWeeklyInput.getDate();
		String todatestr = reportWeeklyInput.getToDate();

		try {

			File file = File.createTempFile("ComplaintReport", ".pdf");

			String s = "";

			s = " " + snmsapi.trim() + "/genComplaintPreview1?assigneTaskID="
					+ datestr;

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
			//	logger.info(e.getMessage());
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

}
