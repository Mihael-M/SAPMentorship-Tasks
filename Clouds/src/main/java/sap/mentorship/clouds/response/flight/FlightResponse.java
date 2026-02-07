package sap.mentorship.clouds.response.flight;

import sap.mentorship.clouds.entity.flight.Flight;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FlightResponse(
    UUID id,
    String from,
    String to,
    Instant departure,
    int capacity,
    BigDecimal price
) {
    public static FlightResponse from(Flight flight) {
        return new FlightResponse(
            flight.id(),
            flight.from(),
            flight.to(),
            flight.departure(),
            flight.capacity(),
            flight.price()
        );
    }
}
