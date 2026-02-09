package sap.mentorship.clouds.service;

import org.springframework.stereotype.Service;
import sap.mentorship.clouds.common.exception.flightexception.NoFlightWithProvidedId;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.entity.flight.FlightOccupancy;
import sap.mentorship.clouds.repository.FlightStore;
import sap.mentorship.clouds.response.metrics.MetricsResponse;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;
import sap.mentorship.clouds.repository.ReservationStore;
import sap.mentorship.clouds.entity.waitlist.WaitlistDepth;
import sap.mentorship.clouds.repository.WaitingStore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MetricsService {
    private final FlightStore flightStore;
    private final ReservationStore reservationStore;
    private final WaitingStore waitingStore;

    public MetricsService(FlightStore flightStore,
                          ReservationStore reservationStore,
                          WaitingStore waitingStore) {
        this.flightStore = flightStore;
        this.reservationStore = reservationStore;
        this.waitingStore = waitingStore;
    }

    public MetricsResponse getMetrics() {
        int totalFlights = countFlights();

        Map<UUID, Flight> flightsById = indexFlightsById();
        List<Reservation> confirmedReservations = findConfirmedReservations();

        Map<UUID, Integer> confirmedSeatsByFlightId = sumConfirmedSeatsByFlight(confirmedReservations);

        BigDecimal revenue = calculateRevenue(confirmedReservations, flightsById);
        List<FlightOccupancy> flightOccupancies = buildFlightOccupancies(flightsById, confirmedSeatsByFlightId);

        List<WaitlistDepth> waitlistDepth = buildWaitlistDepths(flightsById);
        int totalDepth = sumTotalWaitlistDepth(waitlistDepth);

        return new MetricsResponse(totalFlights, revenue, flightOccupancies, waitlistDepth, totalDepth);
    }

    private int countFlights() {
        return flightStore.findAll().size();
    }

    private Map<UUID, Flight> indexFlightsById() {
        return flightStore.findAll().stream()
            .collect(Collectors.toMap(Flight::id, f -> f));
    }

    private List<Reservation> findConfirmedReservations() {
        return reservationStore.findAll().stream()
            .filter(r -> r.status() == ReservationStatus.CONFIRMED)
            .toList();
    }

    private Map<UUID, Integer> sumConfirmedSeatsByFlight(List<Reservation> confirmedReservations) {
        return confirmedReservations.stream()
            .collect(Collectors.groupingBy(Reservation::flightId, Collectors.summingInt(Reservation::seats)));
    }

    private BigDecimal calculateRevenue(List<Reservation> confirmedReservations, Map<UUID, Flight> flightsById) {
        return confirmedReservations.stream()
            .map(r -> {
                Flight flight = requireFlight(flightsById, r.flightId());
                return flight.price().multiply(BigDecimal.valueOf(r.seats()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<FlightOccupancy> buildFlightOccupancies(Map<UUID, Flight> flightsById,
                                                        Map<UUID, Integer> confirmedSeatsByFlightId) {
        return flightsById.values().stream()
            .map(flight -> {
                int confirmedSeats = confirmedSeatsByFlightId.getOrDefault(flight.id(), 0);
                double percent = flight.capacity() == 0 ? 0.0 : (double) confirmedSeats / flight.capacity();
                return new FlightOccupancy(flight.id(), flight.capacity(), confirmedSeats, percent);
            })
            .toList();
    }

    private List<WaitlistDepth> buildWaitlistDepths(Map<UUID, Flight> flightsById) {
        return flightsById.keySet().stream()
            .map(flightId -> new WaitlistDepth(flightId, waitingStore.depth(flightId)))
            .toList();
    }

    private int sumTotalWaitlistDepth(List<WaitlistDepth> waitlistDepth) {
        return waitlistDepth.stream().mapToInt(WaitlistDepth::depth).sum();
    }

    private Flight requireFlight(Map<UUID, Flight> flightsById, UUID flightId) {
        Flight flight = flightsById.get(flightId);
        if (flight == null) {
            throw new NoFlightWithProvidedId("No flight with id " + flightId);
        }
        return flight;
    }

}
