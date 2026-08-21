package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.Discharge;

public interface DischargeRepository extends JpaRepository<Discharge, Long> {

}
