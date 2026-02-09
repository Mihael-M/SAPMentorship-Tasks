package sap.mentorship.clouds.common.sort;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.repository.PassengerStore;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationSort;
import sap.mentorship.clouds.entity.reservation.ReservationSortBy;
import sap.mentorship.clouds.entity.reservation.SortDirection;

import java.util.Comparator;
import java.util.UUID;

@Component
public class ReservationSortProvider implements SortProvider<Reservation, ReservationSort> {
    private final PassengerStore passengerStore;

    public ReservationSortProvider(PassengerStore passengerStore) {
        this.passengerStore = passengerStore;
    }

    @Override
    public Comparator<Reservation> comparatorFor(ReservationSort sort) {
        ReservationSortBy by = (sort == null || sort.by() == null) ? ReservationSortBy.PASSENGER : sort.by();
        SortDirection dir = (sort == null || sort.direction() == null) ? SortDirection.ASC : sort.direction();

        Comparator<Reservation> cmp = switch (by) {
            case PASSENGER -> Comparator.comparing(Reservation::passengerId);
            case PRIORITY -> Comparator.comparingInt(this::priorityOfReservation);
        };

        return dir == SortDirection.DESC ? cmp.reversed() : cmp;
    }

    private int priorityOfReservation(Reservation reservation) {
        UUID passengerId = reservation.passengerId();
        return passengerStore.findById(passengerId)
            .map(p -> p.type().priority())
            .orElse(0);
    }
}
