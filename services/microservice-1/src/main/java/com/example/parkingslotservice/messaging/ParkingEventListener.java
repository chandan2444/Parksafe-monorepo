package com.example.parkingslotservice.messaging;
import com.example.parkingslotservice.event.*;
import com.example.parkingslotservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class ParkingEventListener {
    private final ParkingService parkingService;
    @RabbitListener(
            queues = "parking.slot-allocation.requested"
    )
    public void onAllocationRequested(
            SlotAllocationRequestedEvent event) {
        log.info(
                "Received allocation request for ticket {}",
                event.ticketNumber()
        );
        parkingService.allocate(event);
    }
    @RabbitListener(
            queues = "parking.slot-release.requested"
    )
    public void onSlotReleaseRequested(
            SlotReleaseRequestedEvent event) {
        log.info(
                "Received slot release request for ticket {}",
                event.ticketNumber()
        );
        parkingService.release(event);
    }
}