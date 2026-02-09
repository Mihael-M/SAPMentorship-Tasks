package sap.mentorship.clouds.common.filter;

import java.util.List;

public final class Filters {
    public static <T> Filter<T> andAll(List<Filter<T>> filters) {
        Filter<T> filter = Filter.all();
        for (Filter<T> next : filters) filter = filter.and(next);
        return filter;
    }
}
