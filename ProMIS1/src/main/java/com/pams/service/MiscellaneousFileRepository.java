package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.MiscellaneousFile;

import jakarta.transaction.Transactional;
@Repository
public interface MiscellaneousFileRepository extends JpaRepository<MiscellaneousFile, Long> {
List<MiscellaneousFile>findByAssignedTask (AssignedTaskPuhAfterCOurt assignedTask);
@Transactional
@Query("select max(id) from UploadAdditionalFilesDetails")
public Long findMaxid();
}
