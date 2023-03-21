package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

public class AddEmployeeCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = request.getInputStream()) {
            CreateEmployeeDto employee = mapper.readValue(inputStream, CreateEmployeeDto.class);
            employeeService.addEmployee(employee);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
