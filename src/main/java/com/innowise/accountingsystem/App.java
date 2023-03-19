package com.innowise.accountingsystem;

import com.innowise.accountingsystem.dao.EmployeeDao;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.entity.Employee;
import com.innowise.accountingsystem.entity.Role;
import com.innowise.accountingsystem.mapper.EmployeeMapper;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.util.LocalDateFormatter;

import java.math.BigDecimal;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        EmployeeService employeeService = EmployeeService.getInstance();
        CreateEmployeeDto employee = CreateEmployeeDto.builder()
                .email("asdasd@")
                .password("ewrwerwdfdfd")
                .firstName("asdasd")
                .lastName("dasdas")
                .salary(new BigDecimal("1234.5"))
                .birthday(LocalDateFormatter.format("1997-04-24"))
                .build();
        System.out.println(employee.getBirthday());

        employeeService.addEmployee(employee);


    }
}
