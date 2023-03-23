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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

public class EmployeeServiceTest {

    private Employee testEmployee;

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        openMocks(this);

        testEmployee = Employee.builder()
                .email("test@gmail.com")
                .password("AbCdEfU123123")
                .firstName("Sarah")
                .lastName("Conor")
                .salary(new BigDecimal("321.12"))
                .birthday(LocalDate.of(2000, 4, 24))
                .role(Role.USER)
                .build();
    }

    @Test
    void addEmployeeShouldReturnNotEmptyEmployee() {
        given(employeeDao.save(any(Employee.class))).willReturn(Optional.of(testEmployee));

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

    @Test
    void getEmployeeByIdShouldReturnNotEmptyEmployee() {
        given(employeeDao.findById(1L)).willReturn(Optional.of(testEmployee));
        Optional<EmployeeDto> optionalEmployee = employeeService.getEmployeeById("1");
        assertThat(optionalEmployee).isPresent();
    }

    @Test
    void getEmployeeByIdShouldReturnEmptyEmployee() {
        given(employeeDao.findById(1L)).willReturn(Optional.empty());
        Optional<EmployeeDto> optionalEmployee = employeeService.getEmployeeById("1");
        assertThat(optionalEmployee).isEmpty();
    }

    @Test
    void getAllEmployeesShouldReturnNotEmptyList() {
        List<Employee> employees = List.of(testEmployee);

        given(employeeDao.findAll()).willReturn(employees);
        List<EmployeeDto> employeeDtos = employeeService.getAllEmployees();
        assertThat(employeeDtos).isNotEmpty();
    }

    @Test
    void updateEmployeeShouldReturnNotEmptyEmployee() {
        EmployeeDto employee = EmployeeDto.builder()
                .email("test@gmail.com")
                .password("AbCdEfU123123")
                .firstName("Sarah")
                .lastName("Conor")
                .salary(new BigDecimal("321.12"))
                .birthday(LocalDate.of(2000, 4, 24))
                .role(Role.USER)
                .build();

        given(employeeDao.update(any(Employee.class))).willReturn(Optional.of(testEmployee));
        Optional<EmployeeDto> employeeDto = employeeService.updateEmployee(employee);
        assertThat(employeeDto).isPresent();
    }

    @Test
    void deleteEmployeeShouldReturnTrue() {
        given(employeeDao.delete(1L)).willReturn(true);
        assertTrue(employeeService.deleteEmployee("1"));
    }
}
