package sap.mentorship.clouds.common.util;

import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;

import java.time.Instant;
import java.util.List;

public class AvailableSeatsCalculator {

    public static SeatAvailability calculate(List<Reservation> reservationsForFlight, Flight flight) {
        int confirmedSeats = reservationsForFlight.stream()
            .filter(r -> r.status() == ReservationStatus.CONFIRMED)
            .mapToInt(Reservation::seats)
            .sum();

        int activeHeldSeats = reservationsForFlight.stream()
            .filter(r -> r.status() == ReservationStatus.HELD)
            .filter(r -> r.holdExpiresAt() != null && r.holdExpiresAt().isAfter(Instant.now()))
            .mapToInt(Reservation::seats)
            .sum();

        int availableSeats = flight.capacity() - confirmedSeats - activeHeldSeats;
        return new SeatAvailability(confirmedSeats, activeHeldSeats, availableSeats);
    }

    public static int availableSeats(List<Reservation> reservationsForFlight, Flight flight) {
        return calculate(reservationsForFlight, flight).availableSeats();
    }
}
