package sap.mentorship.clouds.entity;

import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.waitlist.WaitingEntry;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record BackupState(
    List<Flight> flights,
    List<Passenger> passengers,
    List<Reservation> reservations,
    Map<UUID, List<WaitingEntry>> waitlists
) {
    public static BackupState from(List<Flight> flights, List<Passenger> passengers, List<Reservation> reservations,
                             Map<UUID, List<WaitingEntry>> waitlists) {
        return new BackupState(flights, passengers, reservations, waitlists);
    }
}
