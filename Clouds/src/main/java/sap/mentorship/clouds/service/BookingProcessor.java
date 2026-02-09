package sap.mentorship.clouds.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sap.mentorship.clouds.common.exception.flightexception.NoFlightWithProvidedId;
import sap.mentorship.clouds.common.exception.passengerexception.NoPassengerWithProvidedId;
import sap.mentorship.clouds.common.logging.ReservationLogger;
import sap.mentorship.clouds.entity.booking.BookingTask;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.repository.FlightStore;
import sap.mentorship.clouds.entity.passenger.Passenger;
import sap.mentorship.clouds.repository.PassengerStore;
import sap.mentorship.clouds.request.reservation.CreateReservationRequest;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

@Component
public class BookingProcessor {
    private static final int PROCESSING_TIME = 2000;
    private final ReservationService reservationService;
    private final PassengerStore passengerStore;
    private final FlightStore flightStore;

    private final Semaphore permits;
    private final ExecutorService executor;
    private final PriorityBlockingQueue<BookingTask> reservations = new PriorityBlockingQueue<>();

    public BookingProcessor(ReservationService reservationService,
                            PassengerStore passengerStore,
                            FlightStore flightStore,
                            @Value("${booking.maxConcurrent:5}") int maxConcurrent) {
        this.reservationService = reservationService;
        this.flightStore = flightStore;
        this.passengerStore = passengerStore;
        this.executor = Executors.newFixedThreadPool(maxConcurrent);
        this.permits = new Semaphore(maxConcurrent);
    }

    public void submit(CreateReservationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null");
        }
        int priority = passengerStore.findById(request.passengerId())
            .map(passenger -> passenger.type().priority())
            .orElse(0);

        BookingTask task = new BookingTask(
            request,
            priority,
            Instant.now()
        );
        if (permits.tryAcquire()) {
            executor.submit(() -> runTask(task));
        } else {
            reservations.add(task);
        }
    }

    private void runTask(BookingTask task) {
        try {
            Thread.sleep(PROCESSING_TIME);
            Flight flight = flightStore.findById(task.request().flightId())
                .orElseThrow(() -> new NoFlightWithProvidedId(
                    "Flight with id: " + task.request().flightId()));
            Passenger passenger = passengerStore.findById(task.request().passengerId())
                    .orElseThrow(() -> new NoPassengerWithProvidedId(
                        "Passenger with id: " + task.request().passengerId()));
            ReservationLogger.consoleLog(task, flight, passenger);
            reservationService.createHoldOrWaitlist(task.request());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            permits.release();
            scheduleNextIfAny();
        }
    }

    private void scheduleNextIfAny() {
        BookingTask task = reservations.poll();
        if (task == null) {
            return;
        }
        if (permits.tryAcquire()) {
            executor.submit(() -> runTask(task));
        } else {
            reservations.add(task);
        }
    }
}
