/*
 * package com.pams.controllers;
 * 
 * import java.text.SimpleDateFormat; import java.util.Date; import
 * java.util.List;
 * 
 * import javax.validation.Valid;
 * 
 * import org.springframework.beans.BeanUtils; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.ModelMap; import
 * org.springframework.validation.BindingResult; import
 * org.springframework.web.bind.annotation.ModelAttribute; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestMethod; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import com.pams.dao.AccusedCompDAO; import com.pams.dto.CriminalTaskDto;
 * import com.pams.entity.AccusedCompCaseDtl; import
 * com.pams.entity.AccusedMaster; import com.pams.entity.AddAccused; import
 * com.pams.entity.AddCompany; import com.pams.entity.AddDesignation; import
 * com.pams.entity.AssignedTaskPuh; import com.pams.entity.CaseCompany; import
 * com.pams.entity.Complaintdetl; import com.pams.entity.HearingDetails; import
 * com.pams.entity.InvCaseDetails; import com.pams.entity.PairaviDetails; import
 * com.pams.entity.Status; import com.pams.entity.UserDetails; import
 * com.pams.entity.proCourtCaseDetails; import
 * com.pams.service.AccusedCompCaseDtlRepository; import
 * com.pams.service.AccusedMasterRepository; import
 * com.pams.service.AddAccusedRepository; import
 * com.pams.service.AddCompanyRepository; import
 * com.pams.service.AddDesignationRepository; import
 * com.pams.service.AddStatusRepository; import
 * com.pams.service.CaseCompanyRepository; import
 * com.pams.service.InvCaseDetailsRepository; import
 * com.pams.service.ProCourtCaseDetailsRepository; import
 * com.pams.service.StateRepository; import
 * com.pams.service.UserDetailsRepository; import
 * com.pams.service.UserDetailsServiceImpl; import
 * com.pams.service.addDisposalRepository; import
 * com.pams.validation.AccusedCompValidation; import
 * com.pams.validation.ProMISValidator;
 * 
 * 
 * @Controller public class AccusedCompanyController {
 * 
 * @Autowired private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
 * 
 * @Autowired private InvCaseDetailsRepository invCaseDtlRepo;
 * 
 * @Autowired private AccusedCompCaseDtlRepository accusedCompCaseDtlRepo;
 * 
 * @Autowired private AccusedCompDAO accusedComdao;
 * 
 * @Autowired private AddCompanyRepository addCompanyRepo;
 * 
 * @Autowired private UserDetailsServiceImpl userDetailsService;
 * 
 * @Autowired private CaseCompanyRepository caseCompanyRepo;
 * 
 * @Autowired private UserDetailsRepository useDetailRepo;
 * 
 * @Autowired private AddDesignationRepository designationRepo;
 * 
 * @Autowired private AccusedMasterRepository accusedMasterRepo;
 * 
 * @Autowired private AddAccusedRepository addAccusedRepo;
 * 
 * @Autowired private addDisposalRepository disposalRepo;
 * 
 * @Autowired AddStatusRepository addStatusRepo;
 * 
 * @RequestMapping(value = "updateInfo", params = "Newaccused") public String
 * Newaccused(ModelMap modelMap , @ModelAttribute proCourtCaseDetails
 * proCourtCasedetails) { proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(proCourtCasedetails.getId());
 * InvCaseDetails invCasedtl = invCaseDtlRepo.findAllById(courtdtl.getId());
 * AccusedCompCaseDtl accusedCompCaseDtl = new AccusedCompCaseDtl();
 * 
 * modelMap.addAttribute("desilst", designationRepo.findAll());
 * modelMap.addAttribute("courtdtl", courtdtl.getId()); AccusedCompCaseDtl
 * accusedCompCaseDtl1 =
 * accusedCompCaseDtlRepo.findAllByProCourtId(proCourtCasedetails.getId());
 * if(accusedCompCaseDtl1!=null) {
 * accusedCompCaseDtl.setId(accusedCompCaseDtl1.getId());
 * modelMap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl1);
 * modelMap.addAttribute("caseidCompanyList", accusedCompCaseDtl1.getCompany());
 * } if(proCourtCasedetails.getInvCaseDetail().getInvcaseDetailsId()!=null)
 * accusedCompCaseDtl.setCaseId(proCourtCasedetails.getInvCaseDetail().
 * getInvcaseDetailsId());
 * accusedCompCaseDtl.setProCourtId(proCourtCasedetails.getId());
 * List<CaseCompany> compLst = caseCompanyRepo.findByProcourtdtl(courtdtl);
 * modelMap.addAttribute("compLst", compLst); List<Status> StatusList =
 * addStatusRepo.findAllByType("A");
 * 
 * modelMap.addAttribute("statusLst", StatusList);
 * modelMap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl);
 * modelMap.addAttribute("invCasedtl", invCasedtl); return
 * "IOOfficer/AccusedDestails"; }
 * 
 * @RequestMapping(value = "updateInfo", params = "UpAccused") public String
 * UpPairavi(ModelMap modelMap , @ModelAttribute proCourtCaseDetails
 * proCourtCasedetails, @RequestParam(value="UpAccused") Long UpAccused) {
 * 
 * proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(proCourtCasedetails.getId()); AddAccused
 * addAccused = addAccusedRepo.findById(UpAccused).get();
 * addAccused.setType("A"); AccusedCompCaseDtl accusedCompCaseDtl =
 * accusedCompCaseDtlRepo.findAllByProCourtId(courtdtl.getId());
 * accusedCompCaseDtl.setAccusedDto(addAccused);
 * modelMap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl);
 * modelMap.addAttribute("desilst", designationRepo.findAll());
 * modelMap.addAttribute("courtdtl", courtdtl.getId());
 * modelMap.addAttribute("addAccused", addAccused); List<CaseCompany> compLst =
 * caseCompanyRepo.findByProcourtdtl(courtdtl); modelMap.addAttribute("compLst",
 * compLst); List<Status> StatusList = addStatusRepo.findAllByType("A");
 * modelMap.addAttribute("statusLst", StatusList); return
 * "IOOfficer/UpAccusedDestails"; }
 * 
 * @RequestMapping(value="updateAccused") public String upAccused(ModelMap
 * modelMap , @Valid @ModelAttribute AddAccused addAccused) throws Exception {
 * 
 * proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(addAccused.getProcourtdtl().getId());
 * AddAccused addAccused1 = addAccusedRepo.findById(addAccused.getId()).get();
 * // addAccused1.setAccusedstatus(addAccused.getAccusedstatus());
 * addAccused1.setUpdateBy(userDetailsService.getUserDetails().getUserId());
 * addAccused1.setUpdatedDate(new Date()); addAccused1 =
 * addAccusedRepo.save(addAccused1); List<Status> StatusList =
 * addStatusRepo.findAllByType("A"); modelMap.addAttribute("statusLst",
 * StatusList); modelMap.addAttribute("message" ,
 * addAccused1.getAccusedMaster().getAccusedName() +
 * "   is updated successfully "); modelMap.addAttribute("addAccused",
 * addAccused1); modelMap.addAttribute("courtdtl", courtdtl.getId()); return
 * "IOOfficer/UpAccusedDestails"; }
 * 
 * @RequestMapping(value="SaveAccused") public String SaveAccused(ModelMap
 * modelmap,@ModelAttribute AccusedCompCaseDtl accusedCompCaseDtl ,BindingResult
 * bindingResult ) throws Exception { proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(accusedCompCaseDtl.getProCourtId());
 * InvCaseDetails invCasedtl = invCaseDtlRepo.findAllById(courtdtl.getId());
 * List<CaseCompany> compLst = caseCompanyRepo.findByProcourtdtl(courtdtl);
 * modelmap.addAttribute("desilst", designationRepo.findAll());
 * modelmap.addAttribute("compLst", compLst); AccusedCompValidation valid = new
 * AccusedCompValidation();
 * 
 * modelmap.addAttribute("courtdtl", courtdtl.getId());
 * valid.accusedValidation(accusedCompCaseDtl,bindingResult);
 * 
 * 
 * 
 * List<Status> StatusList = addStatusRepo.findAllByType("A");
 * modelmap.addAttribute("statusLst", StatusList);
 * 
 * 
 * if(accusedCompCaseDtl.getAccusedDto().getCompany()==null) {
 * bindingResult.rejectValue("AccusedDto.company", "msg.wrongId"); }
 * 
 * 
 * if(accusedCompCaseDtl.getAccusedDto().getDesignation()==null) {
 * bindingResult.rejectValue("AccusedDto.designation", "msg.wrongId"); }
 * 
 * if (bindingResult.hasErrors()) {
 * 
 * modelmap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl);
 * modelmap.addAttribute("invCasedtl", invCasedtl); return
 * "IOOfficer/AccusedDestails"; }
 * 
 * AccusedCompCaseDtl SavedAccusedCompCaseDtl; UserDetails userdet =
 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
 * )); AccusedMaster accusedMasterdtl =
 * accusedMasterRepo.findAllByPanNumber(accusedCompCaseDtl.getAccusedDto().
 * getAccusedMaster().getPanNumber()); AccusedMaster accusedMaster = new
 * AccusedMaster(); if(accusedMasterdtl == null) {
 * accusedMaster.setAccusedName(accusedCompCaseDtl.getAccusedDto().
 * getAccusedMaster().getAccusedName());
 * accusedMaster.setPanNumber(accusedCompCaseDtl.getAccusedDto().
 * getAccusedMaster().getPanNumber()); accusedMaster.setCreatedDate(new Date());
 * accusedMaster.setCreatedBy(userdet); accusedMaster =
 * accusedMasterRepo.save(accusedMaster); }else {
 * 
 * 
 * 
 * AddAccused addAccuseddtl =
 * addAccusedRepo.findAllByAccusedMasterAndProcourtdtlAndCompany(
 * accusedMasterdtl,courtdtl,accusedCompCaseDtl.getAccusedDto().getCompany());
 * if(addAccuseddtl!=null) { modelmap.addAttribute("message" ,
 * accusedCompCaseDtl.getAccusedDto().getAccusedMaster().getAccusedName()
 * +" is already added  ");
 * accusedCompCaseDtl.setType(accusedCompCaseDtl.getType());
 * modelmap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl); return
 * "IOOfficer/AccusedDestails"; }
 * 
 * } SavedAccusedCompCaseDtl =
 * accusedComdao.findByProCourtId(accusedCompCaseDtl.getProCourtId());
 * SavedAccusedCompCaseDtl.setId(accusedCompCaseDtl.getId()); AddAccused
 * accuseddts =new AddAccused(); accuseddts.setAccusedMaster(accusedMaster); //
 * accuseddts.setCompany(accusedCompCaseDtl.getAccusedDto().getCompany());
 * 
 * accuseddts.setDesignation(accusedCompCaseDtl.getAccusedDto().getDesignation()
 * );
 * 
 * // accuseddts.setAccusedstatus(accusedCompCaseDtl.getAccusedDto().
 * getAccusedstatus()); accuseddts.setProcourtdtl(courtdtl);
 * accuseddts.setCreatedBy(userdet); accuseddts.setCreatedDate(new Date());
 * modelmap.addAttribute("accusedCompCaseDtl", new AccusedCompCaseDtl());
 * addAccusedRepo.save(accuseddts); modelmap.addAttribute("message" ,
 * accusedCompCaseDtl.getAccusedDto().getAccusedMaster().getAccusedName()
 * +" is added successfully ");
 * 
 * return "IOOfficer/AccusedDestails"; }
 * 
 * 
 * @RequestMapping(value="AddAccusedDetails") public String
 * AddAccusedDetails(ModelMap modelMap, @RequestParam(value="compId") Long
 * compId, @RequestParam(value="compName") String compName,
 * 
 * @RequestParam(value="compCin") String compCin
 * , @RequestParam(value="accusedName") String accusedName,
 * 
 * @RequestParam(value="accpan") String accpan ,
 * 
 * @RequestParam(value="accId") Long accId ,
 * 
 * @RequestParam(value="accDesgi") String accDesgi ,
 * 
 * @Valid @ModelAttribute AccusedCompCaseDtl accusedCompCaseDtl ) throws
 * Exception { InvCaseDetails invCasedtl =
 * invCaseDtlRepo.findAllByInvcaseDetailsId(accusedCompCaseDtl.getCaseId());
 * proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(accusedCompCaseDtl.getProCourtId());
 * UserDetails userdet =
 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
 * )); AddCompany companylst = new AddCompany();
 * 
 * companylst = addCompanyRepo.findAllByCin(compCin); AddCompany addCompany =
 * new AddCompany(); if(companylst==null) { addCompany.setCin(compCin);
 * addCompany.setCompanyName(compName); addCompany =
 * addCompanyRepo.save(addCompany); }else { addCompany =
 * addCompanyRepo.save(companylst); } CaseCompany caseCompany=new
 * CaseCompany(addCompany,courtdtl,compId,userdet,new Date());
 * 
 * AccusedCompCaseDtl savedAccusedCompCaseDtl=
 * accusedComdao.findByProCourtId(accusedCompCaseDtl.getProCourtId());
 * 
 * if(accusedCompCaseDtl.getId() != 0){ boolean isCompExist =false; for (int i =
 * 0; i <savedAccusedCompCaseDtl.getCompany().size(); i++) {
 * if(savedAccusedCompCaseDtl.getCompany().get(i).getCompany().getCompanyName().
 * equalsIgnoreCase(addCompany.getCompanyName())) { isCompExist =true;
 * savedAccusedCompCaseDtl.getCompany().get(i).setCompany(addCompany);
 * savedAccusedCompCaseDtl.getCompany().get(i).setProcourtdtl(courtdtl);
 * savedAccusedCompCaseDtl.getCompany().get(i).setUpdateBy(userDetailsService.
 * getUserDetails().getUserId());
 * savedAccusedCompCaseDtl.getCompany().get(i).setUpdatedDate(new Date()); } }
 * if(!isCompExist){ caseCompany.setId(0L);
 * savedAccusedCompCaseDtl.getCompany().add(caseCompany);
 * savedAccusedCompCaseDtl.getCompanyDto().setProcourtdtl(courtdtl);
 * savedAccusedCompCaseDtl.getCompanyDto().setCreatedDate(new Date());
 * savedAccusedCompCaseDtl.getCompanyDto().setCreatedBy(userdet); } }
 * 
 * else{ accusedCompCaseDtl.setCreatedBy(userdet);
 * accusedCompCaseDtl.setCreatedBy(userdet); savedAccusedCompCaseDtl = new
 * AccusedCompCaseDtl();
 * 
 * BeanUtils.copyProperties(accusedCompCaseDtl,savedAccusedCompCaseDtl);
 * BeanUtils.copyProperties(caseCompany ,accusedCompCaseDtl.getCompanyDto());
 * 
 * savedAccusedCompCaseDtl.getCompany().add(caseCompany); }
 * 
 * savedAccusedCompCaseDtl =
 * accusedCompCaseDtlRepo.save(savedAccusedCompCaseDtl);
 * 
 * CaseCompany SavedCaseComp =
 * caseCompanyRepo.findAllByCompanyAndProcourtdtl(addCompany,courtdtl);
 * 
 * 
 * AccusedMaster accusedMasterdtl =
 * accusedMasterRepo.findAllByPanNumber(accpan); AccusedMaster accusedMaster =
 * new AccusedMaster(); if(accusedMasterdtl == null) {
 * accusedMaster.setAccusedName(accusedName);
 * accusedMaster.setPanNumber(accpan); accusedMaster.setCreatedDate(new Date());
 * accusedMaster.setCreatedBy(userdet); accusedMaster =
 * accusedMasterRepo.save(accusedMaster); }else { accusedMaster =
 * accusedMasterRepo.save(accusedMasterdtl); }
 * 
 * savedAccusedCompCaseDtl =
 * accusedComdao.findByProCourtId(accusedCompCaseDtl.getProCourtId());
 * 
 * AddDesignation addDesgi = designationRepo.findAllByDesignation(accDesgi);
 * Status accusedStatus = addStatusRepo.findById(0L).get();
 * 
 * //savedAccusedCompCaseDtl.setId(accusedCompCaseDtl.getId()); AddAccused
 * accuseddts =new AddAccused(); accuseddts.setAccusedMaster(accusedMaster); //
 * accuseddts.setCompany(SavedCaseComp); accuseddts.setInvperId(accId);
 * accuseddts.setProcourtdtl(courtdtl); accuseddts.setDesignation(addDesgi); //
 * accuseddts.setAccusedstatus(accusedStatus);;
 * accuseddts.setCreatedBy(userdet); accuseddts.setCreatedDate(new Date());
 * 
 * accuseddts = addAccusedRepo.save(accuseddts); modelMap.addAttribute("message"
 * , accuseddts.getAccusedMaster().getAccusedName()+" is added successfully ");
 * 
 * 
 * List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtl(courtdtl);
 * accusedCompCaseDtl.setProCourtId(courtdtl.getId());
 * accusedCompCaseDtl.setId(savedAccusedCompCaseDtl.getId());
 * accusedCompCaseDtl.setCaseId(savedAccusedCompCaseDtl.getCaseId());
 * modelMap.addAttribute("CompanyList", savedAccusedCompCaseDtl.getCompany());
 * modelMap.addAttribute("personList", accusedList);
 * modelMap.addAttribute("complst",caseCompanyRepo.findByProcourtdtl(courtdtl)
 * );
 * 
 * modelMap.addAttribute("invCasedtl", invCasedtl);
 * 
 * 
 * return "IOOfficer/CaseDetailsView"; }
 * 
 * 
 * @RequestMapping(value="SaveCompany") public String SaveCompany(ModelMap
 * modelmap , @ModelAttribute AccusedCompCaseDtl
 * accusedCompCaseDtl,BindingResult bindResult) throws Exception {
 * proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(accusedCompCaseDtl.getProCourtId());
 * UserDetails userdet =
 * useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName(
 * )); if(null == accusedCompCaseDtl || accusedCompCaseDtl.equals(0)) { return
 * "IOOfficer/AccusedDestails"; } List<CaseCompany> compLst =
 * caseCompanyRepo.findByProcourtdtl(courtdtl); modelmap.addAttribute("desilst",
 * designationRepo.findAll()); modelmap.addAttribute("compLst", compLst);
 * AccusedCompValidation valid = new AccusedCompValidation(); List<Status>
 * StatusList = addStatusRepo.findAllByType("A");
 * modelmap.addAttribute("statusLst", StatusList);
 * valid.companydtls(accusedCompCaseDtl,bindResult); if(bindResult.hasErrors())
 * { modelmap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl);
 * 
 * return "IOOfficer/AccusedDestails"; } AddCompany companylst = new
 * AddCompany();
 * 
 * companylst =
 * addCompanyRepo.findAllByCin(accusedCompCaseDtl.getCompanyDto().getCompany().
 * getCin()); AddCompany addCompany = new AddCompany(); if(companylst==null) {
 * addCompany.setCin(accusedCompCaseDtl.getCompanyDto().getCompany().getCin());
 * addCompany.setCompanyName(accusedCompCaseDtl.getCompanyDto().getCompany().
 * getCompanyName());
 * addCompany.setAddress(accusedCompCaseDtl.getCompanyDto().getCompany().
 * getAddress());
 * 
 * addCompany = addCompanyRepo.save(addCompany); }else {
 * 
 * 
 * addCompany = addCompanyRepo.save(companylst); }
 * 
 * 
 * 
 * 
 * modelmap.addAttribute("courtdtl", courtdtl.getId());
 * 
 * CaseCompany caseCompany=new CaseCompany(addCompany,courtdtl,userdet,new
 * Date());
 * 
 * AccusedCompCaseDtl savedAccusedCompCaseDtl=
 * accusedComdao.findByProCourtId(accusedCompCaseDtl.getProCourtId());
 * 
 * if(accusedCompCaseDtl.getId() != 0){ boolean isCompExist =false; for (int i =
 * 0; i <savedAccusedCompCaseDtl.getCompany().size(); i++) {
 * if(savedAccusedCompCaseDtl.getCompany().get(i).getCompany().getCompanyName().
 * equalsIgnoreCase(accusedCompCaseDtl.getCompanyDto().getCompany().
 * getCompanyName())) { isCompExist =true;
 * savedAccusedCompCaseDtl.getCompany().get(i).setCompany(addCompany);
 * savedAccusedCompCaseDtl.getCompany().get(i).setProcourtdtl(courtdtl);
 * savedAccusedCompCaseDtl.getCompany().get(i).setUpdateBy(userDetailsService.
 * getUserDetails().getUserId());
 * savedAccusedCompCaseDtl.getCompany().get(i).setUpdatedDate(new Date()); } }
 * if(!isCompExist){ caseCompany.setId(0L);
 * savedAccusedCompCaseDtl.getCompany().add(caseCompany);
 * savedAccusedCompCaseDtl.getCompanyDto().setProcourtdtl(courtdtl);
 * savedAccusedCompCaseDtl.getCompanyDto().setCreatedDate(new Date());
 * savedAccusedCompCaseDtl.getCompanyDto().setCreatedBy(userdet); } }
 * 
 * else{ accusedCompCaseDtl.setCreatedBy(userdet);
 * accusedCompCaseDtl.setCreatedBy(userdet); savedAccusedCompCaseDtl = new
 * AccusedCompCaseDtl();
 * 
 * BeanUtils.copyProperties(accusedCompCaseDtl,savedAccusedCompCaseDtl);
 * BeanUtils.copyProperties(caseCompany ,accusedCompCaseDtl.getCompanyDto());
 * 
 * savedAccusedCompCaseDtl.getCompany().add(caseCompany); }
 * 
 * accusedCompCaseDtlRepo.save(savedAccusedCompCaseDtl);
 * 
 * modelmap.addAttribute("message" ," Company  is added successfully ");
 * 
 * modelmap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl);
 * 
 * return "IOOfficer/AccusedDestails"; }
 * 
 * @RequestMapping(value="updateInfo",params = "snmsAccused") public String
 * NewAccusedCase(ModelMap modelMap , @RequestParam(value="snmsAccused",required
 * = true ) Long caseId , @ModelAttribute proCourtCaseDetails
 * proCourtCasedetails ) { proCourtCaseDetails courtdtl =
 * proCourtCaseDetailsRepo.findALLById(proCourtCasedetails.getId());
 * InvCaseDetails invCasedtl = invCaseDtlRepo.findAllByInvcaseDetailsId(caseId);
 * modelMap.addAttribute("courtdtl", courtdtl.getId()); AccusedCompCaseDtl
 * accusedCompCaseDtl = new AccusedCompCaseDtl();
 * 
 * AccusedCompCaseDtl savedAccusedCompCaseDtl=
 * accusedComdao.findByProCourtId(proCourtCasedetails.getId());
 * 
 * if(savedAccusedCompCaseDtl!=null) {
 * 
 * List<AddAccused> accusedList = addAccusedRepo.findAllByProcourtdtl(courtdtl);
 * accusedCompCaseDtl.setProCourtId(proCourtCasedetails.getId());
 * accusedCompCaseDtl.setId(savedAccusedCompCaseDtl.getId());
 * accusedCompCaseDtl.setCaseId(savedAccusedCompCaseDtl.getCaseId());
 * modelMap.addAttribute("CompanyList", savedAccusedCompCaseDtl.getCompany());
 * modelMap.addAttribute("personList", accusedList);
 * modelMap.addAttribute("complst",caseCompanyRepo.findByProcourtdtl(courtdtl)
 * );
 * 
 * modelMap.addAttribute("invCasedtl", invCasedtl);
 * 
 * 
 * }
 * 
 * 
 * accusedCompCaseDtl.setProCourtId(proCourtCasedetails.getId());
 * accusedCompCaseDtl.setCaseId(proCourtCasedetails.getInvCaseDetail().
 * getInvcaseDetailsId());
 * 
 * modelMap.addAttribute("accusedCompCaseDtl", accusedCompCaseDtl);
 * modelMap.addAttribute("invCasedtl", invCasedtl); return
 * "IOOfficer/CaseDetailsView"; }
 * 
 * 
 * @RequestMapping(value = "getpanDetails", method = RequestMethod.POST)
 * public @ResponseBody AccusedMaster accuseddtl (@RequestParam("panNumber")
 * String panNumber) throws Exception { AccusedMaster accusedMaster = new
 * AccusedMaster();
 * 
 * accusedMaster = accusedMasterRepo.findAllByPanNumber(panNumber); return
 * accusedMaster; }
 * 
 * 
 * }
 */