package sap.mentorship.clouds.entity.flight;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Flight(
    UUID id,
    String from,
    String to,
    Instant departure,
    int capacity,
    BigDecimal price) { }
