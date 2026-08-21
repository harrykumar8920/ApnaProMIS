package com.pams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.BasisofIO;
import com.pams.service.BasisofIORepository;
import com.pams.utils.PromisException;
import com.pams.validation.BasisofInvestigationValidator;

import jakarta.validation.Valid;

@Controller
public class BasisofIOController {

	@Autowired
	BasisofIORepository BasisofIORepo;
	
	@Autowired
	BasisofInvestigationValidator basisofInvestigationValidator;
	
	@RequestMapping("/basisofinvestigation")
	public String investigationPage(Model model) {
		BasisofIO invorder = new BasisofIO();
		model.addAttribute("invorder", invorder);
		model.addAttribute("invorderlist", BasisofIORepo.findAll());
		return "userManagement/basisofinvestigationOrder";
		
	}
	
	
	@RequestMapping(value = "/addnewinvestigation")
	public String addNewBasisofinvestigtaion(@Valid @ModelAttribute(value = "invorder") BasisofIO invorder, BindingResult bindResult,
			Model model, RedirectAttributes redirect) throws PromisException, Exception {
		
		basisofInvestigationValidator.investigationValidator(invorder, bindResult);
			
		if (bindResult.hasErrors()) {

			model.addAttribute("invorder", invorder);
			model.addAttribute("invorderlist", BasisofIORepo.findAll());
			return "userManagement/basisofinvestigationOrder";

		}

		else if (invorder.getInvestigationId() != null) {
			
			BasisofIORepo.save(invorder);
			redirect.addFlashAttribute("massage", "Basis of Investigation Order updated successfully");
		

		} else {
			
			BasisofIORepo.save(invorder);
			redirect.addFlashAttribute("massage", "Basis of Investigation Order added successfully");
			
		}
		return "redirect:/basisofinvestigation";
	}
	
	
	
	
	@RequestMapping(value = "/editinvorder", params = "invedit", method = RequestMethod.GET)
	public String editType(@RequestParam(value = "invedit") Long investigationId, Model model,
			RedirectAttributes redirect) {

		BasisofIO invorder = BasisofIORepo.findById(investigationId)
				.orElseThrow(() -> new IllegalArgumentException("invalid Id" + investigationId));
		

		model.addAttribute("invorder", invorder);
		model.addAttribute("invorderlist", BasisofIORepo.findAll());
		return "userManagement/basisofinvestigationOrder";

	}

	@RequestMapping(value = "/deleteinvorder", params = "invdelete", method = RequestMethod.GET)
	public String deleteType(@RequestParam(value = "invdelete") Long investigationId, RedirectAttributes redirect) throws Exception {
		BasisofIORepo.deleteById(investigationId);
		redirect.addFlashAttribute("massage", "Basis of Investigation Order deleted successfully");
		return "redirect:/basisofinvestigation";

	}
}
