package com.pams.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;




@Repository
@Transactional
public class ReportDao {

	@Autowired
	private EntityManager entityManager;
	
	
	
	
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> findList() {
		
		
		
		List<Object[]> Listfind;

		try {
			
			String sql="select pcc.cause_Title,pd.name,pd.mobile,phd.last_hearing_date from prosecution.pro_hearing_details phd, prosecution.prosecution_Court_Case_Details pcc,prosecution.pairavi_details pd";
			
			Query query = entityManager.createNativeQuery(sql);
			
			
			Listfind = query.getResultList();
			
			if (!Listfind.isEmpty())
				return Listfind;
			else
				return Listfind;

		} catch (NoResultException e) {
			return null;
		}
	
	}

	@SuppressWarnings({ "unchecked" })
	public List<Object[]> findListByCourtTypeAndDate() {
		
		
		
		List<Object[]> Listfind;

		try {
			
			String sql="select pcc.cause_Title,pd.name,pd.mobile,phd.last_hearing_date from prosecution.pro_hearing_details phd, prosecution.prosecution_Court_Case_Details pcc,prosecution.pairavi_details pd";
			
			Query query = entityManager.createNativeQuery(sql);
			
			
			Listfind = query.getResultList();
			
			if (!Listfind.isEmpty())
				return Listfind;
			else
				return Listfind;

		} catch (NoResultException e) {
			return null;
		}
	
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object> findByAssignedTashPuh() {
		
		
		
		List<Object> Listfind;

		try {
			
			String sql="SELECT * FROM prosecution.procomplaint_report_template;";
			
			Query query = entityManager.createNativeQuery(sql);
			
			
			Listfind = query.getResultList();
			
			if (!Listfind.isEmpty())
				return Listfind;
			else
				return Listfind;

		} catch (NoResultException e) {
			return null;
		}
	
	}
	

}
