package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.pams.entity.UnitDetails;

@Service
public interface UnitDetailsRepository extends JpaRepository<UnitDetails, Long> {


}
