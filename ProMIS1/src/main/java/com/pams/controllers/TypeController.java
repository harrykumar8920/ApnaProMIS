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

import com.pams.entity.Type;
import com.pams.entity.UserDetails;
import com.pams.service.AuditBeanBo;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.TypeRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.PromisException;
import com.pams.utils.Utils;

import jakarta.validation.Valid;


@Controller
public class TypeController {
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
	TypeRepository typeRepo;

	@RequestMapping("/type")
	public String typePage(Model model) {
		Type typeobject = new Type();
		model.addAttribute("typeobject", typeobject);
		model.addAttribute("listtype", typeRepo.findAll());
		return "userManagement/type1";

	}
	
		@RequestMapping(value = "/addnewtype")
	public String addNewType(@Valid @ModelAttribute(value = "typeobject") Type type, BindingResult bindResult,
			Model model, RedirectAttributes redirect) throws PromisException, Exception {
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			if(type.getType().equals(""))
			{
				bindResult.rejectValue("type", "errmsg.required");
			}
		if (bindResult.hasErrors()) {

			model.addAttribute("listtype", typeRepo.findAll());
			model.addAttribute("typeobject", type);
			model.addAttribute("massage", "Please enter only charactor");
			return "userManagement/type1";

		}

		else if (type.getId() != null) {
			type.setEditType(true);
			typeRepo.save(type);
			redirect.addFlashAttribute("massage", "Type updated successfully");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateType"),
					utils.getMessage("log.user.updatedType")+" "+"and Type Id is "+type.getId(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();

		} else {
			type.setEditType(false);
			typeRepo.save(type);
			redirect.addFlashAttribute("massage", "Type added successfully");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addType"),
					utils.getMessage("log.user.addedType")+" "+"and Type name is "+type.getType(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();
		}
		return "redirect:/type";
	}

	@RequestMapping(value = "/edittyp", params = "typedit", method = RequestMethod.GET)
	public String editType(@RequestParam(value = "typedit") Long id, Model model,
			RedirectAttributes redirect) {

		Type typeobject = typeRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("invalid Id" + id));
		typeobject.setEditType(true);

		model.addAttribute("typeobject", typeobject);
		model.addAttribute("listtype", typeRepo.findAll());
		return "userManagement/type1";

	}

	@RequestMapping(value = "/deletetyp", params = "typdelete", method = RequestMethod.GET)
	public String deleteType(@RequestParam(value = "typdelete") Long id, RedirectAttributes redirect) throws Exception {
		typeRepo.deleteById(id);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		redirect.addFlashAttribute("massage", "Type deleted successfully");
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.deleteType"),
				utils.getMessage("log.user.deletedType")+" "+"and Type id is "+id,
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
		auditBeanBo.save();
		
		return "redirect:/type";

	}

}
