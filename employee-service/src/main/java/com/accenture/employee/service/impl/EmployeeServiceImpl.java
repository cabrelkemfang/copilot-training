package com.accenture.employee.service.impl;

import com.accenture.employee.dto.EmployeeRequest;
import com.accenture.employee.dto.EmployeeResponse;
import com.accenture.employee.dto.PaginatedResponse;
import com.accenture.employee.entity.EmployeeEntity;
import com.accenture.employee.exception.DuplicateEmailException;
import com.accenture.employee.exception.EmployeeNotFoundException;
import com.accenture.employee.mapper.EmployeeMapper;
import com.accenture.employee.repository.EmployeeRepository;
import com.accenture.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
        EmployeeEntity entity = employeeMapper.toEntity(request);
        EmployeeEntity savedEntity = employeeRepository.save(entity);
        return employeeMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<EmployeeResponse> findAll(Pageable pageable) {
        Page<EmployeeResponse> page = employeeRepository.findAll(pageable)
                .map(employeeMapper::toResponse);
        return PaginatedResponse.<EmployeeResponse>builder()
                .response(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (!entity.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
        employeeMapper.updateEntity(entity, request);
        EmployeeEntity save = employeeRepository.save(entity);
        return employeeMapper.toResponse(save);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }
}
