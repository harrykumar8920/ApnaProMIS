package com.pams.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.AddDesignation;
import com.pams.entity.CategoriesofCompany;
import com.pams.entity.PairaviOfficer;
import com.pams.entity.Type;
import com.pams.entity.TypeofCase;
import com.pams.service.AddDesignationRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.utils.PromisException;

@Controller
public class PairaviOfficerController {
	
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;

	@Autowired
	private AddDesignationRepository designationRepo;
	
	@RequestMapping(value = "pofficer")
	public String pairaviPage(Model model) {
		PairaviOfficer pairavi = new PairaviOfficer();
		model.addAttribute("pairavi", pairavi);
		//List<AddDesignation> sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Officer");
		//model.addAttribute("desilst", sfioOfficerDesignation);
	
		model.addAttribute("listpairavi", pairaviofficerRepo.findAll());
		return "userManagement/pairaviofficer";

	}
	
	@RequestMapping(value = "/getDesignation", method = RequestMethod.GET)
	@ResponseBody

	public List<AddDesignation> getdesignationsss(@RequestParam("courtType") Long typeId) {
		List<AddDesignation> sfioOfficerDesignation = null;
		if (typeId==1)
		{
		 sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Officer");
		}
		else if(typeId==2)
		{
			sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Counsel");	
		}
		else
		{
			sfioOfficerDesignation = null;
		}
		
		return sfioOfficerDesignation;
	}
	
	
	@RequestMapping(value = "/addpairavi", method = RequestMethod.POST)
	public String addNewPairaviOfficer(@ModelAttribute PairaviOfficer pairavi,
	        BindingResult bindResult, Model model, RedirectAttributes redirect) {

	    if (pairavi.getName() == null || pairavi.getName().trim().equals("")) {
	        bindResult.rejectValue("name", "errmsg.fnames");
	    }

	    if (bindResult.hasErrors()) {

	        model.addAttribute("listpairavi", pairaviofficerRepo.findAll());
	        model.addAttribute("pairavi", pairavi);
	        return "userManagement/pairaviofficer";
	    }

	    // UPDATE CASE
	    if (pairavi.getId() != null) {

	        if (pairaviofficerRepo.existsByEmailAndIdNot(pairavi.getEmail(), pairavi.getId())) {

	            redirect.addFlashAttribute("massage", "Email already exists");
	            return "redirect:/pofficer";
	        }

	        pairaviofficerRepo.save(pairavi);
	        redirect.addFlashAttribute("massage", "Officer updated successfully");
	    }

	    // ADD CASE
	    else {

	        if (pairaviofficerRepo.existsByEmail(pairavi.getEmail())) {

	            redirect.addFlashAttribute("massage", "Email already exists");
	            return "redirect:/pofficer";
	        }

	        pairaviofficerRepo.save(pairavi);
	        redirect.addFlashAttribute("massage", "Officer added successfully");
	    }

	    return "redirect:/pofficer";
	}

	
	/*
	 * @RequestMapping(value = "/addpairavi") public String
	 * addNewPairaviOfficer(@ModelAttribute PairaviOfficer pairavi, BindingResult
	 * bindResult, Model model, RedirectAttributes redirect) throws PromisException,
	 * Exception {
	 * 
	 * if(pairavi.getName().equals("")) { bindResult.rejectValue("name",
	 * "errmsg.fnames"); }
	 * 
	 * if (bindResult.hasErrors()) {
	 * 
	 * model.addAttribute("listpairavi", pairaviofficerRepo.findAll());
	 * model.addAttribute("pairavi", pairavi); return
	 * "userManagement/pairaviofficer";
	 * 
	 * }
	 * 
	 * else if (pairavi.getId() != null) {
	 * 
	 * pairaviofficerRepo.save(pairavi); redirect.addFlashAttribute("massage",
	 * "Officer updated successfully");
	 * 
	 * } else {
	 * 
	 * pairaviofficerRepo.save(pairavi); redirect.addFlashAttribute("massage",
	 * "Officer added successfully"); } return "redirect:/pofficer"; }
	 */

	@RequestMapping(value = "/editPairavi", params = "pairaviedit", method = RequestMethod.GET)
	public String editPairaviOfficer(@RequestParam(value = "pairaviedit") Long id, Model model,
			RedirectAttributes redirect) {

		PairaviOfficer pairavi= pairaviofficerRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("invalid Id" + id));
		
		pairavi.setEditPairavi(true);
		model.addAttribute("listpairavi", pairaviofficerRepo.findAll());
		List<AddDesignation> sfioOfficerDesignation = designationRepo.findByDeginationtype("SFIO Officer");
		model.addAttribute("desilst", sfioOfficerDesignation);
		model.addAttribute("pairavi", pairavi);
		model.addAttribute("listcategoriesofcompany", pairaviofficerRepo.findAll());
		return "userManagement/pairaviofficer";


	}

	@RequestMapping(value = "/deletePairavi", params = "pairavidelete", method = RequestMethod.GET)
	public String deletePairaviOfficer(@RequestParam(value = "pairavidelete") Long id, RedirectAttributes redirect) {
		pairaviofficerRepo.deleteById(id);
		redirect.addFlashAttribute("massage", " Officer deleted successfully");
		return "redirect:/pofficer";

	}
	
}
