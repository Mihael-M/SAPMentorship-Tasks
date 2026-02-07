package sap.mentorship.clouds.entity.waitlist;

import java.time.Instant;
import java.util.UUID;

public record WaitingEntry(
    UUID id,
    UUID flightId,
    UUID passengerId,
    int seats,
    int priority,
    Instant createdAt) implements Comparable<WaitingEntry> {

    @Override
    public int compareTo(WaitingEntry entry) {
        int byPriority = Integer.compare(priority, entry.priority);
        if (byPriority != 0) {
            return byPriority;
        }
        int byCreatedAt = createdAt.compareTo(entry.createdAt);
        if (byCreatedAt != 0) {
            return byCreatedAt;
        }
        return id.compareTo(entry.id);
    }

}
