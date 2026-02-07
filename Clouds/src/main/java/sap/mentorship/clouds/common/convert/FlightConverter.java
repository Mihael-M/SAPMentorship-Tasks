package sap.mentorship.clouds.common.convert;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.request.flight.CreateFlightRequest;

import java.util.UUID;

@Component
public class FlightConverter implements Convertion<Flight, CreateFlightRequest> {
    @Override
    public Flight convert(CreateFlightRequest from) {
        return new Flight(
            UUID.randomUUID(),
            from.from(),
            from.to(),
            from.departure(),
            from.capacity(),
            from.price()
        );
    }
}
