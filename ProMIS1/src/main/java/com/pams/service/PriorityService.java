package com.pams.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pams.dao.PriorityDao;
import com.pams.dto.PriorityCaseDTO;
import com.pams.dto.ReportPriorityInput;
import com.pams.entity.ReportWeeklyInput;

@Service
public class PriorityService {

    @Autowired
    private PriorityDao priorityDao;

    public List<PriorityCaseDTO> getPriorityCases(int casePriority) {
        return priorityDao.getPriorityCases(casePriority);
    }
    
    public List<PriorityCaseDTO> getPriorityCases3(int casePriority,long stateId,long userId) {
        return priorityDao.getPriorityCases3(casePriority,stateId,userId);
    }
}
