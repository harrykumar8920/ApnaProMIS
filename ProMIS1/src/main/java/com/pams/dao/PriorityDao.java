package com.pams.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import com.pams.dto.PriorityCaseDTO;
import com.pams.dto.ReportPriorityInput;
import com.pams.entity.ReportWeeklyInput;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Repository
@jakarta.transaction.Transactional
public class PriorityDao {
	@Autowired
	private jakarta.persistence.EntityManager entityManager;

	public List<PriorityCaseDTO> getPriorityCases(int casePriority) {
		String sql = "SELECT sah.case_title as companyName, pcd.cause_title as caseTitle, "
				+ "ct.court_name as courtJurisdiction, pcd.brief as briefOfTheCase, "
				+ "ps.pro_status as caseStatus, pcd.court_case_no as caseNo, "
				+ "act.act as companyAct, offi.name as pairaviOfficer, "
				+ "counsel.name as counsel, counsel.mobile as counselNumber, " + "ofp.name as officerFromProsecution, "
				+ "hd.last_hearing_date as lastDateOfHearing, " + "hd.next_hearing_date as nextDateOfHearing, "
				+ "hd.remarks, " + "cars.relevent_section " + "FROM prosecution.prosecution_sanction_order_details sah "
				+ "LEFT JOIN prosecution.prosecution_court_case_details pcd ON pcd.add_case_pro_sanction_order_id = sah.pro_sanction_order_id "
				+ "LEFT JOIN authentication.court_type ct ON ct.id = pcd.court_type_id "
				+ "LEFT JOIN prosecution.pro_hearing_details hd ON hd.procourtdtl_court_case_id = pcd.court_case_id "
				+ "LEFT JOIN authentication.prosecution_status ps ON ps.pro_status_id = hd.status_pro_status_id "
				+ "LEFT JOIN prosecution.charge_instace_main cim ON cim.procourtdtl_court_case_id = pcd.court_case_id "
				+ "LEFT JOIN prosecution.charge_act_compund_relevant_section cars ON cars.charge_instance_main_id = cim.id "
				+ "LEFT JOIN authentication.act act ON act.id = cars.act_id "
				+ "LEFT JOIN prosecution.pairavi_details pd ON pd.procourtdtl_court_case_id = pcd.court_type_id "
				+ "LEFT JOIN authentication.officers offi ON offi.id = pd.pairavi_officer_id "
				+ "LEFT JOIN authentication.officers counsel ON counsel.id = hd.counsel_name_id "
				+ "LEFT JOIN authentication.officers ofp ON ofp.id = hd.officer_id "
				+ "WHERE pcd.case_status_check = :priority";
		try {
			Query query = entityManager.createNativeQuery(sql);

			query.setParameter("priority", casePriority);
			List<Object[]> results = query.getResultList();
			List<PriorityCaseDTO> list = new ArrayList<>();
			for (Object[] row : results) {
				PriorityCaseDTO dto = new PriorityCaseDTO();
				dto.setCompanyName((String) row[0]);
				dto.setCaseTitle((String) row[1]);
				dto.setCourtJurisdiction((String) row[2]);
				dto.setBriefOfTheCase((String) row[3]);
				dto.setCaseStatus((String) row[4]);
				dto.setCaseNo((String) row[5]);
				dto.setCompanyAct((String) row[6]);
				dto.setPairaviOfficer((String) row[7]);
				dto.setCounsel((String) row[8]);
				dto.setCounselNumber((String) row[9]);
				dto.setOfficerFromProsecution((String) row[10]);
				dto.setDate(row[11] != null ? row[11].toString() : null);
				dto.setToDate(row[12] != null ? row[12].toString() : null);
				dto.setRemerks((String) row[13]);
				dto.setSec((String) row[14]);
				list.add(dto);

			}
			return list;
		} catch (NoResultException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<PriorityCaseDTO> getPriorityCases3(int casePriority, long stateId, long userId) {
		String sql = "				SELECT sah.case_title as companyName, pcd.cause_title as caseTitle, \r\n"
				+ "				ct.court_name as courtJurisdiction, pcd.brief as briefOfTheCase, \r\n"
				+ "				ps.pro_status as caseStatus, pcd.court_case_no as caseNo, \r\n"
				+ "				 offi.name as pairaviOfficer, \r\n"
				+ "				counsel.name as counsel, counsel.mobile as counselNumber,ofp.name as officerFromProsecution, \r\n"
				+ "				hd.last_hearing_date as lastDateOfHearing, hd.next_hearing_date as nextDateOfHearing, \r\n"
				+ "				hd.remarks, CONCAT(pros.salutation,' ', pros.first_name, ' ', pros.middle_name, ' ', pros.last_name) as name,pcd.case_status_check as status\r\n"
				+ "				FROM prosecution.prosecution_court_case_details pcd \r\n"
				+ "				LEFT JOIN  prosecution.prosecution_sanction_order_details sah ON pcd.add_case_pro_sanction_order_id = sah.pro_sanction_order_id \r\n"
				+ "				LEFT JOIN authentication.court_type ct ON ct.id = pcd.court_type_id \r\n"
				+ "				LEFT JOIN prosecution.pro_hearing_details hd  ON hd.id = (SELECT MAX(hd.id)FROM prosecution.pro_hearing_details hd WHERE hd.procourtdtl_court_case_id = pcd.court_case_id)\r\n"
				+ "				LEFT JOIN authentication.prosecution_status ps ON ps.pro_status_id = hd.status_pro_status_id 							\r\n"
				+ "				LEFT JOIN prosecution.pairavi_details pd ON pd.procourtdtl_court_case_id = pcd.court_type_id \r\n"
				+ "				LEFT JOIN authentication.officers offi ON offi.id = pd.pairavi_officer_id \r\n"
				+ "				LEFT JOIN authentication.officers counsel ON counsel.id = hd.counsel_name_id \r\n"
				+ "				LEFT JOIN authentication.officers ofp ON ofp.id = hd.officer_id \r\n"
				+ "				LEFT JOIN authentication.user_details pros ON pros.id = pcd.created_by_id "
				+ "WHERE (:caseStatus = 0 OR pcd.case_status_check = :caseStatus) "
				+ "AND (:createdBy = 0 OR pcd.created_by_id = :createdBy) "
				+ "AND (:stateId = 0 OR pcd.state_id = :stateId)";
		try {
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("caseStatus", casePriority);
			query.setParameter("createdBy", userId);
			query.setParameter("stateId", stateId);
			List<Object[]> results = query.getResultList();
			List<PriorityCaseDTO> list = new ArrayList<>();
			for (Object[] row : results) {
				PriorityCaseDTO dto = new PriorityCaseDTO();
				dto.setCompanyName((String) row[0]);
				dto.setCaseTitle((String) row[1]);
				dto.setCourtJurisdiction((String) row[2]);
				dto.setBriefOfTheCase((String) row[3]);
				dto.setCaseStatus((String) row[4]);
				dto.setCaseNo((String) row[5]);
				
				dto.setPairaviOfficer((String) row[6]);
				dto.setCounsel((String) row[7]);
				dto.setCounselNumber((String) row[8]);
				dto.setOfficerFromProsecution((String) row[9]);
				dto.setDate(row[10] != null ? row[10].toString() : null);
				dto.setToDate(row[11] != null ? row[11].toString() : null);
				dto.setRemerks((String) row[12]);
				
				dto.setName((String) row[13]);
				dto.setStatus(row[14] != null ? ((Number) row[14]).intValue() : 0);
				dto.setCompanyAct("Om Namah Sivay");
				dto.setSec("Jai Ganeshay Namah");
				list.add(dto);

			}
			return list;
		} catch (NoResultException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
