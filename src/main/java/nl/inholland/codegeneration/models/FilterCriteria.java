package nl.inholland.codegeneration.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterCriteria {
    private String key;
    private String operation;
    private Object value;
}
