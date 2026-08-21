package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.country;

public interface countryRepository extends JpaRepository<country, Long> {

}
