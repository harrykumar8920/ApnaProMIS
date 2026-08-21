package com.pams.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pams.dao.AppRoleDAO;
import com.pams.dto.PageNoDTO;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class ProsecutorController {
	
	@Autowired
	private AppRoleDAO appRoleDao;
	 
	 
	  @Autowired
	  ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
		@Autowired

		private UserDetailsServiceImpl userDetailsService;
		@Autowired
		private UserDetailsRepository useDetailRepo;
	 
	

		@RequestMapping(value = "totalNumberOfCourtCases")
		public String listOfCourtCasesS(ModelMap modelMap) throws Exception {
			int pageNo = 0;
			int noOfrecord = 20;

			Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC,"id"));	

			 String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
			 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			 List<ProCourtCaseDetails> proCourtDtl11  = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,Sort.by(Sort.Direction.DESC, "id"));
			 
			 Page<ProCourtCaseDetails> proCourtDtl  = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet, pagable);
			 long totalRow = proCourtDtl.getTotalElements();
				int currentRow = 1;
				int lastRow = proCourtDtl.getNumberOfElements();
				modelMap.addAttribute("totalRow", totalRow);
				modelMap.addAttribute("currentRow", currentRow);
				modelMap.addAttribute("lastRow", lastRow);
			 int pageNo1 = proCourtDtl.getTotalPages();
			 modelMap.addAttribute("currentPage", pageNo+1);
			 modelMap.addAttribute("totalPages", pageNo1);
			 modelMap.addAttribute("totalItems", proCourtDtl.getNumberOfElements());
			 modelMap.addAttribute("pageNoDTO", new PageNoDTO());
			 
			 
			 
			
			modelMap.addAttribute("userRole", userrole);
			
			modelMap.addAttribute("lstCourtCase", proCourtDtl.getContent());
			return "caseDetails/totalNumberOfCourtCases";
		}
		
		@RequestMapping(value = "totalNumberOfCourtCases1")
		public String totalNumberOfCourtCases1(@ModelAttribute PageNoDTO pageDTO, ModelMap model) throws Exception {


			int pageNo;
			if(pageDTO.getPageno()>1) {
			 pageNo = pageDTO.getPageno() - 1;
			}
			else {
				 pageNo=0;
			}
			int noOfrecord = 20;
			Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC,"id"));	

			 String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
			 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			 List<ProCourtCaseDetails> proCourtDtl11  = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet,Sort.by(Sort.Direction.DESC, "id"));
			 
			 Page<ProCourtCaseDetails> proCourtDtl  = proCourtCaseDetailsRepo.findALLByCreatedBy(userdet, pagable);
			 long totalRow = proCourtDtl.getTotalElements();
				int currentRow=(noOfrecord*pageNo)+1;
				int lastRow=(noOfrecord*pageNo)+proCourtDtl.getNumberOfElements();
				model.addAttribute("totalRow", totalRow);
				model.addAttribute("currentRow", currentRow);
				model.addAttribute("lastRow", lastRow);
			
			 
			 long totalRecord= proCourtDtl.getTotalElements();
			 
			 model.addAttribute("totalRecord", totalRecord);
			 int pageNo1 = proCourtDtl.getTotalPages();
			 model.addAttribute("currentPage", pageNo+1);
			 model.addAttribute("totalPages", pageNo1);
			 model.addAttribute("totalItems", proCourtDtl.getNumberOfElements());
			 model.addAttribute("pageNoDTO", new PageNoDTO());
			 
			 
			 
			
			 model.addAttribute("userRole", userrole);
			
			 model.addAttribute("lstCourtCase", proCourtDtl.getContent());
			return "caseDetails/totalNumberOfCourtCases";
		}
	
	
	
		
		@RequestMapping(value = "totalforwardCourtCase")
		public String listOfForwardCourtCases(ModelMap modelMap) throws Exception {
			int pageNo = 0;
			int noOfrecord = 20;
			Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

			 String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
			 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			 
			 Page<ProCourtCaseDetails> proCourtDtl  = proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1,2, userdet,pagable);
			 long totalRow = proCourtDtl.getTotalElements();
				int currentRow = 1;
				int lastRow = proCourtDtl.getNumberOfElements();
				modelMap.addAttribute("totalRow", totalRow);
				modelMap.addAttribute("currentRow", currentRow);
				modelMap.addAttribute("lastRow", lastRow);
				int pageNo1 = proCourtDtl.getTotalPages();
				modelMap.addAttribute("currentPage", pageNo + 1);
				modelMap.addAttribute("totalPages", pageNo1);
				modelMap.addAttribute("totalItems", proCourtDtl.getNumberOfElements());
				modelMap.addAttribute("pageNoDTO", new PageNoDTO());
			 modelMap.addAttribute("userRole", userrole);
			
			modelMap.addAttribute("lstCourtCase", proCourtDtl);
			return "caseDetails/totalforwardCourtCase";
		}
		@RequestMapping(value = "totalforwardCourtCase1")
		public String listOfForwardCourt1Cases(@ModelAttribute PageNoDTO pageDTO,ModelMap modelMap) throws Exception {
			int pageNo;
			if (pageDTO.getPageno() > 1) {
				pageNo = pageDTO.getPageno() - 1;
			} else {
				pageNo = 0;
			}
			int noOfrecord = 20;
			Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));
			
			
			String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
			 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			 
			 Page<ProCourtCaseDetails> proCourtDtl  = proCourtCaseDetailsRepo.findALLByApproveStatusBetweenAndCreatedBy(1,2, userdet,pagable);
			 long totalRow = proCourtDtl.getTotalElements();
				int currentRow=(noOfrecord*pageNo)+1;
				int lastRow=(noOfrecord*pageNo)+proCourtDtl.getNumberOfElements();
				modelMap.addAttribute("totalRow", totalRow);
				modelMap.addAttribute("currentRow", currentRow);
				modelMap.addAttribute("lastRow", lastRow);
				int pageNo1 = proCourtDtl.getTotalPages();
				modelMap.addAttribute("currentPage", pageNo + 1);
				modelMap.addAttribute("totalPages", pageNo1);
				modelMap.addAttribute("totalItems", proCourtDtl.getNumberOfElements());
				modelMap.addAttribute("pageNoDTO", new PageNoDTO());
			 modelMap.addAttribute("userRole", userrole);
			
			modelMap.addAttribute("lstCourtCase", proCourtDtl);
			return "caseDetails/totalforwardCourtCase";
		}
		@RequestMapping(value = "totalNumberOfSendBackCourtCases")
		public String listOfSendBackCourtCases(ModelMap modelMap) throws Exception {
			int pageNo = 0;
			int noOfrecord = 20;
			Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));	

			 String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
			 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			
			 Page<ProCourtCaseDetails> proCourtDtl  = proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(pagable,3, userdet);
			 long totalRow = proCourtDtl.getTotalElements();
				int currentRow = 1;
				int lastRow = proCourtDtl.getNumberOfElements();
				
				modelMap.addAttribute("totalRow", totalRow);
				modelMap.addAttribute("currentRow", currentRow);
				modelMap.addAttribute("lastRow", lastRow);
				int pageNo1 = proCourtDtl.getTotalPages();
				modelMap.addAttribute("currentPage", pageNo + 1);
				modelMap.addAttribute("totalPages", pageNo1);
				modelMap.addAttribute("totalItems", proCourtDtl.getNumberOfElements());
				modelMap.addAttribute("pageNoDTO", new PageNoDTO());
				
			modelMap.addAttribute("userRole", userrole);
			
			modelMap.addAttribute("lstCourtCase", proCourtDtl);
			return "caseDetails/totalNumberOfSendBackCourtCases";
		}
		@RequestMapping(value = "totalNumberOfSendBackCourtCases5")
		public String listOfSendBackCourtCases5(@ModelAttribute PageNoDTO pageDTO,ModelMap modelMap) throws Exception {
			int pageNo;
			if (pageDTO.getPageno() > 1) {
				pageNo = pageDTO.getPageno() - 1;
			} else {
				pageNo = 0;
			}
			int noOfrecord = 20;
			Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));	

			 String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
			 UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			
			 Page<ProCourtCaseDetails> proCourtDtl  = proCourtCaseDetailsRepo.findALLByApproveStatusAndCreatedBy(pagable,3, userdet);
			 long totalRow = proCourtDtl.getTotalElements();
				int currentRow=(noOfrecord*pageNo)+1;
				int lastRow=(noOfrecord*pageNo)+proCourtDtl.getNumberOfElements();
				modelMap.addAttribute("totalRow", totalRow);
				modelMap.addAttribute("currentRow", currentRow);
				modelMap.addAttribute("lastRow", lastRow);	
			 int pageNo1 = proCourtDtl.getTotalPages();
				modelMap.addAttribute("currentPage", pageNo + 1);
				modelMap.addAttribute("totalPages", pageNo1);
				modelMap.addAttribute("totalItems", proCourtDtl.getNumberOfElements());
				modelMap.addAttribute("pageNoDTO", new PageNoDTO());
				
			modelMap.addAttribute("userRole", userrole);
			
			modelMap.addAttribute("lstCourtCase", proCourtDtl);
			return "caseDetails/totalNumberOfSendBackCourtCases";
		}




}
