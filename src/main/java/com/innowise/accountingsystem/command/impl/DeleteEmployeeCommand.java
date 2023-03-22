package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.validation.ErrorMessage;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.innowise.accountingsystem.util.AttributeName.ID;
import static com.innowise.accountingsystem.util.ErrorMessageUtil.CANNOT_DELETE_EMPLOYEE;

public class DeleteEmployeeCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String id = request.getParameter(ID);
            if (employeeService.deleteEmployee(id)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(CANNOT_DELETE_EMPLOYEE);
                String jsonError = mapper.writeValueAsString(errorMessage);
                responseService.processResponse(response, HttpServletResponse.SC_NOT_FOUND, jsonError);
            }
        } catch (IOException | ServiceException e) {
            //log.error
            throw new CommandException(e);
        }
    }
}
