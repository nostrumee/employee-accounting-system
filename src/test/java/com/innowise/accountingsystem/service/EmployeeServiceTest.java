package com.innowise.accountingsystem.service;

import com.innowise.accountingsystem.dao.EmployeeDao;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.entity.Employee;
import com.innowise.accountingsystem.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

public class EmployeeServiceTest {

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void addEmployeeShouldReturnNotEmptyEmployee() {
        Employee testEmployee = Employee.builder()
                .email("test@gmail.com")
                .password("AbCdEfU123123")
                .firstName("Sarah")
                .lastName("Conor")
                .salary(new BigDecimal("321.12"))
                .birthday(LocalDate.of(2000, 4, 24))
                .role(Role.USER)
                .build();

        given(employeeDao.save(testEmployee)).willReturn(Optional.of(testEmployee));

        CreateEmployeeDto employee = CreateEmployeeDto.builder()
                .email("test@gmail.com")
                .password("AbCdEfU123123")
                .firstName("Sarah")
                .lastName("Conor")
                .salary(new BigDecimal("321.12"))
                .birthday(LocalDate.of(2000, 4, 24))
                .build();

        Optional<EmployeeDto> optionalEmployee = employeeService.addEmployee(employee);
        assertThat(optionalEmployee).isPresent();
    }


}
