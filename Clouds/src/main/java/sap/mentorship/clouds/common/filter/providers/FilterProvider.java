package sap.mentorship.clouds.common.filter.providers;

import sap.mentorship.clouds.common.filter.Filter;

public interface FilterProvider<R, C> {
    boolean supports(C criteria);

    Filter<R> build(C criteria);
}
