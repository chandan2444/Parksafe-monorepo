package com.example.sessionservice.service;

import com.example.sessionservice.entity.*;
import com.example.sessionservice.event.*;
import com.example.sessionservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class ParkingSessionService {
    private final ParkingSessionRepository sessionRepository;
    private final ParkingHistoryRepository historyRepository;
    private final SessionEventPublisher publisher;

    @Transactional
    public void start(SlotAllocatedEvent event) {
        if (sessionRepository.findByTicketNumber(event.ticketNumber()).isPresent()) {
            log.info("Session already exists for ticket {}; duplicate event ignored", event.ticketNumber());
            return;
        }
        ParkingSession session = sessionRepository.save(ParkingSession.builder()
                .ticketNumber(event.ticketNumber()).vehicleNumber(event.vehicleNumber())
                .slotId(event.slotId()).slotNumber(event.slotNumber())
                .entryTime(event.allocatedAt()).status(SessionStatus.ACTIVE).build());
        historyRepository.save(ParkingHistory.builder().ticketNumber(event.ticketNumber())
                .vehicleNumber(event.vehicleNumber()).action("SESSION_STARTED")
                .actionTime(event.allocatedAt()).details("Allocated slot " + event.slotNumber()).build());
        log.info("Started parking session {} for vehicle {}", session.getTicketNumber(), session.getVehicleNumber());
    }

    @Transactional
    public void complete(VehicleExitRequestedEvent event) {
        var sessionOpt =

                sessionRepository.findByTicketNumber(

                        event.ticketNumber());

        if (sessionOpt.isEmpty()) {

            log.warn(

                    "Session not found for ticket {}",

                    event.ticketNumber());

            return;

        }

        ParkingSession session = sessionOpt.get();


        if (session.getStatus() == SessionStatus.COMPLETED) {
            log.info("Session {} already completed; duplicate exit ignored", event.ticketNumber());
            return;
        }
        LocalDateTime exitTime = event.exitTime() == null ? LocalDateTime.now() : event.exitTime();
        session.setExitTime(exitTime);
        session.setStatus(SessionStatus.COMPLETED);
        sessionRepository.save(session);

        historyRepository.save(ParkingHistory.builder().ticketNumber(event.ticketNumber())
                .vehicleNumber(event.vehicleNumber()).action("SESSION_COMPLETED")
                .actionTime(exitTime).details("Vehicle exited slot " + session.getSlotNumber()).build());

        publisher.sessionCompleted(new ParkingSessionCompletedEvent(session.getTicketNumber(),
                session.getVehicleNumber(), session.getSlotId(), session.getEntryTime(), exitTime));
        publisher.releaseSlot(new SlotReleaseRequestedEvent(session.getTicketNumber(),
                session.getSlotId(), LocalDateTime.now()));
        log.info("Completed session {}. Slot release requested.", event.ticketNumber());
    }

    public List<ParkingSession> getAll() { return sessionRepository.findAll(); }
    public List<ParkingHistory> getHistory(String ticketNumber) {
        return historyRepository.findByTicketNumberOrderByActionTimeAsc(ticketNumber);
    }
}
