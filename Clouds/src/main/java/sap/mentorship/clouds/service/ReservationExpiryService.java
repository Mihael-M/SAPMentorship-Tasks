package sap.mentorship.clouds.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sap.mentorship.clouds.common.util.AvailableSeatsCalculator;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.repository.FlightStore;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;
import sap.mentorship.clouds.repository.ReservationStore;

import java.time.Instant;
import java.util.List;

@Service
public class ReservationExpiryService {
    private final ReservationStore reservationStore;
    private final FlightStore flightStore;
    private final WaitingService waitingService;

    private static final int FIXED_DELAY = 1000;

    public ReservationExpiryService(ReservationStore reservationStore,
                                    FlightStore flightStore,
                                    WaitingService waitingService) {
        this.reservationStore = reservationStore;
        this.flightStore = flightStore;
        this.waitingService = waitingService;
    }

    @Scheduled(fixedDelay = FIXED_DELAY)
    public void expireHolds() {
        Instant now = Instant.now();
        List<Reservation> expired = findExpiredHolds(now);
        for (Reservation reservation : expired) {
            processExpiredReservation(reservation);
        }
    }

    private List<Reservation> findExpiredHolds(Instant now) {
        return reservationStore.findAll().stream()
            .filter(r -> r.status() == ReservationStatus.HELD)
            .filter(r -> r.holdExpiresAt() != null && !r.holdExpiresAt().isAfter(now))
            .toList();
    }

    private void processExpiredReservation(Reservation reservation) {
        var lock = flightStore.lockFor(reservation.flightId());
        lock.lock();
        try {
            expireAndOfferWaitlist(reservation);
        } finally {
            lock.unlock();
        }
    }

    private void expireAndOfferWaitlist(Reservation reservation) {
        Reservation current = reservationStore.findById(reservation.id()).orElse(null);
        Flight currentFlight = flightStore.findById(reservation.flightId()).orElse(null);
        if (current == null || currentFlight == null) {
            return;
        }
        if (current.status() != ReservationStatus.HELD) {
            return;
        }

        Instant nowLocked = Instant.now();
        if (current.holdExpiresAt() != null && current.holdExpiresAt().isAfter(nowLocked)) {
            return;
        }

        reservationStore.save(Reservation.expire(current));

        var reservationsForFlight = reservationStore.findByFlightId(reservation.flightId());
        int availableSeats = AvailableSeatsCalculator.availableSeats(reservationsForFlight, currentFlight);

        var accepted = waitingService.offerSeatsIfPossible(currentFlight.id(), availableSeats);
        if (accepted != null) {
            reservationStore.save(Reservation.fromWaitlist(accepted));
        }
    }
}
