package sap.mentorship.clouds.controller.reservation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sap.mentorship.clouds.service.BookingProcessor;
import sap.mentorship.clouds.entity.reservation.ReservationQuery;
import sap.mentorship.clouds.request.reservation.CreateReservationRequest;
import sap.mentorship.clouds.response.reservation.ReservationResponse;
import sap.mentorship.clouds.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clouds/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final BookingProcessor bookingProcessor;

    public ReservationController(ReservationService reservationService,
                                 BookingProcessor bookingProcessor) {
        this.reservationService = reservationService;
        this.bookingProcessor = bookingProcessor;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createReservation(
        @Valid @RequestBody CreateReservationRequest createReservationRequest) {
        bookingProcessor.submit(createReservationRequest);
    }

    @PutMapping("/{id}")
    public ReservationResponse confirmReservation(@PathVariable UUID id) {
        return reservationService.confirmReservation(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable UUID id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping("/all")
    public List<ReservationResponse> findAllReservations(
        @ModelAttribute ReservationQuery reservationQuery) {
        return reservationService.findAllReservations(reservationQuery);
    }
}
