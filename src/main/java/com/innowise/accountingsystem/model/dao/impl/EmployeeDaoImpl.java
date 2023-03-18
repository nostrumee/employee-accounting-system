package com.innowise.accountingsystem.model.dao.impl;

import com.innowise.accountingsystem.exception.DaoException;
import com.innowise.accountingsystem.model.connection.ConnectionPool;
import com.innowise.accountingsystem.model.dao.EmployeeDao;
import com.innowise.accountingsystem.model.entity.Employee;
import com.innowise.accountingsystem.model.mapper.RowMapper;
import com.innowise.accountingsystem.model.mapper.impl.EmployeeRowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ColumnName.ID;

public class EmployeeDaoImpl implements EmployeeDao {

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
            SET email = ?, password = ?, first_name = ?, last_name = ?, salary = ?, birthday = ?, role = ?
            WHERE id = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM employee
            WHERE id = ?
            """;

    private static final RowMapper<Employee> mapper = EmployeeRowMapper.getInstance();
    private static final EmployeeDaoImpl instance = new EmployeeDaoImpl();
    private final ConnectionPool connectionPool;

    private EmployeeDaoImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static EmployeeDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Optional<Employee> employee = mapper.mapRow(resultSet);
                employee.ifPresent(employees::add);
            }
        } catch (SQLException e) {
            //logger.error("Dao exception trying find all employees", e);
            throw new DaoException(e);
        }

        return employees;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        Optional<Employee> optionalEmployee;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                optionalEmployee = mapper.mapRow(resultSet);
                if (optionalEmployee.isPresent()){
                    Employee employee = optionalEmployee.get();
                    optionalEmployee = Optional.of(employee);
                }
            } else {
                optionalEmployee = Optional.empty();
            }
        } catch (SQLException e) {
            //logger.error("Dao exception trying find employee by id", e);
            throw new DaoException(e);
        }

        return optionalEmployee;
    }

    @Override
    public Employee save(Employee employee) {
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

            return employee;
        } catch (SQLException e) {
            //logger.error("Dao exception trying save employee", e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Employee employee) {
        boolean updated = false;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)){
            statement.setString(1, employee.getEmail());
            statement.setString(2, employee.getPassword());
            statement.setString(3, employee.getFirstName());
            statement.setString(4, employee.getLastName());
            statement.setBigDecimal(5, employee.getSalary());
            statement.setObject(6, employee.getBirthday());
            statement.setString(7, employee.getRole().name());
            statement.setObject(8, employee.getId());

            if (statement.executeUpdate() != 0){
                updated = true;
            }
        } catch (SQLException e) {
            //logger.error("Dao exception trying update user", e);
            throw new DaoException(e);
        }

        return updated;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)){
            statement.setLong(1, id);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            //logger.error("Dao exception trying delete user", e);
            throw new DaoException(e);
        }
    }
}
