package sap.mentorship.clouds.controller.flight;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sap.mentorship.clouds.common.filter.filterentity.FlightFilter;
import sap.mentorship.clouds.request.flight.CreateFlightRequest;
import sap.mentorship.clouds.response.flight.FlightDetailsResponse;
import sap.mentorship.clouds.response.flight.FlightResponse;
import sap.mentorship.clouds.service.FlightService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clouds/v1/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(
        @Valid @RequestBody CreateFlightRequest request) {
        FlightResponse created = flightService.saveFlight(request);
        return ResponseEntity
            .created(URI.create("/clouds/v1/flights/" + created.id()))
            .body(created);

    }

    @GetMapping("/{id}")
    public FlightDetailsResponse getFlight(@PathVariable UUID id) {
        return flightService.getFlight(id);
    }

    @GetMapping("/all")
    public List<FlightResponse> getAllFlights(
        @ModelAttribute FlightFilter filter) {
        return flightService.getAllFlights(filter);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlight(@PathVariable UUID id) {
        flightService.deleteFlight(id);
    }
}
