package com.innowise.accountingsystem.model.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Employee {
    @EqualsAndHashCode.Exclude
    private long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private LocalDate birthday;
    private Role role;
}
