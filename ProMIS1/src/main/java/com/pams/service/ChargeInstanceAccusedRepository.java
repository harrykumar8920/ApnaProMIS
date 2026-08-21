package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.ChargeInstanceAccused;
@Repository
public interface ChargeInstanceAccusedRepository extends JpaRepository<ChargeInstanceAccused, Long> {

}
