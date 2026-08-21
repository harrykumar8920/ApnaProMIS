package com.pams.controllers;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AccusedStatusDAO;
import com.pams.dto.ChargeInstaceSubDto;
import com.pams.dto.CriminalTaskDto;
import com.pams.dto.ViewAccusedDTO;
import com.pams.dto.ViewAccusedStatusDTO;
import com.pams.entity.AccusedStatusNew;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.ChargeInstaceMain;
import com.pams.service.AccusedStatusNewRepository;
import com.pams.service.AddAccusedRepository;

@Controller
public class AccusedStatusControlller {
	@Autowired
	private AccusedStatusNewRepository accusedStatusNewRepository;
	@Autowired
	AccusedStatusDAO accusedStatusDAO;
	@Autowired
	private AddAccusedRepository addAccusedRepos;
	@Autowired
	private OfficerController officerControl;
	
	@RequestMapping(value = "backFromView")
	public String backtomain(@ModelAttribute(value = "viewAccusedStatusDTO") ViewAccusedStatusDTO viewAccusedStatusDTO,BindingResult bindResult,ModelMap model,RedirectAttributes redirect ) throws Exception {
		
		
		AssignedTaskPuhAfterCOurt assignedTaskPuh = viewAccusedStatusDTO.getAssignedTask();
		int tabId=21;
		
			officerControl.modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId,new CriminalTaskDto());
			
			return "Task/CriminalTaskPage";
			

	}
	
	
	
	
	
	
	@PostMapping("/viewAccusedStatus")
	public String addCharge(Model model, @ModelAttribute ("criminalTaskDto") CriminalTaskDto criminalTaskDto) {
		AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh, Sort.by(Sort.Direction.ASC, "id"));
		
		model.addAttribute("accusedList",accusedList);
		
		ViewAccusedStatusDTO viewAccusedStatusDTO = new ViewAccusedStatusDTO();
		viewAccusedStatusDTO.setAssignedTask(assignedTaskPuh);
		model.addAttribute("viewAccusedStatusDTO",viewAccusedStatusDTO);
		
		return "Prosecutor/ViewAccusedStatus";
		
	}
	
	@PostMapping("/viewStatus1")
	public String viewAccusedStatus(Model model, @ModelAttribute("viewAccusedStatusDTO") ViewAccusedStatusDTO viewAccusedStatusDTO) {

	    AssignedTaskPuhAfterCOurt assignedTaskPuh = viewAccusedStatusDTO.getAssignedTask();
	    List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh, Sort.by(Sort.Direction.ASC, "id"));
	    Long id = viewAccusedStatusDTO.getAccuseName().getId();
	    
	  List<ViewAccusedDTO>  viewAccusedList= accusedStatusDAO.findAccusedStatusByAccusedName(viewAccusedStatusDTO.getAccuseName().getId(),assignedTaskPuh.getId());
	    viewAccusedStatusDTO.setListstatus(true);
	    model.addAttribute("viewAccusedList", viewAccusedList);
	    model.addAttribute("accusedList", accusedList);
	    model.addAttribute("viewAccusedStatusDTO", viewAccusedStatusDTO);
	    List<AccusedStatusNew> findByAccusedIdAndAssignedTask = accusedStatusNewRepository.findByAccusedIdAndAssignedTask(viewAccusedStatusDTO.getAccuseName(),assignedTaskPuh,Sort.by(Sort.Direction.DESC, "id"));

	    Map<Integer, List<AccusedStatusNew>> groupedByStatusCheck = findByAccusedIdAndAssignedTask.stream()
				.collect(Collectors.groupingBy(AccusedStatusNew::getStatusCheck));

	    model.addAttribute("groupedByStatusCheck", groupedByStatusCheck);

	    return "Prosecutor/ViewAccusedStatus"; 
	}


}
