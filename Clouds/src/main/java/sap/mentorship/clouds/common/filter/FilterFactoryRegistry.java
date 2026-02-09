package sap.mentorship.clouds.common.filter;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.common.filter.filterentity.FlightFilter;
import sap.mentorship.clouds.entity.flight.Flight;

import java.util.List;

@Component
public final class FilterFactoryRegistry {
    private final GenericFilterFactory<Flight, FlightFilter> flightFilters;

    public FilterFactoryRegistry() {
        this.flightFilters = new GenericFilterFactory<>(List.of(

        ));
    }

    public GenericFilterFactory<Flight, FlightFilter> flightFilters() {
        return flightFilters;
    }
}
