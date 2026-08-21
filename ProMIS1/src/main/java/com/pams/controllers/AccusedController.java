package com.pams.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.entity.AddAccused;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.ResponseOfRespondent;
import com.pams.entity.ProCourtCaseDetails;
import com.pams.service.AddAccusedRepository;
import com.pams.service.AssignedTasksPuhRepository;
import com.pams.service.ProCourtCaseDetailsRepository;
import com.pams.service.ResponseOfRespondentRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;

@Controller
public class AccusedController {
	
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final long MB_IN_BYTES = 1083741824; // 200 MB file size
	public static final long KB_IN_BYTES = 256000; // 250 kb file size

	@Autowired
	private UserDetailsRepository useDetailRepo;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	ResponseOfRespondentRepository responseOfRespondentRepo;
	@Autowired
	private ProCourtCaseDetailsRepository proCourtCaseDetailsRepo;
	@Autowired
	private AssignedTasksPuhRepository assignedTaskPuhRepo;
	@Autowired
	private OfficerController officerControl;
	@Autowired
	private AddAccusedRepository addAccusedRepo;

	
	

}
