package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.Charge;

public interface ChargeRepository extends JpaRepository<Charge, Long> {

}
