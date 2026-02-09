package sap.mentorship.clouds.request.passenger;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sap.mentorship.clouds.entity.passenger.PassengerType;

public record CreatePassengerRequest(
    @NotBlank String name,
    @Email @NotBlank String email,
    @NotNull PassengerType type
) { }
