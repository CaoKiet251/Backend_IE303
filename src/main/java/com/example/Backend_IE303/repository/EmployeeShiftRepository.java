package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.EmployeeShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeShiftRepository extends JpaRepository<EmployeeShift, Integer> {
    List<EmployeeShift> findByEmployeeId(Integer employeeId);
    
    List<EmployeeShift> findByDate(LocalDateTime date);

    List<EmployeeShift> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<EmployeeShift> findByEmployeeIdAndDateBetween(Integer employeeId, LocalDateTime startDate,
            LocalDateTime endDate);
}
