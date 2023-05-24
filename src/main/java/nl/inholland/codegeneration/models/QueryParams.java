package nl.inholland.codegeneration.models;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.query.SemanticException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.services.FilterSpecification;

@Getter
@Setter
@NoArgsConstructor
public class QueryParams {
    private final List<FilterCriteria> filterCriteria = new ArrayList<>();
    private int limit = 12;
    private int page = 0;
    // This class reference is used to access the fields of the class the filterCriteria needs to filter on
    private Class<?> classReference;

    public QueryParams(Class<?> classReference) {
        this.classReference = classReference;
    }

    public void setFilter(String filterQuery) throws Exception {
//        System.out.println(filterQuery);
        this.filterCriteria.clear();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|>:|<:)'([a-zA-Z0-9:.-]+?)',");
        Matcher matcher = pattern.matcher(filterQuery + ",");
        while (matcher.find()) {
//            System.out.println("First: " + matcher.group(1) + ". Second: " + matcher.group(2) + ". Third: " + matcher.group(3));
            this.addFilter(new FilterCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
    }

    public boolean addFilter(FilterCriteria filterCriterion) throws Exception {
        Field field = this.classReference.getDeclaredField(filterCriterion.getKey());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new BadCredentialsException("Unauthorized!");
        }

        if (!this.classReference.isAnnotationPresent(Filterable.class)) {
            if (!field.isAnnotationPresent(Filterable.class)) {
                throw new SemanticException("Invalid filter option!");
            }

            if (field.getAnnotation(Filterable.class).role() == Role.EMPLOYEE && user.getRole() != Role.EMPLOYEE) {
                throw new BadCredentialsException("Invalid permissions!");
            }
        } else if (this.classReference.getAnnotation(Filterable.class).role() == Role.EMPLOYEE && user.getRole() != Role.EMPLOYEE) {
            throw new BadCredentialsException("Invalid permissions!");
        }

        filterCriterion.setValue(this.castToFieldType(field.getType(), (String) filterCriterion.getValue()));
//        System.out.println(filterCriterion.getKey() + filterCriterion.getOperation() + filterCriterion.getValue() + " (" + filterCriterion.getValue().getClass() + ")");
        return this.filterCriteria.add(filterCriterion);
    }

    private Object castToFieldType(Class<?> fieldType, String value) throws Exception {
        if (fieldType.isAssignableFrom(String.class)) {
            return value;
        } else if (fieldType.isAssignableFrom(Long.class) || fieldType.isAssignableFrom(Long.TYPE)) {
            return Long.parseLong(value);
        } else if (fieldType.isAssignableFrom(Integer.class) || fieldType.isAssignableFrom(Integer.TYPE)) {
            return Integer.parseInt(value);
        } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
            return new BigDecimal(value);
        } else if (fieldType.isAssignableFrom(Double.class) || fieldType.isAssignableFrom(Double.TYPE)) {
            return Double.parseDouble(value);
        } else if (fieldType.isAssignableFrom(Float.class) || fieldType.isAssignableFrom(Float.TYPE)) {
            return Float.parseFloat(value);
        } else if (fieldType.isAssignableFrom(Short.class) || fieldType.isAssignableFrom(Short.TYPE)) {
            return Short.parseShort(value);
        } else if (fieldType.isAssignableFrom(Boolean.class) || fieldType.isAssignableFrom(Boolean.TYPE)) {
            return Boolean.parseBoolean(value);
        } else if (fieldType.isAssignableFrom(Character.class) || fieldType.isAssignableFrom(Character.TYPE)) {
            return value.charAt(0);
        } else if (fieldType.isAssignableFrom(LocalDate.class)) {
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        } else if (fieldType.isAssignableFrom(LocalDateTime.class)) {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            throw new APIException("Unsupported filter field type! " + fieldType.getName(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    public Specification buildFilter() {
        if (filterCriteria.size() == 0) {
            return null;
        }

        Specification combinedSpecification = new FilterSpecification(filterCriteria.get(0));
        for (int i = 1; i < filterCriteria.size(); i++) {
            combinedSpecification = Specification.where(combinedSpecification).and(new FilterSpecification(filterCriteria.get(i)));
        }

        return combinedSpecification;
    }
}
