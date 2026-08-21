package com.pams.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.pams.dao.AppRoleDAO;
import com.pams.dao.ChargeInstaceSubDAO;
import com.pams.dto.ActCompundRelevantSectionDto;
import com.pams.dto.AddActDto;
import com.pams.dto.ChargeDTO;
import com.pams.dto.CriminalTaskDto;
import com.pams.dto.HolololoDTO;
import com.pams.entity.AccusedStatusNew;
import com.pams.entity.ActCompundRelevantSection;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Charge;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.ChargeInstaceSub;
import com.pams.entity.Punishment1;
import com.pams.entity.UserDetails;
import com.pams.service.AccusedStatusNewRepository;
import com.pams.service.ActCompundRelevantSectionRepo;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AddActRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.ChanrgeInstanceRepository;
import com.pams.service.ChargeInstaceSubRepository;
import com.pams.service.ChargeRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.CreateTasksRepository;
import com.pams.service.DischargeRepository;
import com.pams.service.InstanceRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.PunishmentRepository;
import com.pams.service.StateRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.validation.AccusedStatusValidation;

@Controller
public class AccusedStatusNewController {
	@Autowired
	private ChanrgeInstanceRepository chargeInstanceRepo;
	@Autowired
	private InstanceRepository instanceRepository;
	@Autowired
	PunishmentRepository punishmentRepository;
	@Autowired
	CourtTypeRepository courtTypeRepository;
	@Autowired
	private AccusedCompDAO accusedCompDAO;
	@Autowired
	private AddActRepository addActRepo;
	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private AccusedStatusNewRepository accusedStatusNewRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepos;
	@Autowired
	private ActCompundRelevantSectionRepo actCompundRelevantSectionRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private CreateTasksRepository createTasksRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private OfficerController officerControl;
	@Autowired
	private ChargeInstaceSubRepository chargeInstaceSubRepository;
	@Autowired
	private ChargeRepository chargeRepository;
	@Autowired
	private DischargeRepository dischargeRepository;
	@Autowired
	private ChargeInstaceSubDAO subDAO;

	@RequestMapping(value = "backFromAccusedStatus")
	public String backtomain(@ModelAttribute(value = "accusedStatus") AccusedStatusNew accusedStatusNew,
			BindingResult bindResult, ModelMap model, RedirectAttributes redirect) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = accusedStatusNew.getAssignedTask();
		int tabId = 21;

		officerControl.modelAttributeObjectAfterCourt(assignedTaskPuh, model, tabId, new CriminalTaskDto());

