package com.pams.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.pams.entity.AuditTrail;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Repository
@jakarta.transaction.Transactional
public class AuditBeanDaoImpl implements AuditBeanDao {
    @Autowired
    private EntityManager entityManager;

    public void saveAuditTrail(AuditTrail auditTrail) {
        entityManager.persist(auditTrail);
    }

    @Override
    public List<AuditTrail> findAllByActionDate(Date date1) {
        try {
            String sql = "Select e from " + AuditTrail.class.getName() + " e " //
                    + " Where Date(e.actionDate) = :actionDate";

            Query query = entityManager.createQuery(sql, AuditTrail.class);

            if (date1 != null) {
                query.setParameter("actionDate", date1);
            }

            List<AuditTrail> listAuditTrail = query.getResultList();

            return listAuditTrail.isEmpty() ? null : listAuditTrail;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<AuditTrail> findAllByActionDate(Date date1, Sort sort) {
        try {
            String sql = "Select e from " + AuditTrail.class.getName() + " e " +
                         " Where Date(e.actionDate) = :actionDate " +
                         " order by e.id desc";

            Query query = entityManager.createQuery(sql, AuditTrail.class);

            if (date1 != null) {
                query.setParameter("actionDate", date1);
            }

            List<AuditTrail> listAuditTrail = query.getResultList();

            return listAuditTrail.isEmpty() ? null : listAuditTrail;
        } catch (NoResultException e) {
            return null;
        }
    }
}
