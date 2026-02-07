package sap.mentorship.clouds.entity.reservation;

import sap.mentorship.clouds.common.filter.filterentity.ReservationFilter;

public record ReservationQuery(
    ReservationFilter filter,
    ReservationSort sort,
    Integer limit
) {
    public static final int DEFAULT_LIMIT = 20;
    public static final int MAX_LIMIT = 100;

    public ReservationQuery {
        int resolved = (limit == null || limit <= 0) ? DEFAULT_LIMIT : limit;
        if (resolved > MAX_LIMIT) {
            resolved = MAX_LIMIT;
        }
        limit = resolved;
    }
}