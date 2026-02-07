package sap.mentorship.clouds.common.exception.flightexception;

public class NoFlightWithProvidedId extends RuntimeException {
    public NoFlightWithProvidedId(String message) {
        super(message);
    }
}
