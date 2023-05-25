package nl.inholland.codegeneration.models;

import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role implements GrantedAuthority{
    EMPLOYEE(0),
    CUSTOMER(1);

    private final int value;

    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getAuthority() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuthority'");
    }
  
    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
