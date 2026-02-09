package sap.mentorship.clouds.common.exception.reservationexception;

public class NoReservationWithProvidedId extends RuntimeException {
    public NoReservationWithProvidedId(String message) {
        super(message);
    }
}
