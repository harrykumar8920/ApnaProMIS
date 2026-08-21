package com.pams.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.UserDetails;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
//import com.pams.utils.SnmsException;
//import com.pams.validation.GAMSValidator;
import com.pams.validation.ProMISValidator;
import com.pams.validation.UserValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class ActSecController {
	@Autowired
	private UserDetailsRepository useDetailRepo;

	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AddActSecRepository addactsecRepo;
	@Autowired
	private AddActRepository addActRepo;

	
	
	// Rendering on the AddActSec Page when click
	@RequestMapping(value = "/addActSec")
	public String addActSec(Model model) {

		
		List<AddActSec> addSecList = addactsecRepo.findAll();

		model.addAttribute("addActSec", new AddActSec());
		model.addAttribute("actList", addActRepo.findAll());
		model.addAttribute("addSecList", addSecList);
		return "userManagement/addActSec";
	}

	// today
	// add New button click

	@RequestMapping(value = "addNewSec")
	public String NewactSec(Model model, @Valid @ModelAttribute AddActSec addActSec) {
		addActSec.setIsEditActSec(false);
		
		List<AddAct> acts = addActRepo.findAll();

		model.addAttribute("actList", acts);
		model.addAttribute("addActSec", addActSec);
		return "userManagement/createActSec";
	}

	
			
	
	
	
	// after edit click, this Update method for Act & Sec for update/Save
	@RequestMapping(value = "/updateActSec")
	public String updateActSec(Model model, @ModelAttribute @Valid AddActSec addactsec, BindingResult bindResult,
			RedirectAttributes attributes, HttpServletRequest request) throws Exception {

		
		
		/*
		 * if(!addactsec.getSection().equalsIgnoreCase("")) { AddActSec addactsec1 =
		 * addactsecRepo.findAllBySection(addactsec.getSection()); if(addactsec1!=null)
		 * { bindResult.rejectValue("Section", "errmsg.uniqueSection"); } }
		 */	

		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		
		UserValidation validation = new UserValidation();
		validation.validateActandSections(addactsec, bindResult);
		
		if(addactsec.getAct()==null)
		{  
			bindResult.rejectValue("act","msg.wrongAct");
		}
		
		if(bindResult.hasErrors()) {
			
			List<AddAct> acts = addActRepo.findAll();

			model.addAttribute("actList", acts);
				model.addAttribute("addSecList", addactsec);
			
			//model.addAttribute("addActSec", new AddActSec());	
			model.addAttribute("addSecList",addactsecRepo.findAll());
			return "userManagement/createActSec";
		}
		
		else if (addactsec.getId()!= null) {
			
			addactsec.setIsActive(0);
			addactsec.setIsEditActSec(true);
			addactsec.setUpdatedDate(new java.util.Date());
			addactsecRepo.save(addactsec);
			attributes.addFlashAttribute("message", " New Sections updated Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateSections"),
					utils.getMessage("log.user.updatedSections")+" "+"and Sections id is "+addactsec.getId(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
		}
		else
			{	
		addactsec.setIsActive(1);
		addactsec.setIsEditActSec(false);
		addactsec.setCreatedDate(new java.util.Date());
		addactsecRepo.save(addactsec);
		attributes.addFlashAttribute("message", "New Sections add Successfully.");
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addSections"),
				utils.getMessage("log.user.addedSections")+" "+"and new Section is "+addactsec.getSection(),
				userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
		auditBeanBo.save();
		}
		return "redirect:/addActSec";

		// return "userManagement/addActSec";

	}

	// edit button click
	@RequestMapping(value = "/editActSec", params = "editingActSec")
	public String editActSec(@RequestParam(value = "editingActSec", required = true) Long Id, Model model,
			RedirectAttributes redirect) throws Exception {

		System.out.println("Edit Button Calling ---- ====== >>>>> Hello Abc edit  Button click ");
		AddActSec addActSec = addactsecRepo.findById(Id).get();
		List<AddAct> acts = addActRepo.findAll();
		addActSec.setIsEditActSec(true);
		model.addAttribute("actList", acts);
		model.addAttribute("addActSec", addActSec);

		return "userManagement/createActSec";
		
	}

    //DeActive Button fUNTIONALITY 

	  @RequestMapping(value = "/editActSec", params = "Deactivate") 
	  public String Deactivate(@RequestParam(value = "Deactivate", required = true) Long id,
	  Model model) {
	  
	  ProMISValidator snmsValidator = new ProMISValidator();
	  
	  
	  if(!snmsValidator.getValidInteger(id)){ 
	  List<AddActSec> addActsec = addactsecRepo.findAll();
	  model.addAttribute("addActSec", addActsec); 
	  model.addAttribute("message", "Invaild"); 
	  }
	  
	  AddActSec addAct = addactsecRepo.findById(id).get();
	  addAct.setIsActive(0);
	  addactsecRepo.save(addAct);  
	  model.addAttribute("message", " User  Deactivate successfully   ");
	  return "userManagement/PassResetSucc"; 
	  }
	  
	  
	  @RequestMapping(value = "/editActSec",  params = "activate")
		public String Activate(@RequestParam(value = "activate", required = true) Long id, Model model) {
			
			ProMISValidator snmsValidator = new ProMISValidator();

			if(!snmsValidator.getValidInteger(id)){
				
					List<AddActSec> addActsec = addactsecRepo.findAll();
					model.addAttribute("addActsec", addActsec);
					model.addAttribute("message", "Invalid");
			}
			AddActSec addact = addactsecRepo.findById(id).get();
			addact.setIsActive(1);
			addactsecRepo.save(addact);
			model.addAttribute("message", "User Active Successfully");
			return  "userManagement/PassResetSucc";
					}
	  
	  //For Back Button click back to ActSec list page

		@RequestMapping(value = "/backButton")
		public String showAllCase(Model model) {
		
		List<AddActSec> addActsec =  addactsecRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("addActsec", addActsec);
			return "redirect:/addActSec"; // its do redirect on the mapping method
		}
	  
	  
	 
}
