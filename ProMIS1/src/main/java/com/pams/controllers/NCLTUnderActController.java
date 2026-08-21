package com.pams.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.ChargeInstanceDAO;
import com.pams.dto.CriminalTaskDto;
import com.pams.dto.NCLTActofRespondantDTO;
import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.NCLTActofRespondant;
import com.pams.entity.UserDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AddSubSectionRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.NCLTActofRespondantRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class NCLTUnderActController {
	@Autowired
	ChargeInstanceDAO chargeInstanceDAO;

	@Autowired
	private AddActRepository addActRepo;
	@Autowired
	private AddActSecRepository addactsecRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepos;

	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private NCLTActofRespondantRepository ncltActofRespondantRepository;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
AssignedTaskPuhAfterCOurtRepository assignedTaskPuhAfterCOurtRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@SuppressWarnings("null")
	@PostMapping("/addUnderSection")
	public String addUnderSection(Model model, @ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) {

		NCLTActofRespondantDTO nCLTActofRespondantDTO = new NCLTActofRespondantDTO();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhAfterCOurtRepository.findById(criminalTaskDto.getAssignedTask().getId()).get();
		//AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();
		ProCourtCaseDetails proCourtCaseDetails = assignedTaskPuh.getProCourtCaseDetails();
		//proCourtCaseDetails procasedetails = proCourtCaseDetailsRepo.findByAddCase(assignedTaskPuh.getAddCase());
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		nCLTActofRespondantDTO.setProcourtdtl(proCourtCaseDetails);
		model.addAttribute("accusedList", accusedList);
		nCLTActofRespondantDTO.setAssignedTask(assignedTaskPuh);
		model.addAttribute("nCLTActofRespondantDTO", nCLTActofRespondantDTO);
		
		model.addAttribute("act", addActRepo.findAllByIdOrId(1L,6L));
		model.addAttribute("section", addactsecRepo.findAllNCLTCaseAndAct2013());
		List<NCLTActofRespondant> allsaveAct = ncltActofRespondantRepository.findByAssignedTask(assignedTaskPuh);
		model.addAttribute("allsaveAct", allsaveAct);
		return "Prosecutor/addUnderSectionPage";
	}

	@PostMapping("/ncltActSave")
	public String saveNCLTAct(NCLTActofRespondantDTO dto, Model model) throws Exception {

		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		NCLTActofRespondant ncltAct = new NCLTActofRespondant();
		ncltAct.setAct(dto.getAct());
		ncltAct.setSection(dto.getSection());
		List<AddAccused> accusedList1 = dto.getAccuseName();
		ncltAct.setAccuseName1(accusedList1);
		ncltAct.setDescription(dto.getDescription());

		ncltAct.setCreatedBy(userdet);
		ncltAct.setAprovedBy(userdet);
		ncltAct.setCreatedDate(new Date());
		ncltAct.setApproveDate(new Date());
		ncltAct.setAssignedTask(dto.getAssignedTask());
		ncltAct.setProcourtdtl(dto.getProcourtdtl());
		ncltAct.setApprovalStatus(0);
		ncltActofRespondantRepository.save(ncltAct);

		model.addAttribute("message", "Save Successfully.");

		List<NCLTActofRespondant> allsaveAct = ncltActofRespondantRepository.findByAssignedTask(ncltAct.getAssignedTask());
		model.addAttribute("allsaveAct", allsaveAct);

		NCLTActofRespondantDTO nCLTActofRespondantDTO = new NCLTActofRespondantDTO();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = dto.getAssignedTask();
		//proCourtCaseDetails procasedetails = proCourtCaseDetailsRepo.findByAddCase(assignedTaskPuh.getAddCase());
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		nCLTActofRespondantDTO.setProcourtdtl(procasedetails);
		model.addAttribute("accusedList", accusedList);
		nCLTActofRespondantDTO.setAssignedTask(assignedTaskPuh);
		model.addAttribute("nCLTActofRespondantDTO", nCLTActofRespondantDTO);

		model.addAttribute("act", addActRepo.findAllById(1L));
		model.addAttribute("section", addactsecRepo.findAllNCLTCaseAndAct2013());

		return "Prosecutor/addUnderSectionPage";
	}

	@PostMapping("/confirmNCTAct")
	public String confirmNCTAct(@RequestParam("id") Long id, Model model) {
		// Fetch the entity from the database by its ID
		Optional<NCLTActofRespondant> optionalEntity = ncltActofRespondantRepository.findById(id);

		if (optionalEntity.isPresent()) {
			NCLTActofRespondant entity = optionalEntity.get();

			// Update the approvalStatus to 2 (Confirmed)
			entity.setApprovalStatus(2);

			// Save the updated entity back to the database
			ncltActofRespondantRepository.save(entity);
			model.addAttribute("message", "Confirm Successfully.");

			List<NCLTActofRespondant> allsaveAct = ncltActofRespondantRepository.findByAssignedTask(entity.getAssignedTask());
			model.addAttribute("allsaveAct", allsaveAct);

			NCLTActofRespondantDTO nCLTActofRespondantDTO = new NCLTActofRespondantDTO();
			AssignedTaskPuhAfterCOurt assignedTaskPuh = entity.getAssignedTask();
			ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
			List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
					Sort.by(Sort.Direction.ASC, "id"));
			nCLTActofRespondantDTO.setProcourtdtl(procasedetails);
			model.addAttribute("accusedList", accusedList);
			nCLTActofRespondantDTO.setAssignedTask(assignedTaskPuh);
			model.addAttribute("nCLTActofRespondantDTO", nCLTActofRespondantDTO);

			model.addAttribute("act", addActRepo.findAllByIdOrId(1L,6L));
			model.addAttribute("section", addactsecRepo.findAllNCLTCaseAndAct2013());

			return "Prosecutor/addUnderSectionPage";
		}
		return null;

	}

}
