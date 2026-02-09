package sap.mentorship.clouds.common.filter.providers;

import sap.mentorship.clouds.common.filter.Filter;
import sap.mentorship.clouds.common.filter.filterentity.FlightFilter;
import sap.mentorship.clouds.entity.flight.Flight;

import java.math.BigDecimal;

public class FlightPriceProvider implements FilterProvider<Flight, FlightFilter> {
    @Override
    public boolean supports(FlightFilter criteria) {
        return criteria != null && (criteria.maxPrice() != null || criteria.minPrice() != null);
    }

    @Override
    public Filter<Flight> build(FlightFilter criteria) {
        if (criteria == null) {
            return Filter.all();
        }

        BigDecimal minPrice = criteria.minPrice();
        BigDecimal maxPrice = criteria.maxPrice();
        if (minPrice == null && maxPrice == null) {
            return Filter.all();
        }

        if (minPrice != null && minPrice.signum() < 0) {
            return Filter.all();
        }

        if (maxPrice != null && maxPrice.signum() < 0) {
            return Filter.all();
        }

        return flight -> {
            if (flight == null || flight.price() == null) {
                return false;
            }
            BigDecimal price = flight.price();
            if (minPrice != null && price.compareTo(minPrice) < 0) {
                return false;
            }
            return maxPrice == null || price.compareTo(maxPrice) <= 0;
        };
    }
}
