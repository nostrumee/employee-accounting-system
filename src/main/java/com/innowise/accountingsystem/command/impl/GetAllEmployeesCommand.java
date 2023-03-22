package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.CANNOT_FIND_EMPLOYEES;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class GetAllEmployeesCommand implements Command {

    private static final GetAllEmployeesCommand INSTANCE = new GetAllEmployeesCommand();

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public static GetAllEmployeesCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        try {
            if (employees.size() > 0) {
                String jsonEmployees = mapper.writeValueAsString(employees);
                responseService.processResponse(response, HttpServletResponse.SC_OK, jsonEmployees);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, CANNOT_FIND_EMPLOYEES);
            }
        } catch (IOException | ServiceException e) {
            log.error("command exception trying to get all employees", e);
            throw new CommandException(e);
        }
    }
}
