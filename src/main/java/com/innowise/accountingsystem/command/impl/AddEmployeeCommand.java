package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.validation.ErrorMessage;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.CANNOT_CREATE_EMPLOYEE;

public class AddEmployeeCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try (InputStream inputStream = request.getInputStream()) {
            CreateEmployeeDto employee = mapper.readValue(inputStream, CreateEmployeeDto.class);
            Optional<EmployeeDto> employeeDto = employeeService.addEmployee(employee);

            if (employeeDto.isPresent()) {
                String jsonEmployee = mapper.writeValueAsString(employeeDto.get());
                responseService.processResponse(response, HttpServletResponse.SC_CREATED, jsonEmployee);
            } else {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(CANNOT_CREATE_EMPLOYEE);
                String jsonError = mapper.writeValueAsString(errorMessage);
                responseService.processResponse(response, HttpServletResponse.SC_BAD_REQUEST, jsonError);
            }
        } catch (IOException | ServiceException e) {
            //log.error
            throw new CommandException(e);
        }
    }
}
