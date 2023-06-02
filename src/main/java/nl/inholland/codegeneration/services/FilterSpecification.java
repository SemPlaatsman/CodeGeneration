package nl.inholland.codegeneration.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import nl.inholland.codegeneration.models.FilterCriteria;
import org.springframework.data.jpa.domain.Specification;

// FilterSpecification object to add field filters based on the following article: https://www.baeldung.com/rest-api-search-language-spring-data-specifications
@AllArgsConstructor
public class FilterSpecification<T> implements Specification<T> {
    private FilterCriteria filterCriteria;
    @Override
    public Predicate toPredicate (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (filterCriteria.getOperation().equalsIgnoreCase(">:")) {
            return builder.greaterThanOrEqualTo(
                    root.<String>get(filterCriteria.getKey()), filterCriteria.getValue().toString());
        }
        else if (filterCriteria.getOperation().equalsIgnoreCase("<:")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(filterCriteria.getKey()), filterCriteria.getValue().toString());
        }
        else if (filterCriteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThan(
                    root.<String>get(filterCriteria.getKey()), filterCriteria.getValue().toString());
        }
        else if (filterCriteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThan(
                    root.<String>get(filterCriteria.getKey()), filterCriteria.getValue().toString());
        }
        else if (filterCriteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(filterCriteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(filterCriteria.getKey()), "%" + filterCriteria.getValue() + "%");
            } else {
                return builder.equal(root.get(filterCriteria.getKey()), filterCriteria.getValue());
            }
        }
        return null;
    }
}
