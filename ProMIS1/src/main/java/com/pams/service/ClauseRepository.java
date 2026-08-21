package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.Clause;

public interface ClauseRepository extends JpaRepository<Clause , Long> {

	Clause findByClause(String clause);
}
