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
import com.pams.entity.UserDetails;
import com.pams.service.AddActRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
import com.pams.validation.UserValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AddActControllers {
	@Autowired
	private UserDetailsRepository useDetailRepo;

	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AddActRepository addActRepo;

	// Rendering on the AddAct Page when click
	@RequestMapping(value = "/addActPage")
	public String addAct(Model model) {

		 AddAct addact = new AddAct();
		model.addAttribute("addActlist", addActRepo.findAll());
		addact.setIsEditAct(false);
		model.addAttribute("addAct", addact);
		return "userManagement/addAct";
	}

	/*
	 * @RequestMapping(value = "addNewAct") public String NewAct(Model
	 * model, @Valid @ModelAttribute AddAct addAct) {
	 * 
	 * return "userManagement/createAct"; }
	 */

	// When Save Button Click, This Update method for Act update/Save
	@SuppressWarnings("unused")
	@RequestMapping(value = "/updateAct")
	public String updateAct(Model model, @ModelAttribute @Valid AddAct addAct, BindingResult bindResult,
			RedirectAttributes attributes, HttpServletRequest request) throws Exception {
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		boolean existsByAct = addActRepo.existsByActAndActType(addAct.getAct(),addAct.getActType());
		System.out.println("check herer for act AND TYPE " +existsByAct);
			if (existsByAct) {
			UserValidation validation = new UserValidation();
			validation.validateAct(addAct, bindResult);
			attributes.addFlashAttribute("message", " This act '"+ addAct.getAct() +"' already added!!" );
			if (bindResult.hasErrors()) {
				model.addAttribute("addActlist", addAct);
				model.addAttribute("addActlist", addActRepo.findAll());
				return "userManagement/addAct";
			}

		} else {
			if (addAct.getId() != null) {
				addAct.setUpdatedDate(new Date());
				addAct.setIsEditAct(true);
				addAct.setIsActive(0);
				addActRepo.save(addAct);
				model.addAttribute("addActlist", addAct);
				attributes.addFlashAttribute("message", " Act  updated Successfully. ");
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
						"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.user.updateAct"),
						utils.getMessage("log.user.updatedAct")+" "+"and Act id is "+addAct.getId(),
						userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
				auditBeanBo.save();
			} else {
				addAct.setIsActive(1);
				addAct.setIsEditAct(false);
				addAct.setCreatedDate(new Date());
				addActRepo.save(addAct);
				model.addAttribute("addActlist", addAct);
				attributes.addFlashAttribute("message", "New Act  add Successfully.");
				auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
						"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
						utils.getMessage("log.user.addAct"),
						utils.getMessage("log.user.addedAct")+" "+"and Act is "+addAct.getAct(),
						userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
				auditBeanBo.save();
			}
		}
		return "redirect:/addActPage";
	}

	// edit button click
	@RequestMapping(value = "/editingAct", params = "editingAct")
	public String editAct(@RequestParam(value = "editingAct", required = true) Long Id, Model model,
			RedirectAttributes redirect) throws Exception {

		AddAct addActlist = addActRepo.findById(Id).get();
		addActlist.setIsEditAct(true);
		model.addAttribute("addAct", addActlist);

		return "userManagement/addAct";
	}

	// For Back Button click back to Act list page

	@RequestMapping(value = "/backAct")
	public String showAllAct(Model model) {

		List<AddAct> addActlist = addActRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("addAct", addActlist);
		return "redirect:/addActPage"; // its do redirect on the mapping method

	}

}
