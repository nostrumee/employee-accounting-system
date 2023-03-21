package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetAllEmployeesCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        ObjectMapper objectMapper = new ObjectMapper();

        try (PrintWriter writer = response.getWriter()) {
            String json = objectMapper.writeValueAsString(employees);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(json);
        } catch (IOException e) {
            throw new CommandException(e);
        }


    }
}
