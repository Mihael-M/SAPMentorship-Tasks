package sap.mentorship.clouds.response.flight;

import sap.mentorship.clouds.entity.flight.Flight;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FlightDetailsResponse(
    UUID id,
    String from,
    String to,
    Instant departure,
    int capacity,
    BigDecimal price,
    int availableSeats,
    int confirmedSeats,
    int activeHeldSeats
) {
    public static FlightDetailsResponse of(
        Flight flight,
        int availableSeats,
        int confirmedSeats,
        int activeHeldSeats
    ) {
        return new FlightDetailsResponse(
            flight.id(), flight.from(), flight.to(), flight.departure(),
            flight.capacity(), flight.price(),
            availableSeats, confirmedSeats, activeHeldSeats
        );
    }
}
