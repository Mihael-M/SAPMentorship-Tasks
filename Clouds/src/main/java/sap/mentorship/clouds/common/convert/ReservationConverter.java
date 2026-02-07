package sap.mentorship.clouds.common.convert;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;
import sap.mentorship.clouds.request.reservation.CreateReservationRequest;

import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

@Component
public class ReservationConverter implements Convertion<Reservation, CreateReservationRequest> {

    @Override
    public Reservation convert(CreateReservationRequest from) {

        return new Reservation(
            UUID.randomUUID(),
            from.flightId(),
            from.passengerId(),
            from.seats(),
            ReservationStatus.HELD,
            Instant.now(),
            Instant.now().plus(Duration.ofSeconds(30))
        );
    }
}
