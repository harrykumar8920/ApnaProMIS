package com.pams.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.TypeofBench;
import java.lang.String;
import java.util.List;


public interface TypeofBenchRepository extends JpaRepository<TypeofBench, Long> {
	
	List<TypeofBench> findByBenchContainingOrBenchStartsWith(String bench,String bench1);
	List<TypeofBench> findByBenchNotContaining(String bench);
	
	TypeofBench findByBench(String bench);
	
	
}
