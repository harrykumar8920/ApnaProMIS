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

import com.pams.entity.CategoriesofCompany;
import com.pams.entity.TypeofCase;
import com.pams.service.CategoriesofCompanyRepository;
import com.pams.service.TypeofCaseRepository;
import com.pams.utils.PromisException;

@Controller
public class CategoriesofCompanyController {
	
	@Autowired
	CategoriesofCompanyRepository categoriesofCompanyRepo;
	
	@RequestMapping(value = "/categoriesofCompany")
	public String typeCase(Model model) {

		//TypeofResponse typeResponse1 = new TypeofResponse();
		CategoriesofCompany categoriesofCompany = new CategoriesofCompany();
		model.addAttribute("categoriesofCompany", categoriesofCompany);
		model.addAttribute("listcategoriesofcompany", categoriesofCompanyRepo.findAll());
		return "userManagement/CategoriesCompany";
		
	}
	@RequestMapping(value = "/addcategoriesofcompany")
	public String addNewCategoriesofCompany(@ModelAttribute CategoriesofCompany categoriesofcompany, BindingResult bindResult,
			Model model, RedirectAttributes redirect) throws PromisException, Exception {
		
		if(categoriesofcompany.getCategories().equals(""))
		{
			bindResult.rejectValue("categories", "errmsg.fnames");
		}
			
		if (bindResult.hasErrors()) {

			model.addAttribute("listcategoriesofcompany", categoriesofCompanyRepo.findAll());
			

			//model.addAttribute("massage", "Please enter only charactor");

			model.addAttribute("categoriesofCompany", categoriesofcompany);

			return "userManagement/CategoriesCompany";

		}

		else if (categoriesofcompany.getId() != null) {
			//categoriesofcompany.setEditCategories(true);
			categoriesofCompanyRepo.save(categoriesofcompany);
			redirect.addFlashAttribute("massage", "Categories of Company updated successfully");

		} else {
			//categoriesofcompany.setEditCategories(false);
			categoriesofCompanyRepo.save(categoriesofcompany);
			redirect.addFlashAttribute("massage", "Categories of Company  added successfully");
		}
		return "redirect:/categoriesofCompany";
	}

	@RequestMapping(value = "/editcategories", params = "editcategories", method = RequestMethod.GET)
	public String editCategories(@RequestParam(value = "editcategories") Long id, Model model,
			RedirectAttributes redirect) {

		CategoriesofCompany categoriesofCompany= categoriesofCompanyRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("invalid Id" + id));
		categoriesofCompany.setEditCategories(true);

		model.addAttribute("categoriesofCompany", categoriesofCompany);
		model.addAttribute("listcategoriesofcompany", categoriesofCompanyRepo.findAll());
		return "userManagement/CategoriesCompany";


	}

	@RequestMapping(value = "/deletecategories", params = "categoriesdelete", method = RequestMethod.GET)
	public String deleteCategories(@RequestParam(value = "categoriesdelete") Long id, RedirectAttributes redirect) {
		categoriesofCompanyRepo.deleteById(id);
		redirect.addFlashAttribute("massage", "Categories of Company deleted successfully");
		return "redirect:/categoriesofCompany";

	}
	
}



