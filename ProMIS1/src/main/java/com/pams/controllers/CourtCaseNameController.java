package com.pams.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.CourtCaseName;
import com.pams.entity.UserDetails;
import com.pams.service.CourtCaseNameRepository;
import com.pams.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;

@Controller
public class CourtCaseNameController {
	@Autowired
	private  CourtCaseNameRepository courtCaseNameRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@GetMapping("/addCourtCaseName")
	public String getCourtCase(Model model) {
		model.addAttribute("courtCaseNameObj", new CourtCaseName());
		model.addAttribute("list", courtCaseNameRepo.findAll());
		return "userManagement/courtCaseNameP";
	}
	
	
	@RequestMapping(value = "/deleteCourtCase", params = "deleteCourtcase")
	public String deleteCourtCase(@RequestParam(value="deleteCourtcase",required = true) Long id, Model model) {
		courtCaseNameRepo.deleteById(id);
		model.addAttribute("courtCaseNameObj", new CourtCaseName());
		model.addAttribute("list", courtCaseNameRepo.findAll());
		model.addAttribute("message", "Court case Deleted Successfully");
		return "userManagement/courtCaseNameP";
	}
	
	@PostMapping("/saveCourtCaseName")
	public String saveCourtCase(@Valid @ModelAttribute("courtCaseNameObj") CourtCaseName courtCaseName, 
			BindingResult bindResult,	Model model,RedirectAttributes re) throws Exception {
		
		if(courtCaseName.getCCName().equals("")) {
			bindResult.rejectValue("cCName", "msg.courtCaseName");
		}
		if(courtCaseName.getTypeCase().equals("Select")) {
			bindResult.rejectValue("typeOfCase", "errmsg.required");
		}
		if(bindResult.hasErrors()) {
			model.addAttribute("courtCaseName", new CourtCaseName());
			model.addAttribute("list", courtCaseNameRepo.findAll());
			return "userManagement/courtCaseNameP";
		}
		UserDetails user = userDetailsService.getUserDetailssss();
		if(courtCaseName.getId()!=null) {
			courtCaseName.setId(courtCaseName.getId());
			courtCaseName.setCreatedBy(user);
			courtCaseName.setCreatedDate(new Date());
			courtCaseNameRepo.save(courtCaseName);
			model.addAttribute("courtCaseName", new CourtCaseName());
			model.addAttribute("list", courtCaseNameRepo.findAll());
			re.addFlashAttribute("message", "Court case Update Successfully");
			return "redirect:/addCourtCaseName";
		}else {
		courtCaseName.setCreatedBy(user);
		courtCaseName.setCreatedDate(new Date());
		courtCaseNameRepo.save(courtCaseName);
		model.addAttribute("courtCaseName", new CourtCaseName());
		model.addAttribute("list", courtCaseNameRepo.findAll());
		re.addFlashAttribute("message", "Court case added Successfully");
		return "redirect:/addCourtCaseName";
		}
	}
	
	@RequestMapping(value = "/editCourtCase", params = "editCourtcase")
	public String editCourtCase(@RequestParam(value = "editCourtcase", required = true) Long id, Model model) {
		CourtCaseName courtCase = courtCaseNameRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		courtCase.setEdit(true);
        model.addAttribute("courtCaseNameObj", courtCase);
		/*
		 * if (optionalCourtCase.isPresent()) { CourtCaseName courtCase =
		 * optionalCourtCase.get(); courtCase.setEdit(true);
		 * model.addAttribute("courtCaseNameObj", courtCase); }
		 */
		model.addAttribute("list", courtCaseNameRepo.findAll());
		return "userManagement/courtCaseNameP";
	}
	
	
}
