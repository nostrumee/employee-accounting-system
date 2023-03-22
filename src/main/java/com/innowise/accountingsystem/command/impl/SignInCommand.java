package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.dto.LoggingEmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.validation.ErrorMessage;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import com.innowise.accountingsystem.util.AttributeName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.*;

public class SignInCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession();

        try (InputStream inputStream = request.getInputStream()) {
            LoggingEmployeeDto loggingEmployee = mapper.readValue(inputStream, LoggingEmployeeDto.class);

            String email = loggingEmployee.getEmail();
            String password = loggingEmployee.getPassword();
            Optional<EmployeeDto> optionalEmployee = employeeService.getEmployeeByEmailAndPassword(email, password);

            if (optionalEmployee.isPresent()) {
                EmployeeDto employee = optionalEmployee.get();
                session.setAttribute(AttributeName.LOGGED_EMPLOYEE, employee);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setMessage(INVALID_CREDENTIALS);
                String jsonError = mapper.writeValueAsString(errorMessage);
                responseService.processResponse(response, HttpServletResponse.SC_UNAUTHORIZED, jsonError);
            }
        } catch (IOException | ServiceException e) {
            //log.error
            throw new CommandException(e);
        }
    }
}
