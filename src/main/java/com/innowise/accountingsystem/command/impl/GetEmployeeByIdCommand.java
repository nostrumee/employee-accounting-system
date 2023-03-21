package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.exception.ErrorMessage;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.EMPLOYEE_NOT_FOUND;

public class GetEmployeeByIdCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Optional<EmployeeDto> employee = employeeService.getEmployeeById(id);

        try {
            if (employee.isPresent()) {
                String jsonEmployee = mapper.writeValueAsString(employee.get());
                responseService.processResponse(response, HttpServletResponse.SC_OK, jsonEmployee);
            } else {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(EMPLOYEE_NOT_FOUND);
                String jsonError = mapper.writeValueAsString(errorMessage);
                responseService.processResponse(response, HttpServletResponse.SC_NOT_FOUND, jsonError);
            }

        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
