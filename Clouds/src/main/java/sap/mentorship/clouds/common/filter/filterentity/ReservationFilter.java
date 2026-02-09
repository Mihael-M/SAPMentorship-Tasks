package sap.mentorship.clouds.common.filter.filterentity;

import sap.mentorship.clouds.entity.reservation.ReservationStatus;

import java.time.Instant;

public record ReservationFilter(
    Instant from,
    Instant to,
    ReservationStatus status) { }
