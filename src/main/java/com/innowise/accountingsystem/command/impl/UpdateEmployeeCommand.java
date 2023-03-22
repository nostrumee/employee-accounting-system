package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
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
import java.io.PrintWriter;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.CANNOT_UPDATE_EMPLOYEE;

public class UpdateEmployeeCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try (InputStream inputStream = request.getInputStream();
             PrintWriter writer = response.getWriter()) {
            EmployeeDto employee = mapper.readValue(inputStream, EmployeeDto.class);
            Optional<EmployeeDto> employeeDto = employeeService.updateEmployee(employee);

            if (employeeDto.isPresent()) {
                String jsonEmployee = mapper.writeValueAsString(employeeDto.get());
                responseService.processResponse(response, HttpServletResponse.SC_OK, jsonEmployee);
            } else {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(CANNOT_UPDATE_EMPLOYEE);
                String jsonError = mapper.writeValueAsString(errorMessage);
                responseService.processResponse(response, HttpServletResponse.SC_BAD_REQUEST, jsonError);
            }
        } catch (IOException | ServiceException e) {
            //log.error
            throw new CommandException(e);
        }
    }
}