		return "Task/CriminalTaskPage";

	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/getInstanse", method = RequestMethod.GET)
	@ResponseBody
	List<ChargeDTO> getAccused(@RequestParam("courtType") Long id, @RequestParam("assintaskId") Long aid) {

		// List<ChargeInstaceMain> findByAssignedTaskAndAccuseName =
		// chargeInstanceRepo.findByAssignedTaskAndAccuseName(assignedTaskPuhRepo.findById(aid).get(),
		// addAccusedRepos.findById(id).get());
		Long accusiD = id;
		Long assigenTaskId = aid;
		List<Object[]> objects = chargeInstanceRepo.findCustomChargeByAssignedTaskAndAccuseName(accusiD, assigenTaskId);

		List<ChargeDTO> list = new ArrayList<ChargeDTO>();
		// List<ChargeInstaceMain> allList = accusedCompDAO.findChargeByAccused(id,
		// aid);
		if (objects.size() > 0) {
			for (int j = objects.size() - 1; j >= 0; j--) {
				Object[] object = objects.get(j);
				ChargeDTO chargeInstaceDto = new ChargeDTO();
				Long instanceId0 = (Long) object[0];
				long mainId = instanceId0.longValue();
				Integer chargeName = (Integer) object[1];
				Long chargeNameLong = chargeName != null ? chargeName.longValue() : null;
				String ssssss=(String) object[2];
				chargeInstaceDto.setId(chargeNameLong);
				chargeInstaceDto.setChargeName(ssssss);
				
				
				
				
				System.out.println("mainId:- " + mainId + " , Charge Name :-" + chargeName);
				list.add(chargeInstaceDto);
			}
		}

		return list;
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/getChargeSub", method = RequestMethod.GET)
	@ResponseBody
	List<AddActDto> getChargeSub(@RequestParam("courtType") Long id,
			@RequestParam("assintaskId") Long aid, @RequestParam("accusedId") Long accusedId) {
		Long chargeId = id;
		Long acusedId=accusedId;
		Long assintaskId=aid;
		
		List<Object[]> objects = actCompundRelevantSectionRepo
				.findByChargeInstanceMainCostum(accusedId,aid,id);
		List<AddActDto> list = new ArrayList<AddActDto>();
		
		if (objects.size() > 0) {
			for (int j = objects.size() - 1; j >= 0; j--) {
				Object[] object = objects.get(j);
				AddActDto actdto=new AddActDto();
				
				String actName=(String) object[0];
				Long actId1 = (Long) object[1];
				long actId = actId1.longValue();
				Long table = (Long) object[2];
				long tableId = table.longValue();
				/*
				 * System.out.println("actName:- " + actName + " , actId Name :-" + actId+
				 * " , compondability :-" + compondability+ " , relaventSection :-" +
				 * relaventSection+ " , punishmentId1 :-" + punishmentId1+
				 * " , punishmentIdName :-" + punishmentIdName );
				 */
				/* System.out.println("actNAme is :-"+actName); */
				actdto.setActId(actId);
				actdto.setActName(actName);
				actdto.setTableId(tableId);
				list.add(actdto);
			}
		}
		
		
		/*
		 * for (ActCompundRelevantSection Main : findByChargeInstanceMain) {
		 * ActCompundRelevantSectionDto dto = new ActCompundRelevantSectionDto();
		 * dto.setActName(Main.getAct().getAct()); dto.setActId(Main.getAct().getId());
		 * dto.setCompoundability(Main.getCompoundability());
		 * dto.setReleventSection(Main.getReleventSection());
		 * dto.setPunishment(Main.getPunishment().getPunishment1());
		 * dto.setPunishmentId(Main.getPunishment().getId());
		 * dto.setActList(Main.getAct()); list.add(dto); }
		 */
		return list;
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/getReleventData", method = RequestMethod.GET)
	@ResponseBody
	ActCompundRelevantSectionDto getReleventData(@RequestParam("courtType") Long id,
			@RequestParam("chargeName") Long aid) {
		Long tableId = id;
		Long chargeMain = aid;
		ActCompundRelevantSection Main = actCompundRelevantSectionRepo.findById(tableId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + chargeMain));
		
		ActCompundRelevantSectionDto dto = new ActCompundRelevantSectionDto();
		dto.setActName(Main.getAct().getAct());
		dto.setActId(Main.getAct().getId());
		dto.setCompoundability(Main.getCompoundability());
		dto.setReleventSection(Main.getReleventSection());
		dto.setPunishment(Main.getPunishment().getPunishment1());
		dto.setPunishmentId(Main.getPunishment().getId());
		dto.setActList(Main.getAct());
		return dto;
	}

	@RequestMapping(value = "/getPunishment", method = RequestMethod.GET)
	@ResponseBody
	List<HolololoDTO> getPunishment(@RequestParam("courtType") Long id) {
		List<HolololoDTO> findChargeInstaceSub = accusedCompDAO.findChargeInstaceSub(id);
		return findChargeInstaceSub;
	}

	@GetMapping("/addNewAccusedStatusGet")
	public String addNewAccusedStatusGet(Model modelMap,
			@ModelAttribute("criminalTaskDto") AccusedStatusNew accusedStatusNew) throws Exception {

		AssignedTaskPuhAfterCOurt assignedTaskPuh = accusedStatusNew.getAssignedTask();

		if (assignedTaskPuh == null) {
			return "redirect:/userHome";
		}

		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		List<Charge> chargeInstace = chargeRepository.findAll();
		List<ChargeInstaceSub> findAll3 = chargeInstaceSubRepository.findAll();
		List<ChargeInstaceSub> findByChargeId = chargeInstaceSubRepository.findCustomByChargeId();
		List<ChargeInstaceMain> findByAssignedTask = chargeInstanceRepo.findByAssignedTask(assignedTaskPuh);
		List<AccusedStatusNew> findAll = accusedStatusNewRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		List<Punishment1> punishmentList1 = punishmentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<AccusedStatusNew> findAllList = accusedStatusNewRepository.findByAssignedTask(assignedTaskPuh);

		Map<Integer, List<AccusedStatusNew>> groupedByStatusCheck = findAllList.stream()
				.collect(Collectors.groupingBy(AccusedStatusNew::getStatusCheck));

		modelMap.addAttribute("groupedByStatusCheck", groupedByStatusCheck);

		/*
		 * modelMap.addAttribute("collectPunismentList", groupedByStatusCheck.get(1));
		 * modelMap.addAttribute("collectDischargeList",groupedByStatusCheck.get(2));
		 * modelMap.addAttribute("collectStayedList", groupedByStatusCheck.get(3));
		 */
		modelMap.addAttribute("punishmentList1", punishmentList1);
		modelMap.addAttribute("rollID", userrole);
		modelMap.addAttribute("dischargeList", dischargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("instanceByCharged", findByChargeId);
		modelMap.addAttribute("findByAssignedTask", findByAssignedTask);
		modelMap.addAttribute("chargeInstace", chargeInstace);
		modelMap.addAttribute("accusedList", accusedList);
		// modelMap.addAttribute("dischargeList",
		// dischargeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
		modelMap.addAttribute("addCourtList", courtTypeRepository.findByCourtType(2));
		modelMap.addAttribute("addCourtStayed", courtTypeRepository.findByCourtType(4));
		modelMap.addAttribute("addCourtDischageCourt", courtTypeRepository.findByCourtType(3));
		modelMap.addAttribute("findAll3", findAll3);
		modelMap.addAttribute("accusedStatus", accusedStatusNew);
		modelMap.addAttribute("listAccusedStatus", findAll);
		return "Prosecutor/addAccusedStatusNewPage";
	}

	@PostMapping("/addNewAccusedStatus")
	public String addNewAccusedStatus(Model modelMap,
			@ModelAttribute("criminalTaskDto") CriminalTaskDto criminalTaskDto) throws Exception {
		AccusedStatusNew accusedStatusNew = new AccusedStatusNew();
		AssignedTaskPuhAfterCOurt assignedTaskPuh = criminalTaskDto.getAssignedTask();
		accusedStatusNew.setAssignedTask(assignedTaskPuh);
		List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(assignedTaskPuh,
				Sort.by(Sort.Direction.ASC, "id"));
		List<Charge> chargeInstace = chargeRepository.findAll();
		List<ChargeInstaceSub> findAll3 = chargeInstaceSubRepository.findAll();
		List<AccusedStatusNew> findAll = accusedStatusNewRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		List<Punishment1> punishmentList1 = punishmentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());
		List<AccusedStatusNew> findAllList = accusedStatusNewRepository
				.findByAssignedTask(accusedStatusNew.getAssignedTask());

		Map<Integer, List<AccusedStatusNew>> groupedByStatusCheck = findAllList.stream()
				.collect(Collectors.groupingBy(AccusedStatusNew::getStatusCheck));

		modelMap.addAttribute("groupedByStatusCheck", groupedByStatusCheck);
		modelMap.addAttribute("punishmentList1", punishmentList1);
		modelMap.addAttribute("rollID", userrole);
		modelMap.addAttribute("dischargeList", dischargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
		modelMap.addAttribute("addCourtList", courtTypeRepository.findByCourtType(2));
		modelMap.addAttribute("addCourtStayed", courtTypeRepository.findByCourtType(4));
		modelMap.addAttribute("addCourtDischageCourt", courtTypeRepository.findByCourtType(3));
		modelMap.addAttribute("findByAssignedTask", chargeInstanceRepo.findByAssignedTask(assignedTaskPuh));
		modelMap.addAttribute("chargeInstace", chargeInstace);
		modelMap.addAttribute("accusedList", accusedList);
		modelMap.addAttribute("findAll3", findAll3);
		modelMap.addAttribute("accusedStatus", accusedStatusNew);
		modelMap.addAttribute("listAccusedStatus", findAll);
		return "Prosecutor/addAccusedStatusNewPage";
	}

	@RequestMapping(value = "/saveAccusedStatus")
	public String saveAccusedStatus(ModelMap modelMap,
			@ModelAttribute("accusedStatus") AccusedStatusNew accusedStatusNew, BindingResult bindResult,
			RedirectAttributes redirect) throws Exception {

		AccusedStatusValidation accusedValidation = new AccusedStatusValidation();
		accusedValidation.isValidAccused(accusedStatusNew, bindResult);

		if (bindResult.hasErrors()) {
			List<AddAccused> accusedList = addAccusedRepos.findAllByAssignedTask(accusedStatusNew.getAssignedTask(),
					Sort.by(Sort.Direction.ASC, "id"));
			modelMap.addAttribute("punishmentList1", punishmentRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
			List<AccusedStatusNew> findAllList = accusedStatusNewRepository
					.findByAssignedTask(accusedStatusNew.getAssignedTask());

			Map<Integer, List<AccusedStatusNew>> groupedByStatusCheck = findAllList.stream()
					.collect(Collectors.groupingBy(AccusedStatusNew::getStatusCheck));

			modelMap.addAttribute("groupedByStatusCheck", groupedByStatusCheck);
			modelMap.addAttribute("addCourtList", courtTypeRepository.findByCourtType(2));
			modelMap.addAttribute("addCourtStayed", courtTypeRepository.findByCourtType(4));
			modelMap.addAttribute("addCourtDischageCourt", courtTypeRepository.findByCourtType(3));
			modelMap.addAttribute("dischargeList", dischargeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
			modelMap.addAttribute("findByAssignedTask",
					chargeInstanceRepo.findByAssignedTask(accusedStatusNew.getAssignedTask()));
			modelMap.addAttribute("chargeInstace", chargeRepository.findAll());
			modelMap.addAttribute("accusedList", accusedList);
			modelMap.addAttribute("findAll3", chargeInstaceSubRepository.findAll());
			modelMap.addAttribute("accusedStatus", accusedStatusNew);
			modelMap.addAttribute("listAccusedStatus",
					accusedStatusNewRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));

			return "Prosecutor/addAccusedStatusNewPage";
		}
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		accusedStatusNew.setUpdatedBy(userdet);
		//Long id = accusedStatusNew.getActList().getId();
		
		accusedStatusNew.setReleventSectionId(accusedStatusNew.getAdddActTableId());
		accusedStatusNew.setCreatedBy(userdet);
		if (accusedStatusNew.getId() != null) {
			accusedStatusNewRepository.save(accusedStatusNew);
			/*
			 * ChargeInstaceSub id = accusedStatusNew.getInstanceId();
			 * id.setPunishmentDone(true); chargeInstaceSubRepository.save(id);
			 */

			redirect.addFlashAttribute("message", "Accused Status Updated Successfully !!");
		} else {
			/*
			 * ChargeInstaceSub id = accusedStatusNew.getInstanceId();
			 * id.setPunishmentDone(true); chargeInstaceSubRepository.save(id);
			 */
			accusedStatusNewRepository.save(accusedStatusNew);
			redirect.addFlashAttribute("message", "Accused Status Saved Successfully !!");
		}
		AccusedStatusNew criminalTaskDto = new AccusedStatusNew();
		criminalTaskDto.setStatusCheck(accusedStatusNew.getStatusCheck());
		criminalTaskDto.setAssignedTask(accusedStatusNew.getAssignedTask());
		redirect.addFlashAttribute("criminalTaskDto", criminalTaskDto);
		return "redirect:/addNewAccusedStatusGet";
	}

	// both mapping working fine write below here
	// @RequestMapping(value = "/editAccusedStatus", params = "editAccusedStatus")
	/*
	 * @PostMapping("/editAccusedStatus") public String
	 * editAccusedStatus(@RequestParam(value = "editAccusedStatus", required = true)
	 * Long id, ModelMap modelMap) { AccusedStatusNew accusedStatusNew =
	 * accusedStatusNewRepository.findById(id).get();
	 * modelMap.addAttribute("accusedStatus", accusedStatusNew);
	 * modelMap.addAttribute("listAccusedStatus",
	 * accusedStatusNewRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
	 * return "Prosecutor/addAccusedStatusNewPage";
	 * 
	 * }
	 */

	// @DeleteMapping("/deleteAccusedStatus")
	@RequestMapping(value = "/deleteAccusedStatus", params = "deleteAccusedStatus")
	public String deleteAccusedStatus(@RequestParam(value = "deleteAccusedStatus", required = true) Long id,
			ModelMap map, RedirectAttributes redirect) {
		// accusedStatusNewRepository.deleteById(id);
		AccusedStatusNew accusedStatusNew = accusedStatusNewRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		accusedStatusNew.setCheckStatus(false);
		accusedStatusNewRepository.save(accusedStatusNew);
		redirect.addFlashAttribute("message", "Accused Status Deleted Successfully !!");
		CriminalTaskDto criminalTaskDto = new CriminalTaskDto();
		criminalTaskDto.setAssignedTask(accusedStatusNew.getAssignedTask());
		redirect.addFlashAttribute("criminalTaskDto", criminalTaskDto);
		return "redirect:/addNewAccusedStatusGet";
	}

}
