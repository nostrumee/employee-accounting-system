package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.util.ResponseMessage;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.innowise.accountingsystem.util.AttributeName.ID;
import static com.innowise.accountingsystem.util.ResponseMessageUtil.*;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class DeleteEmployeeCommand implements Command {

    private static final DeleteEmployeeCommand INSTANCE = new DeleteEmployeeCommand();

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public static DeleteEmployeeCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String id = request.getParameter(ID);
            if (employeeService.deleteEmployee(id)) {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessage(USER_DELETED);

                String json = mapper.writeValueAsString(responseMessage);
                responseService.processResponse(response, HttpServletResponse.SC_OK, json);
            } else {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessage(CANNOT_DELETE_EMPLOYEE);

                String jsonError = mapper.writeValueAsString(responseMessage);
                responseService.processResponse(response, HttpServletResponse.SC_NOT_FOUND, jsonError);
            }
        } catch (IOException | ServiceException e) {
            log.error("command exception trying to delete employee", e);
            throw new CommandException(e);
        }
    }
}
