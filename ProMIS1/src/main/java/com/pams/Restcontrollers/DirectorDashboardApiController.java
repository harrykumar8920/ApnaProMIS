package com.pams.Restcontrollers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pams.dto.DirectorDashTotalTaskAssignDto;
import com.pams.dto.DirectorDashboardCourtCaseSectionOrderDto;
import com.pams.dto.DirectorDashboardNext7DaysDto;
import com.pams.dto.DirectorDashboardTotalAddCaseDto;
import com.pams.dto.DirectorDashboardTotalNumberCase;
import com.pams.entity.AddCase;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.HearingDetails;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddCaseRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.HearingDetailsRepository;
import com.pams.service.ProCourtCaseDetailsRepository;

@RestController
@RequestMapping("/api/court-cases")
public class DirectorDashboardApiController {
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	AddCaseRepository addCaseRepository;
	@Autowired
	private HearingDetailsRepository hearingDetailsRepos;
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getListOfCourtCases() {
		Map<String, Object> response = new HashMap<>();

		try {
			List<AssignedTaskPuh> assignLst = assignedTaskPuhRepo.findAll();
			List<DirectorDashTotalTaskAssignDto> assignList = assignLst.stream().map(g -> {
				DirectorDashTotalTaskAssignDto dto = new DirectorDashTotalTaskAssignDto();
				if (g.getAddCase() != null) {
					dto.setProSectionOrderNumber(g.getAddCase().getProSectionOrderNumber());
					dto.setProSanctionDate(g.getAddCase().getProSanctionDate());
					dto.setProSanctionFileName(g.getAddCase().getProSanctionFileName());
				}
				if (g.getCreateTask() != null) {
					dto.setTask(g.getCreateTask().getTask());
				}
				if (g.getUnit() != null) {
					dto.setUnitName(g.getUnit().getUnitName());
				}
				if (g.getUser() != null) {
					dto.setUserName(g.getUser().getFirstName());
				}
				return dto;
			}).collect(Collectors.toList());
			response.put("lstCourtCase", assignList);
			response.put("status", "success");
			response.put("message", "Court case list fetched successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Failed to generate monthly progressive report");
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/pending-assigned-task")
	public ResponseEntity<Map<String, Object>> getPendingAssignedTasks() {
		List<ProCourtCaseDetails> proCourtDtls = proCourtCaseDetailsRepo.findALLByApproveStatusBetween(0, 0);
		List<DirectorDashTotalTaskAssignDto> collect = proCourtDtls.stream().map(g -> {
			DirectorDashTotalTaskAssignDto dto = new DirectorDashTotalTaskAssignDto();
			if (g.getAddCase() != null) {
				dto.setProSectionOrderNumber(g.getAddCase().getProSectionOrderNumber());
				dto.setProSanctionDate(g.getAddCase().getProSanctionDate());
				dto.setProSanctionFileName(g.getAddCase().getProSanctionFileName());
			}
			if (g.getAssignedTask().getCreateTask() != null) {
				dto.setTask(g.getAssignedTask().getCreateTask().getTask());
			}
			if (g.getAssignedTask().getUnit() != null) {
				dto.setUnitName(g.getAssignedTask().getUnit().getUnitName());
			}
			if (g.getAssignedTask().getUser() != null) {
				dto.setUserName(g.getAssignedTask().getUser().getFirstName());
			}
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> response = new HashMap<>();
		response.put("totalRecords", collect.size());
		response.put("pendingAssignedTasks", collect);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/listOfCourtCasesView")
	public ResponseEntity<Map<String, Object>> listOfCourtCasesView() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<ProCourtCaseDetails> lstCourt = proCourtCaseDetailsRepo.findAll();
			List<DirectorDashboardTotalNumberCase> hh = lstCourt.stream().map(f -> {
				DirectorDashboardTotalNumberCase df = new DirectorDashboardTotalNumberCase();
				df.setCaseTitle(f.getAssignedTask().getAddCase().getCaseTitle());
				df.setProSectionOrderNumber(f.getAssignedTask().getAddCase().getProSectionOrderNumber());
				df.setProSanctionFileName(f.getAssignedTask().getAddCase().getProSanctionFileName());
				df.setCourtCaseNo(f.getCourtCaseNo());
				df.setCauseTitle(f.getCauseTitle());
				if (f.getCasePosition() == 0) {
					df.setCasePosition("Case created but not forwarded");
				} else if (f.getCasePosition() == 1) {
					df.setCasePosition("Forwarded to PUH by PUH	Staff");
				} else if (f.getCasePosition() == 2) {
					df.setCasePosition("Case finalized but not assigned");
				} else if (f.getCasePosition() == 3) {
					df.setCasePosition("Sent back to PUH Staff by PUH");
				} else if (f.getCasePosition() == 4) {
					df.setCasePosition("Assigned to prosecutor by PUH");
				}
				df.setSfioAs(f.getSfioAs().getSfioAs());
				df.setFirstName(f.getCreatedBy().getFirstName());
				return df;
			}).collect(Collectors.toList());
			response.put("totalRecords", hh.size());
			response.put("lstCourtCase", hh);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Something went wrong while fetching court case details.");
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
		}
	}
	 @GetMapping("/viewAllSanctionOrderDetails")
	    public ResponseEntity<Map<String, Object>> viewAllSanctionOrderDetails() {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            List<AddCase> viewAllSanctionOrderDetails = addCaseRepository
	                    .findALLByFinalisationStatus(2, Sort.by(Sort.Direction.DESC, "id"));
	            List<DirectorDashboardTotalAddCaseDto> collect = viewAllSanctionOrderDetails.stream().map(f -> {
	            	DirectorDashboardTotalAddCaseDto dto=new DirectorDashboardTotalAddCaseDto();
	            	dto.setCaseTitle(f.getCaseTitle());
	            	dto.setProSanctionDate(f.getProSanctionDate());
	            	dto.setProSectionOrderNumber(f.getProSectionOrderNumber());
	            	dto.setFileNumber(f.getFileNumber());
	            	dto.setProSanctionFileName(f.getProSanctionFileName());
	            	dto.setCreatedDate(f.getCreatedDate());
	            	dto.setCreateFirstName(f.getCreatedBy().getFirstName());
	            	dto.setApprovedDate(f.getApprovedDate());
	            	dto.setApproveFirstName(f.getApprovedBy().getFirstName());
	            	return dto;
	            }).collect(Collectors.toList());
	            response.put("status", "success");
	            response.put("totalRecords", collect.size());
	            response.put("data", collect);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("status", "error");
	            response.put("message", "Unable to fetch sanction order details");
	            Map<String, Object> error = new HashMap<>();
				error.put("message", "Something went wrong while fetching court case details.");
				error.put("error", e.getMessage());
				return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
	        }
	    }
	 
	 @GetMapping("/courtCaseSanctionOrderDetails")
	    public ResponseEntity<Map<String, Object>> getCourtCaseSanctionOrderDetails() {
	        try {
	           
	            List<ProCourtCaseDetails> courtDetails = proCourtCaseDetailsRepo.findAll();
	            List<DirectorDashboardCourtCaseSectionOrderDto> collect = courtDetails.stream().map(f->{
	            	DirectorDashboardCourtCaseSectionOrderDto jj=new DirectorDashboardCourtCaseSectionOrderDto();
	            	jj.setCaseTitle(f.getAssignedTask().getAddCase().getCaseTitle());
	            	jj.setProSectionOrderNumber(f.getAssignedTask().getAddCase().getProSectionOrderNumber());
	            	jj.setProSanctionFileName(f.getAssignedTask().getAddCase().getProSanctionFileName());
	            	jj.setCourtCaseNo(f.getCourtCaseNo());
	            	jj.setCauseTitle(f.getCauseTitle());
	            	jj.setCasePosition(f.getCasePosition());
	            	if(f.getCasePosition()==0) {
	            		jj.setCasePositionS("Case created but not forwarded");
	            	}else if(f.getCasePosition()==1) {
	            		jj.setCasePositionS("Forwarded to PUH by PUH Staff");
	            	}else if(f.getCasePosition()==2) {
	            		jj.setCasePositionS("Case finalized but not assigned");
	            	}else if(f.getCasePosition()==3) {
	            		jj.setCasePositionS("Sent back to PUH Staff by PUH");
	            	}else if(f.getCasePosition()==4) {
	            		jj.setCasePositionS("Assigned to prosecutor by PUH");
	            	}
	            	jj.setSfioAs(f.getSfioAs().getSfioAs());
	            	jj.setFirstName(f.getCreatedBy().getFirstName());
	            	return jj;
	            }).collect(Collectors.toList());
	            Map<String, Object> response = new LinkedHashMap<>();
	            response.put("status", "success");
	            response.put("totalCourtCases", collect.size());
				
	            response.put("courtDetails", collect);
	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            e.printStackTrace();
	            Map<String, Object> error = new HashMap<>();
	            error.put("status", "error");
	            error.put("message", e.getMessage());
	            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
	        }
	    }
	 
	  private Date next7days() {
	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DAY_OF_MONTH, 7);
	        return cal.getTime();
	    }

	 @GetMapping("/todayHearingDetails")
	    public ResponseEntity<Map<String, Object>> getTodayHearingDetails() {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            String dateStr = sdf.format(new Date());
	            Date fromDate = sdf.parse(dateStr);
	            Date toDate = next7days();
	            List<HearingDetails> hearingData = hearingDetailsRepos.findByNextHearingDateBetween(fromDate, toDate);
	            List<DirectorDashboardNext7DaysDto> collect = hearingData.stream().map(f->{
	            	DirectorDashboardNext7DaysDto dd=new DirectorDashboardNext7DaysDto();
	            	dd.setCauseTitle(f.getProcourtdtl().getCauseTitle());
	            	dd.setCourtName(f.getProcourtdtl().getCourtType().getCourtName());
	            	dd.setCourtCaseNo(f.getProcourtdtl().getCourtCaseNo());
	            	dd.setType(f.getProcourtdtl().getType().getType());
	            	dd.setState(f.getProcourtdtl().getState().getState());
	            	dd.setBench(f.getProcourtdtl().getBench_Name().getBench());
	            	dd.setOfficerName(f.getOfficer().getName());
	            	dd.setMobile(f.getOfficer().getMobile());
	            	dd.setLastHearingDate(f.getLastHearingDate());
	            	dd.setNextHearingDate(f.getNextHearingDate());
	            	return dd;
	            }).collect(Collectors.toList());
	            
	            Map<String, Object> response = new LinkedHashMap<>();
	            response.put("status", "success");
	            response.put("message", "Due for hearing from " + dateStr + " to " + sdf.format(toDate));
	            response.put("totalHearingCount", collect.size());
	            response.put("hearingDetails", collect);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            e.printStackTrace();
	            Map<String, Object> error = new HashMap<>();
	            error.put("status", "error");
	            error.put("message", e.getMessage());
	            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
	        }
	    }
	

}
