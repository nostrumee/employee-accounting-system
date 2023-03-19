package com.innowise.accountingsystem.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
public class CreateEmployeeDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private LocalDate birthday;
}
