package br.com.empatia.app.enums;

import br.com.empatia.app.exceptions.InvalidAttributeException;

public enum UserRole {
    ROLE_ADMIN,
    ROLE_CUSTOMER,
    ROLE_PSYCHOLOGIST;

    public static UserRole cast(String x) {
        switch (x) {
            case "admin":
                return ROLE_ADMIN;
            case "customer":
                return ROLE_CUSTOMER;
            case "psychologist":
                return ROLE_PSYCHOLOGIST;
        }

        throw new InvalidAttributeException("Invalid role");
    }
}
