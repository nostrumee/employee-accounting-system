package com.innowise.accountingsystem.controller;

import com.innowise.accountingsystem.command.Command;
import com.innowise.accountingsystem.command.CommandType;
import com.innowise.accountingsystem.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.innowise.accountingsystem.util.ErrorMessageUtil.NO_SUCH_COMMAND;

@WebServlet(name = "controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String commandParameter = request.getParameter("command");
            Command command = CommandType.of(commandParameter);
            command.execute(request, response);
        } catch (CommandException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, NO_SUCH_COMMAND);
        }
    }
}
