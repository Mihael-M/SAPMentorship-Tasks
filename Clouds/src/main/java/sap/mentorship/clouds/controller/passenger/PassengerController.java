package sap.mentorship.clouds.controller.passenger;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sap.mentorship.clouds.request.passenger.CreatePassengerRequest;
import sap.mentorship.clouds.response.passenger.PassengerResponse;
import sap.mentorship.clouds.service.PassengerService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clouds/v1/passengers")
public class PassengerController {
    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(
        @Valid @RequestBody CreatePassengerRequest request) {
        PassengerResponse created = passengerService.createPassenger(request);
        return ResponseEntity
            .created(URI.create("/clouds/v1/passengers/" + created.id()))
            .body(created);
    }

    @GetMapping("/{id}")
    public PassengerResponse getPassenger(@PathVariable UUID id) {
        return passengerService.getPassengerById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable UUID id) {
        passengerService.deletePassenger(id);
    }

    @GetMapping("/all")
    public List<PassengerResponse> getAllPassengers() {
        return passengerService.getAllPassengers();
    }
}
