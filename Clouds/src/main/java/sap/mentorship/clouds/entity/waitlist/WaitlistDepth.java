package sap.mentorship.clouds.entity.waitlist;

import java.util.UUID;

public record WaitlistDepth(
    UUID flightId,
    int depth
) { }
