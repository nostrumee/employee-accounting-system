package com.innowise.accountingsystem.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class AttributeName {
    public static final String ID = "id";
    public static final String LOGGED_EMPLOYEE = "logged_employee";
    public static final String COMMAND = "command";
}
