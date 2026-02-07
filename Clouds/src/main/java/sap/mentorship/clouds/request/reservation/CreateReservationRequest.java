package sap.mentorship.clouds.request.reservation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReservationRequest(
    @NotNull UUID flightId,
    @NotNull UUID passengerId,
    @Min(MINIMUM_SEATS) @Max(MAXIMUM_SEATS) int seats
) {
    private static final int MAXIMUM_SEATS = 10;
    private static final int MINIMUM_SEATS = 1;
}
