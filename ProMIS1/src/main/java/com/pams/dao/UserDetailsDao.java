package com.pams.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.entity.UnitDetails;
import com.pams.entity.UserDetails;
import com.pams.entity.UserRole;
import com.pams.service.UserManagementCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional

public class UserDetailsDao implements UserManagementCustom {

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<UserDetails> findByUnit(UnitDetails unitDetails) {
		try {
			// String sql = "Select * from " + UserDetails.class + " e Where e.unit =
			// "+UnitDetails.class;

			String sql = "Select e from " + UserDetails.class.getName() + " e " //
					+ " where e.designation.id !=1 and e.unit = : unitDetails order by e.designation.order";

			Query query = entityManager.createQuery(sql, UserDetails.class);
			query.setParameter("unitDetails", unitDetails);
			List<UserDetails> unitList = query.getResultList();
			if(unitList != null)
			return unitList;
			else
			return unitList;	
		} catch (NoResultException e) {
			return null;
		}
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findByCase(Long caseId) {
		try {
			String sql = "select ud.salutation,ud.full_name,d.designation,i.isio from authentication.user_details ud,investigation.inspector i,"
					+ "investigation.case_details cd,authentication.designation d where cd.id=i.case_details_id and "
					+ "i.app_user_user_id = ud.id and d.id=ud.designation_id and i.case_details_id="+caseId;
			Query query = this.entityManager.createNativeQuery(sql);
			return (List<Object[]>) query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}*/


		
	@Override
	public List<UserDetails> findByRole(int roleUser,UnitDetails unitDetails) {
		try {
			// String sql = "Select * from " + UserDetails.class + " e Where e.unit =
			// "+UnitDetails.class;

			String sql = "Select e from " + UserDetails.class.getName() + " e " //
					+ " where e.designation.id !=1 and e.unit = : unitDetails and e.userId in ("+"Select r.appUser from " + UserRole.class.getName() +" r " 
					 +"where r.appRole="+roleUser  +") order by e.designation.order";

			Query query = entityManager.createQuery(sql, UserDetails.class);
			if(unitDetails != null) {
				query.setParameter("unitDetails", unitDetails);
			}
			else {
				query.setParameter("unitDetails", new UnitDetails());
			}
		
			return (List<UserDetails>) query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	 public UserDetails findUserDetailsMobile(String mobile) {
		  try {
	            String sql = "Select e from " + UserDetails.class.getName() + " e " //
	                    + " Where e.primaryMobile = :primaryMobile OR e.alternateNo = :alternateNo AND e.alternateNo!=''";
	            Query query = entityManager.createQuery(sql, UserDetails.class);
	            query.setParameter("primaryMobile", mobile);
	            query.setParameter("alternateNo", mobile);
	            return (UserDetails) query.getSingleResult();
	        } catch (NoResultException e) {
	            return null;
	        }
	    }
	 
	}
