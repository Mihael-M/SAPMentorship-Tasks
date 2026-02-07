package sap.mentorship.clouds.service;

import org.springframework.stereotype.Service;
import sap.mentorship.clouds.response.booking.BookingOutcomeResponse;
import sap.mentorship.clouds.common.convert.Convertion;
import sap.mentorship.clouds.common.exception.flightexception.NoFlightWithProvidedId;
import sap.mentorship.clouds.common.exception.passengerexception.NoPassengerWithProvidedId;
import sap.mentorship.clouds.common.exception.reservationexception.NoReservationWithProvidedId;
import sap.mentorship.clouds.common.filter.Filter;
import sap.mentorship.clouds.common.filter.GenericFilterFactory;
import sap.mentorship.clouds.common.filter.filterentity.ReservationFilter;
import sap.mentorship.clouds.common.sort.SortProvider;
import sap.mentorship.clouds.common.util.AvailableSeatsCalculator;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.repository.FlightStore;
import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.repository.PassengerStore;
import sap.mentorship.clouds.entity.reservation.Reservation;
import sap.mentorship.clouds.entity.reservation.ReservationQuery;
import sap.mentorship.clouds.entity.reservation.ReservationSort;
import sap.mentorship.clouds.entity.reservation.ReservationStatus;
import sap.mentorship.clouds.repository.ReservationStore;
import sap.mentorship.clouds.request.reservation.CreateReservationRequest;
import sap.mentorship.clouds.response.reservation.ReservationResponse;
import sap.mentorship.clouds.entity.waitlist.WaitingEntry;
import sap.mentorship.clouds.response.waitlist.WaitlistInfo;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ReservationService {
    private final ReservationStore reservationStore;
    private final Convertion<Reservation, CreateReservationRequest> convertor;
    private final GenericFilterFactory<Reservation, ReservationFilter> genericFilterFactory;
    private final SortProvider<Reservation, ReservationSort> sortProvider;
    private final WaitingService waitingService;
    private final PassengerStore passengerStore;
    private final FlightStore flightStore;

    public ReservationService(ReservationStore reservationStore,
                              Convertion<Reservation, CreateReservationRequest> convertor,
                              GenericFilterFactory<Reservation, ReservationFilter> genericFilterFactory,
                              SortProvider<Reservation, ReservationSort> sortProvider,
                              WaitingService waitingService,
                              PassengerStore passengerStore,
                              FlightStore flightStore) {
        this.reservationStore = reservationStore;
        this.convertor = convertor;
        this.genericFilterFactory = genericFilterFactory;
        this.sortProvider = sortProvider;
        this.waitingService = waitingService;
        this.passengerStore = passengerStore;
        this.flightStore = flightStore;
    }

    public BookingOutcomeResponse createHoldOrWaitlist(
        CreateReservationRequest createReservationRequest) {
        if (createReservationRequest == null) {
            throw new IllegalArgumentException("CreateReservationRequest cannot be null.");
        }
        Flight flight = flightStore.findById(createReservationRequest.flightId())
            .orElseThrow(() -> new NoFlightWithProvidedId(
                "No flight found " + createReservationRequest.flightId()));
        Passenger passenger = passengerStore.findById(createReservationRequest.passengerId())
            .orElseThrow(() -> new NoPassengerWithProvidedId(
                "No passenger found " + createReservationRequest.passengerId()
            ));
        ReentrantLock lock = flightStore.lockFor(flight.id());
        lock.lock();
        try {
            int availableSeats = AvailableSeatsCalculator.availableSeats(
                reservationStore.findByFlightId(flight.id()),
                flight);
            if (availableSeats < createReservationRequest.seats()) {
                WaitlistInfo info =
                    waitingService.addToWaitlist(flight, passenger, createReservationRequest.seats());
                return BookingOutcomeResponse.waitlisted(info);
            }
            Reservation reservation = convertor.convert(createReservationRequest);
            Reservation saved = reservationStore.save(reservation);
            return BookingOutcomeResponse.held(ReservationResponse.from(saved));
        } finally {
            lock.unlock();
        }
    }

    public ReservationResponse confirmReservation(UUID reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("reservationId cannot be null.");
        }

        Reservation existing = reservationStore.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

        ReentrantLock lock = flightStore.lockFor(existing.flightId());
        lock.lock();
        try {
            if (existing.status() != ReservationStatus.HELD) {
                throw new IllegalStateException(
                    "Only HELD reservations can be confirmed. Current status: " + existing.status());
            }

            if (existing.holdExpiresAt() != null && !existing.holdExpiresAt().isAfter(Instant.now())) {
                throw new IllegalStateException(
                    "Reservation hold has expired and cannot be confirmed.");
            }
            return ReservationResponse.from(reservationStore.save(Reservation.confirm(existing)));
        } finally {
            lock.unlock();
        }
    }

    public void cancelReservation(UUID reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("reservationId cannot be null.");
        }
        Reservation existing = reservationStore.findById(reservationId)
            .orElseThrow(() -> new NoReservationWithProvidedId("Reservation not found" + reservationId));
        ReentrantLock lock = flightStore.lockFor(existing.flightId());
        lock.lock();
        Flight flight =  flightStore.findById(existing.flightId())
            .orElseThrow(() -> new NoFlightWithProvidedId("No flight found " + existing.flightId()));
        int seats = AvailableSeatsCalculator.availableSeats(
            reservationStore.findAll(), flight);
        try {
            if (!reservationStore.delete(reservationId)) {
                throw new NoReservationWithProvidedId("Reservation not found" + reservationId);
            }
            WaitingEntry entry = waitingService.offerSeatsIfPossible(existing.flightId(), seats);
            if (entry != null) {
                reservationStore.save(Reservation.fromWaitlist(entry));
            }
        } finally {
            lock.unlock();
        }
    }

    public List<ReservationResponse> findAllReservations(ReservationQuery query) {
        Filter<Reservation> reservationFilter = genericFilterFactory.from(query.filter());
        return reservationStore.findAll().stream()
            .filter(reservationFilter::test)
            .sorted(sortProvider.comparatorFor(query.sort()))
            .limit(query.limit())
            .map(ReservationResponse::from).toList();
    }
}
