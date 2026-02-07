package sap.mentorship.clouds.service;

import org.springframework.stereotype.Service;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.entity.waitlist.WaitingEntry;
import sap.mentorship.clouds.repository.WaitingStore;
import sap.mentorship.clouds.response.waitlist.WaitlistInfo;

import java.time.Instant;
import java.util.UUID;

@Service
public class WaitingService {
    private final WaitingStore waitingStore;

    public WaitingService(WaitingStore waitingStore) {
        this.waitingStore = waitingStore;
    }

    public WaitlistInfo addToWaitlist(Flight flight, Passenger passenger, int seats) {
        int prioritySnapshot = passenger.type().priority();

        WaitingEntry entry = new WaitingEntry(
            UUID.randomUUID(),
            flight.id(),
            passenger.id(),
            seats,
            prioritySnapshot,
            Instant.now()
        );

        waitingStore.add(flight.id(), entry);
        return WaitlistInfo.from(entry);
    }

    public WaitingEntry offerSeatsIfPossible(UUID flightId, int availableSeats) {
        if (flightId == null) {
            throw new IllegalArgumentException("flightId cannot be null");
        }
        if (availableSeats <= 0) {
            return null;
        }

        WaitingEntry next = waitingStore.peekNext(flightId);
        if (next == null) {
            return null;
        }

        if (next.seats() <= availableSeats) {
            return waitingStore.pollNext(flightId);
        }

        return null;
    }
}
