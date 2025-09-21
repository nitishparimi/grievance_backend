package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entities.GrievanceEntity;

public interface GrievanceRepository extends JpaRepository<GrievanceEntity, Long> {

    // Query grievances by the submitter's businessId (returns list — a user can have many grievances)
    @Query("SELECT g FROM GrievanceEntity g WHERE g.submittedBy.businessId = :businessId")
    List<GrievanceEntity> findBySubmittedByBusinessId(@Param("businessId") String businessId);

    // Optionally return latest first
    @Query("SELECT g FROM GrievanceEntity g WHERE g.submittedBy.businessId = :businessId ORDER BY g.id DESC")
    List<GrievanceEntity> findBySubmittedByBusinessIdOrderByIdDesc(@Param("businessId") String businessId);

    // Your assigned-to join (this is OK if assignedToIds is @ElementCollection of String)
    @Query("select g from GrievanceEntity g join g.assignedToIds a where a = :managerId")
    List<GrievanceEntity> findByAssignedToManagerId(@Param("managerId") String managerId);
}
