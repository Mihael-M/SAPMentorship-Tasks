package sap.mentorship.clouds.service;

import org.springframework.stereotype.Service;
import sap.mentorship.clouds.common.convert.Convertion;
import sap.mentorship.clouds.common.exception.passengerexception.NoPassengerWithProvidedId;
import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.repository.PassengerStore;
import sap.mentorship.clouds.request.passenger.CreatePassengerRequest;
import sap.mentorship.clouds.response.passenger.PassengerResponse;

import java.util.List;
import java.util.UUID;

@Service
public class PassengerService {
    private final PassengerStore passengerStore;
    private final Convertion<Passenger, CreatePassengerRequest> passengerConverter;

    public PassengerService(PassengerStore passengerStore,
                            Convertion<Passenger, CreatePassengerRequest> passengerConverter) {
        this.passengerStore = passengerStore;
        this.passengerConverter = passengerConverter;
    }

    public PassengerResponse createPassenger(
        CreatePassengerRequest createPassengerRequest) {
        if (createPassengerRequest == null) {
            throw new IllegalArgumentException("createPassengerRequest must not be null");
        }
        Passenger passenger =
            passengerStore.save(passengerConverter.convert(createPassengerRequest));
        return PassengerResponse.from(passenger);
    }

    public List<PassengerResponse> getAllPassengers() {
        List<Passenger> passengers =  passengerStore.findAll();
        return passengers.stream()
            .map(PassengerResponse::from).toList();
    }

    public PassengerResponse getPassengerById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        Passenger passenger =  passengerStore.findById(id)
            .orElseThrow(() -> new NoPassengerWithProvidedId("Passenger not found: " + id));
        return PassengerResponse.from(passenger);
    }

    public void deletePassenger(UUID id) {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        boolean deleted = passengerStore.delete(id);
        if (!deleted) {
            throw new NoPassengerWithProvidedId("Passenger not found: " + id);
        }
    }

}
