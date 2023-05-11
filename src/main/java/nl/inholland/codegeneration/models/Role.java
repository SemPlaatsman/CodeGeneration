package nl.inholland.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    Employee,
    Customer;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
