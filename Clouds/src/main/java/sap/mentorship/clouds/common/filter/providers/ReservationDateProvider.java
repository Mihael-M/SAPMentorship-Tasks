package sap.mentorship.clouds.common.filter.providers;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.common.filter.Filter;
import sap.mentorship.clouds.common.filter.filterentity.ReservationFilter;
import sap.mentorship.clouds.entity.reservation.Reservation;

import java.time.Instant;

@Component
public class ReservationDateProvider implements FilterProvider<Reservation, ReservationFilter> {
    @Override
    public boolean supports(ReservationFilter criteria) {
        return criteria != null && (criteria.from() != null || criteria.to() != null);
    }

    @Override
    public Filter<Reservation> build(ReservationFilter criteria) {
        if (criteria == null) {
            return Filter.all();
        }

        Instant from = criteria.from();
        Instant to = criteria.to();

        if (from == null && to == null) {
            return Filter.all();
        }

        return reservation -> {
            if (reservation == null || reservation.createdAt() == null) {
                return false;
            }
            var createdAt = reservation.createdAt();
            if (from != null && createdAt.isBefore(from)) {
                return false;
            }
            return to == null || !createdAt.isAfter(to);
        };
    }
}
