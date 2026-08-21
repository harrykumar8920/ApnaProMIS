package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pams.entity.AddSubTask;
import com.pams.entity.CreateTasks;

public interface SubTaskRepository  extends JpaRepository<AddSubTask, Long>{

	List<AddSubTask> findAllByTask(CreateTasks task);

}
