package sap.mentorship.clouds.response.booking;

import sap.mentorship.clouds.entity.booking.BookingOutcomeType;
import sap.mentorship.clouds.response.reservation.ReservationResponse;
import sap.mentorship.clouds.response.waitlist.WaitlistInfo;

import java.time.Instant;

public record BookingOutcomeResponse(
    BookingOutcomeType outcome,
    ReservationResponse reservation,
    WaitlistInfo waitlist,
    Instant serverTime,
    String message
) {
    public static BookingOutcomeResponse held(ReservationResponse r) {
        return new BookingOutcomeResponse(
            BookingOutcomeType.HELD,
            r,
            null,
            Instant.now(),
            "Reservation is held. Confirm before it expires."
        );
    }

    public static BookingOutcomeResponse waitlisted(WaitlistInfo w) {
        return new BookingOutcomeResponse(
            BookingOutcomeType.WAITLISTED,
            null,
            w,
            Instant.now(),
            "Flight is full. Passenger added to waitlist."
        );
    }
}
