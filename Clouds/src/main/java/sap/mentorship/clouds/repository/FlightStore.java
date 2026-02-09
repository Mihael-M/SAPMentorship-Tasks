package sap.mentorship.clouds.repository;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.flight.Flight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class FlightStore {
    private final Map<UUID, Flight> flights;
    private final Map<UUID, ReentrantLock> flightLocks;

    public FlightStore() {
        this.flights = new ConcurrentHashMap<>();
        this.flightLocks = new ConcurrentHashMap<>();
    }

    public Flight save(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("flight cannot be null");
        }
        flights.put(flight.id(), flight);
        return flight;
    }

    public Optional<Flight> findById(UUID id) {
        return Optional.ofNullable(flights.get(id));
    }

    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }

    public boolean delete(UUID id) {
        return flights.remove(id) != null;
    }

    public boolean exists(UUID id) {
        return flights.containsKey(id);
    }

    public ReentrantLock lockFor(UUID flightId) {
        return flightLocks.computeIfAbsent(flightId, id -> new ReentrantLock(true));
    }
}
