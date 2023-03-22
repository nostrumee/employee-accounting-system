package com.innowise.accountingsystem.command.impl;

import com.innowise.accountingsystem.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.innowise.accountingsystem.util.AttributeName.LOGGED_EMPLOYEE;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class LogOutCommand implements Command {

    private static final LogOutCommand INSTANCE = new LogOutCommand();

    public static LogOutCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(LOGGED_EMPLOYEE);
        session.invalidate();
    }
}
