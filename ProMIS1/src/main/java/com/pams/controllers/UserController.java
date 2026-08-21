package com.pams.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pams.dao.AppRoleDAO;
import com.pams.dao.AppUserDAO;
import com.pams.dto.DesignationDaoImpl;
import com.pams.dto.MailInfo;
import com.pams.dto.PageNoDTO;
import com.pams.dto.UserForm;
import com.pams.entity.AddCourt;
import com.pams.entity.AddDesignation;
import com.pams.entity.AddSubTask;
import com.pams.entity.AddUnitlocation;
import com.pams.entity.AppRole;
import com.pams.entity.AppUser;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.Charge;
import com.pams.entity.CreateTasks;
import com.pams.entity.Instance;
import com.pams.entity.Status;
import com.pams.entity.TypeofResponse;
import com.pams.entity.UnitDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.UserRole;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AddLocationRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.AppRoleRepository;
import com.pams.service.AppUserRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.AuditBeanBo;
import com.pams.service.ChargeRepository;
import com.pams.service.CourtTypeRepository;
import com.pams.service.CreateTasksRepository;
import com.pams.service.InstanceRepository;
import com.pams.service.MailBo;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.SfioAsRepository;
import com.pams.service.SubTaskRepository;
import com.pams.service.TasksPuhRepository;
import com.pams.service.TypeofResponseRepository;
import com.pams.service.UnitDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.service.UserRoleRepository;
import com.pams.utils.Crypt;
import com.pams.utils.ProMisConstant;
import com.pams.utils.PromisException;
import com.pams.utils.Utils;
import com.pams.validation.ChangePasswordValidator;
import com.pams.validation.ProMISValidator;
import com.pams.validation.UserValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private AppRoleRepository appRoleRepo;

	@Autowired
	private UserDetailsRepository userDetailsRepo;
	@Autowired
	private AppUserRepository appUserRepo;
	@Autowired
	private AddDesignationRepository designationRepo;
	@Autowired
	private UnitDetailsRepository unitDetailsRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;
	@Autowired
	private AppUserDAO appUserDAO;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private SfioAsRepository sfioAsRepo;

	@Autowired
	private SubTaskRepository subTaskRepo;

	@Autowired
	AddStatusRepository addStatusRepo;

	@Autowired
	private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;

	@Autowired
	private AddLocationRepository addlocRepo;

	@Autowired
	private AppRoleDAO appRoleDao;
	@Autowired
	private InstanceRepository instanceRepository;
	@Autowired
	private ChargeRepository chargeRepository;

	@Autowired
	private DesignationDaoImpl designationService;

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserManagementCustom userMangCustom;

	@Autowired
	private CourtTypeRepository courtTypeRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;

	@Autowired
	private TasksPuhRepository taskspuhRepo;

	@Autowired
	private CreateTasksRepository createtasksRepo;

	@Autowired
	private TypeofResponseRepository typeofResponseRepo;

	@Autowired
	private AssignedTasksPuhRepository assignedtaskspuhRepo;
	@Autowired
	private CourtTypeRepository addCourtRepo;
	
	@RequestMapping(value = "/addInstance")
	public String addInstance(ModelMap modelmap) {		
		modelmap.addAttribute("addInstance", new Instance());
		modelmap.addAttribute("Instancelst", instanceRepository.findAll());
		return "userManagement/createInstanse";
	}
	@GetMapping(value = "/adddCharge")
	public String addCharge(ModelMap modelmap) {		
		modelmap.addAttribute("addCharge", new Charge());
		modelmap.addAttribute("chargelst", chargeRepository.findAll());
		return "userManagement/createCharge";
	}
	
	@RequestMapping(value = "/addNewInstance")
	public String addNewInstance(@ModelAttribute("addInstance")  Instance addInstance, BindingResult bindResult, Model model,
			RedirectAttributes attributes) throws Exception {
		
		if(addInstance.getInstanseName().trim()==null || addInstance.getInstanseName().trim().isEmpty()) {
			bindResult.rejectValue("instanseName", "msg.instanseName");
		}
		if (bindResult.hasErrors()) {
			model.addAttribute("addInstance",addInstance);
			model.addAttribute("Instancelst", instanceRepository.findAll());
			return "userManagement/createInstanse";
		}
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if(addInstance.getId()!=null) {
			addInstance.setCreatedBy(userdet);
			addInstance.setCreatedDate(new Date());
			instanceRepository.save(addInstance);
			attributes.addFlashAttribute("message", " New Instance updated Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateInstance"),
					utils.getMessage("log.user.updatedInstance")+" "+"and Instance Id is "+addInstance.getId(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();
		}else {
			addInstance.setCreatedBy(userdet);
			addInstance.setCreatedDate(new Date());
			instanceRepository.save(addInstance);
			attributes.addFlashAttribute("message", " New Instance added Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addInstance"),
					utils.getMessage("log.user.addedInstance")+" "+"and Instance name is "+addInstance.getInstanseName(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();
		}
		
		model.addAttribute("addInstance", new Instance());
		model.addAttribute("Instancelst", instanceRepository.findAll());
		return "redirect:/addInstance";
	}
	@RequestMapping(value = "/addNewCharge")
	public String addNewCharge(@ModelAttribute @Valid Charge addCharge, BindingResult bindResult, Model model,
			RedirectAttributes attributes) throws Exception {
		
		
		if(addCharge.getChargeName().trim()==null || addCharge.getChargeName().trim().isEmpty()) {
			bindResult.rejectValue("chargeName", "msg.chargeName");
		}
			
		if (bindResult.hasErrors()) {
			model.addAttribute("addCharge", addCharge);
			model.addAttribute("chargelst", chargeRepository.findAll());
			return "redirect:/adddCharge";
		}
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if(addCharge.getId()!=null) {
			
			addCharge.setCreatedBy(userdet);
			addCharge.setCreatedDate(new Date());
			chargeRepository.save(addCharge);
			attributes.addFlashAttribute("message", " New Charge updated Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateCharge"),
					utils.getMessage("log.user.updatedCharge")+" "+"and Charge Id is "+addCharge.getId(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();
			
		}else {
			addCharge.setCreatedBy(userdet);
			addCharge.setCreatedDate(new Date());
			chargeRepository.save(addCharge);
			attributes.addFlashAttribute("message", " New Charge added Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addCharge"),
					utils.getMessage("log.user.addedCharge")+" "+"and Charge name is "+addCharge.getChargeName(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();
		}
		model.addAttribute("addCharge", new Charge());
		model.addAttribute("chargelst", chargeRepository.findAll());
		return "redirect:/adddCharge";
	}
	
	@RequestMapping(value = "/editInstance", params = "editinstance")
	public String editInstance(@RequestParam(value = "editinstance", required = true) Long id, Model model) {
		
		model.addAttribute("addInstance", instanceRepository.findById(id).get());
		model.addAttribute("Instancelst", instanceRepository.findAll());
		return "userManagement/createInstanse";
		
	}
	@RequestMapping(value = "/editCharge", params = "editcharge")
	public String editCharge(@RequestParam(value = "editcharge", required = true) Long id, Model model) {
		model.addAttribute("addCharge", chargeRepository.findById(id).get());
		model.addAttribute("chargelst", chargeRepository.findAll());
		return "userManagement/createCharge";
	}
	
	



@RequestMapping(value = "/addStatus")
	public String addStatus(Model model) {
		Status status = new Status();
		List<Status> findAll = addStatusRepo.findAll();
		model.addAttribute("addstaus", status);
		model.addAttribute("liststatus", findAll);
		return "userManagement/addStatus";
	}

	@RequestMapping(value = "/addNewstatus")
	public String addNewstatus(@ModelAttribute("status") Status status, BindingResult bindResult, Model model,
			RedirectAttributes attributes) throws Exception {
		UserValidation userValidation = new UserValidation();
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		userValidation.validateStatus(status, bindResult);
		if (bindResult.hasErrors()) {
			model.addAttribute("liststatus", addStatusRepo.findAll());
			model.addAttribute("addstaus", status);
			
			return "userManagement/addStatus";
		}
		
		if(status.getId()==null)
		{   status.setIsActive(true);
			addStatusRepo.save(status);
		attributes.addFlashAttribute("message", " New Status added Successfully. ");
		}
		else
		{
			addStatusRepo.save(status);
			attributes.addFlashAttribute("message", "  Status updated Successfully. ");
		}
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"User", Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addStatus"),
				utils.getMessage("log.user.addedStatus") + " " + "and Status name is " + status.getStatusName(),
				userdet.getSalutation() + " " + userdet.getFirstName() + " "
						+ (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() + " ")
						+ userdet.getLastName(),
				"true");
		auditBeanBo.save();
		return "redirect:/addStatus";

	}
	
	@GetMapping("/status/activate/{id}")
    public String activate(@PathVariable Long id,
                           RedirectAttributes redirect) {

        Status st = addStatusRepo.findById(id).get();
        st.setIsActive(true);
        addStatusRepo.save(st);

        redirect.addFlashAttribute("message", "Activated Successfully");

        return "redirect:/addStatus";
    }

    // Deactivate
    @GetMapping("/status/deactivate/{id}")
    public String deactivate(@PathVariable Long id,
                             RedirectAttributes redirect) {

        Status st = addStatusRepo.findById(id).get();
        st.setIsActive(false);
        addStatusRepo.save(st);

        redirect.addFlashAttribute("message", "Deactivated Successfully");

        return "redirect:/addStatus";
    }

	@RequestMapping(value = "/addCourt")
	public String addCourt(Model model) {
		model.addAttribute("addCourt", new AddCourt());
		model.addAttribute("courtlst", addCourtRepo.findAll());
		return "userManagement/addCourt";
	}
	
	
	@RequestMapping(value = "/addNewCourt")
	public String addNewCourt(@ModelAttribute @Valid AddCourt addCourt, BindingResult bindResult, Model model,
			RedirectAttributes attributes) throws Exception {
		UserValidation userValidation = new UserValidation();
		userValidation.validateAddCourt(addCourt, bindResult);
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());

		if (bindResult.hasErrors()) {
			model.addAttribute("addCourt", new AddCourt());
			model.addAttribute("courtlst", addCourtRepo.findAll());
			return "userManagement/addCourt";
		}
if(addCourt.getId()==null)
{
		addCourt.setCreatedDate(new Date());
		addCourt.setCreatedBy(userdet);
		addCourtRepo.save(addCourt);
		attributes.addFlashAttribute("message", " New Court added Successfully. ");
}
else
{
	addCourt.setUpdatedDate(new Date());
	addCourt.setCreatedBy(userdet);
	addCourtRepo.save(addCourt);
	attributes.addFlashAttribute("message", "Court updated Successfully. ");
}

		
		
		
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addCourt"),
				utils.getMessage("log.user.addedCourt")+" "+"and Court name is "+addCourt.getCourtName(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
		auditBeanBo.save();
		return "redirect:/addCourt";

	}
	 @RequestMapping(value = "/deactivateCourt", params = "deactivateCourt")
	    public String editCourt(@RequestParam(value = "deactivateCourt", required = true) Long id, RedirectAttributes redirectAttributes) {

		 
		 addCourtRepo.deactivateCourt(id);

	     redirectAttributes.addFlashAttribute("message",
	             "Court deactivated successfully");

	     return  "redirect:/addCourt";
	    }
	 @PostMapping("/courtActivate")
	 public String activateCourt(@RequestParam("id") Long id,
	                              RedirectAttributes redirectAttributes) {

		 addCourtRepo.activateCourt(id);

	     redirectAttributes.addFlashAttribute("message",
	             "Court activated successfully");

	     return "redirect:/addNewCourt";
	 }
	 @PostMapping(value = "/editCourt", params = "editCourt")
	    public String deactivateCourt(@RequestParam(value = "editCourt", required = true) Long id, Model model) {

		 AddCourt court = addCourtRepo.findById(id).get();
	        if (court == null) {
	        	AddCourt dto = new AddCourt();
	            dto.setId(court.getId());
	            dto.setCourtName(court.getCourtName());
	            dto.setCourtType(court.getCourtType());
	            model.addAttribute("addCourt", dto);
	        }
	        else
	        {	          
	            model.addAttribute("addCourt", court);	
	        }

	        model.addAttribute("courtlst", addCourtRepo.findAll());
	        return "userManagement/addCourt";
	    }

	/*
	 * @RequestMapping(value = "/addNewCourt") public String
	 * addNewCourt(@ModelAttribute @Valid AddCourt addCourt, BindingResult
	 * bindResult, Model model, RedirectAttributes attributes) throws Exception {
	 * UserValidation userValidation = new UserValidation();
	 * userValidation.validateAddCourt(addCourt, bindResult); UserDetails userdet =
	 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
	 * ));
	 * 
	 * if (bindResult.hasErrors()) { model.addAttribute("addCourt", new AddCourt());
	 * model.addAttribute("courtlst", addCourtRepo.findAll()); return
	 * "userManagement/addCourt"; }
	 * 
	 * addCourt.setCreatedDate(new Date()); addCourt.setCreatedBy(userdet);
	 * 
	 * addCourtRepo.save(addCourt);
	 * 
	 * attributes.addFlashAttribute("message", " New Court added Successfully. ");
	 * auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails()
	 * .getUserId().toString()), userdet.getSalutation() +" " +
	 * userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" :
	 * userdet.getMiddleName() +" ")+ userdet.getLastName(),
	 * "User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().
	 * toString()), utils.getMessage("log.user.addCourt"),
	 * utils.getMessage("log.user.addedCourt")+" "+"and Court name is "+addCourt.
	 * getCourtName(), userdet.getSalutation() +" " +userdet.getFirstName() + " " +
	 * (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+
	 * userdet.getLastName(), "true"); auditBeanBo.save(); return
	 * "redirect:/addCourt";
	 * 
	 * }
	 */

	@RequestMapping(value = "/typeofResponse")
	public String typeResponse(Model model) {

		TypeofResponse typeResponse1 = new TypeofResponse();

	

		model.addAttribute("typeResponse", typeResponse1);
		model.addAttribute("listResponse", typeofResponseRepo.findAll());
		return "userManagement/TypeofResponse";
	}

	@RequestMapping(value = "/addNewTypeofResponse")
	public String addNewTypeofResponse(@Valid @ModelAttribute  TypeofResponse typeResponse, BindingResult bindResult,
			Model model, RedirectAttributes redirect) throws PromisException, Exception {
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if (bindResult.hasErrors()) {

			model.addAttribute("listResponse", typeofResponseRepo.findAll());
			TypeofResponse typeResponse1 = new TypeofResponse();

			model.addAttribute("massage", "Please enter only charactor");

			model.addAttribute("typeResponse", typeResponse1);

			return "userManagement/TypeofResponse";

			
		}
		typeofResponseRepo.save(typeResponse);
		
		redirect.addFlashAttribute("massage", "Type of Response added successfully");
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addResponseType"),
				utils.getMessage("log.user.addedResponsetype")+" "+"and Type of Response name is "+typeResponse.getResponse(),
				userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
		auditBeanBo.save();
		return "redirect:/typeofResponse";
	}

	@RequestMapping(value = "/addDesignation")
	public String addDesignation(Model model) {

		AddDesignation addDesination1 = new AddDesignation();
		addDesination1.setEditdesgi(false);

		model.addAttribute("addDesignation", addDesination1);
		model.addAttribute("listDesignation", designationRepo.findAll());
		return "userManagement/addDesignation";
	}

	@RequestMapping(value = "/addNewDesiganation")
	public String addNewDesiganation(@ModelAttribute @Valid AddDesignation addDesignation, BindingResult bindResult,
			Model model, RedirectAttributes redirect) throws PromisException, Exception {
		
		if(addDesignation.getDeginationtype().equals("c"))
		{
			bindResult.rejectValue("deginationtype", "msg.dtype");
		}
		if(addDesignation.getDesignation().equals(""))
		{
			bindResult.rejectValue("designation", "msg.dtype");
		}
		if((!addDesignation.getDesignation().equals(""))&&(!addDesignation.getDeginationtype().equals("c")))
		{
			List<AddDesignation> desi = designationRepo.findByDesignationAndDeginationtype(addDesignation.getDesignation(), addDesignation.getDeginationtype());
		if(!desi.isEmpty()) {
			bindResult.rejectValue("designation", "msg.dtype");
		}
		}
		
		if (bindResult.hasErrors()) {
			addDesignation.setEditdesgi(false);
			model.addAttribute("addDesignation", addDesignation);
			model.addAttribute("listDesignation", designationRepo.findAll());

			return "userManagement/addDesignation";
		} else if (addDesignation.getId() != null) {
			addDesignation.setUpdatedDate(new Date());
			addDesignation.setEditdesgi(false);

			designationService.save(addDesignation);
			redirect.addFlashAttribute("message",
					" Designation Updated Successfully as " + addDesignation.getDesignation());

		} else {
			addDesignation.setEditdesgi(false);
			designationService.save(addDesignation);
			System.out.println("error accor");
			redirect.addFlashAttribute("message",
					" New Designation Added Successfully as " + addDesignation.getDesignation());
			UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addDesignation"),
					utils.getMessage("log.user.addedDesignation")+" "+"and Designation name is "+addDesignation.getDesignation(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
			

		}
		return "redirect:/addDesignation";
	}

	/*
	 * @RequestMapping(value = "/getDesignations") public String
	 * getDesignation(Model model) { model.addAttribute("listDesignation",
	 * designationRepo.findAll()); return "userManagement/listDesignation"; }
	 */

	@RequestMapping(value = "getDesignations")
	public String getDesignations(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<AddDesignation> list = designationRepo.findAll(pagable);

		long totalRow = list.getTotalElements();
		int currentRow = 1;
		int lastRow = list.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<AddDesignation> list1 = list.getContent();
		int pageNo1 = list.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", list.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		modelMap.addAttribute("DesignationList", list1);

		return "userManagement/listDesignation";

	}

	@RequestMapping(value = "/getDesignation")
	public String getDesignation(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<AddDesignation> list = designationRepo.findAll(pagable);

		long totalRow = list.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		/* int lastRow = (noOfrecord * pageNo) + list.getSize(); */
		int lastRow = (noOfrecord * pageNo) + list.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<AddDesignation> list1 = list.getContent();
		int pageNo1 = list.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", list.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		modelMap.addAttribute("DesignationList", list1);
		return "userManagement/listDesignation";

	}

	@RequestMapping(value = "/addRole")
	public String addRole(Model model) {
		model.addAttribute("appRole", new AppRole());
		model.addAttribute("rolelist", appRoleRepo.findAll());
		return "userManagement/createRole";
	}

	@RequestMapping(value = "/createNewRole")
	public String createNewRole(@ModelAttribute @Valid AppRole appRole, BindingResult bindResult, Model model,
			RedirectAttributes redirect) throws Exception {

		if (bindResult.hasErrors()) {
			if (null != appRole)
				model.addAttribute("appRole", appRole);
			else
				model.addAttribute("appRole", new AppRole());

			model.addAttribute("rolelist", appRoleRepo.findAll());
			return "userManagement/createRole";
		} else {
			String rolename = "ROLE_" + appRole.getRoleName();
			appRole.setRoleName(rolename);
			AppRole app = appRoleRepo.save(appRole);

		}
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addRole"),
				utils.getMessage("log.user.addedRole")+" "+"and Role name is "+appRole.getRoleName(),
				userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
		auditBeanBo.save();
		redirect.addFlashAttribute("message", "New Role Created SuccessFully !!");
		return "redirect:/addRole";
	}

	/*
	 * @RequestMapping(value = "/getRoles") public String getRole(Model model) {
	 * model.addAttribute("rolelist", appRoleRepo.findAll()); return
	 * "userManagement/listRole"; }
	 */

	@RequestMapping(value = "getRoles")
	public String getRoles(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.ASC, "roleId"));

		/*
		 * Page<proCourtCaseDetails> proCourtCaeDetails =
		 * proCourtCaseDetailsRepo.findALLByApproveStatusBetween(pagable, 0, 6);
		 */
		Page<AppRole> roleDetails = appRoleRepo.findAll(pagable);

		long totalRow = roleDetails.getTotalElements();

		int currentRow = 1;
		int lastRow = roleDetails.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<AppRole> roledtl = roleDetails.getContent();
		int pageNo1 = roleDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", roleDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("rolelist", roledtl);

		return "userManagement/listRole";

	}

	@RequestMapping(value = "/getRoles1")
	public String getRoles1(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.ASC, "roleId"));

		Page<AppRole> roleDetails = appRoleRepo.findAll(pagable);

		long totalRow = roleDetails.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + roleDetails.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<AppRole> roledtl = roleDetails.getContent();
		int pageNo1 = roleDetails.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", roleDetails.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		String userrole = appRoleDao.getRoleName(userDetailsService.getUserDetails().getUserId());

		modelMap.addAttribute("userRole", userrole);
		modelMap.addAttribute("assignedTaskPuh", new AssignedTaskPuh());
		modelMap.addAttribute("rolelist", roledtl);

		return "userManagement/listRole";

	}

	@RequestMapping(value = "/addUser")
	public String addUser(Model model) {
		model.addAttribute("userDetails", new UserDetails());
		List<AddDesignation> designationList = designationRepo.findAll();
		// List<UnitDetails> unitList =
		// unitDetailsRepo.findAll(Sort.by(Sort.Direction.ASC, "unitId"));
		List<UnitDetails> unitList = unitDetailsRepo.findAll();
		if (unitList.isEmpty() || unitList.size() < 0) {
			unitList = null;
		}
		List<AppRole> roleList = appRoleDao.RoleList();
		model.addAttribute("designationList", designationList);
		model.addAttribute("unitList", unitList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("userList", userDetailsRepo.findAll());
		return "userManagement/addUser";
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/addNewUser")
	public String addNewUser(@ModelAttribute @Valid UserDetails userDetails, BindingResult bindResult, Model model,
			RedirectAttributes redirect, HttpServletRequest request) throws Exception {
		UserValidation validation = new UserValidation();
		validation.validateUserRegComplete(userDetails, bindResult, isUniqueUserName(userDetails.getEmail()),
				isUniqueMobile(userDetails.getPrimaryMobile()), isUniqueMobile(userDetails.getAlternateNo()));

		if (userDetails.getDesignationId() == 0L) {
			bindResult.rejectValue("designation", "msg.wrongId");
		} else {
			AddDesignation designationbyId = designationRepo.findById(userDetails.getDesignationId()).get();
			if (designationbyId == null)
				bindResult.rejectValue("designation", "msg.wrongId");
		}
		if (userDetails.getUnitId() != null) {
			UnitDetails unitbyId = unitDetailsRepo.findById(userDetails.getUnitId()).get();
			if (unitbyId == null)
				bindResult.rejectValue("unit", "msg.wrongId");
		}
		if (userDetails.getRoleId() != null) {
			AppRole roleById = appRoleRepo.findById(userDetails.getRoleId()).get();
			if (roleById == null || userDetails.getRoleId() == 1L || userDetails.getRoleId() == 5L)
				bindResult.rejectValue("roleId", "msg.wrongId");
		}

		AppUser user = new AppUser();
		if (bindResult.hasErrors()) {
			if (null != userDetails)
				model.addAttribute("userDetails", userDetails);
			else
				model.addAttribute("userDetails", new UserDetails());
			List<AddDesignation> designationList = designationRepo.findAll();

			if (designationList.isEmpty()) {
				designationList = null;
			}
			// List<UnitDetails> unitList =
			// unitDetailsRepo.findAll(Sort.by(Sort.Direction.ASC, "unitId"));
			// gouthami 15/09/2020
			List<UnitDetails> unitList = unitDetailsRepo.findAll();
			/*
			 * List<UnitDetails> sortedUnitList = null; if(unitList.size()>0) {
			 * sortedUnitList = Collections.sort(unitList); }
			 */
			List<AppRole> roleList = appRoleDao.RoleList();
			model.addAttribute("designationList", designationList);
			if (unitList.size() > 0 || unitList != null) {
				model.addAttribute("unitList", unitList);
			}
			model.addAttribute("roleList", roleList);

			// gouthami 15/09/2020

			List<UserDetails> userdetailList = userDetailsRepo.findAll();

			if (userdetailList.isEmpty() || userdetailList.size() < 0) {
				userdetailList = null;
			}
			model.addAttribute("userList", userdetailList);
			return "userManagement/addUser";
		} else {

			user.setIsApproved(false);
			user.setUserName(userDetails.getEmail());
			user.setEncrytedPassword(
					"8dd48ad39c2e6517b4245e75ceaa1a1ae23b2f4993df5ec588bfba7d196fb837ca213d809b3097b2f29afc27789fad40e23242ee62fe662326ccce6a04209f6b");
			user.setEnabled(1);
			user.setIsApproved(true);
			user.setOldPassword(
					"8dd48ad39c2e6517b4245e75ceaa1a1ae23b2f4993df5ec588bfba7d196fb837ca213d809b3097b2f29afc27789fad40e23242ee62fe662326ccce6a04209f6b"
							+ ProMisConstant.OLD_PASS_SEP);
			user.setPassChanged(false);
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			cal.add(Calendar.MONTH, 1);
			Date nextYear = cal.getTime();

			user.setValidFrom(today);
			user.setValidUpto(nextYear);
			user = appUserRepo.save(user);

			userDetails.setUserId(user);
			userDetails.setDob(userDetails.getUiDob());
			userDetails.setJoiningDate(userDetails.getUiJoiningDate());

			userDetails.setCreatedDate(new Date());
			userDetails.setCreatedBy(userDetailsService.getUserDetails());

			userDetails.setDesignation(new AddDesignation(userDetails.getDesignationId()));
			userDetails.setUnit(new UnitDetails(userDetails.getUnitId()));
			userDetailsRepo.save(userDetails);
			UserRole userRole = new UserRole(user, new AppRole(userDetails.getRoleId()));
			userRoleRepo.save(userRole);

			/*
			 * auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails()
			 * .getUserId().toString()), userDetails.getFirstName() + " " +
			 * userDetails.getMiddleName() + " " + userDetails.getLastName(), "User",
			 * Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
			 * utils.getMessage("log.user.optype"), utils.getMessage("log.user.optdesc"),
			 * userDetailsService.getUserDetails().getUserName(), "true");
			 * auditBeanBo.save();
			 */
			//UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.optype"),
					utils.getMessage("log.user.optdesc")+" "+"and User name is "+userDetails.getFirstName(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
			MailInfo info = new MailInfo();
			info.setEmail(userDetails.getEmail());
		
		}
		redirect.addFlashAttribute("message", "New User Added Successfully with Login Id : " + user.getUserName());
		return "redirect:/addUser";
	}

	/*
	 * @RequestMapping(value = "/getUser") public String getUsers1(@ModelAttribute
	 * PageNoDTO pageDTO, Model model) {
	 * 
	 * int pageNo; if(pageDTO.getPageno()>1) { pageNo = pageDTO.getPageno() - 1; }
	 * else { pageNo=0; }
	 * 
	 * int noOfrecord = 20;
	 * 
	 * Pageable pagable = PageRequest.of(pageNo, noOfrecord);
	 * 
	 * Page<UserDetails> list = userDetailsRepo.findAll(pagable);
	 * 
	 * int pageNo1 = list.getTotalPages();
	 * 
	 * System.out.println(pageNo1);
	 * 
	 * List<UserDetails> list1 = list.getContent();
	 * 
	 * //List<UserDetails> User1List = userDetailsRepo.findAll();
	 * 
	 * model.addAttribute("currentPage", pageNo+1); model.addAttribute("totalPages",
	 * pageNo1); model.addAttribute("totalItems", list.getSize());
	 * 
	 * model.addAttribute("userList", list1); return "userManagement/listUsers";
	 * 
	 * }
	 */

	@RequestMapping(value = "getUsers")
	public String getUsers(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<UserDetails> list = userDetailsRepo.findAll(pagable);
		long totalRow = list.getTotalElements();
		int currentRow = 1;
		int lastRow = list.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<UserDetails> list1 = list.getContent();
		int pageNo1 = list.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", list.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		modelMap.addAttribute("userList", list1);
		return "userManagement/listUsers";

	}

	@RequestMapping(value = "getUser1")
	public String getUser1(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "id"));

		Page<UserDetails> list = userDetailsRepo.findAll(pagable);
		long totalRow = list.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		int lastRow = (noOfrecord * pageNo) + list.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<UserDetails> list1 = list.getContent();
		int pageNo1 = list.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", list.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());
		modelMap.addAttribute("userList", list1);
		return "userManagement/listUsers";

	}

	// Show all cases for director
	@RequestMapping(value = "/showAllCase")
	public String showAllCase(Model model) {
		return "caseDetails/viewAllCase";
	}

	// Show all cases for PUH (director Login)
	@RequestMapping(value = "/showPendingCasePuh")
	public String showPendingCasePuh(Model model) {

		return "caseDetails/viewAllCasesPuh";
	}

//	// Create Task Page List
//		@RequestMapping(value = "/CreateTaskListPuh")
//		public String CreateTaskListPuh(ModelMap modelmap) {
//			
//			
//			
//			List<TasksPuh> taskspuh  = taskspuhRepo.findAll();
//			List<AddCourt> courtType = courtTypeRepo.findAll();	
//			List<UnitDetails> udetails   = unitDetailsRepo.findAll();
//			
//			modelmap.addAttribute("taskPuhList", new TasksPuh());			
//			modelmap.addAttribute("courtType", courtType);
//			modelmap.addAttribute("taskspuh", taskspuh);
//			modelmap.addAttribute("udetails", udetails);		
//			return "caseDetails/createTaskListPuh";
//		}

//		// edit Task List Page
//		@RequestMapping(value = "/editTaskPuh", params = "editingTaskPuh")
//		public String editTaskPuh(@RequestParam(value = "editingTaskPuh", required = true) Long Id, ModelMap modelmap,
//				RedirectAttributes redirect) throws Exception {
//
//			System.out.println("Edit Button Calling ---- ====== >>>>> Hello Abc edit  Button click ");
//			TasksPuh taskPuhList = taskspuhRepo.findById(Id).get();
//			List<AddCourt> courtType = courtTypeRepo.findAll();	
//			List<UnitDetails> udetails   = unitDetailsRepo.findAll();
//			List<TasksPuh> taskspuh  = taskspuhRepo.findAll();
//			
//			modelmap.addAttribute("taskspuh", taskspuh);
//			modelmap.addAttribute("courtType", courtType);
//			modelmap.addAttribute("udetails", udetails);
//			modelmap.addAttribute("taskPuhList", taskPuhList);
//         	return "caseDetails/editTaskPuh";	
//		}

	// Save Assigned Task List
	@RequestMapping(value = "/SaveAssignedTasks")
	public String SaveAssignedTasks(Model model, @ModelAttribute @Valid AssignedTaskPuh assigntaskpuh,
			BindingResult bindResult, RedirectAttributes attributes, HttpServletRequest request) {

//					if(!addactsec.getSection().equalsIgnoreCase(""))
//					{
//						AddActSec addactsec1 = addactsecRepo.findAllBySection(addactsec.getSection());
//						if(addactsec1!=null) {
//							bindResult.rejectValue("Section", "errmsg.uniqueSection");
//						}
//					}	

		assignedtaskspuhRepo.save(assigntaskpuh);
		attributes.addFlashAttribute("message", "New  Successfully.");

		return "redirect:/addActSec";

		// return "userManagement/addActSec";

	}

	// add Task/ Create Task
	@RequestMapping(value = "/addTask")
	public String addTask(ModelMap modelmap) {

		List<CreateTasks> tasklst = createtasksRepo.findAll();
		modelmap.addAttribute("createTasksList", new CreateTasks());
		modelmap.addAttribute("tasklst", tasklst);
		return "userManagement/createTask";
	}

	// added By reena on 25/08/2022
	@RequestMapping(value = "/editTask", params = "editTask")
	public String editTask(@RequestParam(value = "editTask", required = true) Long id, Model model) {
		CreateTasks AddtaskDetails = createtasksRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		AddtaskDetails.setEdittask(true);
		List<CreateTasks> tasklst = createtasksRepo.findAll();

		model.addAttribute("createTasksList", AddtaskDetails);
		model.addAttribute("tasklst", tasklst);

		return "userManagement/createTask";
	}

	// add sub Task/ Create sub Task
	@RequestMapping(value = "/addSubTask")
	public String addSubTask(ModelMap modelmap) {

		List<CreateTasks> tasklst = createtasksRepo.findAll();
		List<AddSubTask> Subtasklst = subTaskRepo.findAll();
		modelmap.addAttribute("addSubTAsk", new AddSubTask());
		modelmap.addAttribute("tasklst", tasklst);
		modelmap.addAttribute("Subtasklst", Subtasklst);
		return "userManagement/createSubTask";
	}

	// Submit Button For Create Task
	@RequestMapping(value = "/createNewTasks")
	public String createNewTasks(@ModelAttribute ("createTasksList") CreateTasks createtasks, BindingResult bindResult, Model model,
			RedirectAttributes redirect) throws Exception {
		
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		
		UserValidation userValidation = new UserValidation();
		//userValidation.validatelocationDetails(locationDetails, bindResult);
		userValidation.validateCreatetasks(createtasks, bindResult);
		
		
		if (bindResult.hasErrors()) {
			if (null != createtasks) {
				model.addAttribute("createTasksList", createtasks);
			}else {
				model.addAttribute("createTasksList", new CreateTasks());
			}
			List<CreateTasks> tasklst = createtasksRepo.findAll();
			
			model.addAttribute("tasklst", tasklst);
			return "userManagement/createTask";
		}
		
		if (createtasks.getId() != null) {
			// CreateTasks taskdtl = createtasksRepo.findById(createtasks.getId()).get();
			redirect.addFlashAttribute("message", "Task updated SuccessFully !!");
			createtasks.setUpdatedDate(new Date());
			createtasksRepo.save(createtasks);
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateTask"),
					utils.getMessage("log.user.updatedTask")+" "+"and Task id is "+createtasks.getId(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
			return "redirect:/addTask";
		} else {
			createtasks.setCreatedDate(new Date());
			createtasksRepo.save(createtasks);
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addTask"),
					utils.getMessage("log.user.addedTask")+" "+"and new Task name is "+createtasks.getTask(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();			
		}
		model.addAttribute("createTasksList", createtasks);
		redirect.addFlashAttribute("message", "New Tasks Created SuccessFully !!");
		return "redirect:/addTask";
	}

	@RequestMapping(value = "/createNewSubTasks")
	public String createNewSubTasks(@ModelAttribute @Valid AddSubTask addSubTAsk, BindingResult bindResult, Model model,
			RedirectAttributes redirect) throws Exception {
		// added By reena on 25/08/2022
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		if (addSubTAsk.getId() != null) {
			// addSubTAsk taskdtl = createtasksRepo.findById(addSubTAsk.getId()).get();
			redirect.addFlashAttribute("message", "Task updated SuccessFully !!");
			addSubTAsk.setUpdatedDate(new Date());
			subTaskRepo.save(addSubTAsk);
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateSubTask"),
					utils.getMessage("log.user.updatedSubTask")+" "+"and Sub-Task id is "+addSubTAsk.getId(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
			return "redirect:/addSubTask";
		} else {
			addSubTAsk.setCreatedDate(new Date());

			/* createtasks.setUpdatedDate(new Date()); */
			subTaskRepo.save(addSubTAsk);
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addSubTask"),
					utils.getMessage("log.user.addedSubTask")+" "+"and new Sub-Task name is "+addSubTAsk.getSubTaskName(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();			
		}
		model.addAttribute("addSubTAsk", new AddSubTask());
		redirect.addFlashAttribute("message", "New Tasks Created SuccessFully !!");
		return "redirect:/addSubTask";
	}

	// added By reena on 25/08/2022
	@RequestMapping(value = "/editSubTask", params = "editSub")
	public String subTask(@RequestParam(value = "editSub", required = true) Long id, Model model) {
		AddSubTask subTaskDetails = subTaskRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		subTaskDetails.setEditsubtask(true);
		List<CreateTasks> tasklst = createtasksRepo.findAll();
		List<AddSubTask> Subtasklst = subTaskRepo.findAll();
		model.addAttribute("Subtasklst", Subtasklst);
		model.addAttribute("addSubTAsk", subTaskDetails);
		model.addAttribute("tasklst", tasklst);

		return "userManagement/createSubTask";
	}

	// create Task for Puh (PUH Login)
//		@RequestMapping(value = "/createTaskForPuh")
//		public String createTaskForPuh(ModelMap modelmap) {
//			
//			List<AddCourt> courtType = courtTypeRepo.findAll();	
//			List<TasksPuh> taskspuh  = taskspuhRepo.findAll();
//			List<UnitDetails> udetails   = unitDetailsRepo.findAll();
//			modelmap.addAttribute("courtType", courtType);
//			modelmap.addAttribute("taskspuh", taskspuh);
//			modelmap.addAttribute("udetails", udetails);
//			
//			
//			return "caseDetails/createTaskPuh";
//		}	

	// Assign Task for Puh (PUH Login)
//		@RequestMapping(value = "/assignTaskForPuh")
//		public String assignTaskForPuh(ModelMap modelmap) {
//			
//			
//			List<AddCourt> courtType = courtTypeRepo.findAll();	
//			List<TasksPuh> taskspuh  = taskspuhRepo.findAll();
//			List<UnitDetails> udetails   = unitDetailsRepo.findAll();
//			modelmap.addAttribute("courtType", courtType);
//			modelmap.addAttribute("taskspuh", taskspuh);
//			modelmap.addAttribute("udetails", udetails);
//			
//			
//			return "caseDetails/assignTaskPuh";
//		}

	// Pending Task for Puh (PUH Login)
	@RequestMapping(value = "/pendingTaskForPuh")
	public String pendingTaskForPuh(Model model) {
		model.addAttribute("listDesignation", designationRepo.findAll());
		return "caseDetails/pendingTaskPuh";
	}

	@RequestMapping(value = "/getUniqueEmail", method = RequestMethod.POST)
	public boolean isUniqueUserName(@RequestParam("email") String email) throws Exception {
		AppUser user = appUserDAO.findUserAccount(email);
		return (user == null);
	}

	@RequestMapping(value = "/getUniqueMobile", method = RequestMethod.POST)
	public boolean isUniqueMobile(@RequestParam("mobile") String mobile) throws Exception {
		UserDetails user = userMangCustom.findUserDetailsMobile(mobile);
		return (user == null);
	}

	@RequestMapping(value = "/addUnit")
	public String addUnit(Model model) {
		List<UnitDetails> unitList = unitDetailsRepo.findAll();
		if (unitList.isEmpty()) {
			unitList = null;
		}
		UnitDetails unitDetails = new UnitDetails();
		unitDetails.setEditUnit(false);
		model.addAttribute("unitDetails", unitDetails);
		model.addAttribute("unitList", unitList);
		model.addAttribute("listunitloc", addlocRepo.findAll());
		return "userManagement/addUnitDetails";

	}

	@RequestMapping(value = "/addNewUnit")
	public String addNewUnit(@ModelAttribute @Valid UnitDetails unitDetails, BindingResult bindResult, Model model,
			RedirectAttributes attributes) throws Exception {
		UserValidation userValidation = new UserValidation();
		userValidation.validateUnitDetails(unitDetails, bindResult);

		if (bindResult.hasErrors()) {
			if (null != unitDetails)

			{
				unitDetails.setEditUnit(false);

				model.addAttribute("unitDetails", unitDetails);
			}

			else
				model.addAttribute("unitDetails", unitDetails);
			model.addAttribute("listunitloc", addlocRepo.findAll());

			model.addAttribute("unitList", unitDetailsRepo.findAll());
			return "userManagement/addUnitDetails";
		}

		else if (unitDetails.getUnitId() != null) {

			unitDetails.setEditUnit(false);
			unitDetails.setUpdatedDate(new Date());

			unitDetails = unitDetailsRepo.save(unitDetails);
			attributes.addFlashAttribute("message",
					" New Unit " + unitDetails.getUnitName() + " Update  Successfully. ");

		} else {
			unitDetails.setEditUnit(false);
			unitDetails.setCreatedDate(new Date());
			// unitDetails.setCreatedBy(new AppUser((long) 1));
			// unitDetails.setLocation(unitDetails.getLocation());
			unitDetails = unitDetailsRepo.save(unitDetails);
			attributes.addFlashAttribute("message", " New Unit " + unitDetails.getUnitName() + " added Successfully. ");
		}
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.addUnit"),
				utils.getMessage("log.user.addedUnit")+" "+"and Unit name is "+unitDetails.getUnitName(),
				userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
		auditBeanBo.save();
		return "redirect:/addUnit";
	}

	@RequestMapping(value = "/editUnit", params = "editunit")
	public String editUnit(@RequestParam(value = "editunit", required = true) Long id, Model model) {
		UnitDetails unitDetails = unitDetailsRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		unitDetails.setEditUnit(true);
		List<UnitDetails> unitList = unitDetailsRepo.findAll(Sort.by(Sort.Direction.ASC, "unitId"));
		List<AppRole> roleList = appRoleDao.RoleList();

		model.addAttribute("unitList", unitList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("unitDetails", unitDetails);
		model.addAttribute("listunitloc", addlocRepo.findAll());
		return "userManagement/addUnitDetails";
	}

	@RequestMapping(value = "getUnits")
	public String getUnits(ModelMap modelMap) throws Exception {

		int pageNo = 0;
		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "unitId"));

		Page<UnitDetails> unitList = unitDetailsRepo.findAll(pagable);

		long totalRow = unitList.getTotalElements();
		int currentRow = 1;
		int lastRow = unitList.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<UnitDetails> list1 = unitList.getContent();
		int pageNo1 = unitList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", unitList.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		modelMap.addAttribute("unitList", list1);
		return "userManagement/listUnits";

	}

	@RequestMapping(value = "/getUnit")
	public String getUnit(@ModelAttribute PageNoDTO pageDTO, ModelMap modelMap) throws Exception {

		int pageNo;
		if (pageDTO.getPageno() > 1) {
			pageNo = pageDTO.getPageno() - 1;
		} else {
			pageNo = 0;
		}

		int noOfrecord = 20;

		Pageable pagable = PageRequest.of(pageNo, noOfrecord, Sort.by(Sort.Direction.DESC, "unitId"));

		Page<UnitDetails> unitList = unitDetailsRepo.findAll(pagable);

		long totalRow = unitList.getTotalElements();
		int currentRow = (noOfrecord * pageNo) + 1;
		/* int lastRow = (noOfrecord * pageNo) + list.getSize(); */
		int lastRow = (noOfrecord * pageNo) + unitList.getNumberOfElements();

		modelMap.addAttribute("totalRow", totalRow);
		modelMap.addAttribute("currentRow", currentRow);
		modelMap.addAttribute("lastRow", lastRow);

		List<UnitDetails> list1 = unitList.getContent();
		int pageNo1 = unitList.getTotalPages();
		modelMap.addAttribute("currentPage", pageNo + 1);
		modelMap.addAttribute("totalPages", pageNo1);
		modelMap.addAttribute("totalItems", unitList.getNumberOfElements());
		modelMap.addAttribute("pageNoDTO", new PageNoDTO());

		modelMap.addAttribute("unitList", list1);
		return "userManagement/listUnits";

	}

	/*
	 * @RequestMapping(value = "/getUnits") public String getUnits(Model model) {
	 * 
	 * // gouthami 15/09/2020 List<UnitDetails> unitList =
	 * unitDetailsRepo.findAll(); if (unitList.size() > 0 || !unitList.isEmpty()) {
	 * model.addAttribute("unitList", unitDetailsRepo.findAll()); return
	 * "userManagement/listUnits"; } else { model.addAttribute("unitList",
	 * unitDetailsRepo.findAll()); return "userManagement/listUnits"; } }
	 */

	// change Password
	@RequestMapping(value = "/changePassword")
	public String changePassword(ModelMap modelMap) throws PromisException, Exception {
		AppUser user = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		UserForm userForm = new UserForm();
		userForm.setId(user.getUserId());
		modelMap.addAttribute("userForm", userForm);
		modelMap.addAttribute("key", Crypt.encodeKey("mustbe16byteskey"));
		return "changePassword";
	}

	@RequestMapping(value = "/passwordCheck", method = RequestMethod.POST)
	public @ResponseBody String passwordCheck(@RequestParam("current_password") String password,
			@RequestParam("key") String key) {

		String dbpass = "";
		String base64EncodedKey = key;
		// String decryptPass=Crypt.decrypt(password, base64EncodedKey);
		try {
			AppUser user = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
			dbpass = user.getEncrytedPassword();

		} catch (Exception er) {

		}
		return (password.equals(dbpass) + "");
	}

	/*
	 * @RequestMapping(value = "/passwordCheck", method = RequestMethod.POST)
	 * public @ResponseBody Boolean passwordCheck(@RequestParam("current_password")
	 * String password) {
	 * 
	 * String dbpass="";
	 * 
	 * String base64EncodedKey=Crypt.encodeKey("mustbe16byteskey"); String
	 * decryptPass=Crypt.decrypt(password, base64EncodedKey);
	 * 
	 * try { AppUser user =
	 * appUserDAO.findUserAccount(userDetailsService.getLoginUserName()); dbpass =
	 * user.getEncrytedPassword();
	 * 
	 * } catch (Exception er) {
	 * 
	 * } if(password==dbpass) return true; return false;
	 */

	@SuppressWarnings("unused")
	@RequestMapping(value = "/saveChangePassword", method = RequestMethod.POST)
	public String resetPassword(@ModelAttribute @Valid UserForm userForm, HttpServletRequest req,
			HttpServletResponse resp, ModelMap modelMap, BindingResult result, RedirectAttributes attributes)
			throws PromisException, Exception {

		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		if (null == userForm || userForm.equals(0)) {
			return "changePassword";
		}
		AppUser passChangeUser = appUserRepo.findById(userForm.getId()).get();

		/*
		 * ChangePasswordValidator chgPassword = new ChangePasswordValidator();
		 * chgPassword.userValidatePassword(userForm, false, 1, result);
		 */
		if (result.hasErrors()) {
			if (null != userForm)
				modelMap.addAttribute("userForm", userForm);
			else
				modelMap.addAttribute("userForm", new UserForm());

			// userForm.setId(loginUser.getUserId());
			if (Crypt.encodeKey("mustbe16byteskey") != null) {
				modelMap.addAttribute("key", Crypt.encodeKey("mustbe16byteskey"));
			}
			return "changePassword";
		}

		String base64EncodedKey = Crypt.encodeKey("mustbe16byteskey");

		// Check if same password is being used
		if (loginUser.getUserId() == passChangeUser.getUserId()) {
			String pass = loginUser.getEncrytedPassword();

			String decryptCurrentPass = userForm.getPassword().trim();

			// Check if password is authenticated
			if (!decryptCurrentPass.equals(pass)) {
				modelMap.addAttribute("unknownUser", utils.getMessage("msg.wrongauthentication"));
				userForm.setId(loginUser.getUserId());
				modelMap.addAttribute("key", base64EncodedKey);
				return "changePassword";
			}
			String decryptNewPass = userForm.getChangePassword();
			// Using same old password
			if (null != decryptNewPass) {
				if (decryptNewPass == pass) {
					modelMap.addAttribute("unknownUser", utils.getMessage("msg.samepassdeny"));
					userForm.setId(loginUser.getUserId());
					if (null != userForm)
						modelMap.addAttribute("userForm", userForm);
					else
						modelMap.addAttribute("userForm", new UserForm());

					modelMap.addAttribute("key", base64EncodedKey);
					return "changePassword";
				}
			} else {
				modelMap.addAttribute("unknownUser", utils.getMessage("msg.wrongauthentication"));
				userForm.setId(loginUser.getUserId());
				modelMap.addAttribute("key", base64EncodedKey);
				return "changePassword";
			}

			// check from db store last 3 old password
			String finalOldPass = utils.getResetOldPass(pass, decryptCurrentPass, passwordEncoder);
			if (finalOldPass.equals(loginUser.getOldPassword())) {
				modelMap.addAttribute("unknownUser", utils.getMessage("msg.usedoldpass"));
				userForm.setId(loginUser.getUserId());
				if (null != userForm)
					modelMap.addAttribute("userForm", userForm);
				else
					modelMap.addAttribute("userForm", new UserForm());

				modelMap.addAttribute("key", base64EncodedKey);
				return "changePassword";
			}
			
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			
			cal.add(Calendar.MONTH, 9);

			Date nextYear = cal.getTime();
			
			passChangeUser.setValidUpto(nextYear);
			
			
			passChangeUser.setOldPassword(finalOldPass);
			passChangeUser.setEncrytedPassword(decryptNewPass);
			passChangeUser.setPassChanged(true);
			appUserRepo.save(passChangeUser);
			
			UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.passwordUpdate"),
					utils.getMessage("log.user.passwordUpdate")+" "+"and User Name is "+userdet.getFirstName(),
					userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
			auditBeanBo.save();
			
			
			attributes.addFlashAttribute("message", "Password updated successfully!" + loginUser.getUserName());
			return "redirect:/login";

		} else {
			modelMap.addAttribute("unknownUser", utils.getMessage("msg.unauthorized"));
			userForm.setId(loginUser.getUserId());
			if (null != userForm)
				modelMap.addAttribute("userForm", userForm);
			else
				modelMap.addAttribute("userForm", new UserForm());

			modelMap.addAttribute("key", base64EncodedKey);
			return "changePassword";
		}

	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/saveChangePassword1", method = RequestMethod.POST)
	public String NewresetPassword(@ModelAttribute @Valid UserForm userForm, HttpServletRequest req,
			HttpServletResponse resp, ModelMap modelMap, BindingResult result, RedirectAttributes attributes)
			throws PromisException, Exception {

		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		if (null == userForm || userForm.equals(0)) {
			return "changePassword";
		}
		AppUser passChangeUser = appUserRepo.findById(userForm.getId()).get();

		if (result.hasErrors()) {
			if (null != userForm)
				modelMap.addAttribute("userForm", userForm);
			else
				modelMap.addAttribute("userForm", new UserForm());

			// userForm.setId(loginUser.getUserId());
			if (Crypt.encodeKey("mustbe16byteskey") != null) {
				modelMap.addAttribute("key", Crypt.encodeKey("mustbe16byteskey"));
			}
			return "NewUserChangedPass";
		}

		String base64EncodedKey = Crypt.encodeKey("mustbe16byteskey");

		// Check if same password is being used
		if (loginUser.getUserId() == passChangeUser.getUserId()) {
			String pass = loginUser.getEncrytedPassword();

			String decryptCurrentPass = Crypt.decrypt(userForm.getPassword(), base64EncodedKey);
			// Check if password is authenticated
			if (!passwordEncoder.matches(decryptCurrentPass, pass)) {
				modelMap.addAttribute("unknownUser", utils.getMessage("msg.wrongauthentication"));
				userForm.setId(loginUser.getUserId());
				modelMap.addAttribute("key", base64EncodedKey);
				return "NewUserChangedPass";
			}
			String decryptNewPass = userForm.getChangePassword();

			// Using same old password
			if (null != decryptNewPass) {
				if (passwordEncoder.matches(decryptNewPass, pass)) {
					modelMap.addAttribute("unknownUser", utils.getMessage("msg.samepassdeny"));
					userForm.setId(loginUser.getUserId());
					if (null != userForm)
						modelMap.addAttribute("userForm", userForm);
					else
						modelMap.addAttribute("userForm", new UserForm());

					modelMap.addAttribute("key", base64EncodedKey);
					return "NewUserChangedPass";
				}
			} else {
				modelMap.addAttribute("unknownUser", utils.getMessage("msg.wrongauthentication"));
				userForm.setId(loginUser.getUserId());
				modelMap.addAttribute("key", base64EncodedKey);
				return "NewUserChangedPass";
			}

			// check from db store last 3 old password
			String finalOldPass = utils.getUpdatedOldPass(loginUser.getOldPassword(), decryptNewPass);
			if (finalOldPass.equals(loginUser.getOldPassword())) {
				modelMap.addAttribute("unknownUser", utils.getMessage("msg.usedoldpass"));
				userForm.setId(loginUser.getUserId());
				if (null != userForm)
					modelMap.addAttribute("userForm", userForm);
				else
					modelMap.addAttribute("userForm", new UserForm());

				modelMap.addAttribute("key", base64EncodedKey);
				return "NewUserChangedPass";
			}
			passChangeUser.setOldPassword(finalOldPass);
			passChangeUser.setEncrytedPassword(passwordEncoder.encode(decryptNewPass));
			passChangeUser.setPassChanged(true);
			appUserRepo.save(passChangeUser);
			req.getSession().setAttribute("menu", passChangeUser.getPassChanged());
			modelMap.addAttribute("NewPassChange",
					"Password updated successfully!  " + loginUser.getUserName() + "  Click Below to Login again ");
			// attributes.addFlashAttribute("NewPassChange", "Password updated
			// successfully!"+ loginUser.getUserName() + " Click here to Login again ");
			return "NewPassSuccess";
		} else {
			modelMap.addAttribute("unknownUser", utils.getMessage("msg.unauthorized"));
			userForm.setId(loginUser.getUserId());
			if (null != userForm)
				modelMap.addAttribute("userForm", userForm);
			else
				modelMap.addAttribute("userForm", new UserForm());

			modelMap.addAttribute("key", base64EncodedKey);
			return "NewUserChangedPass";
		}

	}
	/*
	 * @RequestMapping(value = "/financeYear") public String financeYear(Model
	 * model) { model.addAttribute("financeDetails", new FinancialMaster());
	 * 
	 * model.addAttribute("fincanceList",financialYearRepo.findAll()); return
	 * "userManagement/addFinanceDetails"; }
	 * 
	 * @RequestMapping(value = "/addfinanceYear") public String
	 * addfinanceYear(@ModelAttribute @Valid FinancialMaster financialDetails,
	 * BindingResult bindResult, Model model, RedirectAttributes attributes) {
	 * UserValidation userValidation = new UserValidation();
	 * userValidation.validateFinanceDetails(financialDetails, bindResult); boolean
	 * isUniqueFinancialYear =
	 * officeOrderDao.isUniqueFinYear(financialDetails.getFromY(),financialDetails.
	 * getToY()); if(!isUniqueFinancialYear) bindResult.rejectValue("fromY",
	 * "errmsg.finYearExist"); if (bindResult.hasErrors()) {
	 * model.addAttribute("financeDetails",financialDetails);
	 * model.addAttribute("fincanceList",financialYearRepo.findAll()); return
	 * "userManagement/addFinanceDetails"; } // if(financialDetails.isResetDin()){
	 * financialYearRepo.resetOfficeOrderDinSeq();
	 * financialYearRepo.resetNoticeDinSeq(); financialYearRepo.resetSummonDinSeq();
	 * // } financialYearRepo.deactivateFinancialYear();
	 * financialDetails.setCreatedDate(new Date());
	 * financialDetails.setCreatedBy(new AppUser(1L));
	 * financialDetails.setActive(true); financialDetails =
	 * financialYearRepo.save(financialDetails);
	 * 
	 * attributes.addFlashAttribute("message", "Financial Year "+
	 * financialDetails.getFromY()+"-"+
	 * financialDetails.getToY()+" Added successfully"); return
	 * "redirect:/financeYear"; }
	 */

	@RequestMapping("/forgotPass")
	public String forgotPassword(ModelMap modelMap) throws Exception {
		modelMap.addAttribute("userForm", new UserForm());
		return "ForgotPassword";
	}

	@RequestMapping("/NewUserChangedPass")
	public String NewUserChangedPass(ModelMap modelMap) throws Exception {
		AppUser user = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		UserForm userForm = new UserForm();
		userForm.setPassword(user.getEncrytedPassword());
		userForm.setId(user.getUserId());
		modelMap.addAttribute("userForm", userForm);
		modelMap.addAttribute("key", Crypt.encodeKey("mustbe16byteskey"));
		return "NewUserChangedPass";
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/getForgotOtp", method = RequestMethod.POST)
	public String getEmailOtp(@ModelAttribute @Valid UserForm userForm, BindingResult result, ModelMap model,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (null == userForm || userForm.equals(0)) {
			return "ForgotPassword";
		}
		MailInfo mailInfo = new MailInfo();

		ProMISValidator sfioVal = new ProMISValidator();
		/**/

		if (result.hasErrors()) {
			userForm.setCaptcha("");
			if (null != userForm)
				model.addAttribute("userForm", userForm);
			else
				model.addAttribute("userForm", new UserForm());

			return "ForgotPassword";
		}

		if (isUniqueUserName(userForm.getUsername())) {
			model.addAttribute("usernameInvalid", "This email address hasn't registered.");
			model.addAttribute("userForm", new UserForm());
			return "ForgotPassword";
		}

		if (userForm.getUsername() != null || !"".equals(userForm.getUsername())) {
			String otp = utils.getOTP();

			userDetailsService.getCurrentSession().setAttribute("otp", otp);
			String generateDate = utils.getCurrentDate();
			userDetailsService.getCurrentSession().setAttribute("generateostpDate", generateDate);
			mailInfo.setOtp(otp);
			// userForm.setOtpGenerated(true);
			mailInfo.setEmail(userForm.getUsername());
			// mailBo.sendMail(userInfo, NfraConstant.EMAIL_VARIFY_OTP, req);
			model.addAttribute("sendForgotemailmsg", "The OTP has been sent to your registered email.");
		} else {
			model.addAttribute("invalidemail", "Please Enter valid email address");
			model.addAttribute("userForm", new UserForm());
			return "ForgotPassword";
		}
		model.addAttribute("userForm", userForm);
		return "ForgotPassword";
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String forgotPassProcess(@ModelAttribute UserForm userForm, BindingResult result, ModelMap model,
			HttpServletRequest req, HttpServletResponse resp) throws PromisException, Exception {
		if (null == userForm || userForm.equals(0)) {
			return "ForgotPassword";
		}
		if (result.hasErrors()) {
			if (null != userForm)
				model.addAttribute("userForm", userForm);
			else
				model.addAttribute("userForm", new UserForm());

			return "ForgotPassword";
		}

		SimpleDateFormat format = new SimpleDateFormat(ProMisConstant.DATE_FORMAT);
		Date parseDate = format.parse((String) userDetailsService.getCurrentSession().getAttribute("generateostpDate"));
		Calendar gc = new GregorianCalendar();
		gc.setTime(parseDate);
		gc.add(Calendar.MINUTE, ProMisConstant.OTP_EXPIRE_TIME);
		Date expiryDate = gc.getTime();

		MailInfo mailInfo = new MailInfo();

		if (expiryDate.after(new Date())) {
			if (userForm.getTextOTP().equals(userDetailsService.getCurrentSession().getAttribute("otp"))) {

				mailInfo.setEmail(userForm.getUsername());
				// mailBo.sendMail(userInfo, NfraConstant.SUCCESS_EMAIL_VARIFIED, req);
				userDetailsService.getCurrentSession().removeAttribute("otp");
				model.addAttribute("userForm", userForm);
				model.addAttribute("key", Crypt.encodeKey("mustbe16byteskey"));
				return "ForgotPasswordProcess";

			} else {
				model.addAttribute("validOtp", "Enter OTP is not valid");
				// userForm.setOtpGenerated(true);
				model.addAttribute("userForm", userForm);
				userForm.setTextOTP("");
				return "ForgotPassword";
			}
		} else {
			model.addAttribute("otpExpired", "Enter OTP has expired");
			model.addAttribute("userForm", new UserForm());
			userDetailsService.getCurrentSession().removeAttribute("otp");
			return "ForgotPassword";
		}

	}

	@RequestMapping(value = "/saveForgotPass", method = RequestMethod.POST)
	public String saveForgotPassword(@ModelAttribute @Valid UserForm userForm, BindingResult result, ModelMap model)
			throws Exception {
		ChangePasswordValidator chgPassword = new ChangePasswordValidator();
		chgPassword.userValidatePassword(userForm, false, 2, result);
		if (result.hasErrors()) {
			if (null != userForm || !userForm.equals(0))
				model.addAttribute("userForm", userForm);
			else
				model.addAttribute("userForm", new UserForm());

			return "ForgotPasswordProcess";
		}

		AppUser objUser = appUserDAO.findUserAccount(userForm.getUsername());

		String base64EncodedKey = Crypt.encodeKey("mustbe16byteskey");
		String decryptPass = Crypt.decrypt(userForm.getChangePassword(), base64EncodedKey);

		// check from db store last 3 old password
		String finalOldPass = utils.getUpdatedOldPass(objUser.getOldPassword(), decryptPass);
		if (finalOldPass.equals(objUser.getOldPassword())) {
			model.addAttribute("unknownUser", utils.getMessage("msg.usedoldpass"));
			model.addAttribute("key", base64EncodedKey);
			model.addAttribute("userForm", userForm);
			return "ForgotPasswordProcess";
		}

		String loginUName = appUserDAO.findUserDetails(getUserDetails()).getFirstName() != null
				? appUserDAO.findUserDetails(getUserDetails()).getFirstName()
				: "" + appUserDAO.findUserDetails(getUserDetails()).getMiddleName() != null
						? appUserDAO.findUserDetails(getUserDetails()).getMiddleName()
						: "" + appUserDAO.findUserDetails(getUserDetails()).getLastName() != null
								? appUserDAO.findUserDetails(getUserDetails()).getLastName()
								: "";
		objUser.setEncrytedPassword(passwordEncoder.encode(decryptPass));
		objUser.setOldPassword(finalOldPass);
		appUserRepo.save(objUser);
		model.addAttribute("successForgotmsg",
				"Your Password are successfully changed and sent notification on your registered mail.");
		auditBeanBo.setAuditBean(objUser.getUserId().intValue(), loginUName, "User", objUser.getUserId().intValue(),
				utils.getMessage("log.forgot.pass"), utils.getMessage("log.pass.updated"), loginUName, "true");
		auditBeanBo.save();
		return "redirect:/login";
	}

	public String getLoginUserName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private AppUser getUserDetails() {
		AppUser appUser = this.appUserDAO.findUserAccount(getLoginUserName());
		return appUser;
	}

	@RequestMapping(value = "/editUser", params = "edituser")
	public String editUser(@RequestParam(value = "edituser", required = true) Long id, Model model,RedirectAttributes redirect) {
		UserDetails user = null;
		try {
			
			user= userDetailsRepo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			// TODO: handle exception
			redirect.addFlashAttribute("message", e.getMessage());
			
			return "redirect:/getUsers";
			
		}
		

		
		UserRole userRole = appUserDAO.getRoleId(user.getUserId().getUserId());

		

		List<AddDesignation> designationList = designationRepo.findAll();
		List<UnitDetails> unitList = unitDetailsRepo.findAll(Sort.by(Sort.Direction.ASC, "unitId"));
		List<AppRole> roleList = appRoleDao.RoleList();
		model.addAttribute("designationList", designationList);
		model.addAttribute("unitList", unitList);
		model.addAttribute("roleList", roleList);

		model.addAttribute("userDetails", user);
		return "userManagement/updateUser";
	}

	@RequestMapping(value = "/saveUpdateUser")
	public String updateUser(@ModelAttribute @Valid UserDetails userDetails, BindingResult bindResult, Model model,
			RedirectAttributes redirect, HttpServletRequest request) throws Exception {
		UserValidation validation = new UserValidation();

		validation.validateUserRegComplete(userDetails, bindResult,
				isUniqueUserNameupdate(userDetails.getEmail(), userDetails.getUserId().getUserId()),
				isUniqueMobile_update(userDetails.getPrimaryMobile(), userDetails.getId()),
				isUniqueMobile_update(userDetails.getAlternateNo(), userDetails.getId()));

		if (userDetails.getDesignationId() == 0L) {
			bindResult.rejectValue("designation", "msg.wrongId");
		} else {
			AddDesignation designationbyId = designationRepo.findById(userDetails.getDesignationId()).get();
			if (designationbyId == null)
				bindResult.rejectValue("designation", "msg.wrongId");
		}
		if (userDetails.getUnitId() != null) {
			UnitDetails unitbyId = unitDetailsRepo.findById(userDetails.getUnitId()).get();
			if (unitbyId == null)
				bindResult.rejectValue("unit", "msg.wrongId");
		}
		if (userDetails.getRoleId() != null) {
			AppRole roleById = appRoleRepo.findById(userDetails.getRoleId()).get();
			if (roleById == null || userDetails.getRoleId() == 1L || userDetails.getRoleId() == 4L)
				bindResult.rejectValue("roleId", "msg.wrongId");
		}

		// AppUser user = new AppUser();
		if (bindResult.hasErrors()) {

			if (null != userDetails)
				model.addAttribute("userDetails", userDetails);
			else
				model.addAttribute("userForm", new UserDetails());

			List<AddDesignation> designationList = designationRepo.findAll();
			List<UnitDetails> unitList = unitDetailsRepo.findAll(Sort.by(Sort.Direction.ASC, "unitId"));
			List<AppRole> roleList = appRoleDao.RoleList();
			model.addAttribute("designationList", designationList);
			model.addAttribute("unitList", unitList);
			model.addAttribute("roleList", roleList);
			model.addAttribute("userList", userDetailsRepo.findAll());

			return "userManagement/updateUser";
		} else {

			Optional<AppUser> appuserobj = appUserRepo.findByUserId(userDetails.getUserId().getUserId());
			AppUser user = appuserobj.get();
			/*
			 * user.setUserName(userDetails.getEmail()); user.setEncrytedPassword(
			 * "$2a$10$RpjdPpApu2Gk7uUJfj1jpuWpbGxCJ9yKt1ea5dJHZaO0vJBDxenTW");
			 * user.setEnabled(1); user.setOldPassword(
			 * "$2a$10$RpjdPpApu2Gk7uUJfj1jpuWpbGxCJ9yKt1ea5dJHZaO0vJBDxenTW"+SnmsConstant.
			 * OLD_PASS_SEP);
			 */
			user.setUserName(userDetails.getEmail());
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			cal.add(Calendar.YEAR, 2); // to get previous year add -1
			Date nextYear = cal.getTime();

			// user.setValidFrom(today);
			// user.setValidUpto(nextYear);
			appUserRepo.save(user);

			userDetails.setUserId(user);
			userDetails.setDob(userDetails.getUiDob());
			userDetails.setJoiningDate(userDetails.getUiJoiningDate());

			userDetails.setCreatedDate(new Date());
			userDetails.setCreatedBy(userDetailsService.getUserDetails());

			userDetails.setDesignation(new AddDesignation(userDetails.getDesignationId()));
			userDetails.setUnit(new UnitDetails(userDetails.getUnitId()));
			userDetailsRepo.save(userDetails);

			UserRole userRole = appUserDAO.getRoleId(userDetails.getUserId().getUserId());
			// UserRole userRole = userroleobj.get();
			UserRole userRolenew = new UserRole();
			userRolenew.setId(userRole.getId());
			userRolenew.setAppRole(new AppRole(userDetails.getRoleId()));
			userRolenew.setAppUser(user);
			// UserRole userRole = new UserRole(user,new AppRole(userDetails.getRoleId()));
			userRoleRepo.save(userRolenew);

			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.edit"),
					utils.getMessage("log.user.edited")+" "+"and User name is "+userDetails.getFirstName(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
			/*
			 * MailInfo info=new MailInfo(); info.setEmail(userDetails.getEmail());
			 * mailBo.sendMail(info, SnmsConstant.USER_CREATE, request);
			 */

		}
		redirect.addFlashAttribute("message", " User updated Successfully with Login Id : " + userDetails.getEmail());
		return "redirect:/getUsers";
	}

	@RequestMapping(value = "/getUniqueEmailupdate", method = RequestMethod.POST)
	public boolean isUniqueUserNameupdate(@RequestParam("email") String email, Long userid_toupdate) throws Exception {
		AppUser user = appUserDAO.findUserAccount(email);
		if (user == null)
			return true;
		else if (user.getUserId() == userid_toupdate) {
			return true;
		} else
			return false;
	}

	@RequestMapping(value = "/getUniqueMobile_update", method = RequestMethod.POST)
	public boolean isUniqueMobile_update(@RequestParam("mobile") String mobile, Long userid_toupdate) throws Exception {
		UserDetails user = userMangCustom.findUserDetailsMobile(mobile);
		if (user == null)
			return true;
		else if (user.getId() == userid_toupdate) {
			return true;
		} else
			return false;

	}

	@RequestMapping(value = "/editUser", params = "resetPass")
	public String resetPass(@RequestParam(value = "resetPass", required = true) Long id, Model model) throws Exception, Exception {

		ProMISValidator snmsValidator = new ProMISValidator();

//		UserDetails user = userDetailsRepo.findById(id)
		// .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		// String finalOldPass =
		// utils.getUpdatedOldPass(loginUser.getOldPassword(),decryptNewPass,passwordEncoder);

		if (!snmsValidator.getValidInteger(id)) {
			List<UserDetails> userList = userDetailsRepo.findAll();
			model.addAttribute("userList", userList);
			model.addAttribute("message", "invalid userId");
			return "userManagement/listUsers";
		}

		UserDetails user = userDetailsRepo.findById(id).get();

		AppUser appUser = appUserRepo.findById(id).get();

		if (appUser == null) {
			List<UserDetails> userList = userDetailsRepo.findAll();
			model.addAttribute("userList", userList);
			model.addAttribute("message", "User is not Present");
			return "userManagement/listUsers";
		}
		// String finalOldPass = appUser.getOldPassword();

		String CurrentPass = "8dd48ad39c2e6517b4245e75ceaa1a1ae23b2f4993df5ec588bfba7d196fb837ca213d809b3097b2f29afc27789fad40e23242ee62fe662326ccce6a04209f6b";
		String base64EncodedKey = Crypt.encodeKey("mustbe16byteskey");

		String finalOldPass = utils.getResetOldPass(appUser.getOldPassword(), CurrentPass, passwordEncoder);

		appUser.setEncrytedPassword(
				"8dd48ad39c2e6517b4245e75ceaa1a1ae23b2f4993df5ec588bfba7d196fb837ca213d809b3097b2f29afc27789fad40e23242ee62fe662326ccce6a04209f6b");
		appUser.setOldPassword(finalOldPass);
		appUser.setPassChanged(false);
		appUserRepo.save(appUser);
		// List<UserDetails> userList = userDetailsRepo.findAll();
		
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				userdet.getSalutation() +" " + userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(),
				"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
				utils.getMessage("log.user.resetUser"),
				utils.getMessage("log.user.resetUser")+" "+"and User Name is "+user.getFirstName(),
				userdet.getSalutation() +" " +userdet.getFirstName() + " " + (userdet.getMiddleName().equals("") ? "" : userdet.getMiddleName() +" ")+ userdet.getLastName(), "true");
		auditBeanBo.save();
		
		
		

		model.addAttribute("message",
				"Password  is Reset successfully of User  " + userDetailsService.getFullName(user));

		return "userManagement/PassResetSucc";
	}

	@RequestMapping(value = "/editUser", params = "Deactivate")
	public String Deactivate(@RequestParam(value = "Deactivate", required = true) Long id, Model model) {

		ProMISValidator snmsValidator = new ProMISValidator();

//		UserDetails user = userDetailsRepo.findById(id)
		// .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		// String finalOldPass =
		// utils.getUpdatedOldPass(loginUser.getOldPassword(),decryptNewPass,passwordEncoder);

		if (!snmsValidator.getValidInteger(id)) {
			List<UserDetails> userList = userDetailsRepo.findAll();
			model.addAttribute("userList", userList);
			model.addAttribute("message", "invalid userId");
			return "userManagement/listUsers";
		}

		UserDetails user = userDetailsRepo.findById(id).get();

		AppUser appUser = appUserRepo.findById(id).get();

		if (appUser == null) {
			List<UserDetails> userList = userDetailsRepo.findAll();
			model.addAttribute("userList", userList);
			model.addAttribute("message", "User is not Present");
			return "userManagement/listUsers";
		}
		// String finalOldPass = appUser.getOldPassword();

		appUser.setEnabled(0);
		appUserRepo.save(appUser);
		// List<UserDetails> userList = userDetailsRepo.findAll();

		model.addAttribute("message", " User  Deactivate successfully   " + userDetailsService.getFullName(user));

		return "userManagement/PassResetSucc";
	}

	@RequestMapping(value = "/editUser", params = "activate")
	public String Activate(@RequestParam(value = "activate", required = true) Long id, Model model) {

		ProMISValidator snmsValidator = new ProMISValidator();

//		UserDetails user = userDetailsRepo.findById(id)
		// .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		// String finalOldPass =
		// utils.getUpdatedOldPass(loginUser.getOldPassword(),decryptNewPass,passwordEncoder);

		if (!snmsValidator.getValidInteger(id)) {
			List<UserDetails> userList = userDetailsRepo.findAll();
			model.addAttribute("userList", userList);
			model.addAttribute("message", "invalid userId");
			return "userManagement/listUsers";
		}

		UserDetails user = userDetailsRepo.findById(id).get();

		AppUser appUser = appUserRepo.findById(id).get();

		if (appUser == null) {
			List<UserDetails> userList = userDetailsRepo.findAll();
			model.addAttribute("userList", userList);
			model.addAttribute("message", "User is not Present");
			return "userManagement/listUsers";
		}
		// String finalOldPass = appUser.getOldPassword();

		appUser.setEnabled(1);
		appUserRepo.save(appUser);
		// List<UserDetails> userList = userDetailsRepo.findAll();

		model.addAttribute("message", " User  activate successfully   " + userDetailsService.getFullName(user));

		return "userManagement/PassResetSucc";
	}

	@RequestMapping(value = "/editDesignation", params = "editdesig")
	public String editDesignation(@RequestParam(value = "editdesig", required = true) Long id, Model model) {
		AddDesignation desigDetails = designationRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		desigDetails.setEditdesgi(true);

		List<AppRole> roleList = appRoleDao.RoleList();

		model.addAttribute("addDesignation", desigDetails);
		model.addAttribute("listDesignation", designationRepo.findAll());
		model.addAttribute("roleList", roleList);

		return "userManagement/addDesignation";
	}

	@RequestMapping(value = "/addlocation")
	public String addlocation(Model model) {

		AddUnitlocation addUnitlocation = new AddUnitlocation();
		addUnitlocation.setEdit(false);
		model.addAttribute("addUnitlocation", addUnitlocation);
		model.addAttribute("listunitloc", addlocRepo.findAll());
		return "userManagement/addLocation";
	}

	@RequestMapping(value = "/editDetails", params = "editdetails")
	public String editdetails(@RequestParam(value = "editdetails", required = true) Long id, Model model) {
		AddUnitlocation addUnitlocation = addlocRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		addUnitlocation.setEdit(true);
		model.addAttribute("addUnitlocation", addUnitlocation);
		model.addAttribute("listunitloc", addlocRepo.findAll());
		return "userManagement/addLocation";
	}

	@RequestMapping(value = "/addNewLocation")
	public String addNewLocation(@ModelAttribute @Valid AddUnitlocation locationDetails, BindingResult bindResult,
			Model model, RedirectAttributes attributes) throws Exception {
		UserValidation userValidation = new UserValidation();
		userValidation.validatelocationDetails(locationDetails, bindResult);
		UserDetails userDetails = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		
		if (bindResult.hasErrors()) {
			if (null != locationDetails)
				model.addAttribute("addUnitlocation", locationDetails);
			else
				model.addAttribute("addUnitlocation", new AddUnitlocation());

			model.addAttribute("listunitloc", addlocRepo.findAll());
			return "userManagement/addLocation";
		}

		else if (locationDetails.getId() != null) {

			locationDetails.setEdit(false);
			locationDetails.setUpdatedDate(new Date());

			locationDetails = addlocRepo.save(locationDetails);
			attributes.addFlashAttribute("message", " location  Update  Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.updateLocation"),
					utils.getMessage("log.user.updatedLocation")+" "+"and Location name is "+locationDetails.getLocation()+" "+"and Location id is "+locationDetails.getId(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
		} else {
			locationDetails.setEdit(false);
			locationDetails.setCreatedDate(new Date());
			// unitDetails.setCreatedBy(new AppUser((long) 1));
			locationDetails = addlocRepo.save(locationDetails);
			attributes.addFlashAttribute("message", " New Location  added Successfully. ");
			auditBeanBo.setAuditBean(Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					userDetails.getSalutation() +" " + userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(),
					"User",Integer.parseInt(userDetailsService.getUserDetails().getUserId().toString()),
					utils.getMessage("log.user.addLocation"),
					utils.getMessage("log.user.addedLocation")+" "+"and Location name is "+locationDetails.getLocation(),
					userDetails.getSalutation() +" " +userDetails.getFirstName() + " " + (userDetails.getMiddleName().equals("") ? "" : userDetails.getMiddleName() +" ")+ userDetails.getLastName(), "true");
			auditBeanBo.save();
		}

		
		return "redirect:/addlocation";
	}

	@RequestMapping(value = "getlocaddress", method = RequestMethod.POST)
	public @ResponseBody String getlocaddress(@RequestParam("Id") Long Id) throws Exception {
		AddUnitlocation locationdetails = new AddUnitlocation();
		if (null != Id)
			locationdetails = addlocRepo.findById(Id).get();
		return locationdetails.getLocAddress();
	}
}
