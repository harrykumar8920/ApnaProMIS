package com.pams.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pams.dto.AssignTaskDTO;

@Repository
@Transactional
public class AssignTaskDAO {

    @Autowired
    private EntityManager entityManager;

    // By Keyraj 25.05.2023
    @SuppressWarnings({ "unchecked" })
    public List<AssignTaskDTO> findSectionOrderDetails() {
        try {
            String sql = "SELECT prosecution.prosecution_sanction_order_details.pro_sanction_file_name,  prosecution.prosecution_sanction_order_details.pro_section_order_number, " +
                    "prosecution.prosecution_sanction_order_details.pro_sanction_date, prosecution.prosecution_sanction_order_details.pro_sanction_order_id, " +
                    "prosecution.prosecution_sanction_order_details.case_title, prosecution.prosecution_court_case_details.court_case_no, " +
                    "prosecution.prosecution_court_case_details.approve_status, prosecution.prosecution_court_case_details.cause_title, " +
                    "authentication.court_type.court_name " +
                    "FROM prosecution.prosecution_sanction_order_details " +
                    "LEFT JOIN prosecution.prosecution_court_case_details ON prosecution.prosecution_sanction_order_details.pro_sanction_order_id = prosecution.prosecution_court_case_details.add_case_pro_sanction_order_id " +
                    "LEFT JOIN authentication.court_type ON prosecution.prosecution_court_case_details.court_type_id = authentication.court_type.id " +
                    "WHERE prosecution.prosecution_sanction_order_details.finalisation_status='2' " +
                    "ORDER BY prosecution.prosecution_sanction_order_details.pro_sanction_order_id ASC";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> objects = query.getResultList();
            List<AssignTaskDTO> list = new ArrayList<>();

            if (objects.size() > 0) {
                for (int j = objects.size() - 1; j >= 0; j--) {
                    Object[] object = objects.get(j);
                    AssignTaskDTO assignDTO = new AssignTaskDTO();

                    String SanctionFileName = (String) object[0];
                    String proSanctionOrderNo = (String) object[1];
                    Date prosanctiondate = (Date) object[2];
                    Long proSanctionID = (Long) object[3];
                  //  long proSanctionID1 = proSanctionID.longValue();
                    String proSanctionCompName = (String) object[4];
                    String courtCaseNo = (String) object[5];
                    Integer courtCaseApproveStatus = (Integer) object[6];
                    String causeTitle = (String) object[7];
                    String courtType = (String) object[8];

                    assignDTO.setCaseTitle(proSanctionCompName);
                    assignDTO.setProSanctionFileName(SanctionFileName);
                    assignDTO.setProSanctionDate(prosanctiondate);
                    assignDTO.setProSectionOrderNumber(proSanctionOrderNo);
                    assignDTO.setCourtCaseNo(courtCaseNo);
                    assignDTO.setCourtType(courtType);
                    assignDTO.setCourtCaseApproveStatus(courtCaseApproveStatus);
                    assignDTO.setCauseTitle(causeTitle);
                    assignDTO.setId(proSanctionID);

                    list.add(assignDTO);
                }
            }

            return list.isEmpty() ? list : list;

        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings({ "unchecked" })
    public List<AssignTaskDTO> findSectionOrderDetailsAfterCourt() {
        try {
            String sql = "SELECT prosecution.prosecution_sanction_order_details.pro_sanction_file_name,  prosecution.prosecution_sanction_order_details.pro_section_order_number, \r\n"
            		+ "                    prosecution.prosecution_sanction_order_details.pro_sanction_date, prosecution.prosecution_sanction_order_details.pro_sanction_order_id, \r\n"
            		+ "                    prosecution.prosecution_sanction_order_details.case_title, prosecution.prosecution_court_case_details.court_case_no, \r\n"
            		+ "                    prosecution.prosecution_court_case_details.approve_status, prosecution.prosecution_court_case_details.cause_title, \r\n"
            		+ "                    authentication.court_type.court_name\r\n"
            		+ "                    FROM prosecution.prosecution_sanction_order_details \r\n"
            		+ "                    LEFT JOIN prosecution.prosecution_court_case_details ON prosecution.prosecution_sanction_order_details.pro_sanction_order_id = prosecution.prosecution_court_case_details.add_case_pro_sanction_order_id \r\n"
            		+ "                    LEFT JOIN authentication.court_type ON prosecution.prosecution_court_case_details.court_type_id = authentication.court_type.id \r\n"
            		+ "                    WHERE prosecution.prosecution_sanction_order_details.finalisation_status='2' and prosecution.prosecution_court_case_details.approve_status='2' \r\n"
            		+ "                    ORDER BY prosecution.prosecution_sanction_order_details.pro_sanction_order_id ASC;";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> objects = query.getResultList();
            List<AssignTaskDTO> list = new ArrayList<>();

            if (objects.size() > 0) {
                for (int j = objects.size() - 1; j >= 0; j--) {
                    Object[] object = objects.get(j);
                    AssignTaskDTO assignDTO = new AssignTaskDTO();

                    String SanctionFileName = (String) object[0];
                    String proSanctionOrderNo = (String) object[1];
                    Date prosanctiondate = (Date) object[2];
                    Long proSanctionID = (Long) object[3];
                  //  long proSanctionID1 = proSanctionID.longValue();
                    String proSanctionCompName = (String) object[4];
                    String courtCaseNo = (String) object[5];
                    Integer courtCaseApproveStatus = (Integer) object[6];
                    String causeTitle = (String) object[7];
                    String courtType = (String) object[8];

                    assignDTO.setCaseTitle(proSanctionCompName);
                    assignDTO.setProSanctionFileName(SanctionFileName);
                    assignDTO.setProSanctionDate(prosanctiondate);
                    assignDTO.setProSectionOrderNumber(proSanctionOrderNo);
                    assignDTO.setCourtCaseNo(courtCaseNo);
                    assignDTO.setCourtType(courtType);
                    assignDTO.setCourtCaseApproveStatus(courtCaseApproveStatus);
                    assignDTO.setCauseTitle(causeTitle);
                    assignDTO.setId(proSanctionID);

                    list.add(assignDTO);
                }
            }

            return list.isEmpty() ? list : list;

        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    public List<AssignTaskDTO> findAllSectionOrderDetailsWithAssignTask(int offsetValue, int limitValue) {
        String sql = """
            SELECT 
                p.pro_sanction_order_id, p.pro_sanction_file_name, p.pro_section_order_number, p.pro_sanction_date, 
                p.case_title, p.file_number, t.task, u.unit_name, ud.first_name, 
                ud.middle_name, ud.last_name, COUNT(*) OVER() 
            FROM prosecution.prosecution_sanction_order_details p
            LEFT JOIN prosecution.assigned_task_puh at ON p.pro_sanction_order_id = at.add_case_pro_sanction_order_id
            LEFT JOIN authentication.tasks t ON at.create_task_id = t.id
            LEFT JOIN authentication.unit_details u ON at.unit_unit_id = u.unit_id
            LEFT JOIN authentication.user_details ud ON at.user_id = ud.id
            ORDER BY p.pro_sanction_order_id DESC 
            LIMIT ? OFFSET ? 
        """;

        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, limitValue);
            query.setParameter(2, offsetValue);

            @SuppressWarnings("unchecked")
            List<Object[]> objects = query.getResultList();
            if (objects.isEmpty()) {
                return List.of(); // Return an immutable empty list
            }

            return objects.stream()
                    .map(object -> {
                        AssignTaskDTO assignDTO = new AssignTaskDTO();
                        assignDTO.setId(((Long) object[0]));
                        assignDTO.setProSanctionFileName((String) object[1]);
                        assignDTO.setProSectionOrderNumber((String) object[2]);
                        assignDTO.setProSanctionDate((Date) object[3]);
                        assignDTO.setCaseTitle((String) object[4]);
                        assignDTO.setFileNumber((String) object[5]);
                        assignDTO.setTask((String) object[6]);
                        assignDTO.setUnitName((String) object[7]);
                        assignDTO.setFirstName((String) object[8]);
                        assignDTO.setMiddelName((String) object[9]);
                        assignDTO.setLastName((String) object[10]);
                        assignDTO.setTotalcount(((Number) object[11]).intValue());

                        return assignDTO;
                    })
                    .toList(); // Immutable list using Java 17's toList()
        } catch (NoResultException e) {
            return List.of(); // Return an immutable empty list
        }
    }
    public List<AssignTaskDTO> findAllSectionOrderDetailsWithAssignTask1() {
        String sql = """
            SELECT 
                p.pro_sanction_order_id, 
                p.pro_sanction_file_name, 
                p.pro_section_order_number, 
                p.pro_sanction_date, 
                p.case_title, 
                p.file_number, 
                t.task, 
                u.unit_name, 
                ud.first_name, 
                ud.middle_name, 
                ud.last_name, 
                COUNT(*) OVER() 
            FROM prosecution.prosecution_sanction_order_details p
            LEFT JOIN prosecution.assigned_task_puh at 
                ON p.pro_sanction_order_id = at.add_case_pro_sanction_order_id
            LEFT JOIN authentication.tasks t 
                ON at.create_task_id = t.id
            LEFT JOIN authentication.unit_details u 
                ON at.unit_unit_id = u.unit_id
            LEFT JOIN authentication.user_details ud 
                ON at.user_id = ud.id
            ORDER BY p.pro_sanction_order_id DESC
        """;

        try {
            Query query = entityManager.createNativeQuery(sql);

            @SuppressWarnings("unchecked")
            List<Object[]> objects = query.getResultList();
            if (objects.isEmpty()) {
                return List.of(); // Return an immutable empty list
            }

            return objects.stream()
                    .map(object -> {
                        AssignTaskDTO assignDTO = new AssignTaskDTO();
                        assignDTO.setId(((Number) object[0]).longValue());
                        assignDTO.setProSanctionFileName((String) object[1]);
                        assignDTO.setProSectionOrderNumber((String) object[2]);
                        assignDTO.setProSanctionDate((Date) object[3]);
                        assignDTO.setCaseTitle((String) object[4]);
                        assignDTO.setFileNumber((String) object[5]);
                        assignDTO.setTask((String) object[6]);
                        assignDTO.setUnitName((String) object[7]);
                        assignDTO.setFirstName((String) object[8]);
                        assignDTO.setMiddelName((String) object[9]);
                        assignDTO.setLastName((String) object[10]);
                        assignDTO.setTotalcount(((Number) object[11]).intValue());
                        return assignDTO;
                    })
                    .toList();
        } catch (NoResultException e) {
            return List.of();
        }
    }
	/*
	 * public List<AssignTaskDTO> findAllSectionOrderDetailsWithAssignTask(int
	 * offsetValue, int limitValue) { try { String sql =
	 * "SELECT prosecution.prosecution_sanction_order_details.pro_sanction_order_id, prosecution.prosecution_sanction_order_details.pro_sanction_file_name, "
	 * +
	 * "prosecution.prosecution_sanction_order_details.pro_section_order_number, prosecution.prosecution_sanction_order_details.pro_sanction_date, "
	 * +
	 * "prosecution.prosecution_sanction_order_details.case_title, prosecution.prosecution_sanction_order_details.file_number, "
	 * +
	 * "authentication.tasks.task, authentication.unit_details.unit_name, authentication.user_details.first_name, "
	 * +
	 * "authentication.user_details.middle_name, authentication.user_details.last_name, "
	 * + "COUNT(*) OVER() " + "FROM prosecution.prosecution_sanction_order_details "
	 * +
	 * "LEFT JOIN prosecution.assigned_task_puh ON prosecution.prosecution_sanction_order_details.pro_sanction_order_id = prosecution.assigned_task_puh.add_case_pro_sanction_order_id "
	 * +
	 * "LEFT JOIN authentication.tasks ON prosecution.assigned_task_puh.create_task_id = authentication.tasks.id "
	 * +
	 * "LEFT JOIN authentication.unit_details ON prosecution.assigned_task_puh.unit_unit_id = authentication.unit_details.unit_id "
	 * +
	 * "LEFT JOIN authentication.user_details ON prosecution.assigned_task_puh.user_id = authentication.user_details.id "
	 * +
	 * "ORDER BY prosecution.prosecution_sanction_order_details.pro_sanction_order_id DESC LIMIT "
	 * + limitValue + " OFFSET " + offsetValue;
	 * 
	 * Query query = entityManager.createNativeQuery(sql); List<Object[]> objects =
	 * query.getResultList(); List<AssignTaskDTO> list = new ArrayList<>();
	 * 
	 * if (objects.size() > 0) { for (Object[] object : objects) { AssignTaskDTO
	 * assignDTO = new AssignTaskDTO();
	 * 
	 * BigInteger proSanctionID = (BigInteger) object[0]; long proSanctionID1 =
	 * proSanctionID.longValue();
	 * 
	 * String SanctionFileName = (String) object[1]; String proSanctionOrderNo =
	 * (String) object[2]; Date prosanctiondate = (Date) object[3]; String
	 * proSanctionCompName = (String) object[4]; String fileNumber = (String)
	 * object[5]; String task = (String) object[6]; String unit = (String)
	 * object[7]; String firstName = (String) object[8]; String middelName =
	 * (String) object[9]; String lastName = (String) object[10]; BigInteger
	 * totoalRecord = (BigInteger) object[11]; int totalcount =
	 * totoalRecord.intValue();
	 * 
	 * assignDTO.setTotalcount(totalcount);
	 * assignDTO.setCaseTitle(proSanctionCompName);
	 * assignDTO.setProSanctionFileName(SanctionFileName);
	 * assignDTO.setProSanctionDate(prosanctiondate);
	 * assignDTO.setProSectionOrderNumber(proSanctionOrderNo);
	 * assignDTO.setFirstName(firstName); assignDTO.setMiddelName(middelName);
	 * assignDTO.setLastName(lastName); assignDTO.setTask(task);
	 * assignDTO.setFileNumber(fileNumber); assignDTO.setUnitName(unit);
	 * assignDTO.setId(proSanctionID1);
	 * 
	 * list.add(assignDTO); } }
	 * 
	 * return list.isEmpty() ? list : list;
	 * 
	 * } catch (NoResultException e) { return null; } }
	 */
}
