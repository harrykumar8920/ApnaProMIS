package com.pams.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.entity.ActCompundRelevantSection;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Repository
@jakarta.transaction.Transactional
public class ActReportDao {

	@Autowired
	private jakarta.persistence.EntityManager entityManager;

	public List<ActCompundRelevantSection> getActAndSectionByCourtCaseId(int courtCaseId) throws SQLException {
	    String sql = "SELECT rs.act_id AS actID, rs.relevent_section AS relevantSection " +
	                 "FROM prosecution.charge_instace_main AS m " +
	                 "INNER JOIN prosecution.charge_act_compund_relevant_section AS rs " +
	                 "ON rs.charge_instance_main_id = m.id " +
	                 "WHERE m.procourtdtl_court_case_id = :pID " +
	                 "GROUP BY rs.act_id, rs.relevent_section " +
	                 "ORDER BY rs.act_id";

	    try {
	        Query query = entityManager.createNativeQuery(sql);
	        query.setParameter("pID", courtCaseId);  // Ensure parameter name matches with query

	        List<Object[]> result = query.getResultList();
	        List<ActCompundRelevantSection> list = new ArrayList<>();

	        // Map the result to ActCompundRelevantSection objects
	        if (result.size() > 0) {
	            for (Object[] row : result) {
	                Integer actId = (Integer) row[0];  // act_id
	                String relevantSection = (String) row[1];  // relevant_section

	                ActCompundRelevantSection actCompundRelevantSection = new ActCompundRelevantSection(actId, relevantSection);
	              

	                list.add(actCompundRelevantSection);
	            }
	        }

	        return list;

	    } catch (NoResultException e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
}
