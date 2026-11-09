package com.parking.entryexitservice.service;
import com.parking.entryexitservice.config.RabbitConfig;
import com.parking.entryexitservice.event.SlotAllocationRequestedEvent;
import com.parking.entryexitservice.event.VehicleExitRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class EntryEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    // Publish slot allocation request
    public void requestSlot(SlotAllocationRequestedEvent event) {
        log.info("Sending allocation request for ticket {}",
                event.ticketNumber());
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                "slot.allocation.requested",
                event
        );
        log.info("Allocation request published for ticket {}",
                event.ticketNumber());
    }
    // Publish vehicle exit request
    public void requestExit(VehicleExitRequestedEvent event) {
        log.info("Sending vehicle exit request for ticket {}",
                event.ticketNumber());
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                "vehicle.exit.requested",
                event
        );
        log.info("Vehicle exit request published for ticket {}",
                event.ticketNumber());
    }
}