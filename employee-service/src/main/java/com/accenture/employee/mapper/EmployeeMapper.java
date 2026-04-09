package com.accenture.employee.mapper;

import com.accenture.employee.dto.EmployeeRequest;
import com.accenture.employee.dto.EmployeeResponse;
import com.accenture.employee.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface EmployeeMapper {

    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EmployeeEntity toEntity(EmployeeRequest request);

    EmployeeResponse toResponse(EmployeeEntity entity);

    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget EmployeeEntity entity, EmployeeRequest request);
}
