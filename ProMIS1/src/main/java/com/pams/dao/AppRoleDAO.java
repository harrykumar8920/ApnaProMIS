package com.pams.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pams.entity.AppRole;
import com.pams.entity.UserRole;
import com.pams.service.AppRoleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
@Transactional
public class AppRoleDAO {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AppRoleRepository appRole;

    public List<String> getRoleNames(Long userId) {
        String sql = "Select ur.appRole.roleName from " + UserRole.class.getName() + " ur "
                + " where ur.appUser.userId = :userId ";

        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public String getRoleName(Long userId) {
        String sql = "Select ur.appRole.roleName from " + UserRole.class.getName() + " ur "
                + " where ur.appUser.userId = :userId ";

        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return (String) query.getResultList().get(0);
    }

    @SuppressWarnings("unchecked")
    public List<AppRole> RoleList() {
        String sql = "Select e from " + AppRole.class.getName() + " e "
                + " where e.roleId != :adminId and e.roleId != :dirId";

        Query query = this.entityManager.createQuery(sql, AppRole.class);
        query.setParameter("adminId", 1L);
        query.setParameter("dirId", 5L);

        List<AppRole> Rolelist = query.getResultList();
        if (!Rolelist.isEmpty())
            return Rolelist;
        else
            return null;
    }
}
