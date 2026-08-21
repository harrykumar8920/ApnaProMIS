package com.pams.Restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pams.dao.AssignTaskDAO;
import com.pams.dto.AssignTaskDTO;

@RestController
@RequestMapping("/api/puhdash")
public class PuhDashboardApiController {
	@Autowired
	private AssignTaskDAO assignTaskDAO;
	
	@GetMapping("/assigned")
    public ResponseEntity<Map<String, Object>> getAllAssignedTasks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AssignTaskDTO> allTasks = assignTaskDAO.findAllSectionOrderDetailsWithAssignTask1();
            response.put("totalItems", allTasks.size());
            response.put("assignedTasks", allTasks);
            response.put("message", "All assigned tasks fetched successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Something went wrong: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
