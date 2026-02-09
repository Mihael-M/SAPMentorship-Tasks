package sap.mentorship.clouds.repository;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReservationStore {
    private final Map<UUID, Reservation> reservations;

    public ReservationStore() {
        this.reservations = new ConcurrentHashMap<>();
    }

    public Reservation save(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(UUID id) {
        return Optional.ofNullable(this.reservations.get(id));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(this.reservations.values());
    }

    public boolean delete(UUID id) {
        return this.reservations.remove(id) != null;
    }

    public List<Reservation> findByFlightId(UUID flightId) {
        return reservations.values().stream()
            .filter((
                reservation ->
                    reservation.flightId().equals(flightId)))
            .toList();
    }

    public List<Reservation> findByPassengerId(UUID passengerId) {
        return reservations.values().stream()
            .filter((
                reservation ->
                    reservation.passengerId().equals(passengerId)))
            .toList();
    }
}
