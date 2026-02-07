package sap.mentorship.clouds.entity.flight;

import java.util.UUID;

public record FlightOccupancy(
    UUID flightId,
    int capacity,
    int confirmedSeats,
    double occupancyPercent
) { }
