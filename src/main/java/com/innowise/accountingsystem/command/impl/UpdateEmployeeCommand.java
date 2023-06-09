package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import com.innowise.accountingsystem.util.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ResponseMessageUtil.CANNOT_UPDATE_EMPLOYEE;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class UpdateEmployeeCommand implements Command {

    private static final UpdateEmployeeCommand INSTANCE = new UpdateEmployeeCommand();

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public static UpdateEmployeeCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try (InputStream inputStream = request.getInputStream()) {
            EmployeeDto employee = mapper.readValue(inputStream, EmployeeDto.class);
            Optional<EmployeeDto> employeeDto = employeeService.updateEmployee(employee);

            if (employeeDto.isPresent()) {
                String jsonEmployee = mapper.writeValueAsString(employeeDto.get());
                responseService.processResponse(response, HttpServletResponse.SC_OK, jsonEmployee);
            } else {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessage(CANNOT_UPDATE_EMPLOYEE);

                String jsonError = mapper.writeValueAsString(responseMessage);
                responseService.processResponse(response, HttpServletResponse.SC_BAD_REQUEST, jsonError);
            }
        } catch (IOException | ServiceException e) {
            log.error("command exception trying to update employee", e);
            throw new CommandException(e);
        }
    }
}
