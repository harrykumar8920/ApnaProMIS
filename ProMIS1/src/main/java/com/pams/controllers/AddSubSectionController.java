package com.pams.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AddSubSec;
import com.pams.entity.UserDetails;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AddSubSectionRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
import com.pams.validation.ProMISValidator;
import com.pams.validation.UserValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
public class AddSubSectionController {
	@Autowired
	private UserDetailsRepository useDetailRepo;

	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AddSubSectionRepository addsubsecRepo;
	@Autowired
	private AddActRepository addActRepo;
	@Autowired
	private AddActSecRepository addactsecRepo;
	
	
	// Rendering to the Addsubsection module
	@RequestMapping(value = "/addSubSection")
	public String addSubSection(Model model) {
		
		List<AddSubSec> addSubSec = addsubsecRepo.findAll();
		List<AddActSec> addActSec =  addactsecRepo.findAll();
		model.addAttribute("addsubsec1", addActSec);
		model.addAttribute("addsubsec", new AddSubSec());
		model.addAttribute("addsubsec1", addSubSec);
		return "userManagement/addSubSection";	
	}
	
	
	@RequestMapping(value = "/saveAddSubSec")
	public String saveAddSubSec(@Valid @ModelAttribute AddSubSec addSubSec,BindingResult bindResult,
			RedirectAttributes attributes, HttpServletRequest request,Model model) throws Exception {
		UserValidation validation = new UserValidation();
		validation.validateSubSections(addSubSec, bindResult);
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if(addSubSec.getAct()==null)
		{  
			bindResult.rejectValue("act","msg.wrongAct");
		}
		
		
		if(addSubSec.getSection()==null)
		{ 
			bindResult.rejectValue("Section","msg.wrongSection");
		}
		
		if(addSubSec.getSubSection()==null)
		{  
			bindResult.rejectValue("SubSection","msg.wrongSubSection");
		}
		if(bindResult.hasErrors()) {
			List<AddAct> acts = addActRepo.findAll();
			List<AddActSec> addactsec = addactsecRepo.findAllByAct(addSubSec.getAct());
			model.addAttribute("seclst", addactsec);
			model.addAttribute("actList", acts);
			return "userManagement/createSubSec";
		}
		
		if (addSubSec.getId() != null) {
			AddSubSec addSubSeccheck = addsubsecRepo.findById(addSubSec.getId()).get();
			addSubSec.setUpdatedDate(new Date());
			addSubSec.setIsActive(1);
			addSubSec.setCreatedDate(addSubSeccheck.getCreatedDate());
			addsubsecRepo.save(addSubSec);
			attributes.addFlashAttribute("message", "New Sub Sections Updated Successfully.");
			
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateSubSections"),
					utils.getMessage("log.user.updatedSubSections")+" "+"and Sub-Sections id is "+addSubSec.getId(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
		} else {
			addSubSec.setIsActive(1);
			addSubSec.setIsEditSubSec(false);
			addSubSec.setCreatedDate(new Date());
			addsubsecRepo.save(addSubSec);
			attributes.addFlashAttribute("message", "New Sub Sections add Successfully.");	
			
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addSubSections"),
					utils.getMessage("log.user.addedSubSections")+" "+"and new Sub-Section is "+addSubSec.getSubSection(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
		}
		model.addAttribute("addsubsec", addSubSec);
		return "redirect:/addSubSection";
		
	}
	
	
	
	

		// add new button click

		@RequestMapping(value = "/addNewSubSec")
		public String NewSubSec(Model model, @Valid @ModelAttribute AddSubSec addSubSec) {
			
			 addSubSec.setIsEditSubSec(false);
			// List<AddActSec> addActSec1 = addactsecRepo.findAll();
			List<AddAct> acts = addActRepo.findAll();
			List<AddActSec> addactsec = addactsecRepo.findAll();
            
			addSubSec.setCreatedDate(new Date());
			addSubSec.setUpdatedDate(new Date());
			
			model.addAttribute("addactsec1", addactsec);
			model.addAttribute("actList", acts);
			model.addAttribute("addSubSec", addSubSec);
			return "userManagement/createSubSec";
		}

		  //	Save/Update Button
			@RequestMapping(value = "/updateSubSec")
			public String updateSubSec(Model model, @ModelAttribute @Valid AddSubSec addSubSec, BindingResult bindResult,
					RedirectAttributes attributes, HttpServletRequest request) {

				
//				if(!addSubSec.getSubSection().equalsIgnoreCase(""))
//				{
//					AddSubSec addSubSec1 =addsubsecRepo.findAllBySubSection(addSubSec.getSubSection());
//					if(addSubSec1!=null) {
//						bindResult.rejectValue("SubSection", "errmsg.uniqueSection");
//					}
//				}	
//				
		
			UserValidation validation = new UserValidation();
			validation.validateSubSections(addSubSec, bindResult);
			
			if(bindResult.hasErrors()) {
				
				List<AddActSec> addActSec =  addactsecRepo.findAll();
				model.addAttribute("addActSec", addActSec);
				
			//	model.addAttribute("addSubSec", addSubSec);
			
			model.addAttribute("addSubSec", new AddSubSec());	
			model.addAttribute("addSubSec",addsubsecRepo.findAll());
		
			return "userManagement/createSubSec";
		}
       else if (addSubSec.getId()!= null) {		
	          addSubSec.setIsActive(0);
	          addSubSec.setIsEditSubSec(true);
	          addSubSec.setUpdatedDate(new Date());
	          addsubsecRepo.save(addSubSec);
			attributes.addFlashAttribute("message", " New Sub Sections updated Successfully. ");			
		}
		
		else {
			addSubSec.setIsActive(1);
			addSubSec.setIsEditSubSec(false);
			addSubSec.setCreatedDate(new Date());
			addsubsecRepo.save(addSubSec);
			attributes.addFlashAttribute("message", "New Sub Sections add Successfully.");
		}
     return "redirect:/addSubSection";

		
		}	
		
		

		// edit button click
		@RequestMapping(value = "/editSubSec", params = "editingSubSec") 
		public String editSubSec(@RequestParam(value = "editingSubSec", required = true) Long Id, Model model,
				RedirectAttributes redirect) throws Exception {

			
			AddSubSec addSubSec = addsubsecRepo.findById(Id).get();
			//List<AddSubSec> subsecList = addsubsecRepo.findAll();
			
		
			List<AddAct> acts = addActRepo.findAll();
		
			List<AddActSec> addactsec = addactsecRepo.findAll();
			
			addSubSec.setIsEditSubSec(true);
			
			model.addAttribute("seclst", addactsec);
			model.addAttribute("actList", acts);
	
			model.addAttribute("addSubSec", addSubSec);
			return "userManagement/createSubSec";			
		}

		//DeActive Button fUNTIONALITY 

		  @RequestMapping(value = "/editSubSec", params = "Deactivate") 
		  public String Deactivate(@RequestParam(value = "Deactivate", required = true) Long id,
		  Model model) {
			  			
		  ProMISValidator snmsValidator = new ProMISValidator();
		  
		  
		  if(!snmsValidator.getValidInteger(id)){ 
		  List<AddSubSec> addsubsec = addsubsecRepo.findAll();
		  model.addAttribute("addSubSec", addsubsec); 
		  model.addAttribute("message", "Invaild"); 
		  }
		  
		  AddSubSec addsubSec1 = addsubsecRepo.findById(id).get();
		  addsubSec1.setIsActive(0);
		  addsubsecRepo.save(addsubSec1);
		  model.addAttribute("message", " User  Deactivate successfully   ");
		  return "userManagement/PassResetSucc"; 
		  }
		  
		  
		  @RequestMapping(value = "/editSubSec",  params = "activate")
			public String Activate(@RequestParam(value = "activate", required = true) Long id, Model model) {
				
				ProMISValidator snmsValidator = new ProMISValidator();

				if(!snmsValidator.getValidInteger(id)){
					
					List<AddSubSec> addSubSec =  addsubsecRepo.findAll();
					model.addAttribute("addSubSec", addSubSec);
					model.addAttribute("message", "Invalid");
				}
				AddSubSec addSubSec1 = addsubsecRepo.findById(id).get();
				addSubSec1.setIsActive(1);
				addsubsecRepo.save(addSubSec1);
				model.addAttribute("message", "User Active Successfully");
				return  "userManagement/PassResetSucc";
				
						}


		
		@RequestMapping("/backSubSec")
		public String backSubSec(Model model)
		{
			List<AddSubSec> addSubSec =  addsubsecRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("addsubsec", addSubSec);
			return "redirect:/addSubSection"; // its do redirect on the mapping method
		}
		}
		
		
	
	
	


