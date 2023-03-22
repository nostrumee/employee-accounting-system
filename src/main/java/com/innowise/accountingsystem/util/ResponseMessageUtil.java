package com.innowise.accountingsystem.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseMessageUtil {
    public static final String SUCCESSFULLY_SIGNED_IN = "successfully signed in";
    public static final String SUCCESSFULLY_LOGGED_OUT = "successfully logged out";
    public static final String USER_DELETED= "user deleted";
    public static final String EMPLOYEE_NOT_FOUND = "employee not found";
    public static final String CANNOT_DELETE_EMPLOYEE = "cannot delete employee";
    public static final String INVALID_CREDENTIALS = "invalid credentials";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String CANNOT_CREATE_EMPLOYEE = "cannot create employee";
    public static final String CANNOT_FIND_EMPLOYEES = "cannot find any employees";
    public static final String CANNOT_UPDATE_EMPLOYEE = "cannot update employee";
    public static final String NO_SUCH_COMMAND = "there is no such command";
}
