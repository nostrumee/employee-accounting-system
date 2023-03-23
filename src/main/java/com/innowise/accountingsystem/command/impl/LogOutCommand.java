package com.innowise.accountingsystem.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.exception.CommandException;
import com.innowise.accountingsystem.service.ResponseService;
import com.innowise.accountingsystem.util.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.innowise.accountingsystem.util.AttributeName.LOGGED_EMPLOYEE;
import static com.innowise.accountingsystem.util.ResponseMessageUtil.SUCCESSFULLY_LOGGED_OUT;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class LogOutCommand implements Command {

    private static final LogOutCommand INSTANCE = new LogOutCommand();

    private final ResponseService responseService = ResponseService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public static LogOutCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(LOGGED_EMPLOYEE);
        session.invalidate();

        try {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage(SUCCESSFULLY_LOGGED_OUT);

            String json = mapper.writeValueAsString(responseMessage);
            responseService.processResponse(response, HttpServletResponse.SC_OK, json);
        } catch (IOException e) {
            log.error("command exception trying to log out", e);
            throw new CommandException(e);
        }
    }
}
