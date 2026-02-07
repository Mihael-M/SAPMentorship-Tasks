package sap.mentorship.clouds.entity.booking;

import sap.mentorship.clouds.request.reservation.CreateReservationRequest;

import java.time.Instant;

public record BookingTask(CreateReservationRequest request, int priority, Instant createdAt)
    implements Comparable<BookingTask> {

    @Override
    public int compareTo(BookingTask o) {
        int byPriority = Integer.compare(this.priority, o.priority);
        if (byPriority != 0) {
            return byPriority;
        }
        return Long.compare(this.createdAt.getEpochSecond(), o.createdAt.getEpochSecond());
    }
}
