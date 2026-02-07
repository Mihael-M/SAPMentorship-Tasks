package sap.mentorship.clouds.entity.passenger;

public enum PassengerType {
    STANDARD(2, 1),
    BUSINESS(3, 1),
    BUDGET(1, 1),
    CORPORATE(2, 10);

    private final int priority;
    private final int maxGroupSize;

    PassengerType(int priority, int maxGroupSize) {
        this.priority = priority;
        this.maxGroupSize = maxGroupSize;
    }

    public int priority() {
        return this.priority;
    }

    public int maxGroupSize() {
        return this.maxGroupSize;
    }
}