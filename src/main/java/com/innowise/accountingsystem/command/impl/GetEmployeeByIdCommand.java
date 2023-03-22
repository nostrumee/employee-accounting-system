package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.util.ResponseMessage;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ResponseMessageUtil.EMPLOYEE_NOT_FOUND;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class GetEmployeeByIdCommand implements Command {

    private static final GetEmployeeByIdCommand INSTANCE = new GetEmployeeByIdCommand();

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public static GetEmployeeByIdCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        try {
            Optional<EmployeeDto> employee = employeeService.getEmployeeById(id);
            if (employee.isPresent()) {
                String jsonEmployee = mapper.writeValueAsString(employee.get());
                responseService.processResponse(response, HttpServletResponse.SC_OK, jsonEmployee);
            } else {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessage(EMPLOYEE_NOT_FOUND);

                String jsonError = mapper.writeValueAsString(responseMessage);
                responseService.processResponse(response, HttpServletResponse.SC_NOT_FOUND, jsonError);
            }
        } catch (ServiceException | IOException e) {
            log.error("command exception trying to get employee by id {}", id, e);
            throw new CommandException(e);
        }
    }
}
