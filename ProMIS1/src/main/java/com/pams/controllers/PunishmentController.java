package com.pams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.Punishment1;
import com.pams.entity.UserDetails;
import com.pams.service.AuditBeanBo;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.PunishmentRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;

@Controller
public class PunishmentController {

	;
	@Autowired
	PairaviOfficerRepository pairaviofficerRepo;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
    @Autowired
	private PunishmentRepository punishmentRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	
	@RequestMapping("/punishment")
	public String showPunishment(Model modelMap)  throws Exception {
		modelMap.addAttribute("punishments", new Punishment1());
		 modelMap.addAttribute("list", punishmentRepo.findAll());
       // modelMap.addAttribute("User", userDetailsService.getUserDetails());
        return "userManagement/punishment1";
	}
	
	@PostMapping("/savePunishment")
	public String savePunishment(@ModelAttribute("punishments") Punishment1 punishment, Model modelMap,
			BindingResult bindResult, RedirectAttributes redirect) throws Exception{
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		//UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if(punishment.getPunishment1().equals(""))
		{
			bindResult.rejectValue("punishment1", "punishment.error");
		}
		
		if (bindResult.hasErrors()) {
			modelMap.addAttribute("punishment", new Punishment1());
			 modelMap.addAttribute("list", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
				modelMap.addAttribute("punishments", punishment);
			 return "userManagement/punishment1";
		}
		
		
		  
		
				
				punishmentRepo.save(punishment);
		 
		
		 redirect.addFlashAttribute("message", " Punishment saved Successfully. ");
		 
		 auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"User", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addPunishment"),
					utils.getMessage("log.user.addedPunishment") + " " + "and Punishment name is " + punishment.getPunishment1(),
					userdet.getSalutation() + " " + userdet.getFirstName() + " "
							+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
							+ userdet.getLastName(),
					"true");
			auditBeanBo.save();
		// modelMap.addAttribute("punishment1", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
        return "redirect:/punishment";
	}
	

}
