package com.innowise.accountingsystem.model.mapper.impl;

import com.innowise.accountingsystem.model.entity.Employee;
import com.innowise.accountingsystem.model.entity.Role;
import com.innowise.accountingsystem.model.mapper.RowMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.innowise.accountingsystem.util.ColumnName.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeRowMapper implements RowMapper<Employee> {

    private static final EmployeeRowMapper instance = new EmployeeRowMapper();

    public static EmployeeRowMapper getInstance() {
        return instance;
    }

    @Override
    public Optional<Employee> mapRow(ResultSet resultSet) {
        Optional<Employee> optionalEmployee;

        try {
            Employee employee = Employee.builder()
                    .id(resultSet.getLong(ID))
                    .email(resultSet.getString(EMAIL))
                    .password(resultSet.getString(PASSWORD))
                    .firstName(resultSet.getString(FIRST_NAME))
                    .lastName(resultSet.getString(LAST_NAME))
                    .salary(resultSet.getBigDecimal(SALARY))
                    .birthday(resultSet.getDate(BIRTHDAY).toLocalDate())
                    .role(Role.valueOf(resultSet.getString(ROLE)))
                    .build();
            optionalEmployee = Optional.of(employee);
        } catch (SQLException e) {
            //logger.error("Can not read resultset", e);
            optionalEmployee = Optional.empty();
        }

        return optionalEmployee;
    }
}
