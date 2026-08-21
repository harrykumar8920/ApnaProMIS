package com.pams.controllers;

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

import com.pams.entity.Type;
import com.pams.entity.TypeofCase;
import com.pams.entity.UserDetails;
import com.pams.service.AuditBeanBo;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.TypeRepository;
import com.pams.service.TypeofCaseRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.PromisException;
import com.pams.utils.Utils;

import jakarta.validation.Valid;

@Controller
public class TypeofCaseController {
	@Autowired
	private TypeRepository typeRepository;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	TypeofCaseRepository typeofCaseRepo;

	@RequestMapping(value = "/typeCase")
	public String typeCase(Model model) {

		// TypeofResponse typeResponse1 = new TypeofResponse();
		TypeofCase typeofCase1 = new TypeofCase();
		model.addAttribute("typeofCase", typeofCase1);
		List<Type> typeList = typeRepository.findAll();
		model.addAttribute("listCase", typeofCaseRepo.findAll());
		model.addAttribute("typeList", typeList);

		return "userManagement/TypeofCase";

	}

	@RequestMapping(value = "/addNewTypeofCase")
	public String addNewTypeofCase(@ModelAttribute @Valid TypeofCase typeofCase, BindingResult bindResult, Model model,
			RedirectAttributes redirect) throws PromisException, Exception {

		if (typeofCase.getType() == null || typeofCase.getType().equals("")) {
			bindResult.rejectValue("type", "errmsg.required");
		}

		if (bindResult.hasErrors()) {

			model.addAttribute("message", "Type Of Case must be in alphabet with length ranging 2-40");
			model.addAttribute("listCase", typeofCaseRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
			return "userManagement/TypeofCase";

		} 
		else if (typeofCase.getId() != null) {
			typeofCaseRepo.save(typeofCase);

			redirect.addFlashAttribute("message", "Type Of Case updated successfully");
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"User", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateTypeOfCase"),
					utils.getMessage("log.user.updatedTypeOfCase") + " " + "and Type of Case id is " + typeofCase.getId(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true");
			auditBeanBo.save();
		}else {
			typeofCaseRepo.save(typeofCase);

			redirect.addFlashAttribute("message", "Type Of Case added successfully");
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"User", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addTypeOfCase"),
					utils.getMessage("log.user.addedTypeOfCase") + " " + "and Type of Case name is " + typeofCase.getTypeOfCase(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true");
			auditBeanBo.save();
		}
		
		return "redirect:/typeCase";
	}

	@RequestMapping(value = "/editTypeofCase", params = "editCase")
	public String editTypeofCase(@RequestParam(value = "editCase", required = true) Long id, Model model) {
		TypeofCase typeCase = typeofCaseRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		typeCase.setEditcase(true);

		model.addAttribute("typeofCase", typeCase);
		model.addAttribute("listCase", typeofCaseRepo.findAll());
		List<Type> typeList = typeRepository.findAll();
		model.addAttribute("typeList", typeList);
		return "userManagement/TypeofCase";
	}

}
