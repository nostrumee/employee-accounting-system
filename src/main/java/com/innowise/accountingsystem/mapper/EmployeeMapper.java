package com.innowise.accountingsystem.mapper;

import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.entity.Employee;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {

    CreateEmployeeDto toCreateEmployeeDto(Employee employee);

    Employee fromCreateEmployeeDto(CreateEmployeeDto createEmployeeDto);

    Employee toEntity(EmployeeDto createEmployeeDto);

    EmployeeDto toDto(Employee employee);
}
