package sap.mentorship.clouds.entity.passenger;

import java.util.UUID;

public record Passenger(
    UUID id,
    String name,
    String email,
    PassengerType type,
    int priority) {
    private static final int MAXIMUM_SEATS = 10;
    private static final int MINIMUM_SEATS = 1;

    public int priority() {
        return type.priority();
    }

    public int maxGroupSize() {
        return type ==
            PassengerType.CORPORATE ? MAXIMUM_SEATS : MINIMUM_SEATS;
    }
}
