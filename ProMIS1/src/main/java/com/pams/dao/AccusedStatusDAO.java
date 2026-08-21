package com.pams.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.dto.ViewAccusedDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AccusedStatusDAO {
	@Autowired
	private EntityManager entityManager;

	public List<ViewAccusedDTO> findAccusedStatusByAccusedName(Long id, Long aId) {
		try {
			
			String sql = "SELECT m.description,cis1.relevent_section,cis1.compoundability,cd.charge_name,act.act,p.punishment1,a.accused_name FROM prosecution.charge_instace_main AS m  \r\n"

					+ "INNER JOIN prosecution.charge_act_compund_relevant_section AS cis1 ON m.id = cis1.charge_instance_main_id \r\n"

					+ "INNER JOIN authentication.act as act on cis1.act_id=act.id\r\n"

					+ "INNER JOIN authentication.charge_details as cd on m.charge_id=cd.id \r\n"

					+ "INNER JOIN authentication.punishment as p on cis1.punishment_id=p.id\r\n"

					+ "INNER JOIN prosecution.charge_instance_accused as hh on m.id=hh.charge_instance_main_id\r\n"

					+ "INNER JOIN prosecution.prosecution_accused_details as a on a.accused_id=hh.accuse_id\r\n"

					+ "WHERE hh.accuse_id = :id and m.assigned_task_id= :aId";
			Query query = entityManager.createNativeQuery(sql);

			query.setParameter("id", id);
			query.setParameter("aId", aId);

			@SuppressWarnings("unchecked")
			List<Object[]> objects = query.getResultList();

			List<ViewAccusedDTO> resultList = new ArrayList<>();

			for (Object[] row : objects) {
				ViewAccusedDTO dto = new ViewAccusedDTO();
				dto.setDescription((String) row[0]);
				dto.setRelevantSection((String) row[1]);
				dto.setCompoundability((String) row[2]);
				//dto.setInstanceRemarks((String) row[3]);
				dto.setChargeName((String) row[3]);
				dto.setAct((String) row[4]);
				dto.setPunishment((String) row[5]);
				//dto.setInstanceName((String) row[7]);
				dto.setAccusedName((String) row[6]);

				resultList.add(dto);
			}

			return resultList;
		} catch (Exception e) {
			// Handle exceptions appropriately
			e.printStackTrace();
			return Collections.emptyList(); // or throw an exception based on your error handling strategy
		}
	}

}
