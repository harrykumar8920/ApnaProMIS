package com.pams.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AccusedCompDAO;
import com.pams.dao.ChargeInstanceDAO;
import com.pams.dto.CriminalTaskDto;
import com.pams.entity.ActCompundRelevantSection;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Charge;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.ChargeInstanceAccused;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.UserDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AddActSecRepository;
import com.pams.service.AddSubSectionRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.ChanrgeInstanceRepository;
import com.pams.service.ChargeInstaceSubRepository;
import com.pams.service.ChargeInstanceAccusedRepository;
import com.pams.service.ChargeRepository;
import com.pams.service.ClauseRepository;
import com.pams.service.InstanceRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.PunishmentRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.utils.Utils;
import com.pams.validation.ChargeInstanceValidation;

@Controller
public class ChargeInstanceController {
	@Autowired
	ChargeInstanceDAO chargeInstanceDAO;
	@Autowired
	private AccusedCompDAO accusedCompDAO;
	@Autowired
	private AddSubSectionRepository addsubsecRepo;
	@Autowired
	private AddActRepository addActRepo;
	@Autowired
	private AddActSecRepository addactsecRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepos;
	@Autowired
private ChargeInstanceAccusedRepository chargeInstanceAccusedRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskPuhRepo1;

	@Autowired
	private ClauseRepository clauseRepo;
	@Autowired
	private PunishmentRepository punishmentRepo;
	@Autowired
	private ChanrgeInstanceRepository chargeInstanceRepo;
	@Autowired
	private InstanceRepository instanceRepo;
	@Autowired
	private ChargeRepository chargeRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private Utils utils;
	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private OfficerController officerControl;
	@Autowired
	private ChargeInstaceSubRepository chargeInstaceSubRepo;

	@RequestMapping(value = "backFromCharge")
	public String backtomain(@ModelAttribute(value = "chargeInstance") ChargeInstaceMain chargeInstance,
			BindingResult bindResult, ModelMap model, RedirectAttributes redirect) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = chargeInstance.getAssignedTask();
		int tabId = 21;

