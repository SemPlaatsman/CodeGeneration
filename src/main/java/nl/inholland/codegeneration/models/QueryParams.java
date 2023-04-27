package nl.inholland.codegeneration.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QueryParams {
    private List<FilterCriteria> filterSpecifications = new ArrayList<>();
    private int limit = 12;
    private int page = 0;

    public void setFilter(String filterQuery) {
        String[] filters = filterQuery.split(",");
        for(String filter : filters) {
            String[] filterSections = filter.split("((?<=[:<>])|(?=[:<>]))");
            this.filterSpecifications.add(new FilterCriteria(filterSections[0], filterSections[1], filterSections[2]));
        }
    }
}
