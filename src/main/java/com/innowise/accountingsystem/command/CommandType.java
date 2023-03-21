package com.innowise.accountingsystem.command;

import com.innowise.accountingsystem.command.impl.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum CommandType {

    SIGN_IN(new SignInCommand()),
    LOG_OUT(new LogOutCommand()),
    GET_ALL_EMPLOYEES(new GetAllEmployeesCommand()),
    GET_EMPLOYEE_BY_ID(new GetEmployeeByIdCommand()),
    ADD_EMPLOYEE(new AddEmployeeCommand()),
    UPDATE_EMPLOYEE(new UpdateEmployeeCommand()),
    DELETE_EMPLOYEE(new DeleteEmployeeCommand());

    private final Command command;

    public static Command of(String commandName) {
        Command command = GET_ALL_EMPLOYEES.command;

        try {
            if(commandName != null) {
                command  = valueOf(commandName.toUpperCase()).getCommand();
            }
        } catch (IllegalArgumentException e){
            //logger.error(e.getMessage());
        }

        return command;
    }
}
