package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.Instance;

public interface InstanceRepository extends JpaRepository<Instance, Long> {

}
