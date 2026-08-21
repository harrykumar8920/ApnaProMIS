package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.Type;

public interface TypeRepository extends JpaRepository<Type, Long> {

}
