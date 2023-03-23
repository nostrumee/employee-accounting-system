package com.innowise.accountingsystem.validation;

import com.innowise.accountingsystem.dto.CreateEmployeeDto;
import com.innowise.accountingsystem.dto.EmployeeDto;

public class EmployeeValidator {

    private static final EmployeeValidator INSTANCE = new EmployeeValidator();

    private static final String NAME_PATTERN = "/\\b[^\\d\\W]+\\b/g";
    private static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
    private static final String ID_PATTERN = "^\\d{1,6}$";

    public static EmployeeValidator getInstance() {
        return INSTANCE;
    }

    public boolean isCreateEmployeeDtoValid(CreateEmployeeDto employee) {
        return isNameValid(employee.getFirstName())
                && isNameValid(employee.getLastName())
                && isEmailValid(employee.getEmail())
                && isPasswordValid(employee.getPassword());
    }

    public boolean isEmployeeDtoValid(EmployeeDto employee) {
        return isNameValid(employee.getFirstName())
                && isNameValid(employee.getLastName())
                && isEmailValid(employee.getEmail());
    }

    public boolean isIdValid(String id) {
        return id != null && id.matches(ID_PATTERN);
    }

    private boolean isNameValid(String name) {
        return name != null && name.matches(NAME_PATTERN);
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }

    public boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_PATTERN);

    }
}
