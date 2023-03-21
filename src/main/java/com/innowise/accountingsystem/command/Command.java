package com.innowise.accountingsystem.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {

    void execute(HttpServletRequest request, HttpServletResponse response);

}
