package com.accenture.employee.repository;

import com.accenture.employee.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByEmail(String email);
}
