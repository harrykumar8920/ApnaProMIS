package com.pams.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pams.entity.AccusedDisposalqua;
import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.Disposed;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.entity.SupplementaryComplaint;
import com.pams.entity.UserDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AssignedTaskPuhAfterCOurtRepository;
import com.pams.service.DisposedRepository;
import com.pams.service.PairaviOfficerRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.SupplementaryComplaintRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class DisposedController {
	@Autowired
	private AddAccusedRepository addAccusedRepo;
	@Autowired
	private AssignedTaskPuhAfterCOurtRepository assignedTaskRepo;
	@Autowired
	private DisposedRepository disposedRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtRepo;
	@Autowired
	private PairaviOfficerRepository councilRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private PairaviOfficerRepository officersRepo;
	@Autowired
	private AddAccusedRepository addAccusedRepos;
	@Autowired
	private AddAccusedRepository addAccusedRepository;
	@Autowired
	SupplementaryComplaintRepository suppCompRepo;
	
	
	@PostMapping("/saveDisposedDetails2")
	public String saveDisposedDetails2(@RequestParam("mannerDisposal") String mannerDisposal,
			@RequestParam("assignedTask") Long assignedTaskId, @RequestParam("procourtdtl") Long procourtdtlId,
			@RequestParam(value = "id", required = false) Long id,

			// Outer form fields
			@RequestParam("counsel") Long counselId, @RequestParam("pairaviOfficer") Long pairaviOfficerId,

			// 3 PDF files
			@RequestParam("orderFile") MultipartFile orderFile,
			@RequestParam(value = "dismissalOrder", required = false) MultipartFile dismissalOrder,
			@RequestParam(value = "accusedOrderFiles", required = false) List<MultipartFile> accusedOrderFiles,
			
			// Accused JSON data
			@RequestParam("accusedJsonData") String accusedJsonData,

			Model model, RedirectAttributes redirect) throws Exception {
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		// ── 1. Disposed object build ──
		Disposed disposed = (id != null) ? disposedRepo.findById(id).orElse(new Disposed()) : new Disposed();
		disposed.setAssignedTask(assignedTaskRepo.findById(assignedTaskId).orElseThrow());
		disposed.setProcourtdtl(proCourtRepo.findById(procourtdtlId).orElseThrow());
		disposed.setCounsel(councilRepo.findById(counselId).orElseThrow());
		disposed.setPairaviOfficer(councilRepo.findById(pairaviOfficerId).orElseThrow());
		disposed.setCreatedDate(new Date());
		disposed.setCreatedBy(userdet);
		disposed.setUpdatedBy(userdet);
		if (orderFile != null && !orderFile.isEmpty()) {
			String fname = saveFile(orderFile); // helper method neeche
			disposed.setOrderfilename(fname);
		}

		
		if (dismissalOrder != null && !dismissalOrder.isEmpty()) {
			String fname = saveFile(dismissalOrder);
			//disposed.setDismissalfname(fname);
		}

		disposedRepo.save(disposed);

		if (accusedJsonData != null && !accusedJsonData.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> accusedList = mapper.readValue(accusedJsonData,
					new TypeReference<List<Map<String, Object>>>() {
					});
			disposed.getAccusedDisposal().clear();
			for (Map<String, Object> row : accusedList) {
				AccusedDisposalqua aqd = new AccusedDisposalqua();
				Long accusedId = Long.valueOf(row.get("accusedId").toString());
				aqd.setAccused(addAccusedRepo.findById(accusedId).orElseThrow());
				aqd.setCaseStatus(row.get("caseStatus").toString());
				aqd.setFineimposed(getStr(row, "fineimposed"));
				aqd.setImprisonment(getStr(row, "imprisonment"));
				aqd.setSenctioncompunded(getStr(row, "senctioncompunded"));
				aqd.setForumcompounding(getStr(row, "forumcompounding"));
				aqd.setConvictionaccusedguilty(getStr(row, "convictionaccusedguilty"));
				aqd.setBriefdetails(getStr(row, "briefdetails"));
				aqd.setDisposed(disposed);
				disposed.getAccusedDisposal().add(aqd);
			}
			disposedRepo.save(disposed);
		}
		// ✅ Ab redirect ki jagah, seedha usi assignedTask/proCourtCaseDetails se model populate karo aur same view dikhao
		
		AssignedTaskPuhAfterCOurt orElseThrow = assignedTaskRepo.findById(assignedTaskId).orElseThrow();
		ProCourtCaseDetails proCourtCaseDetails = orElseThrow.getProCourtCaseDetails();

		model.addAttribute("assignedDtl", orElseThrow);
		model.addAttribute("procourtdtl", proCourtCaseDetails);
		Disposed disposed1 = new Disposed();
		disposed1.setAssignedTask(orElseThrow);
		disposed1.setProcourtdtl(proCourtCaseDetails);
		List<AddAccused> accusedList = addAccusedRepos.findAllByProcourtdtlAndAssignedTask(proCourtCaseDetails,
				orElseThrow, Sort.by(Sort.Direction.DESC, "id"));
		
		model.addAttribute("personList", accusedList);
		
		model.addAttribute("councelRepo", officersRepo.findByType(2, Sort.by(Sort.Direction.ASC, "name")));
		model.addAttribute("pairaviOfficerList",officersRepo.findByType(1, Sort.by(Sort.Direction.ASC, "name")));
		model.addAttribute("disposed", disposed1);
		List<AddAccused> allByAssignedTask = addAccusedRepository.findAllByAssignedTask(orElseThrow);
		List<SupplementaryComplaint> byAssignedTask = suppCompRepo.findByAssignedTask(orElseThrow);
		model.addAttribute("accusedList1", allByAssignedTask);
		model.addAttribute("byAssignedTask", byAssignedTask);
		model.addAttribute("message",
		            "Disposed details Saved Successfully");
		return "Prosecutor/disposedNewPage";   //
	}

	@PostMapping("/saveDisposedDetails")
	public String saveDisposedDetails(@RequestParam("mannerDisposal") String mannerDisposal,@RequestParam("dismissalOrder") String dismissalOrder,
			@RequestParam("assignedTask") Long assignedTaskId, @RequestParam("procourtdtl") Long procourtdtlId,
			@RequestParam(value = "id", required = false) Long id,

			// Outer form fields
			@RequestParam("counsel") Long counselId, @RequestParam("pairaviOfficer") Long pairaviOfficerId,

			// 3 PDF files
			@RequestParam("orderFile") MultipartFile orderFile,
						
			// Accused JSON data
			@RequestParam("accusedJsonData") String accusedJsonData,

			Model model, RedirectAttributes redirect) throws Exception {
		UserDetails userdet = useDetailRepo.findAllByEmail(userDetailsService.getUserDetails().getUserName());
		// ── 1. Disposed object build ──
		Disposed disposed = (id != null) ? disposedRepo.findById(id).orElse(new Disposed()) : new Disposed();
		disposed.setMannerDisposal(mannerDisposal);
		disposed.setAssignedTask(assignedTaskRepo.findById(assignedTaskId).orElseThrow());
		disposed.setProcourtdtl(proCourtRepo.findById(procourtdtlId).orElseThrow());
		disposed.setCounsel(councilRepo.findById(counselId).orElseThrow());
		disposed.setPairaviOfficer(councilRepo.findById(pairaviOfficerId).orElseThrow());
		disposed.setDismissalOrder(dismissalOrder);
		disposed.setCreatedDate(new Date());
		disposed.setCreatedBy(userdet);
		disposed.setUpdatedBy(userdet);
		if (orderFile != null && !orderFile.isEmpty()) {
			String fname = saveFile(orderFile); // helper method neeche
			disposed.setOrderfilename(fname);
		}


		disposedRepo.save(disposed);

		if (accusedJsonData != null && !accusedJsonData.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> accusedList = mapper.readValue(accusedJsonData,
					new TypeReference<List<Map<String, Object>>>() {
					});
			disposed.getAccusedDisposal().clear();
			for (Map<String, Object> row : accusedList) {
				AccusedDisposalqua aqd = new AccusedDisposalqua();
				Long accusedId = Long.valueOf(row.get("accusedId").toString());
				aqd.setAccused(addAccusedRepo.findById(accusedId).orElseThrow());
				aqd.setCaseStatus(row.get("caseStatus").toString());
				aqd.setFineimposed(getStr(row, "fineimposed"));
				aqd.setImprisonment(getStr(row, "imprisonment"));
				aqd.setSenctioncompunded(getStr(row, "senctioncompunded"));
				aqd.setForumcompounding(getStr(row, "forumcompounding"));
				aqd.setConvictionaccusedguilty(getStr(row, "convictionaccusedguilty"));
				aqd.setBriefdetails(getStr(row, "briefdetails"));
				aqd.setDisposed(disposed);
				disposed.getAccusedDisposal().add(aqd);
			}
			disposedRepo.save(disposed);
		}
		
		redirect.addFlashAttribute("message",
		            "Disposed details Saved Successfully");
		return "redirect:/disposedStatuscriminalView?assignedTaskId=" + assignedTaskId;
	}
	
	
	
	
	@Value("${file.upload}")
	public String filePath1;

	// ── File save helper ──
	private String saveFile(MultipartFile file) throws IOException {
		String uploadDir = filePath1;
		String replace = file.getOriginalFilename().replace(" ", "_");
		
		
		String fname = UUID.randomUUID() + "_" + replace;
		Path path = Paths.get(uploadDir +"/"+ fname);
		Files.createDirectories(path.getParent());
		Files.write(path, file.getBytes());
		return fname;
	}

	// ── Null-safe string getter ──
	private String getStr(Map<String, Object> map, String key) {
		Object val = map.get(key);
		return val != null ? val.toString() : null;
	}
}
