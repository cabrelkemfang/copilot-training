package com.accenture.employee.dto;

import com.accenture.employee.entity.Department;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeeResponse(Long employeeId,
                               String firstName,
                               String lastName,
                               String phoneNumber,
                               String email,
                               Department department,
                               LocalDateTime createdAt) {
}
