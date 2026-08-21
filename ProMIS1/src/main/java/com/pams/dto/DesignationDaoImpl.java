package com.pams.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pams.entity.AddDesignation;
import com.pams.service.AddDesignationRepository;
import com.pams.utils.PromisException;

@Service
@Transactional
public class DesignationDaoImpl{
	
	@Autowired
	private AddDesignationRepository repo;
	public void save(AddDesignation des) throws PromisException,Exception
	{
		
		 try {
			 repo.save(des);
		    } catch (Exception er) {
		      PromisException dex = new PromisException(er.getMessage(), er.getCause());
		      dex.setERROR_CODE("001");
		      if (des == null)
		        dex.setParameter(new String[] { "Nill designation" });
		      else
		        dex.setParameter(new String[] {des + "" });
		      throw dex;
		    }
		
	}

}
