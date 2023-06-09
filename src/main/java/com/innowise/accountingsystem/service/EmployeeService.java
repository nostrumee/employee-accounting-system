package com.innowise.accountingsystem.service;

import com.innowise.accountingsystem.exception.DaoException;
import com.innowise.accountingsystem.exception.ServiceException;
import com.innowise.accountingsystem.dao.EmployeeDao;
import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;
import com.innowise.accountingsystem.entity.Employee;
import com.innowise.accountingsystem.entity.Role;
import com.innowise.accountingsystem.mapper.EmployeeMapper;
import com.innowise.accountingsystem.util.PasswordEncoder;
import com.innowise.accountingsystem.validation.EmployeeValidator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class EmployeeService {

    private static final EmployeeService INSTANCE = new EmployeeService();
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeValidator employeeValidator;

    private EmployeeDao employeeDao;

    private EmployeeService() {
        this.employeeDao = EmployeeDao.getInstance();
        this.employeeMapper = Mappers.getMapper(EmployeeMapper.class);
        this.passwordEncoder = PasswordEncoder.getInstance();
        this.employeeValidator = EmployeeValidator.getInstance();
    }

    public static EmployeeService getInstance() {
        return INSTANCE;
    }

    public Optional<EmployeeDto> addEmployee(CreateEmployeeDto createEmployeeDto) {
        Optional<EmployeeDto> optionalEmployeeDto = Optional.empty();

        if (employeeValidator.isCreateEmployeeDtoValid(createEmployeeDto)) {
            Employee employee = employeeMapper.fromCreateEmployeeDto(createEmployeeDto);
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employee.setRole(Role.USER);

            try {
                Optional<Employee> optionalEmployee = employeeDao.save(employee);
                optionalEmployeeDto = optionalEmployee.map(employeeMapper::toDto);
            } catch (DaoException e) {
                log.error("service exception trying to add employee", e);
                throw new ServiceException(e);
            }
        }

        return optionalEmployeeDto;
    }

    public Optional<EmployeeDto> getEmployeeById(String id) {
        Optional<EmployeeDto> optionalEmployee = Optional.empty();

        if (employeeValidator.isIdValid(id)) {
            try {
                Optional<Employee> employee = employeeDao.findById(Long.parseLong(id));
                optionalEmployee = employee.map(employeeMapper::toDto);
            } catch (DaoException e) {
                log.error("service exception trying to get employee by id {}", id, e);
                throw new ServiceException(e);
            }
        }

        return optionalEmployee;
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeDao.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> getEmployeeByEmailAndPassword(String email, String password) {
        Optional<EmployeeDto> optionalEmployee = Optional.empty();

        try {
            Optional<Employee> employee = employeeDao.findByEmail(email);
            if (employee.isPresent()) {
                String hash = employee.get().getPassword();
                if (passwordEncoder.verify(hash, password)) {
                    optionalEmployee = employee.map(employeeMapper::toDto);
                }
            }
        } catch (DaoException e) {
            log.error("service exception trying to get employee by email and password", e);
            throw new ServiceException(e);
        }

        return optionalEmployee;
    }

    public Optional<EmployeeDto> updateEmployee(EmployeeDto employeeDto) {
        Optional<EmployeeDto> optionalEmployee = Optional.empty();

        if (employeeValidator.isEmployeeDtoValid(employeeDto)) {
            Employee employee = employeeMapper.toEntity(employeeDto);

            try {
                Optional<Employee> updatedEmployee = employeeDao.update(employee);

                if (updatedEmployee.isPresent()) {
                    EmployeeDto updatedEmployeeDto = employeeMapper.toDto(employee);
                    optionalEmployee = Optional.of(updatedEmployeeDto);
                }
            } catch (DaoException e) {
                log.error("service exception trying to update employee", e);
                throw new ServiceException(e);
            }
        }

        return optionalEmployee;
    }

    public boolean deleteEmployee(String id) {
        if (employeeValidator.isIdValid(id)) {
            try {
                employeeDao.findById(Long.parseLong(id));
                return employeeDao.delete(Long.parseLong(id));
            } catch (DaoException e) {
                log.error("service exception trying to delete employee", e);
                throw new ServiceException(e);
            }
        } else {
            return false;
        }
    }
}
