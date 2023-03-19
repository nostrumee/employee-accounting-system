package com.innowise.accountingsystem.service;

import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.dao.EmployeeDao;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.entity.Employee;
import com.innowise.accountingsystem.entity.Role;
import com.innowise.accountingsystem.mapper.EmployeeMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {

    private static final EmployeeService INSTANCE = new EmployeeService();
    private final EmployeeDao employeeDao;
    private final EmployeeMapper employeeMapper;

    private EmployeeService() {
        this.employeeDao = EmployeeDao.getInstance();
        this.employeeMapper = Mappers.getMapper(EmployeeMapper.class);
    }

    public static EmployeeService getInstance() {
        return INSTANCE;
    }

    public Long addEmployee(CreateEmployeeDto createEmployeeDto) {
        System.out.println(createEmployeeDto);
        //validate
        Employee employee = employeeMapper.fromCreateEmployeeDto(createEmployeeDto);
        //hash password
        employee.setRole(Role.USER);
        System.out.println(employee);
        employeeDao.save(employee);
        return employee.getId();
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeDao.findById(id).orElseThrow(
                () -> new ServiceException("service exception trying to get employee by id {}, id")
        );

        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeDao.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toEntity(employeeDto);
        employeeDao.update(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeDao.findById(id).orElseThrow(
                () -> new ServiceException("service exception trying to delete employee by id {}, id")
        );
        employeeDao.delete(id);
    }

}
