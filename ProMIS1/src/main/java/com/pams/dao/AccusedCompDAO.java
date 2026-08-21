package com.pams.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.dto.ChargeInstaceSubDto;
import com.pams.dto.HolololoDTO;
import com.pams.entity.AccusedCompCaseDtl;
import com.pams.entity.ChargeInstaceMain;
import com.pams.entity.ChargeInstaceSub;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AccusedCompDAO {
	@Autowired
	private EntityManager entityManager;

	public AccusedCompCaseDtl findByProCourtId(Long proCourtId) {

		try {

			String sql = "Select e from " + AccusedCompCaseDtl.class.getName() + " e " //
					+ " Where e.proCourtId = :proCourtId";

			Query query = entityManager.createQuery(sql, AccusedCompCaseDtl.class);

			query.setParameter("proCourtId", proCourtId);
			List<AccusedCompCaseDtl> aclist = query.getResultList();
			if (!aclist.isEmpty())
				return aclist.get(0);
			else
				return null;

			// return (SummonDetails) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<ChargeInstaceMain> findChargeByAccused(Long id, Long aId) {
		try {
			String sql = "SELECT * FROM prosecution.charge_instace_main AS m "
					+ "INNER JOIN charge_instace_main_accuse_name AS ca ON m.id = ca.charge_instace_main_id "
					+ "WHERE ca.accuse_name_accused_id = :id and m.assigned_task_id= :aId";

			Query query = entityManager.createNativeQuery(sql, ChargeInstaceMain.class);
			query.setParameter("id", id);
			query.setParameter("aId", aId);
			List<ChargeInstaceMain> chargeInstaceMain = query.getResultList();
			return chargeInstaceMain;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	/*
	 * public List<Charge> findChargeByAccusedAndAssignedTask(String id, Long aId) {
	 * try { String sql =
	 * "SELECT * FROM authentication.charge_details WHERE id NOT IN " +
	 * "(SELECT c.charge_id FROM prosecution.charge_instace_main AS c " +
	 * "LEFT JOIN charge_instace_main_accuse_name AS an ON c.id = an.charge_instace_main_id "
	 * +
	 * "WHERE assigned_task_id = :aId AND an.accuse_name_accused_id IN (:idList))";
	 * 
	 * Query query = entityManager.createNativeQuery(sql, Charge.class);
	 * 
	 * // Convert the comma-separated string to a list of Long
	 * 
	 * List<Long> idList = Arrays.asList(id.split(",")) .stream()
	 * .map(Long::parseLong) .collect(Collectors.toList());
	 * 
	 * query.setParameter("idList", idList); query.setParameter("aId", aId);
	 * 
	 * @SuppressWarnings("unchecked") List<Charge> chargeList =
	 * query.getResultList(); return chargeList; } catch (NoResultException e) {
	 * return null; } }
	 */

	
	
	
	
	
	
	

	public List<ChargeInstaceSubDto> findChargeByAccusedSub(Long id, Long aId,Long accusedId) {
		try {
			/*
			 * String sql =
			 * "select s.instance_remarks,s.instance_id,s.punishment_id,pds.punishment1,ids.instanse_name,s.id from prosecution.charge_instace_main as m\r\n"
			 * +
			 * "INNER JOIN prosecution.charge_instace_sub as s on m.id=s.charge_instance_main_id\r\n"
			 * +
			 * "INNER JOIN authentication.instance_details as ids on ids.id=s.instance_id\r\n"
			 * + "INNER JOIN authentication.punishment as pds on pds.id=s.punishment_id\r\n"
			 * +
			 * "where m.charge_id= :id and m.assigned_task_id= :aId and s.punishment_done='false'"
			 * ;
			 */
			
			String sql ="select s.instance_remarks,s.instance_id,s.punishment_id,pds.punishment1,ids.instanse_name,s.id from prosecution.charge_instace_main as m\r\n" + 
			"					INNER JOIN prosecution.charge_instace_sub as s on m.id=s.charge_instance_main_id\r\n" + 
			"					INNER JOIN authentication.instance_details as ids on ids.id=s.instance_id\r\n" + 
			"					INNER JOIN authentication.punishment as pds on pds.id=s.punishment_id\r\n" + 

			"					where m.charge_id= :id and m.accuse_name_accused_id = :accusedId and m.assigned_task_id= :aId and s.id not IN (select cis1.id from prosecution.prosecution_accused_status_new as an \r\n" + 
			"INNER JOIN prosecution.charge_instace_sub AS cis1 ON an. instance_id_id = cis1.id\r\n" + 
			"where an.assigned_task_id=:aId and an.accused_id_accused_id=:accusedId)";

			Query query = entityManager.createNativeQuery(sql);

			query.setParameter("id", id);
			query.setParameter("aId", aId);
			query.setParameter("accusedId", accusedId);
			

			@SuppressWarnings("unchecked")
			List<Object[]> objects = query.getResultList();
			List<ChargeInstaceSubDto> chageDto=new ArrayList<ChargeInstaceSubDto>();
			if (objects.size() > 0) {
				for (int j = objects.size() - 1; j >= 0; j--) {
					Object[] object = objects.get(j);
					
					ChargeInstaceSubDto chargeInstaceDto=new ChargeInstaceSubDto();
					String iRemark = (String) object[0];
					Integer instanceId0 = (Integer) object[1];
					long instanseId = instanceId0.longValue();
					Integer punishmentId = (Integer) object[2];
					Integer punishId = punishmentId.intValue();
					String punishMnetName = (String) object[3];
					String instanceName = (String) object[4];
					Long mainId = (Long) object[5];
					long subTableId = mainId.longValue();
					chargeInstaceDto.setSubTableId(subTableId);
					chargeInstaceDto.setInstanceRemarks(iRemark);
					chargeInstaceDto.setInstanceName(instanceName);
					chargeInstaceDto.setPunishmentName(punishMnetName);
					chargeInstaceDto.setInstanceId(instanseId);
					chargeInstaceDto.setPunishmentId(punishId);
					chageDto.add(chargeInstaceDto);
				}
			}
			
			if (!(chageDto).isEmpty())
				return chageDto;
			else
				return chageDto;
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<HolololoDTO> findChargeInstaceSub(Long id) {
		try {
			String sql = "select s.instance_remarks,s.punishment_id,p.punishment1  from prosecution.charge_instace_sub as s \r\n" + 
					"INNER JOIN authentication.punishment as p on p.id=s.punishment_id\r\n" + 
					"where s.id= :id" ;
					

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("id", id);
			List<Object[]> objects=  query.getResultList();
			List<ChargeInstaceSub> xyz=new ArrayList<ChargeInstaceSub>();
			List<HolololoDTO> chargeInstaceMain= new ArrayList<HolololoDTO>();
			if (objects.size() > 0) {
				for (int j = objects.size() - 1; j >= 0; j--) {
					Object[] object = objects.get(j);
					HolololoDTO holololoDTO=new HolololoDTO();
					String instanceRemarks = (String) object[0];
				
					Integer punishmentId = (Integer) object[1];
					
					Integer punishment = punishmentId.intValue();
					String punishmentName = (String) object[2];
					holololoDTO.setInstanceRemarks(instanceRemarks);
					holololoDTO.setPunishment(punishment);
					holololoDTO.setPunishmentName(punishmentName);
					chargeInstaceMain.add(holololoDTO);
				}
			}
		
			
			
			return chargeInstaceMain;
		} catch (NoResultException e) {
			return null;
		}
	}

}
