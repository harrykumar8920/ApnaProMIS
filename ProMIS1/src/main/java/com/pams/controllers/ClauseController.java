package com.pams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.entity.Clause;
import com.pams.entity.UserDetails;
import com.pams.service.AuditBeanBo;
import com.pams.service.ClauseRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.PromisException;
import com.pams.utils.Utils;

import jakarta.validation.Valid;

@Controller
public class ClauseController {

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
    private ClauseRepository clauseRepo;

    @GetMapping("/clauses")
    public String clausePage(Model model) {
        model.addAttribute("clauseob", new Clause());
        model.addAttribute("clauselist", clauseRepo.findAll());
        return "userManagement/clause1";
    }

    @PostMapping("/addnewclause")
    public String addNewClause(@Valid @ModelAttribute("clauseob") Clause clause,
            BindingResult bindResult, Model model, RedirectAttributes redirect) throws PromisException, Exception {
    	UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
       
        Clause existingClause = clauseRepo.findByClause(clause.getClause());
        if (existingClause != null) {
      
          //  redirect.addFlashAttribute("massage", "This clause is already added");
        	bindResult.rejectValue("clause", "clause.error");
            model.addAttribute("clauselist", clauseRepo.findAll());

            return "userManagement/clause1";
        } else if (bindResult.hasErrors()) {
            
           // redirect.addFlashAttribute("massage", "Please enter only characters");
            model.addAttribute("clauselist", clauseRepo.findAll());

            return "userManagement/clause1";
        } else {
            clauseRepo.save(clause);
            redirect.addFlashAttribute("massage", "Clause added successfully");
        }
        auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"User", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addClause"),
				utils.getMessage("log.user.addedClause") + " " + "and Clause name is " + clause.getClause(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true");
		auditBeanBo.save();
        model.addAttribute("clauseob", new Clause());
        model.addAttribute("clauselist", clauseRepo.findAll());

        return "redirect:/clauses";
    }



}
