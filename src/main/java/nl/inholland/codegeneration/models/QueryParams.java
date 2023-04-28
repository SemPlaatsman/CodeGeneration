package nl.inholland.codegeneration.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.inholland.codegeneration.services.FilterSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class QueryParams {
    private final List<FilterCriteria> filterCriteria = new ArrayList<>();
    private int limit = 12;
    private int page = 0;

    public void setFilter(String filterQuery) {
//        System.out.println(filterQuery);
        this.filterCriteria.clear();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(filterQuery + ",");
        while (matcher.find()) {
//            System.out.println("First: " + matcher.group(1) + ". Second: " + matcher.group(2) + ". Third: " + matcher.group(3));
            this.addFilter(new FilterCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
    }

    public boolean addFilter(FilterCriteria filterCriterion) {
        return this.filterCriteria.add(filterCriterion);
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
