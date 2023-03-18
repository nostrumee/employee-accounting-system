package com.innowise.accountingsystem;

import com.innowise.accountingsystem.model.dao.Dao;
import com.innowise.accountingsystem.model.dao.impl.EmployeeDaoImpl;
import com.innowise.accountingsystem.model.entity.Employee;
import com.innowise.accountingsystem.model.entity.Role;
import com.innowise.accountingsystem.util.LocalDateFormatter;

import java.math.BigDecimal;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        Dao<Long, Employee> employeeDao = EmployeeDaoImpl.getInstance();
        Employee employee = Employee
                .builder()
                .email("asdasd")
                .password("ewrwerw")
                .firstName("asdasd")
                .lastName("dasdas")
                .salary(new BigDecimal("1234.5"))
                .birthday(LocalDateFormatter.format("1997-04-24"))
                .role(Role.USER)
                .build();
        //employeeDao.save(employee);
        //List<Employee> employeeList = employeeDao.findAll();
        employeeDao.delete(1L);
        Optional<Employee> employee1 = employeeDao.findById(1L);
        employee1.ifPresent(System.out::println);


    }
}
