package sap.mentorship.clouds.common.filter.providers;

import sap.mentorship.clouds.common.filter.Filter;
import sap.mentorship.clouds.common.filter.filterentity.FlightFilter;
import sap.mentorship.clouds.entity.flight.Flight;

public class FlightDestinationProvider implements FilterProvider<Flight, FlightFilter> {
    @Override
    public boolean supports(FlightFilter criteria) {
        return criteria != null &&
            ((criteria.from() != null && !criteria.from().isBlank()) ||
             (criteria.to() != null && !criteria.to().isBlank()));
    }

    @Override
    public Filter<Flight> build(FlightFilter criteria) {
        if (criteria == null) {
            return Filter.all();
        }

        String from = criteria.from() == null ? null : criteria.from().trim();
        String to = criteria.to() == null ? null : criteria.to().trim();

        if ((from == null || from.isBlank()) && (to == null || to.isBlank())) {
            return Filter.all();
        }

        return flight -> {
            if (flight == null) {
                return false;
            }
            if (from != null && !from.isBlank()) {
                if (flight.from() == null || !flight.from().equalsIgnoreCase(from)) {
                    return false;
                }
            }
            if (to != null && !to.isBlank()) {
                return flight.to() != null && flight.to().equalsIgnoreCase(to);
            }
            return true;
        };
    }
}
