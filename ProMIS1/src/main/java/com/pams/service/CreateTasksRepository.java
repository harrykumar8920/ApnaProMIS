package com.pams.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pams.entity.CreateTasks;
import com.pams.entity.TasksPuh;

public interface CreateTasksRepository extends JpaRepository<CreateTasks, Long> {

	List<CreateTasks> findAll(Sort Sort);

	@Query(nativeQuery = true, value = "select * from authentication.tasks where id !=1 order by id")
	public List<CreateTasks> findBottomData();

	@Query(nativeQuery = true, value = "select * from authentication.tasks where  id=18 or id=19 or id=16 order by id")
	public List<CreateTasks> findTopOneData();
	
	@Query(nativeQuery = true, value = "select * from authentication.tasks where id=0 order by id")
	public List<CreateTasks> findTopOneData1();
	
	@Query(nativeQuery = true, value = "select * from authentication.tasks where id =11 or id=0 order by id")
	public List<CreateTasks> findNCLTtask();
	
	@Query(nativeQuery = true, value = "select * from authentication.tasks where id !=11 and id!=1 order by id")
	public List<CreateTasks> findNotNCLTtask();
	
	List<CreateTasks> findByIdNotIn(List<Long> ids);
	
	
	List<CreateTasks> findByIdIn(List<Long> ids);
	

}
