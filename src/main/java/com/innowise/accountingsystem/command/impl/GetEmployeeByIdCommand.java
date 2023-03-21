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
import java.util.Optional;

public class GetEmployeeByIdCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<EmployeeDto> employee = employeeService.getEmployeeById(id);
        ObjectMapper objectMapper = new ObjectMapper();

        try (PrintWriter writer = response.getWriter()) {
            String json = objectMapper.writeValueAsString(employee.get());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(json);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
