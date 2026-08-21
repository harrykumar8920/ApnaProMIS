package com.pams.service;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pams.dto.DynamicReportRequestDTO;
import com.pams.dto.DynamicReportRequestDTONew;

@Service
public interface DynamicReportService {

    List<Map<String, Object>> generateDynamicReport(DynamicReportRequestDTO requestDTO);
    List<Object[]> getDynamicReport(DynamicReportRequestDTONew dto);
    List<Object[]> getDynamicReport1(DynamicReportRequestDTONew dto);
    
    List<Map<String, Object>> generateDynamicReport1(DynamicReportRequestDTO requestDTO);
}