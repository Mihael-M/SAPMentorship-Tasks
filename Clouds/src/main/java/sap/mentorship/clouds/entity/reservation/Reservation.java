package sap.mentorship.clouds.entity.reservation;

import sap.mentorship.clouds.entity.waitlist.WaitingEntry;

import java.time.Instant;
import java.util.UUID;

public record Reservation(
    UUID id,
    UUID flightId,
    UUID passengerId,
    int seats,
    ReservationStatus status,
    Instant createdAt,
    Instant holdExpiresAt
) {
    public static Reservation fromWaitlist(WaitingEntry entry) {
        return new Reservation(
            UUID.randomUUID(),
            entry.flightId(),
            entry.passengerId(),
            entry.seats(),
            ReservationStatus.CONFIRMED,
            Instant.now(),
            null
        );
    }

    public static Reservation confirm(Reservation existing) {
        return new Reservation(
            existing.id(),
            existing.flightId(),
            existing.passengerId(),
            existing.seats(),
            ReservationStatus.CONFIRMED,
            existing.createdAt(),
            existing.holdExpiresAt()
        );
    }

    public static Reservation expire(Reservation existing) {
        return new Reservation(
            existing.id(),
            existing.flightId(),
            existing.passengerId(),
            existing.seats(),
            ReservationStatus.EXPIRED,
            existing.createdAt(),
            existing.holdExpiresAt()
        );
    }
}
