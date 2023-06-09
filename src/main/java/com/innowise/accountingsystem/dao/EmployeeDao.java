package com.innowise.accountingsystem.dao;

import com.innowise.accountingsystem.exception.DaoException;
import com.innowise.accountingsystem.connection.ConnectionPool;
import com.innowise.accountingsystem.entity.Employee;
import com.innowise.accountingsystem.entity.Role;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ColumnName.*;

@Slf4j
public class EmployeeDao {

    private static final String SQL_FIND_ALL = """
            SELECT id, email, password, first_name, last_name, salary, birthday, role
            FROM employee
            """;
    private static final String SQL_FIND_BY_ID = """
            SELECT id, email, password, first_name, last_name, salary, birthday, role
            FROM employee
            WHERE id = ?
            """;
    private static final String SQL_SAVE = """
            INSERT INTO employee (email, password, first_name, last_name, salary, birthday, role)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String SQL_UPDATE = """
            UPDATE employee
            SET email = ?, first_name = ?, last_name = ?, salary = ?, birthday = ?, role = ?
            WHERE id = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM employee
            WHERE id = ?
            """;
    private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD = """
            SELECT id, email, password, first_name, last_name, salary, birthday, role
            FROM employee
            WHERE email = ? AND password = ?
            """;
    private static final String SQL_FIND_BY_EMAIL = """
            SELECT id, email, password, first_name, last_name, salary, birthday, role
            FROM employee
            WHERE email = ?
            """;

    private static final EmployeeDao instance = new EmployeeDao();
    private ConnectionPool connectionPool;

    private EmployeeDao() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static EmployeeDao getInstance() {
        return instance;
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = mapToEntity(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e) {
            log.error("dao exception trying to find all employees", e);
            throw new DaoException(e);
        }

        return employees;
    }

    public Optional<Employee> findById(Long id) {
        Optional<Employee> optionalEmployee;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Employee employee = mapToEntity(resultSet);
                optionalEmployee = Optional.of(employee);
            } else {
                optionalEmployee = Optional.empty();
            }
        } catch (SQLException e) {
            log.error("dao exception trying to find employee by id {}", id, e);
            throw new DaoException(e);
        }

        return optionalEmployee;
    }

    public Optional<Employee> findByEmail(String email) {
        Optional<Employee> optionalEmployee;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Employee employee = mapToEntity(resultSet);
                optionalEmployee = Optional.of(employee);
            } else {
                optionalEmployee = Optional.empty();
            }
        } catch (SQLException e) {
            log.error("dao exception trying to find employee by email {}", email, e);
            throw new DaoException(e);
        }

        return optionalEmployee;
    }

    public Optional<Employee> save(Employee employee) {
        Optional<Employee> optionalEmployee;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getEmail());
            statement.setString(2, employee.getPassword());
            statement.setString(3, employee.getFirstName());
            statement.setString(4, employee.getLastName());
            statement.setBigDecimal(5, employee.getSalary());
            statement.setObject(6, employee.getBirthday());
            statement.setString(7, employee.getRole().name());

            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            employee.setId(generatedKeys.getObject(ID, Long.class));
            optionalEmployee = Optional.of(employee);
        } catch (SQLException e) {
            log.error("dao exception trying to save employee", e);
            throw new DaoException(e);
        }

        return optionalEmployee;
    }

    public Optional<Employee> update(Employee employee) {
        Optional<Employee> optionalEmployee = Optional.empty();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)){
            statement.setString(1, employee.getEmail());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getLastName());
            statement.setBigDecimal(4, employee.getSalary());
            statement.setObject(5, employee.getBirthday());
            statement.setString(6, employee.getRole().name());
            statement.setObject(7, employee.getId());

            if (statement.executeUpdate() != 0){
                optionalEmployee = findById(employee.getId());
            }
        } catch (SQLException e) {
            log.error("dao exception trying update employee", e);
            throw new DaoException(e);
        }

        return optionalEmployee;
    }

    public boolean delete(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)){
            statement.setLong(1, id);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.error("dao exception trying delete employee", e);
            throw new DaoException(e);
        }
    }

    private Employee mapToEntity(ResultSet resultSet) throws SQLException {
        return Employee.builder()
                .id(resultSet.getLong(ID))
                .email(resultSet.getString(EMAIL))
                .password(resultSet.getString(PASSWORD))
                .firstName(resultSet.getString(FIRST_NAME))
                .lastName(resultSet.getString(LAST_NAME))
                .salary(resultSet.getBigDecimal(SALARY))
                .birthday(resultSet.getDate(BIRTHDAY).toLocalDate())
                .role(Role.valueOf(resultSet.getString(ROLE)))
                .build();
    }
}
