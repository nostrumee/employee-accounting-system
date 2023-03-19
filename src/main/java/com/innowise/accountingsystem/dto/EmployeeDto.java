package com.innowise.accountingsystem.dto;

import com.innowise.accountingsystem.entity.Role;
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
public class EmployeeDto {
    long id;
    String email;
    String password;
    String firstName;
    String lastName;
    BigDecimal salary;
    LocalDate birthday;
    Role role;
}
