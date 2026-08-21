package com.pams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.TypeofBench;
import com.pams.entity.UserDetails;
import com.pams.service.AuditBeanBo;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.TypeofBenchRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;

import jakarta.validation.Valid;

@Controller
public class TypeofBenchController {
	@Autowired
	TypeofBenchRepository typeofBenchRepo;
	private UserDetailsRepository useDetailRepo;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@GetMapping(value = "/typebench")
	public String typeBench(Model model) {
	    model.addAttribute("typebench", new TypeofBench());
	    model.addAttribute("listbench", typeofBenchRepo.findAll());
	    return "userManagement/Typeofbench";
	}

	@GetMapping("/editTypeofBench")
	public String editTypeofBench(@RequestParam("id") Long id, Model model) {

	    TypeofBench typebench = typeofBenchRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Type of Bench not found"));

	    model.addAttribute("typebench", typebench);
	    model.addAttribute("listbench", typeofBenchRepo.findAll());
	    
	    return "userManagement/Typeofbench";
	}

	@GetMapping("/deleteTypeofBench/{id}")
	public String deleteTypeofBench(@PathVariable("id") Long id, RedirectAttributes redirect) {
	    typeofBenchRepo.deleteById(id);
	    redirect.addFlashAttribute("massage", "Type of bench deleted successfully");
	    return "redirect:/typebench";
	}

	@RequestMapping(value = "/addTypeofResponse", method = RequestMethod.POST)
	public String addNewTypeofResponse(@Valid @ModelAttribute("typebench") TypeofBench typebench,
	                                   BindingResult bindResult,
	                                   Model model,
	                                   RedirectAttributes redirect) throws Exception {

	    // Duplicate check (sirf new record ke liye)
	    if (typebench.getId() == null) {
	        TypeofBench existing = typeofBenchRepo.findByBench(typebench.getBench());
	        if (existing != null) {
	            bindResult.rejectValue("bench", "errmsg.required");
	            model.addAttribute("listbench", typeofBenchRepo.findAll());
	            model.addAttribute("typebench", typebench);
	            model.addAttribute("massage", "This bench is already added");
	            return "userManagement/Typeofbench";
	        }
	    }

	    if (bindResult.hasErrors()) {
	        model.addAttribute("listbench", typeofBenchRepo.findAll());
	        model.addAttribute("typebench", typebench);
	        model.addAttribute("massage", "Please enter valid characters");
	        return "userManagement/Typeofbench";
	    }

	    // Save / Update
	    typeofBenchRepo.save(typebench);

	    // Success Message
	    if (typebench.getId() == null) {
	        redirect.addFlashAttribute("massage", "Type of bench saved successfully");
	    } else {
	        redirect.addFlashAttribute("massage", "Type of bench updated successfully");
	    }

	    // Audit Logging (optional - aapke existing code ke according)
	    try {
	        UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
	        String middleName = (userdet.getMiddleName() != null && !userdet.getMiddleName().trim().isEmpty())
	                ? userdet.getMiddleName() + " " : "";
	        String fullName = userdet.getSalutation() + " " + userdet.getFirstName() + " " + middleName + userdet.getLastName();

	        auditBeanBo.setAuditBean(
	                Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
	                fullName,
	                "User",
	                Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
	                utils.getMessage("log.user.addTypeOfBench"),
	                utils.getMessage("log.user.addedTypeOfBench") + " and Type of Bench name is " + typebench.getBench(),
	                fullName,
	                "true"
	        );
	        auditBeanBo.save();
	    } catch (Exception e) {
	        // log error if needed
	    }

	    return "redirect:/typebench";
	}

}