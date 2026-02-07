package sap.mentorship.clouds.common.logging;

import sap.mentorship.clouds.entity.booking.BookingTask;
import sap.mentorship.clouds.entity.flight.Flight;
import sap.mentorship.clouds.entity.passenger.Passenger;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReservationLogger {
    private static final int LINE_REPEAT = 72;
    public static void consoleLog(BookingTask bookingTask, Flight flight, Passenger passenger) {
        Objects.requireNonNull(bookingTask, "bookingTask cannot be null");
        Objects.requireNonNull(flight, "flight cannot be null");
        Objects.requireNonNull(passenger, "passenger cannot be null");

        String sep = "=".repeat(LINE_REPEAT);
        String sub = "-".repeat(LINE_REPEAT);

        String departure = flight.departure() == null ? "-" :
            DateTimeFormatter.ISO_INSTANT.format(flight.departure());
        String taskCreated = bookingTask.createdAt() == null ? "-" :
            DateTimeFormatter.ISO_INSTANT.format(bookingTask.createdAt());

        System.out.println(sep);
        System.out.println("RESERVATION TASK EXECUTION");
        System.out.println(sub);

        System.out.printf("Task      | priority=%d | seats=%d | createdAt=%s%n",
            bookingTask.priority(), bookingTask.request().seats(), taskCreated);
        System.out.printf("Flight     | id=%s | route=%s -> %s | departure=%s | capacity=%d | price=%s%n",
            flight.id(), safe(flight.from()), safe(flight.to()), departure, flight.capacity(), flight.price());
        System.out.printf("Passenger  | id=%s | name=%s | type=%s | email=%s%n",
            passenger.id(), safe(passenger.name()), passenger.type(), safe(passenger.email()));

        System.out.println(sep);
    }

    private static String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}
