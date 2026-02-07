package sap.mentorship.clouds.response.reservation;

import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationResponse(
    UUID id,
    UUID flightId,
    UUID passengerId,
    int seats,
    ReservationStatus status,
    Instant createdAt,
    Instant holdExpiresAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.id(),
            reservation.flightId(),
            reservation.passengerId(),
            reservation.seats(),
            reservation.status(),
            reservation.createdAt(),
            reservation.holdExpiresAt()
        );
    }
}
