package com.pams.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pams.entity.AppUser;
import com.pams.entity.UserDetails;
import com.pams.entity.UserRole;
import com.pams.utils.PromisException;

@Repository
@Transactional
public class AppUserDAO {

    @Autowired
    private EntityManager entityManager;

    public AppUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e "
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);

            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long findUserDesignation(Long id) {
        try {
            String sql = "Select designation_id from authentication.user_details where id=:id";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("id", id);
            int designationId = (int) query.getResultList().get(0);
            return Long.valueOf(designationId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public UserDetails findUserDetails(AppUser appUser) {
        try {
            String sql = "Select e from " + UserDetails.class.getName() + " e "
                    + " Where e.userId = :userId ";

            Query query = entityManager.createQuery(sql, UserDetails.class);
            query.setParameter("userId", appUser);

            return (UserDetails) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserRole getRoleId(Long userId) {
        String sql = "Select ur from " + UserRole.class.getName() + " ur "
                + " where ur.appUser.userId = :userId ";

        Query query = this.entityManager.createQuery(sql, UserRole.class);
        query.setParameter("userId", userId);
        return (UserRole) query.getResultList().get(0);
    }
}
