package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.SnmsErrorReference;

@Repository
public interface SnmsErrorRefRepository extends JpaRepository<SnmsErrorReference, Long>{

}
