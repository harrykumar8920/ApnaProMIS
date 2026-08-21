package com.pams.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.dto.CinSearchDTO;
import com.pams.dto.ReportByCINDTO;
import com.pams.entity.AddAccused;
import com.pams.entity.AddCase;
import com.pams.entity.CaseProcessingDates;
import com.pams.entity.Complaintdetl;
import com.pams.entity.CouncilDetails;
import com.pams.entity.HearingDetails;
import com.pams.entity.Inspector;
import com.pams.entity.PairaviDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AccusedMasterRepository;
import com.pams.service.AccusedResponseRepository;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddCaseRepository;
import com.pams.service.AddCompanyRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.CaseProcessingDatesRepository;
import com.pams.service.ComplaintReportRepository;
import com.pams.service.ComplaintdetlRepository;
import com.pams.service.CouncilDetailsRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.InspectorRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.PerformaPartyRepo;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.RemarksRepository;
import com.pams.service.UploadAdditionalFilesDetailsRepository;

@Controller
public class ReportControllerByCIN {
	
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
	private PairaviDetailsRepository pairaviDetailRepo;

	@Autowired
	private HearingDetailsRepository hearingdtlRepo;
	
	@Autowired
	private AddCompanyRepository addCompanyRepo;

	@Autowired
	AddStatusRepository addStatusRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepo;

	@Autowired
	private CaseProcessingDatesRepository caseProcessingRepo;

	@Autowired
	private ComplaintdetlRepository complaintdetlRepo;

	@Autowired
	private AccusedMasterRepository accusedMasterRepo;

	@Autowired
	private CouncilDetailsRepository councilDetailsRepo;

	@Autowired
	private InspectorRepository inspectorRepository;

	@Autowired
	private RemarksRepository remarksRepo;

    @Autowired
    private AddCaseRepository addCaseRepository;

    @Autowired
    private ProCourtCaseDetailsRepository proCourtCaseDetailsRepository;

    @PostMapping("/viewDetails")
    public String viewDetails(@RequestParam("caseId") Long caseId, Model model) {     
    	
    	
    	ReportByCINDTO reportByCINDTO=new ReportByCINDTO();
		ProCourtCaseDetails proCourtCaseDetails =  proCourtCaseDetailsRepository.findById(caseId).get();
		List<CouncilDetails> allouncilDetails = councilDetailsRepo.findAllByProcourtdtl(proCourtCaseDetails);
		
		List<HearingDetails> allHearingDetails1 = hearingdtlRepo.findByProcourtdtl(proCourtCaseDetails);
		
		List<HearingDetails> allHearingDetails = allHearingDetails1.stream()
			    .sorted(Comparator.comparing(HearingDetails::getNextHearingDate).reversed())
			    .collect(Collectors.toList());
		
		
		Complaintdetl complaintdetl = complaintdetlRepo.findByProcourtdtl(proCourtCaseDetails);
		List<PairaviDetails> allPairaviDetails = pairaviDetailRepo.findAllByProcourtdtl(proCourtCaseDetails);
		CaseProcessingDates caseProcessingDates = caseProcessingRepo.findByProcourtdtl(proCourtCaseDetails);
		List<Inspector> byProCourtCaseDetails = inspectorRepository.findByProcourtdtl(proCourtCaseDetails);
		List<AddAccused> allByProcourtdtlAndApproveStatus = addAccusedRepo.findAllByProcourtdtlAndApproveStatus(proCourtCaseDetails, 2);
		
		reportByCINDTO.setCaseProcessingDates(caseProcessingDates);
		reportByCINDTO.setComplaintdetl(complaintdetl);
		reportByCINDTO.setHearingDetails(allHearingDetails);
		reportByCINDTO.setPairaviDetails(allPairaviDetails);
		reportByCINDTO.setCouncilDetails(allouncilDetails);
		reportByCINDTO.setProCourtCaseDetails(proCourtCaseDetails);
		reportByCINDTO.setInspector(byProCourtCaseDetails);
		reportByCINDTO.setAddAccused(allByProcourtdtlAndApproveStatus);
		
		
		 model.addAttribute("reportByCINDTO", reportByCINDTO);
		 model.addAttribute("cinSearch", new CinSearchDTO());
  	  model.addAttribute("caseListt",new ArrayList<ProCourtCaseDetails>());
  	
  	return "report/viewDetailsByCIN";
  }
    @GetMapping("/reportByCIN")
    public String reportPage(Model model) {

        model.addAttribute("cinSearch", new CinSearchDTO());
        return "report/reportGenByCin";
    }

    
    @PostMapping("/searchCaseByCin")
    public String searchCaseByCin(@ModelAttribute("cinSearch") CinSearchDTO dto,
                                  Model model) {
    	
    	
    	AddCase byProSectionOrderNumber = addCaseRepository.findByCinNumber(dto.getCin());
    	List<ProCourtCaseDetails> caseListt=null;
    	if(byProSectionOrderNumber!=null)
    	 caseListt = proCourtCaseDetailsRepository.findByAddCaseId(byProSectionOrderNumber.getId());
    	
    	  model.addAttribute("caseListt",caseListt);
    	  
    	  if(caseListt==null)
    	  model.addAttribute("massage", "CIN is not available in Database");

    	return "report/reportGenByCin";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	/*
	 * public ResponseEntity<byte[]> getReportByCIN(
	 * 
	 * @RequestParam("cin") String cin,
	 * 
	 * @RequestParam(name = "col", required = false) List<String> selectedColumns) {
	 * 
	 * 
	 * ReportByCINDTO report = reportByCINService.getFullReportByCIN(cin);
	 * 
	 * 
	 * byte[] pdfBytes = pdfService.generateDynamicPdf(report, selectedColumns);
	 * 
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Report_" +
	 * cin + ".pdf");
	 * 
	 * return ResponseEntity.ok() .headers(headers)
	 * .contentType(MediaType.APPLICATION_PDF) .body(pdfBytes); }
	 */
}
