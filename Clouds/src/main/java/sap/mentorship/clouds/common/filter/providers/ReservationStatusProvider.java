package sap.mentorship.clouds.common.filter.providers;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.common.filter.Filter;
import sap.mentorship.clouds.common.filter.filterentity.ReservationFilter;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;

@Component
public class ReservationStatusProvider implements FilterProvider<Reservation, ReservationFilter> {

    @Override
    public boolean supports(ReservationFilter criteria) {
        return criteria != null && criteria.status() != null;
    }

    @Override
    public Filter<Reservation> build(ReservationFilter criteria) {
        if (criteria == null) {
            return Filter.all();
        }
        ReservationStatus status = criteria.status();
        if (status == null) {
            return Filter.all();
        }
        return reservation -> reservation != null
            && reservation.status() != null
            && reservation.status() == status;
    }
}
