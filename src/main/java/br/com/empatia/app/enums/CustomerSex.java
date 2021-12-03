package br.com.empatia.app.enums;

import br.com.empatia.app.exceptions.InvalidAttributeException;

public enum CustomerSex {
    MALE,
    FEMALE,
    OTHER;

    public static CustomerSex cast(String x) {
        switch (x) {
            case "male":
                return MALE;
            case "female":
                return FEMALE;
            case "other":
                return OTHER;
        }

        throw new InvalidAttributeException("Invalid sex");
    }
}
