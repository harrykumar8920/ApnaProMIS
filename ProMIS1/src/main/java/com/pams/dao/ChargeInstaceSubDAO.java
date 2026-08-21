package com.pams.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.entity.AccusedCompCaseDtl;
import com.pams.entity.ChargeInstaceSub;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class ChargeInstaceSubDAO {
	@Autowired
	private EntityManager entityManager;

	public List<ChargeInstaceSub> findByChargeInstanceMainId(Long mainTable) {
		try {

			String sql = "Select e from " + ChargeInstaceSub.class.getName() + " e " //
					+ " Where e.chargeInstanceMain = :id";

			Query query = entityManager.createQuery(sql, AccusedCompCaseDtl.class);

			query.setParameter("mainTable", mainTable);
			List<ChargeInstaceSub> aclist = query.getResultList();
			
				return aclist;

			// return (SummonDetails) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
