package sap.mentorship.clouds.repository;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.passenger.Passenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PassengerStore {
    private final Map<UUID, Passenger> passengers;

    public PassengerStore() {
        this.passengers = new ConcurrentHashMap<>();
    }

    public Passenger save(Passenger passenger) {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger must not be null");
        }
        return passengers.putIfAbsent(passenger.id(), passenger);
    }

    public Optional<Passenger> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Passenger id must not be null");
        }
        return Optional.ofNullable(passengers.get(id));
    }

    public List<Passenger> findAll() {
        return new ArrayList<>(this.passengers.values());
    }

    public boolean delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Passenger id must not be null");
        }
        return passengers.remove(id) != null;
    }
}
