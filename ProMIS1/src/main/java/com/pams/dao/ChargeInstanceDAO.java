package com.pams.dao;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;

@Repository
@Transactional
public class ChargeInstanceDAO {
    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings({ "unchecked" })
    public List<Object[]> findActSectionSubSectionList(AssignedTaskPuhAfterCOurt assignTaskID) {
        try {
            String sql = "SELECT DISTINCT ON (charge_id, samecharge_type) samecharge_type, id, charge_id " + 
                         "FROM prosecution.charge_instace_main " + 
                         "WHERE assigned_task_id = :task_id " + 
                         "ORDER BY charge_id DESC, samecharge_type DESC";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("task_id", assignTaskID.getId()); // Assuming getId() returns the task ID

            List<Object[]> objects = query.getResultList();

            return objects.isEmpty() ? Collections.emptyList() : objects;
        } catch (NoResultException e) {
            e.printStackTrace(); // Log or handle the exception as needed
            return Collections.emptyList();
        }
    }
}
