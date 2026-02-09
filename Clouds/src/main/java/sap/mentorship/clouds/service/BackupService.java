package sap.mentorship.clouds.service;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import sap.mentorship.clouds.entity.BackupState;
import sap.mentorship.clouds.repository.FlightStore;
import sap.mentorship.clouds.repository.PassengerStore;
import sap.mentorship.clouds.repository.ReservationStore;
import sap.mentorship.clouds.repository.WaitingStore;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Service
public class BackupService {
    private final FlightStore flightStore;
    private final PassengerStore passengerStore;
    private final ReservationStore reservationStore;
    private final WaitingStore waitingStore;
    private final ObjectMapper objectMapper;

    public BackupService(FlightStore flightStore, PassengerStore passengerStore,
                         ReservationStore reservationStore, WaitingStore waitingStore,
                         ObjectMapper objectMapper) {
        this.flightStore = flightStore;
        this.passengerStore = passengerStore;
        this.reservationStore = reservationStore;
        this.waitingStore = waitingStore;
        this.objectMapper = objectMapper;
    }

    @PreDestroy
    public void onShutdown() {
        saveToFile();
    }

    private void saveToFile() {
        BackupState backup = BackupState.from(
            flightStore.findAll(),
            passengerStore.findAll(),
            reservationStore.findAll(),
            waitingStore.getWaitlists()
        );
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(Path.of("backup_state.json").toFile(), backup);
        } catch (InvalidPathException e) {
            throw new RuntimeException(e);
        }
    }
}
