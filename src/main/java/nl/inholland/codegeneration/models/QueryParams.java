package nl.inholland.codegeneration.models;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.Null;
import org.hibernate.query.SemanticException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
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
public class QueryParams<T> {
    // TODO: implement default value if role is invalid
    private final List<FilterCriteria> filterCriteria = new ArrayList<>();
    private int limit = 12;
    private int page = 0;
    // This class reference is used to access the fields of the class the filterCriteria needs to filter on
    private Class<?> classReference;

    public QueryParams(Class<?> classReference, @Nullable Integer limit, @Nullable Integer page) {
        this.classReference = classReference;
        if (limit != null) { this.limit = limit; }
        if (page != null) { this.page = page; }
    }

    public void setFilter(String filterQuery) throws Exception {
//        System.out.println(filterQuery);
        this.filterCriteria.clear();
        Pattern pattern = Pattern.compile("(\\w.+?)(:|<|>|>:|<:)'([a-zA-Z0-9:.-]+?)',");
        Matcher matcher = pattern.matcher(filterQuery + ",");
        while (matcher.find()) {
            System.out.println("First: " + matcher.group(1) + ". Second: " + matcher.group(2) + ". Third: " + matcher.group(3));
            this.addFilter(new FilterCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
    }

    public boolean addFilter(FilterCriteria filterCriterion) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new BadCredentialsException("Unauthorized!");
        }

        String[] parts = filterCriterion.getKey().split("\\.");

        // Check if it's a nested variable using the NestedFilterable annotation and change the current class reference if it is nested
        Class<?> currentClass = this.classReference;
        Field field = null;
        for (int i = 0; i < parts.length; i++) {
            field = currentClass.getDeclaredField(parts[i]);

            if (field.isAnnotationPresent(NestedFilterable.class)) {
                if (i + 1 >= parts.length || !Arrays.asList(field.getAnnotation(NestedFilterable.class).nestedProperty()).contains(parts[i + 1])) {
                    throw new SemanticException("Invalid filter option!");
                }
                currentClass = field.getType();
            }
        }

        // Check for Filterable annotations in the current class or field
        if (!currentClass.isAnnotationPresent(Filterable.class)) {
            if (!field.isAnnotationPresent(Filterable.class)) {
                throw new SemanticException("Invalid filter option!");
            }
            Filterable filterable = field.getAnnotation(Filterable.class);
            if (filterable.role() == Role.EMPLOYEE && !user.getRoles().contains(Role.EMPLOYEE)) {
                if (!filterable.defaultValue().isEmpty()) { filterCriterion.setValue(filterable.defaultValue()); }
                else { throw new BadCredentialsException("Invalid permissions!"); }
            }
        } else if (this.classReference.getAnnotation(Filterable.class).role() == Role.EMPLOYEE && !user.getRoles().contains(Role.EMPLOYEE)) {
            throw new BadCredentialsException("Invalid permissions!");
        }

        // Cast to correct field type
        filterCriterion.setValue(this.castToFieldType(field.getType(), (String) filterCriterion.getValue()));

        System.out.println(filterCriterion.getKey() + filterCriterion.getOperation() + filterCriterion.getValue() + " (" + filterCriterion.getValue().getClass() + ")");

        return this.filterCriteria.add(filterCriterion);
    }

    private Object castToFieldType(Class<?> fieldType, String value) throws Exception {
        //why not use multiple if statements? it breaks out of the method anyway when the return is called
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
        } else if (fieldType.isAssignableFrom(Role.class)) {
            return Role.fromInt(Integer.parseInt(value));
        } else if (fieldType.isAssignableFrom(AccountType.class)) {
            return AccountType.fromInt(Integer.parseInt(value));
        } else {
            throw new APIException("Unsupported filter field type! " + fieldType.getName(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    public Specification<T> buildFilter() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (FilterCriteria criterion : filterCriteria) {
                String[] parts = criterion.getKey().split("\\.");
                Specification<T> spec;

                if (parts.length > 1) {
                    Join<T, ?> join = root.join(parts[0]);
                    spec = new FilterSpecification<>(new FilterCriteria(parts[1], criterion.getOperation(), criterion.getValue()), join);
                } else {
                    spec = new FilterSpecification<>(criterion, null);
                }
                predicates.add(spec.toPredicate(root, query, builder));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public boolean fieldHasBeenAdded(String field) {
        return this.filterCriteria.stream().anyMatch(filterCriterion -> filterCriterion.getKey().equals(field));
    }
}
