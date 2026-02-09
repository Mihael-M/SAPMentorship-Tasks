package sap.mentorship.clouds.common.convert;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.request.passenger.CreatePassengerRequest;

import java.util.UUID;

@Component
public class PassengerConverter implements Convertion<Passenger, CreatePassengerRequest> {

    @Override
    public Passenger convert(CreatePassengerRequest from) {
        return new Passenger(
            UUID.randomUUID(),
            from.name(),
            from.email(),
            from.type(),
            from.type().priority()
        );
    }
}
