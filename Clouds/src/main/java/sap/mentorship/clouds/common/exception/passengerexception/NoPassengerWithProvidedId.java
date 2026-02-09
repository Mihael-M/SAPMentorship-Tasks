package sap.mentorship.clouds.common.exception.passengerexception;

public class NoPassengerWithProvidedId extends RuntimeException {
    public NoPassengerWithProvidedId(String message) {
        super(message);
    }
}
