package com.innowise.accountingsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.service.EmployeeService;
import com.innowise.accountingsystem.util.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class App {


    public static void main(String[] args) throws JsonProcessingException {
        final EmployeeService employeeService = EmployeeService.getInstance();
        final PasswordEncoder passwordEncoder = PasswordEncoder.getInstance();
        String hash1 = passwordEncoder.encode("AbcdeFu321");
        System.out.println(hash1);;

        System.out.println(passwordEncoder.verify(hash1, "AbcdeFu321"));
    }
}
