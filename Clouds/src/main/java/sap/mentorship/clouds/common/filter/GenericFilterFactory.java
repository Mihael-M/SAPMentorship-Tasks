package sap.mentorship.clouds.common.filter;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.common.filter.providers.FilterProvider;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenericFilterFactory<R, C> {
    List<FilterProvider<R, C>> providers;

    public GenericFilterFactory(List<FilterProvider<R, C>> providers) {
        this.providers = List.copyOf(providers);
    }

    public Filter<R> from(C criteria) {
        List<Filter<R>> active = new ArrayList<>();
        for (FilterProvider<R, C> provider : providers) {
            if (provider.supports(criteria)) {
                active.add(provider.build(criteria));
            }
        }
        return Filters.andAll(active);
    }
}
