package com.pams.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.entity.ActSecDetailsInfo;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.persistence.NoResultException;

@Repository
@jakarta.transaction.Transactional
public class ActSectionDAO {

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public boolean findActSectionSubSectionList(ProCourtCaseDetails proCourtCaseDetails) {
        try {
            String sql = "Select e.act_id, e.section_id, e.sub_section_id from prosecution.act_sec_details_info e "
                    + "where e.act_id = " + proCourtCaseDetails.getAct().getId() 
                    + " and e.section_id = " + proCourtCaseDetails.getSection().getId()
                    + " and e.sub_section_id = " + proCourtCaseDetails.getSubsection().getId()
                    + " and e.procourtdtlid = " + proCourtCaseDetails.getId();

            jakarta.persistence.Query query = entityManager.createNativeQuery(sql);

            List<ActSecDetailsInfo> Listfind = query.getResultList();

            return !Listfind.isEmpty();

        } catch (NoResultException e) {
            return false; // Adjusting the exception handling as per Java 17
        }
    }

    @SuppressWarnings("unchecked")
    public boolean findActSectionlist(ProCourtCaseDetails proCourtCaseDetails) {
        try {
            String sql = "Select e.act_id, e.section_id, e.sub_section_id from prosecution.act_sec_details_info e "
                    + "where e.act_id = " + proCourtCaseDetails.getAct().getId()
                    + " and e.section_id = " + proCourtCaseDetails.getSection().getId()
                    + " and e.procourtdtlid = " + proCourtCaseDetails.getId()
                    + " and e.created_by_id = " + proCourtCaseDetails.getCreatedBy().getId();

            jakarta.persistence.Query query = entityManager.createNativeQuery(sql);

            List<ActSecDetailsInfo> Listfind = query.getResultList();

            return !Listfind.isEmpty();

        } catch (NoResultException e) {
            return false; // Adjusting the exception handling as per Java 17
        }
    }
}
