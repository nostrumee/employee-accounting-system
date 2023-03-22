package com.innowise.accountingsystem.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseService {

    private static final ResponseService INSTANCE = new ResponseService();

    public static ResponseService getInstance() {
        return INSTANCE;
    }

    public void processResponse(HttpServletResponse response, int code, String message) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            response.setStatus(code);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(message);
        }
    }
}
