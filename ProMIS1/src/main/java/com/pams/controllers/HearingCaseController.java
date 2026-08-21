package com.pams.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pams.entity.HearingDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class HearingCaseController {
	
private static final Logger logger = LoggerFactory.getLogger(HearingCaseController.class);
	
	@Value("${file.upload}")
	public String filePath;
	@Autowired
	private HearingDetailsRepository hearingdtlRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	
	
	
	
	
	
	// When Click on AddHearing Button 
		@RequestMapping(value = "updateInfo", params = "AddHearingDetail")
		public String AddHearingDetail(ModelMap modelMap, @ModelAttribute ProCourtCaseDetails proCourtCasedetails) {
			ProCourtCaseDetails courtdtl = proCourtCaseDetailsRepo.findALLById(proCourtCasedetails.getId());
			HearingDetails hearingDetails = new HearingDetails();

			List<HearingDetails> hearingdtls1 = hearingdtlRepo.findAll();
			hearingDetails.setProcourtdtl(courtdtl);
			modelMap.addAttribute("hearingDetails", hearingDetails);
			modelMap.addAttribute("hearingDtls1", hearingdtls1);
			modelMap.addAttribute("courtdtl", courtdtl.getId());
			return "IOOfficer/AddHearingDetail";
		}

		/*
		 * // Save/Update click call
		 * 
		 * @RequestMapping(value = "saveHearingDetail") public String
		 * saveHearingDetail(ModelMap modelMap, @Valid @ModelAttribute HearingDetails
		 * hearingDetails, RedirectAttributes redirect, HttpServletRequest request,
		 * BindingResult bindResult) throws Exception {
		 * 
		 * UserValidation hearValid = new UserValidation();
		 * hearValid.validateHearing(hearingDetails, bindResult);
		 * 
		 * UserDetails userdet =
		 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
		 * )); if (bindResult.hasErrors()) { modelMap.addAttribute("procourtdtl",
		 * hearingDetails.getId()); modelMap.addAttribute("hearingDetails",
		 * hearingDetails); return "IOOfficer/AddHearingDetail"; }
		 * 
		 * else if (hearingDetails.getId() != null) { HearingDetails hdtls =
		 * hearingdtlRepo.findAllById(hearingDetails.getId());
		 * //hearingDetails.setProcourtdtl(hdtls);
		 * 
		 * hearingDetails.setUpdateBy(userdet.getId());
		 * hearingDetails.setUpdatedDate(new java.util.Date());
		 * 
		 * hearingdtlRepo.save(hearingDetails); if
		 * (!hearingDetails.getHearingFile().isEmpty()) { String fileExt =
		 * hearingDetails.getHearingFile().getOriginalFilename(); fileExt =
		 * fileExt.substring(fileExt.lastIndexOf("."));
		 * 
		 * hearingDetails.setSupportDoc(hearingDetails.getHearingFile().
		 * getOriginalFilename() + hearingDetails.getId() + fileExt);
		 * 
		 * caseFileUpload(hearingDetails.getHearingFile(),
		 * hearingDetails.getSupportDoc()); } modelMap.addAttribute("courtdtl",
		 * hearingDetails.getProcourtdtl().getId()); modelMap.addAttribute("message",
		 * "Hearing Details Updated Successfully "); } else { Long id =
		 * (hearingdtlRepo.findMaxid() != null) ? (hearingdtlRepo.findMaxid() + 1) : 1;
		 * 
		 * 
		 * if (!hearingDetails.getHearingFile().isEmpty()) { String fileExt =
		 * hearingDetails.getHearingFile().getOriginalFilename(); fileExt =
		 * fileExt.substring(fileExt.lastIndexOf("."));
		 * 
		 * hearingDetails.setSupportDoc(hearingDetails.getHearingFile().
		 * getOriginalFilename() + id + fileExt);
		 * 
		 * caseFileUpload(hearingDetails.getHearingFile(),
		 * hearingDetails.getSupportDoc()); } hearingDetails.setCreatedBy(userdet);
		 * hearingDetails.setCreatedDate(new java.util.Date());
		 * hearingdtlRepo.save(hearingDetails); modelMap.addAttribute("message",
		 * "Hearing Details Added Successfully "); }
		 * modelMap.addAttribute("hearingDetails", hearingDetails);
		 * modelMap.addAttribute("courtdtl", hearingDetails.getProcourtdtl().getId());
		 * return "IOOfficer/AddHearingDetail"; }
		 * 
		 */
		  
		//When click on Update button
		@RequestMapping(value = "updateInfo", params = "UpHearing")
		public String UpHearing(ModelMap modelMap,@ModelAttribute HearingDetails hearingdetails, 
				@RequestParam(value = "UpHearing", required = true) Long Id) 
		{
			HearingDetails hearingdtls = hearingdtlRepo.findAllById(Id);
			ProCourtCaseDetails courtdtl = proCourtCaseDetailsRepo.findALLById(hearingdtls.getProcourtdtl().getId());
			
			hearingdtls.setProcourtdtl(courtdtl);
			modelMap.addAttribute("courtdtl", courtdtl.getId());
			modelMap.addAttribute("hearingDetails", hearingdtls);
			return "IOOfficer/AddHearingDetail";
		}
		
		
		public void caseFileUpload(@RequestParam("file") MultipartFile file, String name) {
			BufferedOutputStream stream = null;

			try {

				// String directory = filePath;

				File parent = new File(filePath).getParentFile().getCanonicalFile();
				String directory = ESAPI.validator().getValidDirectoryPath("DirectoryName", filePath, parent, false);

				Boolean validFileName = ESAPI.validator().isValidFileName("FileName", name.trim(), false);
				String filepath = null;
				if (validFileName == true) {
					filepath = Paths.get(directory + File.separator + name.trim()).toString();
				}

				// Save the file locally
				stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
				stream.write(file.getBytes());
				stream.close();
			}

			catch (Exception e) {
				logger.info(e.getMessage());
			}

			finally {
				if (stream != null) {
					safeClose(stream);
				}
			}

		}

		private void safeClose(BufferedOutputStream stream) {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
	

	
}
