package sap.mentorship.clouds.common.filter.filterentity;

import java.math.BigDecimal;

public record FlightFilter(
    String from,
    String to,
    BigDecimal minPrice,
    BigDecimal maxPrice
) { }
