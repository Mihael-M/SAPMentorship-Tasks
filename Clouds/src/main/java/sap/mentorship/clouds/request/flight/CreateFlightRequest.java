package sap.mentorship.clouds.request.flight;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateFlightRequest(
    @NotBlank String from,
    @NotBlank String to,
    @NotNull Instant departure,
    @Min(1) int capacity,
    @DecimalMin("0.0") @NotNull BigDecimal price
) { }
