package sap.mentorship.clouds.service;

import org.springframework.stereotype.Service;
import sap.mentorship.clouds.common.convert.Convertion;
import sap.mentorship.clouds.common.exception.flightexception.NoFlightWithProvidedId;
import sap.mentorship.clouds.common.filter.Filter;
import sap.mentorship.clouds.common.filter.GenericFilterFactory;
import sap.mentorship.clouds.common.filter.filterentity.FlightFilter;
import sap.mentorship.clouds.common.util.AvailableSeatsCalculator;
import sap.mentorship.clouds.common.util.SeatAvailability;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.repository.FlightStore;
import sap.mentorship.clouds.request.flight.CreateFlightRequest;
import sap.mentorship.clouds.response.flight.FlightDetailsResponse;
import sap.mentorship.clouds.response.flight.FlightResponse;
import sap.mentorship.clouds.repository.ReservationStore;

import java.util.List;
import java.util.UUID;

@Service
public class FlightService {
    private final FlightStore flightStore;
    private final Convertion<Flight, CreateFlightRequest> convertion;
    private final GenericFilterFactory<Flight, FlightFilter> genericFilterFactory;
    private final ReservationStore reservationStore;

    public FlightService(FlightStore flightStore,
                         Convertion<Flight, CreateFlightRequest> convertion,
                         GenericFilterFactory<Flight, FlightFilter> genericFilterFactory,
                         ReservationStore reservationStore) {
        this.flightStore = flightStore;
        this.convertion = convertion;
        this.genericFilterFactory = genericFilterFactory;
        this.reservationStore = reservationStore;
    }

    public FlightResponse saveFlight(CreateFlightRequest flightRequest) {
        if (flightRequest == null) {
            throw new IllegalArgumentException("flightRequest cannot be null");
        }
        Flight flight = flightStore.save(convertion.convert(flightRequest));
        return FlightResponse.from(flight);
    }

    public FlightDetailsResponse getFlight(UUID id) {
        if  (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        Flight flight =  flightStore.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("flight with id " + id + " not found"));
        var reservationsForFlight = reservationStore.findByFlightId(flight.id());

        SeatAvailability availableSeats = AvailableSeatsCalculator.calculate(reservationsForFlight, flight);

        return FlightDetailsResponse.of(
            flight,
            availableSeats.availableSeats(),
            availableSeats.confirmedSeats(),
            availableSeats.activeHeldSeats());
    }

    public List<FlightResponse> getAllFlights(FlightFilter filter) {
        Filter<Flight> flightFilter = genericFilterFactory.from(filter);

        List<Flight> flights = flightStore.findAll();
        return flights.stream()
            .filter(flightFilter::test)
            .map(FlightResponse::from)
            .toList();
    }

    public void deleteFlight(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (!flightStore.delete(id)) {
            throw new NoFlightWithProvidedId("No flight with id " + id);
        }
    }
}
