package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.dto.LoggedEmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.util.PasswordEncoder;
import com.innowise.accountingsystem.util.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class SignInCommand implements Command {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoder.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession();

        try (InputStream inputStream = request.getInputStream()) {
            LoggedEmployeeDto loggingEmployee = mapper.readValue(inputStream, LoggedEmployeeDto.class);

            String email = loggingEmployee.getEmail();
            String plainPassword = loggingEmployee.getPassword();
            Optional<EmployeeDto> optionalEmployee = employeeService.getEmployeeByEmail(email);

            if (optionalEmployee.isPresent()) {
                EmployeeDto employee = optionalEmployee.get();
                String hashedPassword = employeeService.getEmployeePassword(email);

                if (passwordEncoder.verify(hashedPassword, plainPassword)) {
                    session.setAttribute(RequestParam.LOGGED_EMPLOYEE, employee);
                } else {
                    // return error  invalid password
                }
            } else {
                // print error employee not exist
            }

        } catch (IOException |ServiceException e) {
            //log.error
            throw new CommandException(e);
        }
    }
}
