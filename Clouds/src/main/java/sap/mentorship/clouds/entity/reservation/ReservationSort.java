package sap.mentorship.clouds.entity.reservation;

public record ReservationSort(
    ReservationSortBy by,
    SortDirection direction
) { }
