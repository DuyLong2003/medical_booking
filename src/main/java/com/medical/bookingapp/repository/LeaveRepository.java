package com.medical.bookingapp.repository;

import com.medical.bookingapp.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    List<Leave> findByDoctorId(Integer doctorId);
    @Query("SELECT l FROM Leave l WHERE l.doctor.id = :doctorId " +
            "AND (:start BETWEEN l.startTime AND l.endTime " +
            "OR :end BETWEEN l.startTime AND l.endTime)")
    List<Leave> findConflictingLeaves(@Param("doctorId") Integer doctorId,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);


}
