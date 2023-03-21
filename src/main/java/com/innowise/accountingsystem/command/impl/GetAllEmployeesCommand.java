package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.exception.ErrorMessage;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.CANNOT_FIND_EMPLOYEES;

public class GetAllEmployeesCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        try {
            if (employees.size() > 0) {
                String jsonEmployees = mapper.writeValueAsString(employees);
                responseService.processResponse(response, HttpServletResponse.SC_OK, jsonEmployees);
            } else {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(CANNOT_FIND_EMPLOYEES);
                String jsonError = mapper.writeValueAsString(errorMessage);
                responseService.processResponse(response, HttpServletResponse.SC_NOT_FOUND, jsonError);
            }
        } catch (IOException | ServiceException e) {
            throw new CommandException(e);
        }
    }
}
