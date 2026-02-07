package sap.mentorship.clouds.repository;

import org.springframework.stereotype.Component;
import sap.mentorship.clouds.entity.waitlist.WaitingEntry;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class WaitingStore {
    private final Map<UUID, PriorityBlockingQueue<WaitingEntry>> waitlists;

    public WaitingStore() {
        this.waitlists = new ConcurrentHashMap<>();
    }

    public Map<UUID, List<WaitingEntry>> getWaitlists() {
        Map<UUID, List<WaitingEntry>> snapshot = new ConcurrentHashMap<>();
        for (var entry : waitlists.entrySet()) {
            var queue = entry.getValue();
            snapshot.put(entry.getKey(), queue == null ? List.of() : List.copyOf(queue));
        }
        return snapshot;
    }

    public void add(UUID flightId, WaitingEntry waitingEntry) {
        if (flightId == null) {
            throw new IllegalArgumentException("flightId cannot be null");
        }
        if (waitingEntry == null) {
            throw new IllegalArgumentException("waitingEntry cannot be null");
        }
        waitlists.computeIfAbsent(flightId, k -> new PriorityBlockingQueue<>())
            .add(waitingEntry);
    }

    public WaitingEntry peekNext(UUID flightId) {
        var queue = waitlists.get(flightId);
        return queue == null ? null : queue.peek();
    }

    public WaitingEntry pollNext(UUID flightId) {
        var queue = waitlists.get(flightId);
        return queue == null ? null : queue.poll();
    }

    public int depth(UUID flightId) {
        var queue = waitlists.get(flightId);
        return queue == null ? 0 : queue.size();
    }
}
