package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AddState;
import com.pams.entity.District;

public interface districtRepository extends JpaRepository<District, Long> {

	List<District> findAllByState(AddState state);

	List<District> findAllById(long l);

}
