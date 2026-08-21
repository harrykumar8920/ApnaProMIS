package com.pams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.Discharge;
import com.pams.service.DischargeRepository;

@Controller
public class DischargeController {
	@Autowired
	private DischargeRepository dischargeRepo;

	@RequestMapping("/dischargep")
	public String dischargePage(Model model) {
		Discharge dischargeob = new Discharge();
		model.addAttribute("disob", dischargeob);
		model.addAttribute("listdischarge", dischargeRepo.findAll());
		return "userManagement/dishchargePage.html";

	}

	@RequestMapping(value = "/addDischarge", method = RequestMethod.POST)
	public String saveDischarge(@ModelAttribute Discharge discharge, BindingResult bindResult,Model model,
			RedirectAttributes redirect) {
		if(discharge.getDischargeName().trim().equals("")) {
			bindResult.rejectValue("dischargeName", "errmsg.required");
		}
		
		if (bindResult.hasErrors()) {
			model.addAttribute("listdischarge", dischargeRepo.findAll());
			model.addAttribute("disob", discharge);
			return "userManagement/dishchargePage.html";
		}
		dischargeRepo.save(discharge);
		redirect.addFlashAttribute("message", "Discharge added successfully");
		return "redirect:/dischargep";

	}
}
