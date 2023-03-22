package com.innowise.accountingsystem.command;

import com.innowise.accountingsystem.command.impl.*;
import com.innowise.accountingsystem.exception.CommandException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum CommandType {

    SIGN_IN(SignInCommand.getInstance()),
    LOG_OUT(LogOutCommand.getInstance()),
    GET_ALL_EMPLOYEES(GetAllEmployeesCommand.getInstance()),
    GET_EMPLOYEE_BY_ID(GetEmployeeByIdCommand.getInstance()),
    ADD_EMPLOYEE(AddEmployeeCommand.getInstance()),
    UPDATE_EMPLOYEE(UpdateEmployeeCommand.getInstance()),
    DELETE_EMPLOYEE(DeleteEmployeeCommand.getInstance());

    private final Command command;

    public static Command of(String commandName) {
        Command command = GET_ALL_EMPLOYEES.command;

        try {
            if (commandName != null) {
                command  = valueOf(commandName.toUpperCase()).getCommand();
            }
        } catch (IllegalArgumentException e) {
            log.error("command type exception trying to get command", e);
            throw new CommandException(e);
        }

        return command;
    }
}
