package com.innowise.accountingsystem.dao;

import com.innowise.accountingsystem.connection.ConnectionPool;
import com.innowise.accountingsystem.entity.Employee;
import com.innowise.accountingsystem.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
public class EmployeeDaoTest {

    private Employee testEmployee;
    private Connection connection;

    @Mock
    private ConnectionPool connectionPool;

    @InjectMocks
    private EmployeeDao employeeDao;

    @BeforeEach
    void setUp() {
        openMocks(this);
        testEmployee = Employee.builder()
                .email("test@gmail.com")
                .password("$argon2i$v=19$m=65536,t=2,p=1$R5EaZY4/ILE33vD2IR9lYA$cs18Jw4hX/CMV4DFWIn+2vKaSZI5a2cmbzW7GvixtW8")
                .firstName("Sarah")
                .lastName("Conor")
                .salary(new BigDecimal("321.12"))
                .birthday(LocalDate.of(2000, 4, 24))
                .role(Role.USER)
                .build();
        TestConnectionConfig config = new TestConnectionConfig();
        connection = config.getConnection();
        config.updateDatabase(connection);
        when(connectionPool.getConnection()).thenReturn(connection);
    }

    @Test
    void saveShouldReturnNotEmptyEmployee() {
        Optional<Employee> savedEmployee = employeeDao.save(testEmployee);
        assertThat(savedEmployee).isPresent();
    }

    @Test
    void findAllShouldReturnNotEmptyList() {
        List<Employee> employeeList = employeeDao.findAll();
        assertThat(employeeList).isNotEmpty();
    }

    @Test
    void findByIdShouldReturnNotEmptyEmployee() {
        Optional<Employee> employee = employeeDao.findById(1L);
        assertThat(employee).isPresent();
    }

    @Test
    void findByIdShouldReturnEmptyEmployee() {
        Optional<Employee> employee = employeeDao.findById(3L);
        assertThat(employee).isEmpty();
    }

    @Test
    void findByEmailShouldReturnNotEmptyEmployee() {
        Optional<Employee> employee = employeeDao.findByEmail("abcdefu@gmail.com");
        assertThat(employee).isPresent();
    }

    @Test
    void findByEmailShouldReturnEmptyEmployee() {
        Optional<Employee> employee = employeeDao.findByEmail("abcdefu3333@gmail.com");
        assertThat(employee).isEmpty();
    }

    @Test
    void updateShouldReturnNotEmptyEmployee() {
        testEmployee.setId(1L);
        Optional<Employee> employee = employeeDao.update(testEmployee);
        assertThat(employee).isPresent();
    }

    @Test
    void updateShouldReturnEmptyEmployee() {
        testEmployee.setId(3L);
        Optional<Employee> employee = employeeDao.update(testEmployee);
        assertThat(employee).isEmpty();
    }

    @Test
    void deleteShouldReturnTrue() {
        assertTrue(employeeDao.delete(1L));
    }

    @Test
    void deleteShouldReturnFalse() {
        assertFalse(employeeDao.delete(3L));
    }

    @AfterEach
    void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
