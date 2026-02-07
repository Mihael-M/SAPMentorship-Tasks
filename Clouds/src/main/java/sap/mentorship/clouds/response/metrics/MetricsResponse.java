package sap.mentorship.clouds.response.metrics;

import sap.mentorship.clouds.entity.flight.FlightOccupancy;
import sap.mentorship.clouds.entity.waitlist.WaitlistDepth;

import java.math.BigDecimal;
import java.util.List;

public record MetricsResponse(
    int totalFlights,
    BigDecimal currentRevenue,
    List<FlightOccupancy> occupancyByFlight,
    List<WaitlistDepth> waitlistDepthByFlight,
    int totalWaitlistDepth
) { }
