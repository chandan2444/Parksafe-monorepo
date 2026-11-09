package com.example.sessionservice.messaging;
import com.example.sessionservice.event.*;
import com.example.sessionservice.service.ParkingSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component @RequiredArgsConstructor @Slf4j
public class SessionEventListener {
    private final ParkingSessionService service;

    @RabbitListener(queues="session.slot-allocated")
    public void onSlotAllocated(SlotAllocatedEvent event) {
        log.info("Received slot allocation for ticket {}", event.ticketNumber());
        service.start(event);
    }

    @RabbitListener(queues="session.vehicle-exit-requested")
    public void onVehicleExit(VehicleExitRequestedEvent event) {
        log.info("Received vehicle exit for ticket {}", event.ticketNumber());
        service.complete(event);
    }
}
