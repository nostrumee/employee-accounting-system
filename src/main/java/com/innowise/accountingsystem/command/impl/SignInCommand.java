package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.dto.LoggingEmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.util.ResponseMessage;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import com.innowise.accountingsystem.util.AttributeName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ResponseMessageUtil.*;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class SignInCommand implements Command {

    private static final SignInCommand INSTANCE = new SignInCommand();

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();

    public static SignInCommand getInstance() {
        return INSTANCE;
    }

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

                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessage(SUCCESSFULLY_SIGNED_IN);

                String json = mapper.writeValueAsString(responseMessage);
                responseService.processResponse(response, HttpServletResponse.SC_OK, json);
            } else {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessage(INVALID_CREDENTIALS);
                String jsonError = mapper.writeValueAsString(responseMessage);
                responseService.processResponse(response, HttpServletResponse.SC_UNAUTHORIZED, jsonError);
            }
        } catch (IOException | ServiceException e) {
            log.error("command exception trying to sign in", e);
            throw new CommandException(e);
        }
    }
}
