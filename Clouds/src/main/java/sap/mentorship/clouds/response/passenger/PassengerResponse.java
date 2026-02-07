package sap.mentorship.clouds.response.passenger;

import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.entity.passenger.PassengerType;

import java.util.UUID;

public record PassengerResponse(
        UUID id,
        String name,
        String email,
        PassengerType type,
        int priority) {

    public static PassengerResponse from(Passenger p) {
        return new PassengerResponse(
            p.id(),
            p.name(),
            p.email(),
            p.type(),
            p.type().priority()
        );
    }
}
