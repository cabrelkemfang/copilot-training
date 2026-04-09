package com.accenture.employee.service;

import com.accenture.employee.dto.EmployeeRequest;
import com.accenture.employee.dto.EmployeeResponse;
import com.accenture.employee.dto.PaginatedResponse;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse findById(Long id);

    PaginatedResponse<EmployeeResponse> findAll(Pageable pageable);

    EmployeeResponse update(Long id, EmployeeRequest request);

    void delete(Long id);
}
