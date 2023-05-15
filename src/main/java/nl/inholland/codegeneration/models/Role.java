package nl.inholland.codegeneration.models;

import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role implements GrantedAuthority{
    EMPLOYEE,
    CUSTOMER;

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
