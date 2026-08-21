package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.SupplementaryComplaint;
@Repository
public interface SupplementaryComplaintRepository extends JpaRepository<SupplementaryComplaint, Long> {
List<SupplementaryComplaint> findByAssignedTask(AssignedTaskPuhAfterCOurt assignedTask);
}
