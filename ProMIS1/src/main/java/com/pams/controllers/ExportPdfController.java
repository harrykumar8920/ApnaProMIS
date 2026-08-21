package com.pams.controllers;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pams.dto.ProCourtDtlDto;
import com.pams.entity.DetailsType;
import com.pams.entity.PairaviDetails;
import com.pams.entity.PetRespDetail;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.DetailsTypeRespository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.PairaviDetailsRepository;
import com.pams.service.PairaviTypeRepository;
import com.pams.service.PetRespDetailRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.CourtCasePdfExportal;
@Controller
public class ExportPdfController {
	@Autowired
	private PetRespDetailRepository petRespDetailRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private DetailsTypeRespository detailsTypeRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private PairaviDetailsRepository pairaviDetailRepo;
	@Autowired
	private PairaviTypeRepository pairaviTypeRepo;

	@Autowired
	private HearingDetailsRepository hearingdtlRepo;
	
	/*
	 * @RequestMapping(value ="/getPrintReport", produces =
	 * MediaType.APPLICATION_PDF_VALUE)
	 */
	@RequestMapping(value = "updateInfo", params = "getPrintReport" , produces =MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> exportSummonToExcel(@ModelAttribute  ProCourtCaseDetails proCourtCaseDetails) throws Exception {
		
		
		ProCourtDtlDto  courtdtlDto = new ProCourtDtlDto();
		DetailsType  petType = detailsTypeRepo.findById(2L).get();
		DetailsType  respType = detailsTypeRepo.findById(1L).get();
		ProCourtCaseDetails   courtdtl = proCourtCaseDetailsRepo.findALLById(proCourtCaseDetails.getId());
		List<PetRespDetail> petdtl = petRespDetailRepo.findAllByProcourtdtl(courtdtl);
	     List<PairaviDetails> pairavidtl = pairaviDetailRepo.findAllByProcourtdtl(courtdtl);
		/*PetRespDetail   petDetails = petRespDetailRepo.findAllByProcourtdtlAndDetailsType(courtdtl,petType);
	    PairaviDetails   pairdetl = pairaviDetailRepo.findAllByProcourtdtlAndDetailsType(courtdtl,respType);*/
	    courtdtlDto.setProCourtdtl(courtdtl);
	    courtdtlDto.setPetRespDetail(petdtl);
	    courtdtlDto.setPairaviDetails(pairavidtl);
	    
	    ByteArrayInputStream bis = CourtCasePdfExportal.officeOrderFixed(courtdtlDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=courtCase.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	    
		/*
		 * CourtCasePdfExportal excelExporter = new CourtCasePdfExportal(courtdtlDto);
		 * 
		 * excelExporter.export(response);
		 */
		
	}
}


