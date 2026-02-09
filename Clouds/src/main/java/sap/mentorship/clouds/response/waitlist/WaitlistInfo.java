package sap.mentorship.clouds.response.waitlist;

import java.time.Instant;
import java.util.UUID;
import sap.mentorship.clouds.entity.waitlist.WaitingEntry;

public record WaitlistInfo(
    UUID flightId,
    UUID passengerId,
    int seats,
    int prioritySnapshot,
    Instant queuedAt
) {
    public static WaitlistInfo from(WaitingEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("entry cannot be null");
        }
        return new WaitlistInfo(
            entry.flightId(),
            entry.passengerId(),
            entry.seats(),
            entry.priority(),
            entry.createdAt());
    }
}
