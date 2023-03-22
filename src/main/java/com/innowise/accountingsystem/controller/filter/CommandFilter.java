package com.innowise.accountingsystem.controller.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.command.CommandType;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.validation.ErrorMessage;
import com.innowise.accountingsystem.service.ResponseService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static com.innowise.accountingsystem.command.CommandType.*;
import static com.innowise.accountingsystem.util.AttributeName.COMMAND;
import static com.innowise.accountingsystem.util.AttributeName.LOGGED_EMPLOYEE;
import static com.innowise.accountingsystem.util.ErrorMessageUtil.UNAUTHORIZED;

public class CommandFilter implements Filter {

    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    private List<Command> adminCommands;
    private List<Command> employeeCommands;
    private List<Command> guestCommands;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminCommands = List.of(
                LOG_OUT.getCommand(),
                GET_ALL_EMPLOYEES.getCommand(),
                GET_EMPLOYEE_BY_ID.getCommand(),
                ADD_EMPLOYEE.getCommand(),
                UPDATE_EMPLOYEE.getCommand(),
                DELETE_EMPLOYEE.getCommand()
        );
        employeeCommands = List.of(
                LOG_OUT.getCommand(),
                GET_ALL_EMPLOYEES.getCommand(),
                GET_EMPLOYEE_BY_ID.getCommand()
        );
        guestCommands = List.of(
                SIGN_IN.getCommand()
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean access;

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession(false);
        String commandParameter = servletRequest.getParameter(COMMAND);
        Command command = CommandType.of(commandParameter);

        if (session != null && session.getAttribute(LOGGED_EMPLOYEE) != null) {
            EmployeeDto employee = (EmployeeDto) session.getAttribute(LOGGED_EMPLOYEE);
            access = switch (employee.getRole()) {
                case USER -> employeeCommands.contains(command);
                case ADMIN -> adminCommands.contains(command);
            };
        } else {
            access = guestCommands.contains(command);
        }

        if (access) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage(UNAUTHORIZED);
            String jsonError = mapper.writeValueAsString(errorMessage);
            responseService.processResponse(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED, jsonError);
        }
    }
}
