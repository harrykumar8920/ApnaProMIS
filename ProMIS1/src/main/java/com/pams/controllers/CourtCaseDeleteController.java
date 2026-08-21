package com.pams.controllers;

import java.awt.desktop.PrintFilesEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AppRoleDAO;
import com.pams.dto.PageNoDTO;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.HistoryOfDeleteCase;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.UserDetails;

import com.pams.service.AddStatusRepository;

import com.pams.service.HistoryOfDeleteCaseRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;


@Controller
public class CourtCaseDeleteController {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;	
	@Autowired
	private AppRoleDAO appRoleDao;	
	@Autowired
	AddStatusRepository addStatusRepo;
	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private HistoryOfDeleteCaseRepository historyOfDeleteCaseRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	
	@GetMapping("/updateCasePriority6")
	public String updateCasePriority2(@RequestParam Long caseId,RedirectAttributes redirect) throws Exception {
		
		 
		 ProCourtCaseDetails proCourtCaseDetails = proCourtCaseDetailsRepo
				    .findById(caseId)
				    .orElseThrow(() -> new RuntimeException("Data is not available for caseId: " + caseId));
		
		String deleteCourtCaseById = proCourtCaseDetailsRepo.deleteCourtCaseById(caseId);
		
		 HistoryOfDeleteCase historyOfDeleteCase = new HistoryOfDeleteCase();
		
		 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		 historyOfDeleteCase.setUpdatedBy(userdet);
		 historyOfDeleteCase.setUpdatedDate(LocalDate.now());
		 historyOfDeleteCase.setCauseTitle(proCourtCaseDetails.getCauseTitle());
		 historyOfDeleteCase.setCourtCaseNo(proCourtCaseDetails.getCourtCaseNo());
		 historyOfDeleteCase.setCreatedBy(proCourtCaseDetails.getCreatedBy());
		 historyOfDeleteCase.setCaseid(proCourtCaseDetails.getId());
		 historyOfDeleteCaseRepo.save(historyOfDeleteCase);
		redirect.addFlashAttribute("message", deleteCourtCaseById);
	    return "redirect:/totalCourtCaseDtlnew"; 
	}
	
	@RequestMapping(value = "totalCourtCaseDtlnew")
	public String totalCourtCaseDtl(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 2000;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<ProCourtCaseDetails> proCourtCaeDetails = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0,
				6);
		long totalRow = proCourtCaeDetails.getTotalElements();
		int currentRow = 1;
		int lastRow = proCourtCaeDetails.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<ProCourtCaseDetails> proCourtDtls = proCourtCaeDetails.getContent();
		int pageNo1 = proCourtCaeDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", proCourtCaeDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("lstCourtCase", proCourtDtls);

		return "caseDetails/totalCourtCaseDetailsnew";

	}
	
	

}
