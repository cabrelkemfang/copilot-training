package com.accenture.employee.configuration;

import com.accenture.employee.controller.EmployeeController;
import com.accenture.employee.entity.EmployeeEntity;
import com.accenture.employee.mapper.EmployeeMapper;
import com.accenture.employee.repository.EmployeeRepository;
import com.accenture.employee.service.impl.EmployeeServiceImpl;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = EmployeeRepository.class)
@EntityScan(basePackageClasses = EmployeeEntity.class)
@ComponentScan(basePackageClasses = {
        EmployeeMapper.class,
        EmployeeServiceImpl.class,
        EmployeeController.class
})
public class EmployeeConfiguration {
}
