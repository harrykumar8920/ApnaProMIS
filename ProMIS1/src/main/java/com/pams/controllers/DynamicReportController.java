package com.pams.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pams.dto.DynamicReportRequestDTO;
import com.pams.dto.DynamicReportRequestDTONew;
import com.pams.entity.AddAct;
import com.pams.entity.Status;
import com.pams.entity.UserDetails;
import com.pams.service.AddActRepository;
import com.pams.service.AddStatusRepository;
import com.pams.service.DynamicReportService;
import com.pams.service.StateRepository;
import com.pams.service.UserDetailsRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DynamicReportController {

	@Autowired
	private DynamicReportService dynamicReportService;
	@Autowired
	AddStatusRepository addStatusRepo;
	@Autowired
	private AddActRepository act;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private UserDetailsRepository useDetailRepo;

	@GetMapping("/dynamic-report")
	public String showReportForm(Model model) {

		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActiveOrderByStatusNameAsc("C",true);
		List<AddAct> allact = act.findAllByOrderByIdAsc();
		
		model.addAttribute("allact", allact);
		model.addAttribute("statusLst1", StatusList1);
		List<UserDetails> prosecuter = useDetailRepo.findAll();
		
	

		prosecuter.sort(Comparator.comparing(UserDetails::getFirstName)
		        .thenComparing(u -> u.getMiddleName() != null ? u.getMiddleName() : "")
		        .thenComparing(UserDetails::getLastName));

	
		model.addAttribute("prosecuter", prosecuter);

		model.addAttribute("dynamicReportRequestDTO", new DynamicReportRequestDTONew());

		model.addAttribute("statel", stateRepo.findAllByOrderByStateAsc());
		// return "report/dynamic-report";
		return "report/dynamic-reportnew";
	}

	@PostMapping("/dynamic-report")
	public String generateDynamicReportnew(@ModelAttribute DynamicReportRequestDTONew dto1, Model model) {

		List<Object[]> reportList = dynamicReportService.getDynamicReport(dto1);
		model.addAttribute("statel", stateRepo.findAllByOrderByStateAsc());
		List<Status> StatusList1 = addStatusRepo.findAllByTypeAndIsActiveOrderByStatusNameAsc("C",true);
		List<AddAct> allact = act.findAllByOrderByIdAsc();
		model.addAttribute("reportList", reportList);
		model.addAttribute("allact", allact);
		List<UserDetails> prosecuter = useDetailRepo.findAll();
		prosecuter.sort(Comparator.comparing(UserDetails::getFirstName)
		        .thenComparing(u -> u.getMiddleName() != null ? u.getMiddleName() : "")
		        .thenComparing(UserDetails::getLastName));
		model.addAttribute("prosecuter", prosecuter);

		model.addAttribute("statusLst1", StatusList1);
		model.addAttribute("dynamicReportRequestDTO", new DynamicReportRequestDTONew());

		return "report/dynamic-reportnew";

	}

	@PostMapping("/dynamic-reporta")
	public String generateDynamicReport(@ModelAttribute DynamicReportRequestDTONew dto1, Model model) {

		DynamicReportRequestDTO dto = new DynamicReportRequestDTO();
		dto.setFromDate(dto1.getFromDate());
		dto.setToDate(dto1.getToDate());
		dto.setDateType(dto1.getDateType());
		dto.setSelectedFields(dto1.getSelectedFields());

		List<String> selectedFields1 = dto1.getSelectedFields();

		List<Map<String, Object>> reportData = dynamicReportService.generateDynamicReport(dto);

		// Define column order
		List<String> columns = new ArrayList<>();
		columns.add("cin_number");
		columns.add("case_title");
		if (selectedFields1.contains("HEARING_DETAILS")) {
			columns.add("last_hearing_date");
			columns.add("next_hearing_date");
			columns.add("brif_hd");
		}

		if (selectedFields1.contains("COUNSEL_DETAILS")) {

			columns.add("officer_name");
		}

		if (selectedFields1.contains(" PROSECUTOR_NAME")) {

			columns.add("officer_name");
		}

		if (selectedFields1.contains(" PROSECUTOR_NAME")) {

			columns.add("officer_name");
		}

		List<List<Object>> tableData = reportData.stream()
				.map(row -> columns.stream().map(col -> row.get(col)).toList()).toList();

		model.addAttribute("columns", columns);
		model.addAttribute("data", tableData);

		model.addAttribute("columns", columns);
		model.addAttribute("data", tableData);
		model.addAttribute("fromDate", dto1.getFromDate());
		model.addAttribute("toDate", dto1.getToDate());
		model.addAttribute("dateType", dto1.getDateType());
		model.addAttribute("selectedFields", dto1.getSelectedFields());

		return "report/dynamic-report-result";
	}

	@PostMapping("/dynamic-report1")
	public void generateDynamicReport(
			@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			@RequestParam("dateType") String dateType,
			@RequestParam(value = "selectedFields", required = false) List<String> selectedFields,
			@RequestParam(value = "exportType", required = false, defaultValue = "HTML") String exportType,
			HttpServletResponse response, Model model) throws Exception {

		// Build DTO
		DynamicReportRequestDTO dto = new DynamicReportRequestDTO();
		dto.setFromDate(fromDate);
		dto.setToDate(toDate);
		dto.setDateType(dateType);
		dto.setSelectedFields(selectedFields);

		// Fetch data
		List<Map<String, Object>> reportData = dynamicReportService.generateDynamicReport(dto);

		// Define columns
		List<String> columns = new ArrayList<>();
		columns.add("cin_number");
		columns.add("case_title");
		if (selectedFields != null && selectedFields.contains("HEARING_DETAILS")) {
			columns.add("last_hearing_date");
			columns.add("next_hearing_date");
			columns.add("brif_hd");
		}

		if (selectedFields.contains("COUNSEL_DETAILS")) {

			columns.add("officer_name");

		}

		// Convert Map to List of values
		List<List<Object>> tableData = reportData.stream()
				.map(row -> columns.stream().map(col -> row.get(col)).toList()).toList();

		if ("PDF".equalsIgnoreCase(exportType)) {
			// Export PDF
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=dynamic_report.pdf");
			exportPDF(columns, tableData, response);
		} else if ("EXCEL".equalsIgnoreCase(exportType)) {
			// Export Excel
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=dynamic_report.xlsx");
			exportExcel(columns, tableData, response);
		} else {
			// Default HTML view
			model.addAttribute("columns", columns);
			model.addAttribute("data", tableData);
			model.addAttribute("selectedFields", selectedFields);
			model.addAttribute("fromDate", fromDate);
			model.addAttribute("toDate", toDate);
			model.addAttribute("dateType", dateType);
			// Forward to Thymeleaf view
			response.sendRedirect("/dynamic-report-result");
		}
	}

	private void exportPDF(List<String> columns, List<List<Object>> tableData, HttpServletResponse response)
			throws Exception {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
		document.open();

		com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(columns.size() + 1); // +1 for
																											// S.No.
		pdfTable.setWidthPercentage(100);

		// Header
		pdfTable.addCell("S.No.");
		for (String col : columns) {
			pdfTable.addCell(col.replace("_", " ").toUpperCase());
		}

		// Data
		int sno = 1;
		for (List<Object> row : tableData) {
			pdfTable.addCell(String.valueOf(sno++));
			for (Object cell : row) {
				pdfTable.addCell(cell != null ? cell.toString() : "");
			}
		}

		document.add(pdfTable);
		document.close();
	}

	private void exportExcel(List<String> columns, List<List<Object>> tableData, HttpServletResponse response)
			throws Exception {
		org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Dynamic Report");

		int rowCount = 0;

		// Header row
		org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(rowCount++);
		headerRow.createCell(0).setCellValue("S.No.");
		for (int i = 0; i < columns.size(); i++) {
			headerRow.createCell(i + 1).setCellValue(columns.get(i).replace("_", " ").toUpperCase());
		}

		// Data rows
		int sno = 1;
		for (List<Object> rowData : tableData) {
			org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowCount++);
			row.createCell(0).setCellValue(sno++);
			for (int i = 0; i < rowData.size(); i++) {
				Object cell = rowData.get(i);
				row.createCell(i + 1).setCellValue(cell != null ? cell.toString() : "");
			}
		}

		workbook.write(response.getOutputStream());
		workbook.close();
	}

}