		officerControl.modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, new CriminalTaskDto());

		return "Task/CriminalTaskPage";

	}

	@SuppressWarnings("null")
	@PostMapping("/addCharge")
	public String addCharge(Model model, @ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) {

		ChargeInstaceMain chargeInstance = new ChargeInstaceMain();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = assignedTaskPuhRepo1
				.findById(criminalTaskDto.getAssignedTask().getId()).get();
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		chargeInstance.setProcourtdtl(procasedetails);
		model.addAttribute("accusedList", accusedList);
		chargeInstance.setAssignedTask(assignedTaskPuh);
		model.addAttribute("chargeInstance", chargeInstance);

		model.addAttribute("act", addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("section", addactsecRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("subsection", addsubsecRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("clause", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("punishment", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("chargelist", chargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("instancelist", instanceRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		List<ChargeInstaceMain> findByAssignedTask = new ArrayList<>();
		// List<ChargeInstaceMain> findByAssignedTask = chargeInstanceRepo.findAll();

		List<Object[]> list = chargeInstanceDAO.findActSectionSubSectionList(assignedTaskPuh);

		if (list.size() > 0) {
			for (int j = list.size() - 1; j >= 0; j--) {
				Object[] object = list.get(j);
				int dataTypeS = (int) object[0];
				List<AddAccused> accuseName1 = new ArrayList<>();

				List<ChargeInstaceMain> list1 = chargeInstanceRepo.findByAssignedTaskAndSamechargeType(assignedTaskPuh,
						dataTypeS, Sort.by(Sort.Direction.ASC, "charge"));

				/*
				 * for (ChargeInstaceMain chargeInstaceMain2 : list1) {
				 * accuseName1.add(chargeInstaceMain2.getAccuseName()); }
				 */

				for (ChargeInstaceMain chargeInstaceMain2 : list1) {
					chargeInstaceMain2.setAccuseName1(accuseName1);
					findByAssignedTask.add(chargeInstaceMain2);
					break;
				}
			}
		}

		model.addAttribute("chargeInstanceList", findByAssignedTask);

		return "Prosecutor/addChageInstance";

	}

	@SuppressWarnings("null")
	@GetMapping("/addCharge2")
	public String addCharge2(Model model, @ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) {

		ChargeInstaceMain chargeInstance = new ChargeInstaceMain();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		chargeInstance.setProcourtdtl(procasedetails);
		model.addAttribute("accusedList", accusedList);
		chargeInstance.setAssignedTask(assignedTaskPuh);
		model.addAttribute("chargeInstance", chargeInstance);

		model.addAttribute("act", addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("section", addactsecRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("subsection", addsubsecRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("clause", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("punishment", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("chargelist", chargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("instancelist", instanceRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		List<ChargeInstaceMain> findByAssignedTask = new ArrayList<>();
		// List<ChargeInstaceMain> findByAssignedTask = chargeInstanceRepo.findAll();

		List<Object[]> list = chargeInstanceDAO.findActSectionSubSectionList(assignedTaskPuh);

		if (list.size() > 0) {
			for (int j = list.size() - 1; j >= 0; j--) {
				Object[] object = list.get(j);
				int dataTypeS = (int) object[0];
				List<AddAccused> accuseName1 = new ArrayList<>();

				List<ChargeInstaceMain> list1 = chargeInstanceRepo.findByAssignedTaskAndSamechargeType(assignedTaskPuh,
						dataTypeS, Sort.by(Sort.Direction.ASC, "charge"));

				/*
				 * for (ChargeInstaceMain chargeInstaceMain2 : list1) {
				 * accuseName1.add(chargeInstaceMain2.getAccuseName()); }
				 */

				for (ChargeInstaceMain chargeInstaceMain2 : list1) {
					chargeInstaceMain2.setAccuseName1(accuseName1);
					findByAssignedTask.add(chargeInstaceMain2);
					break;
				}
			}
		}

		model.addAttribute("chargeInstanceList", findByAssignedTask);

		return "Prosecutor/addChageInstance";

	}
	@PostMapping("/chargeDelete")
	public String adeleteCharge(@RequestParam("id") Long id, RedirectAttributes redirect) {
		Optional<ChargeInstaceMain> optionalChargeInstance = chargeInstanceRepo.findById(id);
		if (optionalChargeInstance.isPresent()) {
			ChargeInstaceMain byId = optionalChargeInstance.get();
			byId.setApprovalStatus(1);
			chargeInstanceRepo.deleteById(id);
			// Set success message for the redirection
			redirect.addFlashAttribute("message", "Charge has been Deleted");

			// Populate the CriminalTaskDto
			CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
			criminalTaskDto.setAssignedTask(byId.getAssignedTask());

			// Pass the DTO to the redirect target
			redirect.addFlashAttribute("message", "Charge has been Deleted.");
			redirect.addFlashAttribute("criminalTaskDto", criminalTaskDto); // Flash attributes are ideal for one-time
																			// use

			return "redirect:/addCharge2"; // Redirect to the desired view
		} else {
			// Handle the case where the ID is not found
			redirect.addFlashAttribute("error", "Charge instance not found");
			return "redirect:/errorPage"; // Redirect to an error page or a fallback view
		}
	}
	@PostMapping("/confirm")
	public String approveCharge(@RequestParam("id") Long id, RedirectAttributes redirect) {
		// Fetch the ChargeInstanceMain entity by its ID
		Optional<ChargeInstaceMain> optionalChargeInstance = chargeInstanceRepo.findById(id);
		if (optionalChargeInstance.isPresent()) {
			ChargeInstaceMain byId = optionalChargeInstance.get();
			byId.setApprovalStatus(2);
			chargeInstanceRepo.save(byId);
			// Set success message for the redirection
			redirect.addFlashAttribute("message", "Charge has been confirmed");

			// Populate the CriminalTaskDto
			CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
			criminalTaskDto.setAssignedTask(byId.getAssignedTask());

			// Pass the DTO to the redirect target
			redirect.addFlashAttribute("message", "Charge has been confirmed.");
			redirect.addFlashAttribute("criminalTaskDto", criminalTaskDto); // Flash attributes are ideal for one-time
																			// use

			return "redirect:/addCharge2"; // Redirect to the desired view
		} else {
			// Handle the case where the ID is not found
			redirect.addFlashAttribute("error", "Charge instance not found");
			return "redirect:/errorPage"; // Redirect to an error page or a fallback view
		}
	}

	@PostMapping("/deleteInstance")
	public String deleteInstance(@ModelAttribute(value = "chargeInstance") ChargeInstaceMain chargeInstace,
			BindingResult bindResult, Model model, RedirectAttributes redirect) throws Exception {

		ChargeInstaceMain chargeInstance = chargeInstanceRepo.findById(chargeInstace.getId()).get();

		chargeInstance.setDeleteStatus(true);
		chargeInstanceRepo.save(chargeInstance);
		UserDetails user = userDetailsService.getUserDetailssss();
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Delete"),
				utils.getMessage("log.login.chargeInstanceDelete") + " " + "and Investigation Number is "
						+ chargeInstace.getAssignedTask().getProCourtCaseDetails().getAddCase()
								.getInvestigationOrderNo(),
				user.getFullName(), "true",
				chargeInstace.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		redirect.addFlashAttribute("message", "Deleted Successfully");

		redirect.addFlashAttribute("chargeInstace", chargeInstace);

		return "redirect:/chargeAdd";
	}

	@PostMapping("/saveChargeInstance")
	public String saveInspectorDet(@ModelAttribute(value = "chargeInstance") ChargeInstaceMain chargeInstace,
			BindingResult bindResult, Model model, RedirectAttributes redirect) throws Exception {		
		ChargeInstanceValidation chargeInstance1 = new ChargeInstanceValidation();
		List<ChargeInstaceMain> instance = chargeInstanceRepo.findByAssignedTask(chargeInstace.getAssignedTask(),
				Sort.by(Sort.Direction.ASC, "charge"));
		chargeInstance1.chargeValidation(chargeInstace, bindResult);
		AssignedTaskPuhAfterCOurt assignedTaskPuh = chargeInstace.getAssignedTask();
		if (bindResult.hasErrors()) {
			List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
					Sort.by(Sort.Direction.ASC, "id"));
			model.addAttribute("accusedList", accusedList);
			model.addAttribute("chargeInstance", chargeInstace);
			model.addAttribute("act", addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			model.addAttribute("punishment", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			model.addAttribute("chargelist", chargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
			model.addAttribute("instancelist", instanceRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
			model.addAttribute("chargeInstanceList",
					chargeInstanceRepo.findByAssignedTask(assignedTaskPuh, Sort.by(Sort.Direction.ASC, "charge")));
			return "Prosecutor/addChageInstance";
		}

		Random random = new Random();
		int randomNumber = random.nextInt(900000000) + 100000000;
		UserDetails user = userDetailsService.getUserDetailssss();
		
			java.util.List<ActCompundRelevantSection> actCompundRelevantSection = new ArrayList<>();
			ChargeInstaceMain newChargeInstace = new ChargeInstaceMain();
			newChargeInstace.setCreatedBy(user);
			newChargeInstace.setAprovedBy(user);
			newChargeInstace.setCreatedDate(new Date());
			newChargeInstace.setAssignedTask(chargeInstace.getAssignedTask());
			newChargeInstace.setCharge(chargeInstace.getCharge());
			newChargeInstace.setProcourtdtl(chargeInstace.getProcourtdtl());
			newChargeInstace.setDescription(chargeInstace.getDescription());
			newChargeInstace.setSamechargeType(randomNumber);
			for (int it11 = 0; it11 < chargeInstace.getCompoundabilityA().length; it11++) {
				String releventSectionA = chargeInstace.getReleventSectionA();
				String addActSecId = chargeInstace.getAddActSecId();
				String[] releventSections = releventSectionA.split("\\*#");
				String[] addActSecId1 = addActSecId.split("\\*#");
				String str1 =addActSecId.replace("*#", ",");
				String[] split = str1.split(",");
				String test=split[it11];
				long long1 = Long.parseLong(test);
				ActCompundRelevantSection actCompundRelevantSectiona = new ActCompundRelevantSection();
				actCompundRelevantSectiona.setAddActSecId(long1);
				actCompundRelevantSectiona.setAct(chargeInstace.getActA()[it11]);
				actCompundRelevantSectiona.setCompoundability(chargeInstace.getCompoundabilityA()[it11]);
				actCompundRelevantSectiona.setReleventSection(releventSections[it11]);
				actCompundRelevantSectiona.setPunishment(chargeInstace.getPunishmentIDA()[it11]);
				System.out.println(chargeInstace.getPunishmentIDA()[it11]);
				actCompundRelevantSectiona.setChargeInstanceMain(newChargeInstace);
				actCompundRelevantSection.add(actCompundRelevantSectiona);
			}
			newChargeInstace.setActCompundRelevantSection(actCompundRelevantSection);
			List<ChargeInstanceAccused> accusedlist = chargeInstace.getAccuseName1()
				    .stream()
				    .map(accused -> {
				        ChargeInstanceAccused ss = new ChargeInstanceAccused();
				        ss.setAccuseName(accused);
				        ss.setChargeInstanceMain(newChargeInstace);
				        return ss;
				    })
				    .collect(Collectors.toList());

				newChargeInstace.setChargeInstanceAccused(accusedlist);
				chargeInstanceRepo.save(newChargeInstace);
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName(), "ProMIS",
				Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.login.Save"),
				utils.getMessage("log.login.chargeInsaved") + " " + "and Investigation Number is "
						+ chargeInstace.getAssignedTask().getProCourtCaseDetails().getAddCase()
								.getInvestigationOrderNo(),
				user.getFullName(), "true",
				chargeInstace.getAssignedTask().getProCourtCaseDetails().getAddCase().getId());
		auditBeanBo.save();
		redirect.addFlashAttribute("message", "Save Successfully");
		redirect.addFlashAttribute("chargeInstace", chargeInstace);
		return "redirect:/chargeAdd";

	}

	@SuppressWarnings({ "null" })
	@GetMapping("/chargeAdd")
	public String chargeAdd(Model model, @ModelAttribute("chargeInstace") ChargeInstaceMain chargeInstaceMain1) {

		ChargeInstaceMain chargeInstance = new ChargeInstaceMain();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = chargeInstaceMain1.getAssignedTask();
		ProCourtCaseDetails procasedetails = assignedTaskPuh.getProCourtCaseDetails();
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		chargeInstance.setProcourtdtl(procasedetails);
		model.addAttribute("accusedList", accusedList);
		chargeInstance.setAssignedTask(assignedTaskPuh);
		model.addAttribute("chargeInstance", chargeInstance);

		model.addAttribute("act", addActRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("section", addactsecRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("subsection", addsubsecRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("clause", clauseRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("punishment", punishmentRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("chargelist", chargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		model.addAttribute("instancelist", instanceRepo.findAll(Sort.by(Sort.Direction.ASC, "id")));

		List<ChargeInstaceMain> findByAssignedTask = new ArrayList<>();

		List<Object[]> list = chargeInstanceDAO.findActSectionSubSectionList(assignedTaskPuh);

		if (list.size() > 0) {
			for (int j = list.size() - 1; j >= 0; j--) {
				Object[] object = list.get(j);
				int dataTypeS = (int) object[0];
				List<AddAccused> accuseName1 = new ArrayList<>();

				List<ChargeInstaceMain> list1 = chargeInstanceRepo.findByAssignedTaskAndSamechargeType(assignedTaskPuh,
						dataTypeS, Sort.by(Sort.Direction.ASC, "charge"));
				for (ChargeInstaceMain chargeInstaceMain2 : list1) {
					chargeInstaceMain2.setAccuseName1(accuseName1);
					findByAssignedTask.add(chargeInstaceMain2);
					break;
				}
			}
		}

		model.addAttribute("chargeInstanceList", findByAssignedTask);

		return "Prosecutor/addChageInstance";

	}

	@RequestMapping(value = "/chagerget", method = RequestMethod.GET)
	@ResponseBody
	List<Charge> getAccused(@RequestParam("courtType") String id, @RequestParam("assintaskId") Long aid) {

		// List<Charge> listCharge =
		// accusedCompDAO.findChargeByAccusedAndAssignedTask(id, aid);

		ArrayList listCharge;
		return listCharge = new ArrayList();
	}

}
